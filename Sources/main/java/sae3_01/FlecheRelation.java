package sae3_01;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

/**
 * Classe abstraite qui représente une flèche de relation entre deux classes.
 */
public abstract class FlecheRelation extends Group {

    protected Line line = new Line();
    protected Polyline head = new Polyline();
    protected SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    protected SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    protected SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    protected SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    protected VueClasse destination;
    protected final double ARROW_HEAD_ANGLE = Math.toRadians(20);
    protected final double ARROW_HEAD_LENGTH = 20;

    /**
     * Constructeur.
     * @param destination Vue de la classe de destination
     */
    public FlecheRelation(VueClasse destination) {
        this.destination = destination;
        this.getChildren().addAll(line, head);

        for (SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.y1, this.x2, this.y2}) {
            s.addListener((l,o,n) -> update() );
        }
    }

    /**
     * Redimensionne la flèche en fonction des coordonnées de départ et d'arrivée.
     * @param x1 Coordonnée x de départ
     * @param y1 Coordonnée y de départ
     * @param x2 Coordonnée x d'arrivée
     * @param y2 Coordonnée y d'arrivée
     * @return Coordonnées redimensionnées
     */
    protected double[] scale(double x1, double y1, double x2, double y2) {
        double theta = Math.atan2(y2 - y1, x2 - x1);

        // Demi-longueur et demi-largeur
        double halfWidth = destination.getWidth() / 2.0;
        double halfHeight = destination.getHeight() / 2.0;

        // Coordonnées initiales
        double x, y;

        // Calcul les coordonnées en fonction de l'angle
        double tanTheta = Math.tan(theta);

        // Quadrants et bordure
        if (Math.abs(tanTheta) <= halfHeight / halfWidth) {
            // Bord horizontal (haut ou bas)
            x = halfWidth * Math.signum(Math.cos(theta));
            y = x * Math.tan(theta);
        } else {
            // Bord vertical (gauche ou droite)
            y = halfHeight * Math.signum(Math.sin(theta));
            x = y / Math.tan(theta);
        }

        // Prend en compte la position de la destination dans le parent
        x += destination.getLayoutX() + halfWidth;
        y += destination.getLayoutY() + halfHeight;

        // Retourner les coordonnées
        return new double[]{x + Math.cos(theta), y + Math.sin(theta)};
    }

    /**
     * Met à jour la flèche.
     */
    abstract void update();

    public double getX1() {
        return x1.get();
    }

    public SimpleDoubleProperty x1Property() {
        return x1;
    }

    public double getY1() {
        return y1.get();
    }

    public SimpleDoubleProperty y1Property() {
        return y1;
    }

    public SimpleDoubleProperty x2Property() {
        return x2;
    }

    public SimpleDoubleProperty y2Property() {
        return y2;
    }
}
