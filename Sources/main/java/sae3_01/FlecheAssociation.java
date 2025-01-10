package sae3_01;

import javafx.scene.text.Text;

/**
 * Représente une flèche d'association entre deux classes dans un diagramme UML.
 */
public class FlecheAssociation extends FlecheRelation {

    /** Texte représentant le nom de l'association affiché près de la flèche. */
    private final Text nom;

    /**
     * Constructeur de la flèche d'association.
     * Crée une flèche pointant vers la classe de destination, avec un nom et
     * la possibilité d'indiquer la multiplicité de l'association.
     * @param destination La vue de classe vers laquelle pointe la flèche d'association.
     * @param nom Le nom de l'association à afficher sur la flèche.
     * @param multiple Indique si l'association est multiple ([*]) ou unique ([1]).
     */
    public FlecheAssociation(VueClasse destination, String nom, boolean multiple) {
        super(destination);
        if (multiple) {
            this.nom = new Text("[*]  " + nom);
        } else {
            this.nom = new Text("[1]  " + nom);
        }
        this.nom.setStyle("-fx-font-size: 15;");
        this.getChildren().add(this.nom);
    }

    /**
     * Met à jour la position et l'affichage de la flèche et de son texte
     * associé. Cette méthode ajuste également la position de la tête de la flèche
     * et l'orientation du texte en fonction des coordonnées des extrémités de la flèche.
     * Cette méthode est appelée automatiquement lors des modifications
     * de la position des classes associées.
     */
    @Override
    void update() {
        // Calcul des coordonnées ajustées pour dessiner la flèche
        double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());

        double x1 = getX1();
        double y1 = getY1();
        double x2 = end[0];
        double y2 = end[1];

        // Mise à jour de la ligne principale de la flèche
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);

        // Calcul de l'angle pour orienter la tête de la flèche
        double theta = Math.atan2(y2 - y1, x2 - x1);

        // Dessiner la tête de la flèche (premier côté)
        double x = x2 - Math.cos(theta + ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        double y = y2 - Math.sin(theta + ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        head.getPoints().setAll(x, y, x2, y2); // Ajout du premier trait

        // Dessiner la tête de la flèche (deuxième côté)
        x = x2 - Math.cos(theta - ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        y = y2 - Math.sin(theta - ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        head.getPoints().addAll(x, y); // Ajout du deuxième trait

        // Positionner le texte à proximité de la flèche
        double textOffset = ARROW_HEAD_LENGTH * 3; // Décalage du texte par rapport à la pointe
        double textX = x2 - Math.cos(theta) * textOffset;
        double textY = y2 - Math.sin(theta) * textOffset;
        nom.setX(textX);
        nom.setY(textY);
    }
}
