package sae3_01;

import java.util.ArrayList;

public class ModelArborescence implements Sujet {

    private FileComposite arborescence;
    private ArrayList<Observateur> observateurs;

    public ModelArborescence(FileComposite arborescence) {
        this.arborescence = arborescence;
        this.observateurs = new ArrayList<>();
    }

    public void updateArborescence() {
        this.arborescence.actualiser();
    }

    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateurs() {
        for (Observateur o : this.observateurs) o.actualiser();
    }
}
