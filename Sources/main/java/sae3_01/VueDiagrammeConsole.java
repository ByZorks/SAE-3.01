package sae3_01;

/**
 * Vue du diagramme de classes en console.
 */
public class VueDiagrammeConsole implements Observateur {

    /**
     * Constructeur.
     */
    public VueDiagrammeConsole() {
    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;
        System.out.println(model.genererPlantUML());
    }
}
