package sae3_01;

import java.util.ArrayList;

public class ModelDiagramme implements Sujet {

    private ArrayList<Classe> classes;
    private Analyseur analyseur;
    private ArrayList<Observateur> observateurs;

    public ModelDiagramme() {
        this.classes = new ArrayList<>();
        this.observateurs = new ArrayList<>();
    }

    public Classe analyserClasse(String nomClasse) {
        Classe c = null;
        try {
            c = Analyseur.analyseClasse(nomClasse);
        } catch (ClassNotFoundException e) {
            System.out.println("Classe non trouvée");
        }
        return c;
    }

    public void ajouterClasse(Classe classe) {
        this.classes.add(classe);
        notifierObservateurs();
    }

    public void supprimerClasse(Classe classe) {
        this.classes.remove(classe);
        notifierObservateurs();
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
        for (Observateur o : this.observateurs) o.actualiser(this);
    }
}
