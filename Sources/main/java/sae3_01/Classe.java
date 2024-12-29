package sae3_01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Classe permettant de représenter une classe Java
 */
public class Classe {

    /** Type de la classe */
    private String type;
    /** Nom simple de la classe */
    private String nomSimple;
    /** Nom de la classe avec ses interfaces et sa classe mère */
    private String nomExtended;
    /** Package de la classe */
    private String Package;
    /** Liste des attributs */
    private ArrayList<String> attributs;
    /** Liste des méthodes */
    private ArrayList<String> methodes;
    /** Coordonnées de la classe */
    private double[] coordonnees;

    /**
     * Constructeur
     * @param type Type de la classe
     * @param nomSimple Nom simple de la classe
     * @param nomExtended Nom de la classe avec ses interfaces et sa classe mère
     * @param Package Package de la classe
     * @param attributs Liste des attributs
     * @param methodes Liste des méthodes
     * @param coordonnees Coordonnées de la classe
     */
    public Classe(String type, String nomSimple, String nomExtended, String Package, ArrayList<String> attributs, ArrayList<String> methodes, double[] coordonnees) {
        this.type = type;
        this.nomSimple = nomSimple;
        this.nomExtended = nomExtended;
        this.Package = Package;
        this.coordonnees = coordonnees;
        this.attributs = attributs;
        this.methodes = methodes;
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
        return nomExtended;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Classe classe = (Classe) o;
        return Objects.equals(nomSimple, classe.nomSimple);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nomSimple);
    }

    @Override
    public String toString() {
        return "Classe {" +
                "\ntype=" + type +
                "\nnom=" + nomExtended +
                "\nPackage=" + Package +
                "\nattributs=" + attributs +
                "\nmethodes=" + methodes +
                "\ncoordonnees=" + Arrays.toString(coordonnees) +
                "\n}";
    }
}
