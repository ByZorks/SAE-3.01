package sae3_01;

/**
 * Représente une flèche d'héritage entre deux classes dans un diagramme UML.
 */
public class FlecheHeritage extends FlecheRelation {

    /**
     * Crée une nouvelle flèche d'héritage entre deux classes dans le diagramme.
     * @param destination La vue de classe vers laquelle pointe la flèche d'héritage (classe parente).
     */
    public FlecheHeritage(VueClasse destination) {
        super(destination);
        // Style de la tête de flèche : triangle blanc
        head.setStyle("-fx-fill: white;");
    }

    /**
     * Met à jour la position et l'apparence de la flèche d'héritage.
     * Cette méthode ajuste automatiquement la ligne reliant les deux classes
     * et dessine la tête triangulaire représentant l'héritage.
     * Elle est appelée lors de la modification des coordonnées des classes liées.
     */
    @Override
    protected void update() {
        // Calculer les coordonnées de fin ajustées pour le placement précis de la flèche
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

        // Calcul de l'angle pour orienter correctement la tête de flèche
        double theta = Math.atan2(y2 - y1, x2 - x1);

        // Dessin du triangle de la tête de flèche (premier côté)
        head.getPoints().setAll(x2, y2); // Point de départ (pointe de la flèche)
        double x = x2 - Math.cos(theta + ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        double y = y2 - Math.sin(theta + ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        head.getPoints().addAll(x, y); // Premier trait du triangle

        // Dessin du deuxième côté du triangle
        x = x2 - Math.cos(theta - ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        y = y2 - Math.sin(theta - ARROW_HEAD_ANGLE) * ARROW_HEAD_LENGTH;
        head.getPoints().addAll(x, y); // Deuxième trait du triangle

        // Fermeture du triangle (retour au sommet de la flèche)
        head.getPoints().addAll(x2, y2);
    }
}
