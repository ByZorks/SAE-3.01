package sae3_01;

/**
 * Représente une flèche d'implémentation entre deux classes
 */
public class FlecheImplementation extends FlecheHeritage {

    public FlecheImplementation(VueClasse destination) {
        super(destination);
        line.setStyle("-fx-stroke-dash-array: 5 5;");
    }
}
