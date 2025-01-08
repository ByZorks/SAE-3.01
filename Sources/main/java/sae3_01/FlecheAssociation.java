package sae3_01;

import javafx.scene.text.Text;

/**
 * Représente une flèche d'association entre deux classes
 */
public class FlecheAssociation extends FlecheRelation {

    private final Text nom;

    public FlecheAssociation(VueClasse destination, String nom) {
        super(destination);
        this.nom = new Text();
        this.nom.setText(nom);
        this.nom.setStyle("-fx-font-size: 15;");
        this.getChildren().add(this.nom);
    }

    @Override
    void update() {
        double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());

        double x1 = getX1();
        double y1 = getY1();
        double x2 = end[0];
        double y2 = end[1];

        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);

        double theta = Math.atan2(y2 - y1, x2 - x1);

        // Dessiner la tête de la flèche (premier trait)
        double x = x2 - Math.cos(theta + ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        double y = y2 - Math.sin(theta + ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        head.getPoints().setAll(x, y, x2, y2); // Ligne de x,y vers x2,y2
        // Dessiner la tête de la flèche (deuxième trait)
        x = x2 - Math.cos(theta - ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        y = y2 - Math.sin(theta - ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        head.getPoints().addAll(x, y); // ligne de x2,y2 vers x,y

        // Position texte
        double textOffset = ARROW_HEAD_LENGTH * 3; // Distance du texte par rapport à la pointe
        double textX = x2 - Math.cos(theta) * textOffset;
        double textY = y2 - Math.sin(theta) * textOffset;

        // Centre le texte par rapport à sa position
        nom.setX(textX);
        nom.setY(textY);
    }
}
