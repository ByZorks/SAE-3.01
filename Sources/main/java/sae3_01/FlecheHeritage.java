package sae3_01;

/**
 * Représente une flèche d'héritage entre deux classes
 */
public class FlecheHeritage extends FlecheRelation {

    public FlecheHeritage(VueClasse destination) {
        super(destination);
        head.setStyle("-fx-fill: white;");
    }

    protected void update() {
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
        head.getPoints().setAll(x2, y2);
        double x = x2 - Math.cos(theta + ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        double y = y2 - Math.sin(theta + ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        head.getPoints().addAll(x, y); // Ligne de x2,y2 vers x,y
        // Dessiner la tête de la flèche (deuxième trait)
        x = x2 - Math.cos(theta - ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        y = y2 - Math.sin(theta - ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        head.getPoints().addAll(x, y); // Ligne de x,y vers nouveau x,y
        // Dessiner la tête de la flèche (troisième trait)
        head.getPoints().addAll(x2, y2); // Ligne de x,y vers x2,y2
    }
}
