package sae3_01;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Vue du diagramme de classes.
 */
public class VueDiagramme extends Pane implements Observateur {

    /** Vues du diagramme. */
    private Map<String, VueClasse> vuesClasses;
    /** Fleches de relations entre les classes. Key: classe1;classe2 */
    private Map<String, FlecheRelation> flechesRelations;

    /**
     * Constructeur.
     */
    public VueDiagramme() {
        this.vuesClasses = new HashMap<>();
        this.flechesRelations = new HashMap<>();
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
            ArrayList<String> keysToRemove = new ArrayList<>();
            // Récupère les flèches de relations associées à la classe
            for (String key : flechesRelations.keySet()) {
                if (key.contains(nomClasse)) {
                    keysToRemove.add(key);
                }
            }
            // Supprime les flèches de relations
            for (String key : keysToRemove) {
                this.getChildren().remove(flechesRelations.get(key));
                flechesRelations.remove(key);
            }
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
            for (String key : flechesRelations.keySet()) {
                if (key.startsWith(nomClasse)) {
                    String nomClasseAssociee = key.split(";")[1];
                    VueClasse vueAssociee = vuesClasses.get(nomClasseAssociee);
                    flechesRelations.get(key).setVisible(vue.isVisible() && vueAssociee.isVisible());
                } else if (key.endsWith(nomClasse)) {
                    String nomClasseAssociee = key.split(";")[0];
                    VueClasse vueAssociee = vuesClasses.get(nomClasseAssociee);
                    flechesRelations.get(key).setVisible(vue.isVisible() && vueAssociee.isVisible());
                }
            }
        }
    }

    /**
     * Masquer une vue de méthode du diagramme sans la supprimer.
     * @param nomClasse nom de la classe
     * @param nomMethode nom de la méthode
     */
    public void toggleAffichageMethode(String nomClasse, String nomMethode) {

    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;

        // Créer toutes les vues de classes
        for (Classe classe : model.getClasses()) {
            String nomClasse = classe.getNomSimple();
            if (!vuesClasses.containsKey(nomClasse)) {
                afficherClasse(nomClasse);
                vuesClasses.get(nomClasse).actualiser(s);
            }
        }

        // Créer toutes les relations
        for (Classe classe : model.getClasses()) {
            String nomClasse = classe.getNomSimple();
            VueClasse vueClasse = vuesClasses.get(nomClasse);

            // Relations d'héritage
            if (!classe.getParent().equals("Object") && vuesClasses.containsKey(classe.getParent())) {
                String key = nomClasse + ";" + classe.getParent();
                if (!flechesRelations.containsKey(key)) {
                    VueClasse vueClasseParent = vuesClasses.get(classe.getParent());
                    FlecheHeritage flecheHeritage = new FlecheHeritage(vueClasseParent);
                    connecterFleche(flecheHeritage, vueClasse, vueClasseParent);
                    flechesRelations.put(key, flecheHeritage);
                    this.getChildren().add(flecheHeritage);
                    flecheHeritage.toBack();
                }
            }

            // Relations d'implémentation
            for (String nomInterface : classe.getInterfaces()) {
                if (vuesClasses.containsKey(nomInterface)) {
                    String key = nomClasse + ";" + nomInterface;
                    if (!flechesRelations.containsKey(key)) {
                        VueClasse vueInterface = vuesClasses.get(nomInterface);
                        FlecheImplementation flecheImplementation = new FlecheImplementation(vueInterface);
                        connecterFleche(flecheImplementation, vueClasse, vueInterface);
                        flechesRelations.put(key, flecheImplementation);
                        this.getChildren().add(flecheImplementation);
                        flecheImplementation.toBack();
                    }
                }
            }

            // Relations d'association
            for (String association : classe.getAssociations()) {
                String nomClasseCible = association.split(" ")[0];
                if (vuesClasses.containsKey(nomClasseCible)) {
                    String key = nomClasse + ";" + nomClasseCible;
                    if (!flechesRelations.containsKey(key)) {
                        VueClasse vueClasseAssociee = vuesClasses.get(nomClasseCible);
                        System.out.println(association);
                        FlecheAssociation flecheAssociation;
                        if (association.endsWith("[*]")) {
                            flecheAssociation = new FlecheAssociation(vueClasseAssociee, association.split(" ")[1], true);
                        } else {
                            flecheAssociation = new FlecheAssociation(vueClasseAssociee, association.split(" ")[1], false);
                        }
                        connecterFleche(flecheAssociation, vueClasse, vueClasseAssociee);
                        flechesRelations.put(key, flecheAssociation);
                        this.getChildren().add(flecheAssociation);
                        flecheAssociation.toBack();
                    }
                }
            }
        }
    }

    /**
     * Connecte une flèche entre deux vues de classes.
     * @param fleche La flèche à connecter
     * @param source La vue de classe source
     * @param cible La vue de classe cible
     */
    private void connecterFleche(FlecheRelation fleche, VueClasse source, VueClasse cible) {
        fleche.x1Property().bind(source.layoutXProperty().add(source.widthProperty().divide(2)));
        fleche.y1Property().bind(source.layoutYProperty().add(source.heightProperty().divide(2)));
        fleche.x2Property().bind(cible.layoutXProperty().add(cible.widthProperty().divide(2)));
        fleche.y2Property().bind(cible.layoutYProperty().add(cible.heightProperty().divide(2)));
    }

    /**
     * Supprime toutes les vues de classes et les relations du diagramme.
     */
    public void clearDiagram() {
        this.getChildren().clear();       // Supprime tout du panneau graphique
        this.vuesClasses.clear();         // Vide la collection des vues de classes
        this.flechesRelations.clear();    // Vide la collection des relations
    }
}