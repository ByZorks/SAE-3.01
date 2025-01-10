package sae3_01;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * Classe permettant de représenter une classe Java
 */
public class Classe implements Serializable {

    /** Type de la classe */
    private String type;
    /** Nom simple de la classe */
    private String nomSimple;
    /** Package de la classe */
    private String Package;
    /** Liste des attributs */
    private ArrayList<String> attributs;
    /** Liste des méthodes */
    private ArrayList<String> methodes;
    /** Coordonnées de la classe */
    private double[] coordonnees;
    /** Relations de la classe */
    private HashMap<String, ArrayList<String>> relations;

    /**
     * Constructeur
     * @param type Type de la classe
     * @param nomSimple Nom simple de la classe
     * @param Package Package de la classe
     * @param attributs Liste des attributs
     * @param methodes Liste des méthodes
     * @param coordonnees Coordonnées de la classe
     */
    public Classe(String type, String nomSimple, String Package, ArrayList<String> attributs, ArrayList<String> methodes, double[] coordonnees, HashMap<String, ArrayList<String>> relations) {
        this.type = type;
        this.nomSimple = nomSimple;
        this.Package = Package;
        this.coordonnees = coordonnees;
        this.attributs = attributs;
        this.methodes = methodes;
        this.relations = relations;
    }

    /**
     * Retourne le type de la classe
     * @return Type de la classe
     */
    public String getType() {
        return type;
    }

    /**
     * Retourne le nom simple de la classe
     * @return Nom simple de la classe
     */
    public String getNomSimple() {
        return nomSimple;
    }

    /**
     * Retourne le nom de la classe avec ses interfaces et sa classe mère
     * @return Nom de la classe
     */
    public String getNomExtended() {
        // Nom de la classe
        StringBuilder res = new StringBuilder(nomSimple);

        // ajout du parent
        if (!relations.get("parent").getFirst().equals("Object")) {
            res.append(" extends ").append(relations.get("parent").getFirst());
        }

        // ajout des interfaces
        if (!relations.get("interface").isEmpty()) {
            res.append(" implements ");
            for (String i : relations.get("interface")) {
                res.append(i).append(", ");
            }
            res = new StringBuilder(res.substring(0, res.length() - 2)); // Supprime la dernière virgule
        }

        return res.toString();
    }

    /**
     * Retourne le package de la classe
     * @return Package de la classe
     */
    public String getPackage() {
        return Package;
    }

    /**
     * Retourne la liste des attributs
     * @return Liste des attributs
     */
    public ArrayList<String> getAttributs() {
        return attributs;
    }

    /**
     * Retourne la liste des méthodes
     * @return Liste des méthodes
     */
    public ArrayList<String> getMethodes() {
        return methodes;
    }

    /**
     * Retourne la coordonnée x de la classe
     * @return Coordonnée x
     */
    public double getX() {
        return coordonnees[0];
    }

    /**
     * Retourne la coordonnée y de la classe
     * @return Coordonnée y
     */
    public double getY() {
        return coordonnees[1];
    }

    /**
     * Modifie les coordonnées de la classe
     * @param x Coordonnée x
     * @param y Coordonnée y
     */
    public void setCoordonnees(double x, double y) {
        this.coordonnees[0] = x;
        this.coordonnees[1] = y;
    }

    /**
     * Retourne le nom de la classe mère
     * @return Nom de la classe mère
     */
    public String getParent() {
        return relations.get("parent").getFirst();
    }

    /**
     * Retourne les interfaces implémentées par la classe
     * @return Interfaces implémentées
     */
    public ArrayList<String> getInterfaces() {
        return relations.get("interface");
    }

    /**
     * Retourne les associations de la classe
     * @return Associations
     */
    public ArrayList<String> getAssociations() {
        return relations.get("associations");
    }

    /**
     * Compare cette classe à une autre basée sur leur nom simple.
     * @param o Objet à comparer
     * @return true si les classes ont le même nom simple, sinon false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Classe classe = (Classe) o;
        return Objects.equals(nomSimple, classe.nomSimple);
    }

    /**
     * Génère un code de hachage basé sur le nom simple de la classe.
     * @return Code de hachage
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(nomSimple);
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la classe,
     * incluant toutes ses propriétés principales.
     * @return Représentation textuelle de la classe
     */
    @Override
    public String toString() {
        return "Classe {" +
                "\ntype=" + type +
                "\nnom=" + nomSimple +
                "\nPackage=" + Package +
                "\nattributs=" + attributs +
                "\nmethodes=" + methodes +
                "\ncoordonnees=" + Arrays.toString(coordonnees) +
                "\nrelations=" + relations +
                "\n}";
    }

    /**
     * Retourne les coordonnées de la classe sous forme d'un tableau de doubles.
     * @return Un tableau contenant les coordonnées de la classe sous la forme [x, y].
     */
    public double[] getCoordonnees() {
        return coordonnees;
    }

    /**
     * Retourne les relations de la classe sous forme d'une HashMap.
     * @return Une HashMap
     */
    public HashMap<String, ArrayList<String>> getRelations() {
        return relations;
    }
}
