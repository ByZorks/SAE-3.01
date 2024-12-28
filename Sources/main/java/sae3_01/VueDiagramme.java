package sae3_01;

import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

/**
 * Vue du diagramme de classes.
 */
public class VueDiagramme extends Pane implements Observateur {

    /** Vues du diagramme. */
    private Map<String, VueClasse> vuesClasses; // Pour garder trace des vues créées

    /**
     * Constructeur.
     */
    public VueDiagramme() {
        this.vuesClasses = new HashMap<>();
    }

    /**
     * Créer une vue pour une classe et l'ajouter au diagramme.
     * @param nomClasse Nom de la classe.
     */
    public void afficherClasse(String nomClasse) {
        if (vuesClasses.containsKey(nomClasse)) return;
        VueClasse vueClasse = new VueClasse();
        vueClasse.setNom(nomClasse);
        vuesClasses.put(nomClasse, vueClasse);
        this.getChildren().add(vueClasse);
    }

    /**
     * Retirer une vue de classe du diagramme.
     * @param nomClasse Nom de la classe.
     */
    public void retirerClasse(String nomClasse) {
        VueClasse vue = vuesClasses.remove(nomClasse);
        if (vue != null) {
            this.getChildren().remove(vue);
        }
    }

    /**
     * Récupérer une vue de classe du diagramme.
     * @param nomClasse Nom de la classe.
     * @return Vue de la classe.
     */
    public VueClasse getVueClasse(String nomClasse) {
        return vuesClasses.get(nomClasse);
    }

    /**
     * Masquer une vue de classe du diagramme sans la supprimer.
     * @param nomClasse Nom de la classe.
     */
    public void toggleAffichageClasse(String nomClasse) {
        VueClasse vue = vuesClasses.get(nomClasse);
        if (vue != null) {
            vue.setVisible(!vue.isVisible());
        }
    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;
        for (Classe classe : model.getClasses()) {
            String nomClasse = classe.getNomSimple();
            if (!vuesClasses.containsKey(nomClasse)) {
                afficherClasse(nomClasse);
                vuesClasses.get(nomClasse).actualiser(s);
            }
        }
    }
}