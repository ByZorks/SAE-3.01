package sae3_01;

/**
 * Interface Observateur
 */
public interface Observateur {

    /**
     * Actualise l'observateur
     * @param s le sujet qui a changé
     */
    void actualiser(Sujet s);

}
