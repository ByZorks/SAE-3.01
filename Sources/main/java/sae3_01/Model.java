package sae3_01;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe Model
 */
public class Model implements Sujet {

    /** Ensemble des classes */
    private Set<Classe> classes;
    /** Liste des observateurs */
    private ArrayList<Observateur> observateurs;

    /**
     * Constructeur
     */
    public Model() {
        this.classes = new HashSet<>();
        this.observateurs = new ArrayList<>();
    }

    /**
     * Analyse une classe
     * @param nomClasse Nom de la classe
     * @return Classe
     */
    public Classe analyserClasse(String nomClasse) {
        Classe c = null;
        try {
            c = Analyseur.analyseClasse(nomClasse);
        } catch (ClassNotFoundException e) {
            System.out.println("Classe non trouvée");
        }
        return c;
    }

    /**
     * Ajoute une classe
     * @param classe Classe
     */
    public void ajouterClasse(Classe classe) {
        this.classes.add(classe);
        notifierObservateurs();
    }

    /**
     * Supprime une classe
     * @param classe Classe
     */
    public void supprimerClasse(Classe classe) {
        this.classes.remove(classe);
        notifierObservateurs();
    }

    /**
     * Génère le code PlantUML
     * @return Code PlantUML
     */
    public String genererPlantUML() {
        StringBuilder plantUML = new StringBuilder();
        StringBuilder associations = new StringBuilder();
        plantUML.append("@startuml\n");
        for (Classe c1 : classes) {
            plantUML.append("class ").append(c1.getNomExtended()).append(" {\n");
            // Attributs
            for (String attribut : c1.getAttributs()) {
                plantUML.append(attribut).append("\n");
            }

            // Methodes
            for (String methode : c1.getMethodes()) {
                plantUML.append(methode).append("\n");
            }
            plantUML.append("}\n");

            // Associations
            for (Classe c2 : classes) {
                if (checkAssociation(c1, c2)) {
                    associations.append(c1.getNomSimple()).append(" --> ").append(c2.getNomSimple()).append("\n");
                }
            }
        }
        plantUML.append(associations).append("@enduml");
        return plantUML.toString();
    }

    /**
     * Vérifie si une association existe entre deux classes
     * @param c1 Classe 1
     * @param c2 Classe 2
     * @return Vrai si une association existe, faux sinon
     */
    public boolean checkAssociation(Classe c1, Classe c2) {
        if (c1.equals(c2)) return false;
        String[] attributs = Analyseur.getDetailledFieldType(c1.getPackage() + "." + c1.getNomSimple());
        for (String attribut : attributs) {
            if (attribut.contains(c2.getNomSimple())) return true;
        }
        return false;
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
