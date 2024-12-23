package sae3_01;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * VueClasse est une classe qui hérite de VBox et qui implémente l'interface Observateur.
 * Elle permet de représenter graphiquement une classe.
 */
public class VueClasse extends VBox implements Observateur {

    /** Nom de la classe */
    private Label nom;
    /** Header de la classe */
    private VBox header;
    /** Attributs de la classe */
    private VBox attributs;
    /** Méthodes de la classe */
    private VBox methodes;
    /** Ligne de séparation entre le header et les attributs */
    private Line separation1;
    /** Ligne de séparation entre les attributs et les méthodes */
    private Line separation2;
    /** Coordonnée x de this */
    private double x;
    /** Coordonnée y de this */
    private double y;
    /** Indique si le menu contextuel est affiché */
    private boolean contextMenuShown = false;

    /**
     * Constructeur de VueClasse.
     * Initialise les attributs et le style de la VBox.
     */
    public VueClasse() {
        this.setStyle("-fx-background-color: yellow; -fx-border-color: black;");
        this.nom = new Label();
        this.header = new VBox();
        this.header.setAlignment(Pos.CENTER);
        this.attributs = new VBox();
        this.methodes = new VBox();
        this.separation1 = new Line(0, 0, 400, 0);
        this.separation1.setStyle("-fx-stroke: black;");
        this.separation2 = new Line(0, 0, 400, 0);
        this.separation2.setStyle("-fx-stroke: black;");
        this.drag();
        this.setContextMenu();
    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;
        String nomClasse = this.nom.getText().substring(this.nom.getText().lastIndexOf(".") + 1);
        this.getChildren().clear();
        updateHeader(model);
        this.getChildren().addAll(header, separation1);
        updateAttributs(model.getClasse(nomClasse).getAttributs());
        this.getChildren().addAll(attributs, separation2);
        updateMethodes(model.getClasse(nomClasse).getMethodes());
        this.getChildren().add(methodes);
    }

    /**
     * Setter du nom de la classe.
     * @param nom Nom de la classe.
     */
    public void setNom(String nom) {
        this.nom.setText(nom);
    }

    /**
     * Met à jour le header de la classe.
     * @param model Modèle de l'application.
     */
    private void updateHeader(Model model) {
        this.header.getChildren().clear();
        String nomClasse = this.nom.getText().substring(this.nom.getText().lastIndexOf(".") + 1);
        this.header.getChildren().add(new Label(model.getClasse(nomClasse).getPackage()));
        this.header.getChildren().add(this.nom);
    }

    /**
     * Met à jour les attributs de la classe.
     * @param attributs Liste des attributs de la classe.
     */
    private void updateAttributs(ArrayList<String> attributs) {
        this.attributs.getChildren().clear();
        if (attributs.isEmpty()) {
            this.attributs.getChildren().add(new Label("")); // Pour éviter que la VBox ne se réduise à 0
        }
        for (String attribut : attributs) {
            this.attributs.getChildren().add(new Label(attribut));
        }
    }

    /**
     * Met à jour les méthodes de la classe.
     * @param methodes Liste des méthodes de la classe.
     */
    private void updateMethodes(ArrayList<String> methodes) {
        this.methodes.getChildren().clear();
        if (methodes.isEmpty()) {
            this.methodes.getChildren().add(new Label("")); // Pour éviter que la VBox ne se réduise à 0
        }
        for (String methode : methodes) {
            this.methodes.getChildren().add(new Label(methode));
        }
    }

    /**
     * Permet de drag la classe.
     */
    public void drag() {
        this.setOnMousePressed(e -> {
            x = e.getSceneX() - this.getLayoutX();
            y = e.getSceneY() - this.getLayoutY();
        });

        this.setOnMouseDragged(e -> {
            if (contextMenuShown) return; // On ne veut pas déplacer la classe si le menu contextuel est affiché
            double newX = e.getSceneX() - x;
            double newY = e.getSceneY() - y;

            // Dimensions du parent
            double parentMaxX = getParent().getLayoutBounds().getWidth();
            double parentMaxY = getParent().getLayoutBounds().getHeight();

            // Dimensions de la VBox (this)
            double nodeWidth = this.getBoundsInLocal().getWidth();
            double nodeHeight = this.getBoundsInLocal().getHeight();

            boolean xValide = (newX >= 0) && (newX + nodeWidth <= parentMaxX);
            boolean yValide = (newY >= 0) && (newY + nodeHeight <= parentMaxY);

            if (xValide) {
                this.setLayoutX(newX);
            }
            if (yValide) {
                this.setLayoutY(newY);
            }
        });
    }

    /**
     * Permet d'afficher le menu contextuel.
     */
    private void setContextMenu() {
        this.setOnContextMenuRequested(e -> {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem modifier = new MenuItem("Modifier (non implémenté)");
            MenuItem supprimer = new MenuItem("Supprimer");
            supprimer.setOnAction(event -> {
                VueDiagramme diagramme = (VueDiagramme) this.getParent();
                diagramme.retirerClasse(this.nom.getText());
            });
            contextMenu.getItems().addAll(modifier, supprimer);
            contextMenu.setOnShowing(e2 -> contextMenuShown = true);
            contextMenu.setOnHidden(e2 -> contextMenuShown = false);
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }
}