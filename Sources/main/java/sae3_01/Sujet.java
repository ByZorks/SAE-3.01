package sae3_01;

/**
 * Interface Sujet
 */
public interface Sujet {

    /**
     * Méthode permettant d'enregistrer un observateur
     * @param o l'observateur à enregistrer
     */
    void enregistrerObservateur(Observateur o);

    /**
     * Méthode permettant de supprimer un observateur
     * @param o l'observateur à supprimer
     */
    void supprimerObservateur(Observateur o);

    /**
     * Méthode permettant de notifier tous les observateurs
     */
    void notifierObservateurs();

}
