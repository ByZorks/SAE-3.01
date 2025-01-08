package sae3_01;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ContextMenuEvent;

public class ControllerContextMenu implements EventHandler<ContextMenuEvent> {

    /** Modèle */
    private Model model;

    /**
     * Constructeur
     * @param model Modèle
     */
    public ControllerContextMenu(Model model) {
        this.model = model;
    }

    /**
     * Gère l'événement de clic droit pour afficher un menu contextuel.
     * Un menu contextuel est affiché avec des options pour :
     * - Modifier (non implémenté)
     * - Masquer/Afficher une classe
     * - Supprimer une classe
     * @param event L'événement de clic droit déclenchant l'affichage du menu contextuel.
     */
    @Override
    public void handle(ContextMenuEvent event) {
        VueClasse vueClasse = null;

        if (event.getSource() instanceof VueDiagramme vueDiagramme) {
            // Récupération de la vue classe dans le diagramme
            for (Node node : vueDiagramme.getChildren()) {
                if (node instanceof VueClasse temp) {
                    if (temp.localToScene(temp.getBoundsInLocal()).contains(event.getSceneX(), event.getSceneY())) {
                        vueClasse = temp;
                        if (!vueClasse.isVisible()) return; // Evite de pouvoir afficher le menu contextuel sur une classe masquée dans le diagramme
                        break;
                    }
                }
            }
        } else {
            // Récupération de la vue classe dans l'arborescence
            TreeView<FileComposite> treeView = (TreeView<FileComposite>) event.getSource();
            TreeItem<FileComposite> selectedItem = treeView.getSelectionModel().getSelectedItem();
            FileComposite file = selectedItem.getValue();

            if (!file.isDirectory()) {
                String nomFichier = file.toString().replace(".class", "");
                Classe classe = model.getClasse(nomFichier);

                if (classe != null) {
                    vueClasse = model.getVueDiagramme().getVueClasse(nomFichier);
                }
            }
        }



        if (vueClasse == null || vueClasse.isContextMenuShown()) return;
        VueClasse finalVueClasse = vueClasse; // Variable finale pour accès dans les lambda

        // Création du menu contextuel
        ContextMenu contextMenu = new ContextMenu();
        MenuItem modifier = new MenuItem("Modifier (non implémenté)");
        MenuItem masquer = new MenuItem("Masquer / Afficher");
        MenuItem supprimer = new MenuItem("Supprimer");

        masquer.setOnAction(e -> {
            VueDiagramme diagramme = (VueDiagramme) finalVueClasse.getParent();
            diagramme.toggleAffichageClasse(finalVueClasse.getNom());
        });

        supprimer.setOnAction(e -> {
            VueDiagramme diagramme = (VueDiagramme) finalVueClasse.getParent();
            diagramme.retirerClasse(finalVueClasse.getNom());
            model.supprimerClasse(model.getClasse(finalVueClasse.getNom()));
        });
        contextMenu.getItems().addAll(modifier, masquer, supprimer);
        contextMenu.setOnShowing(e -> finalVueClasse.setContextMenuShown(true));
        contextMenu.setOnHidden(e -> finalVueClasse.setContextMenuShown(false));
        contextMenu.show(vueClasse, event.getScreenX(), event.getScreenY());
    }
}
