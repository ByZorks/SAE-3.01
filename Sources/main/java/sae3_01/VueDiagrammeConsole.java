package sae3_01;

public class VueDiagrammeConsole implements Observateur {

    public VueDiagrammeConsole() {
    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;
        System.out.println("Diagramme :");
        for (Classe c : model.getClasses()) {
            System.out.println(c);
        }
    }
}
