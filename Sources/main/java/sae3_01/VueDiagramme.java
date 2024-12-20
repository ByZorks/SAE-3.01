package sae3_01;

import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public class VueDiagramme extends Pane implements Observateur {

    private Model model;
    private Map<String, VueClasse> vuesClasses; // Pour garder trace des vues créées

    public VueDiagramme(Model model) {
        this.model = model;
        this.vuesClasses = new HashMap<>();
    }

    public void afficherClasse(String nomClasse) {
        if (vuesClasses.containsKey(nomClasse)) {
            return;
        }
        VueClasse vueClasse = new VueClasse();
        vueClasse.setNom(nomClasse);
        vueClasse.actualiser(model);
        model.enregistrerObservateur(vueClasse);
        vuesClasses.put(nomClasse, vueClasse);
        this.getChildren().add(vueClasse);
        this.getChildren().getLast().setLayoutX(vueClasse.getClasse().getCoordonnees()[0]);
        this.getChildren().getLast().setLayoutY(vueClasse.getClasse().getCoordonnees()[1]);
        vueClasse.drag(vueClasse);
    }

    public void retirerClasse(String nomClasse) {
        VueClasse vue = vuesClasses.remove(nomClasse);
        if (vue != null) {
            model.supprimerObservateur(vue);
            this.getChildren().remove(vue);
        }
    }

    @Override
    public void actualiser(Sujet s) {
        // Mise à jour si nécessaire
    }
}