package sae3_01;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;

public class ControllerContextMenu implements EventHandler<ContextMenuEvent> {

    private Model model;

    public ControllerContextMenu(Model model) {
        this.model = model;
    }

    @Override
    public void handle(ContextMenuEvent event) {
        // Récupération de la vue classe
        VueDiagramme vueDiagramme = (VueDiagramme) event.getSource();
        VueClasse vueClasse = null;
        for (Node node : vueDiagramme.getChildren()) {
            if (node instanceof VueClasse temp) {
                if (temp.localToScene(temp.getBoundsInLocal()).contains(event.getSceneX(), event.getSceneY())) {
                    vueClasse = temp;
                    break;
                }
            }
        }
        if (vueClasse == null || vueClasse.isContextMenuShown()) return;
        VueClasse finalVueClasse = vueClasse; // Variable finale pour accès dans les lambda

        // Création du menu contextuel
        ContextMenu contextMenu = new ContextMenu();
        MenuItem modifier = new MenuItem("Modifier (non implémenté)");
        MenuItem masquer = new MenuItem("Masquer");
        MenuItem supprimer = new MenuItem("Supprimer");

        masquer.setOnAction(e -> {
            VueDiagramme diagramme = (VueDiagramme) finalVueClasse.getParent();
            diagramme.masquerClasse(finalVueClasse.getNom());
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
