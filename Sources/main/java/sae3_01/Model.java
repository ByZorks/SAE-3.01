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
            if (c1.getType().contains("interface")) {
                plantUML.append("interface ");
            } else if (c1.getType().contains("abstract")) {
                plantUML.append("abstract ");
            } else {
                plantUML.append("class ");
            }
            plantUML.append(c1.getNomExtended()).append(" {\n");

            // Attributs
            for (String attribut : c1.getAttributs()) {
                plantUML.append("\t").append(attribut).append("\n");
            }

            // Methodes
            for (String methode : c1.getMethodes()) {
                plantUML.append("\t").append(methode).append("\n");
            }
            plantUML.append("}\n");

            // Associations
            for (Classe c2 : classes) {
                String association = checkAssociation(c1, c2);
                if (association != null) {
                    associations.append(c1.getNomSimple()).append(" --> ").append(c2.getNomSimple()).append(" : ").append(association).append("\n");
                }
            }
        }
        plantUML.append("\n").append(associations).append("@enduml");
        return plantUML.toString();
    }

    /**
     * Vérifie si une association existe entre deux classes
     * @param c1 Classe 1
     * @param c2 Classe 2
     * @return Nom de l'attribut
     */
    public String checkAssociation(Classe c1, Classe c2) {
        if (c1.equals(c2)) return null;

        for (String attribut : c1.getAssociations()) {
            if (attribut.split(" ")[0].equals(c2.getNomSimple())) return attribut.split(" ")[1];
        }
        return null;
    }

    /**
     * Retourne la classe correspondant au nom
     * @param nom Nom de la classe
     * @return Classe
     */
    public Classe getClasse(String nom) {
        for (Classe c : classes) {
            if (c.getNomSimple().equals(nom)) return c;
        }
        return null;
    }

    /**
     * Retourne l'ensemble des classes
     * @return Ensemble des classes
     */
    public Set<Classe> getClasses() {
        return this.classes;
    }

    /**
     * Retourne la vue diagramme
     * @return Vue diagramme
     */
    public VueDiagramme getVueDiagramme() {
        for (Observateur o : this.observateurs) {
            if (o instanceof VueDiagramme) return (VueDiagramme) o;
        }
        return null;
    }


    /**
     * Supprime toutes les classes et leurs données associées du modèle.
     */
    public void supprimerToutesLesClasses() {
        this.classes.clear();
        getVueDiagramme().clearDiagram();
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
