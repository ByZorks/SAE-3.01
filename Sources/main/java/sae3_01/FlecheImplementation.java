package sae3_01;

/**
 * Représente une flèche d'implémentation entre deux classes
 */
public class FlecheImplementation extends FlecheHeritage {

    /**
     * Crée une nouvelle flèche d'implémentation entre deux classes dans le diagramme.
     * @param destination La vue de classe vers laquelle pointe la flèche d'implémentation.
     */
    public FlecheImplementation(VueClasse destination) {
        super(destination);
        line.setStyle("-fx-stroke-dash-array: 5 5;");
    }
}
