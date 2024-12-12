package sae3_01;

import java.util.ArrayList;
import java.util.HashMap;

public class Classe {

    // Attributs
    private String type;
    private String nom;
    private String Package;

    private HashMap<String, Integer> attributs;

    private HashMap<String, Integer> methodes;
    private int[] coordonnees;


    //Constructeur
    public Classe(String type, String nom, String Package, ArrayList<String> attributs, ArrayList<String> methodes, int[] coordonnees){
        this.type = type;
        this.nom = nom;
        this.Package = Package;
        this.coordonnees = coordonnees;
        this.attributs = new HashMap<>();
        this.methodes = new HashMap<>();
    }

    public Classe(String classe1) {
        this.type = classe1;
        this.nom = classe1;
        this.Package = classe1;
        this.attributs = new HashMap<>();
        this.methodes = new HashMap<>();

    }


    //Getter
    public String getType(){
        return type;
    }

    public String getNom(){
        return nom;
    }

    public String getPackage(){
        return Package;
    }

    public ArrayList<String> getAttributs(){
        return new ArrayList<>(attributs.keySet());
    }

    public ArrayList<String> getMethodes() {
        return new ArrayList<>(methodes.keySet());
    }

    public int[] getCoordonnees(){
        return coordonnees;
    }


    //Setter
    public void SetCoordonnees(int x, int y){
        this.coordonnees[0] = x;
        this.coordonnees[1] = y;
    }
}
