package sae3_01;

import java.util.ArrayList;
import java.util.HashMap;

public class Classe {

    // Attributs
    private String type;
    private String nom;
    private String Package;
    private ArrayList<String> attributs;
    private ArrayList<String> methodes;
    private int[] coordonnees;

    //Constructeur
    public Classe(String type, String nom, String Package, ArrayList<String> attributs, ArrayList<String> methodes, int[] coordonnees) {
        this.type = type;
        this.nom = nom;
        this.Package = Package;
        this.coordonnees = coordonnees;
        this.attributs = attributs;
        this.methodes = methodes;
    }

    //Getter
    public String getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }

    public String getPackage() {
        return Package;
    }

    public ArrayList<String> getAttributs() {
        return attributs;
    }

    public ArrayList<String> getMethodes() {
        return methodes;
    }

    public int[] getCoordonnees() {
        return coordonnees;
    }

    //Setter
    public void SetCoordonnees(int x, int y) {
        this.coordonnees[0] = x;
        this.coordonnees[1] = y;
    }
}
