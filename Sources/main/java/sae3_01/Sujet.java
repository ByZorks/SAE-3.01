package sae3_01;

public interface Sujet {

    void enregistrerObservateur(Observateur o);
    void supprimerObservateur(Observateur o);
    void notifierObservateurs();

}
