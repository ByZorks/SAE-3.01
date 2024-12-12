package sae3_01;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class Interface extends Application {

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Création fenetre
        stage.setTitle("SAE3-01");
        HBox root = new HBox();

        // Création des classes
        Classe c1 = new Classe("Classe1");
        Classe c2 = new Classe("Classe2");

        Repertoire r1 = new Repertoire(new File("C:/"));

        // Création des modèles
        ModelDiagramme modelDiagramme = new ModelDiagramme();
        ModelArborescence modelArborescence = new ModelArborescence(r1);

        // Création des observateurs
        VueDiagramme vueDiagramme = new VueDiagramme();
        VueArborescence vueArborescence = new VueArborescence();

        // Enregistrement des observateurs
        modelDiagramme.enregistrerObservateur(vueDiagramme);
        modelArborescence.enregistrerObservateur(vueArborescence);

        // Affichage
        stage.show();
    }
}
