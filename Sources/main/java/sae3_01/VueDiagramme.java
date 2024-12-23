package sae3_01;

import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

/**
 * Vue du diagramme de classes.
 */
public class VueDiagramme extends Pane implements Observateur {

    /** Modèle associé à la vue. */
    private Model model;
    /** Vues du diagramme. */
    private Map<String, VueClasse> vuesClasses; // Pour garder trace des vues créées

    /**
     * Constructeur.
     * @param model Modèle associé à la vue.
     */
    public VueDiagramme(Model model) {
        this.model = model;
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
        vueClasse.actualiser(model);
        model.enregistrerObservateur(vueClasse);
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
            Classe classe = new Classe(nomClasse);
            model.supprimerObservateur(vue);
            model.supprimerClasse(classe);
            this.getChildren().remove(vue);
        }
    }

    @Override
    public void actualiser(Sujet s) {
        // Rien à faire
    }
}