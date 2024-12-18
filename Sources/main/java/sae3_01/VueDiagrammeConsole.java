package sae3_01;

public class VueDiagrammeConsole implements Observateur {

    public VueDiagrammeConsole() {
    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;
        System.out.println(model.genererPlantUML());
    }
}
