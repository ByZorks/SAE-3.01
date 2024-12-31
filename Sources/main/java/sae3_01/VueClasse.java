package sae3_01;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * VueClasse est une classe qui hérite de VBox et qui implémente l'interface Observateur.
 * Elle permet de représenter graphiquement une classe.
 */
public class VueClasse extends VBox implements Observateur {

    /** Nom de la classe */
    private String nom;
    /** Coordonnée x de la classe */
    private double x;
    /** Coordonnée y de la classe */
    private double y;
    /** Indique si le menu contextuel est affiché */
    private boolean contextMenuShown = false;

    /**
     * Constructeur de VueClasse.
     */
    public VueClasse() {
        this.setStyle("-fx-background-color: yellow; -fx-border-color: black;");
        this.activerDragAndDrop();
    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;
        String nomClasse = this.nom.substring(this.nom.lastIndexOf(".") + 1);
        this.getChildren().clear();

        // Header
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(
                new Label(model.getClasse(nomClasse).getPackage()),
                new Label(model.getClasse(nomClasse).getType()),
                new Label(this.nom)
        );

        // Attributs
        VBox attributs = createVBox(model.getClasse(nomClasse).getAttributs());

        // Méthodes
        VBox methodes = createVBox(model.getClasse(nomClasse).getMethodes());

        // Séparations
        Line separation1 = createSeparator();
        Line separation2 = createSeparator();

        // Position de la vue (this)
        this.setLayoutX(model.getClasse(nomClasse).getX());
        this.setLayoutY(model.getClasse(nomClasse).getY());

        this.getChildren().addAll(header, separation1, attributs, separation2, methodes);
    }

    /**
     * Crée une VBox à partir d'une liste de String.
     * @param items Liste de String
     * @return VBox
     */
    private VBox createVBox(ArrayList<String> items) {
        VBox vBox = new VBox();
        if (items.isEmpty()) {
            vBox.getChildren().add(new Label("")); // Pour éviter que la VBox ne se réduise à 0
        } else {
            for (String item : items) {
                if (item.contains("{abstract}")) {
                    item = item.replace("{abstract}", "");
                    Label label = new Label(item);
                    label.setStyle("-fx-font-style: italic;");
                    vBox.getChildren().add(label);
                    continue;
                }
                if (item.contains("{static}")) {
                    item = item.replace("{static}", "");
                    Label label = new Label(item);
                    label.setUnderline(true);
                    vBox.getChildren().add(label);
                    continue;
                }
                vBox.getChildren().add(new Label(item));
            }
        }
        return vBox;
    }

    /**
     * Crée un séparateur
     * @return Line
     */
    private Line createSeparator() {
        Line separator = new Line(0, 0, 400, 0); // Valeur arbitraire
        separator.setStyle("-fx-stroke: black;");
        return separator;
    }

    /**
     * Getter du nom de la classe.
     * @return Nom de la classe.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Setter du nom de la classe.
     * @param nom Nom de la classe.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Permet de drag la classe.
     */
    public void activerDragAndDrop() {
        this.setOnMousePressed(e -> {
            if (!this.isVisible()) return; // On ne veut pas déplacer une classe masquée
            this.x = e.getSceneX() - this.getLayoutX();
            this.y = e.getSceneY() - this.getLayoutY();
            this.toFront();
        });

        this.setOnMouseDragged(e -> {
            if (this.contextMenuShown || !isVisible()) return; // On ne veut pas déplacer la classe si le menu contextuel est affiché ou si la classe est masquée
            double newX = e.getSceneX() - this.x;
            double newY = e.getSceneY() - this.y;

            // Dimensions du parent
            double parentMaxX = getParent().getLayoutBounds().getWidth();
            double parentMaxY = getParent().getLayoutBounds().getHeight();

            // Dimensions de la VBox (this)
            double nodeWidth = this.getBoundsInLocal().getWidth();
            double nodeHeight = this.getBoundsInLocal().getHeight();

            boolean xValide = (newX >= 0) && (newX + nodeWidth <= parentMaxX);
            boolean yValide = (newY >= 0) && (newY + nodeHeight <= parentMaxY);

            if (xValide && yValide) {
                this.setLayoutX(newX);
                this.setLayoutY(newY);
            }
        });
    }

    /**
     * Getter de contextMenuShown.
     * @return true si le menu contextuel est affiché, false sinon.
     */
    public boolean isContextMenuShown() {
        return this.contextMenuShown;
    }

    /**
     * Setter de contextMenuShown.
     * @param contextMenuShown true si le menu contextuel est affiché, false sinon.
     */
    public void setContextMenuShown(boolean contextMenuShown) {
        this.contextMenuShown = contextMenuShown;
    }
}