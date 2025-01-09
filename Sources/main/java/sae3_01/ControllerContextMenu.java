package sae3_01;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

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
     * - Masquer/Afficher les méthodes
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
        MenuItem modifier = new MenuItem("Modifier");
        MenuItem masquer = new MenuItem("Masquer / Afficher");
        MenuItem masquerAttributs = new MenuItem("Masquer / Afficher les attributs");
        MenuItem masquerMethode = new MenuItem("Masquer / Afficher les méthodes");
        MenuItem supprimer = new MenuItem("Supprimer");

        masquer.setOnAction(e -> {
            VueDiagramme diagramme = (VueDiagramme) finalVueClasse.getParent();
            diagramme.toggleAffichageClasse(finalVueClasse.getNom());
        });

        masquerMethode.setOnAction(e -> {
            VueDiagramme diagramme = (VueDiagramme) finalVueClasse.getParent();
            VueClasse classe = (VueClasse) diagramme.getVueClasse(finalVueClasse.getNom());
            classe.hideMethodes();
        });

        masquerAttributs.setOnAction(e -> {
            VueDiagramme diagramme = (VueDiagramme) finalVueClasse.getParent();
            VueClasse classe = (VueClasse) diagramme.getVueClasse(finalVueClasse.getNom());
            classe.hideAttributs();
        });

        supprimer.setOnAction(e -> {
            VueDiagramme diagramme = (VueDiagramme) finalVueClasse.getParent();
            diagramme.retirerClasse(finalVueClasse.getNom());
            model.supprimerClasse(model.getClasse(finalVueClasse.getNom()));
        });

        // Modification d'une classe existante
        modifier.setOnAction(e -> {
            if (finalVueClasse.isVisible()) {
                Stage fenetreModification = new Stage();
                fenetreModification.setTitle("Modifier la classe : " + finalVueClasse.getNom());

                // Récupérer la classe à modifier
                String nomClasse = finalVueClasse.getNom().substring(finalVueClasse.getNom().lastIndexOf(".") + 1);
                Classe classeAModifier = model.getClasse(nomClasse);

                // Créer le formulaire de modification
                GridPane formulaire = new GridPane();
                formulaire.setHgap(10);
                formulaire.setVgap(10);
                formulaire.setPadding(new Insets(20, 150, 10, 10));

                // Champ pour le type de classe
                Label typeLabel = new Label("Type de classe :");
                ComboBox<String> typeComboBox = new ComboBox<>();
                typeComboBox.getItems().addAll("public", "abstract", "private");
                typeComboBox.setValue(classeAModifier.getType());
                formulaire.add(typeLabel, 0, 0);
                formulaire.add(typeComboBox, 1, 0);

                // Champ pour le nom de la classe
                Label nomLabel = new Label("Nom de la classe :");
                TextField nomField = new TextField(classeAModifier.getNomSimple());
                formulaire.add(nomLabel, 0, 1);
                formulaire.add(nomField, 1, 1);

                // Champ pour le package
                Label packageLabel = new Label("Package :");
                TextField packageField = new TextField(classeAModifier.getPackage());
                formulaire.add(packageLabel, 0, 2);
                formulaire.add(packageField, 1, 2);

                // Liste des attributs
                Label attributsLabel = new Label("Attributs :");
                HBox attributsContainer = new HBox(10);
                ListView<String> attributsListView = new ListView<>();
                attributsListView.getItems().addAll(classeAModifier.getAttributs());
                HBox boutonsAttributs = new HBox(10);
                Button ajouterAttribut = new Button("Ajouter");
                Button supprimerAttribut = new Button("Supprimer");
                boutonsAttributs.getChildren().addAll(ajouterAttribut, supprimerAttribut);
                boutonsAttributs.setAlignment(Pos.CENTER_RIGHT);

                attributsContainer.getChildren().addAll(attributsListView, boutonsAttributs);
                formulaire.add(attributsLabel, 0, 3);
                formulaire.add(attributsContainer, 1, 3);

                // Liste des méthodes
                Label methodesLabel = new Label("Méthodes :");
                HBox methodesContainer = new HBox(10);
                ListView<String> methodesListView = new ListView<>();
                methodesListView.getItems().addAll(classeAModifier.getMethodes());
                HBox boutonsMethodes = new HBox(10);
                Button ajouterMethode = new Button("Ajouter");
                Button supprimerMethode = new Button("Supprimer");
                boutonsMethodes.getChildren().addAll(ajouterMethode, supprimerMethode);
                boutonsMethodes.setAlignment(Pos.CENTER_RIGHT);

                methodesContainer.getChildren().addAll(methodesListView, boutonsMethodes);
                formulaire.add(methodesLabel, 0, 4);
                formulaire.add(methodesContainer, 1, 4);

                // Boutons Enregistrer et Annuler
                HBox boutonsActions = new HBox(10);
                Button enregistrer = new Button("Enregistrer");
                Button annuler = new Button("Annuler");
                boutonsActions.getChildren().addAll(enregistrer, annuler);
                formulaire.add(boutonsActions, 1, 7);

                // Logique d'ajout et de suppression d'attributs
                ajouterAttribut.setOnAction(eAjouter -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Ajouter un attribut");
                    dialog.setHeaderText("Entrez un nouvel attribut");
                    dialog.showAndWait().ifPresent(attributsListView.getItems()::add);
                });

                supprimerAttribut.setOnAction(eSupprimer -> {
                    String attributSelectionne = attributsListView.getSelectionModel().getSelectedItem();
                    if (attributSelectionne != null) {
                        attributsListView.getItems().remove(attributSelectionne);
                    }
                });

                // Logique d'ajout et de suppression de méthodes
                ajouterMethode.setOnAction(eAjouterMethode -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Ajouter une méthode");
                    dialog.setHeaderText("Entrez une nouvelle méthode");
                    dialog.showAndWait().ifPresent(methodesListView.getItems()::add);
                });

                supprimerMethode.setOnAction(eSupprimerMethode -> {
                    String methodeSelectionnee = methodesListView.getSelectionModel().getSelectedItem();
                    if (methodeSelectionnee != null) {
                        methodesListView.getItems().remove(methodeSelectionnee);
                    }
                });

                // Action d'enregistrement
                enregistrer.setOnAction(eEnregistrer -> {
                    // Créer une nouvelle classe avec les modifications
                    Classe classeModifiee = new Classe(
                            typeComboBox.getValue(),
                            nomField.getText(),
                            packageField.getText(),
                            new ArrayList<>(attributsListView.getItems()),
                            new ArrayList<>(methodesListView.getItems()),
                            classeAModifier.getCoordonnees(),
                            classeAModifier.getRelations()
                    );

                    // Mettre à jour le modèle
                    model.modifierClasse(classeAModifier, classeModifiee);

                    // Fermer la fenêtre
                    fenetreModification.close();
                });

                // Action d'annulation
                annuler.setOnAction(eAnnuler -> fenetreModification.close());

                // Configurer la scène
                Scene scene = new Scene(formulaire, 800, 800);
                fenetreModification.setScene(scene);
                fenetreModification.setResizable(false);
                fenetreModification.initModality(Modality.APPLICATION_MODAL);
                fenetreModification.show();
            }
        });

        contextMenu.getItems().addAll(modifier, masquer, masquerAttributs, masquerMethode, supprimer);
        contextMenu.setOnShowing(e -> finalVueClasse.setContextMenuShown(true));
        contextMenu.setOnHidden(e -> finalVueClasse.setContextMenuShown(false));
        contextMenu.show(vueClasse, event.getScreenX(), event.getScreenY());
    }
}