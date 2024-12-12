package sae3_01;

import javafx.application.Application;
import javafx.scene.Scene;
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

        Repertoire rootDir = new Repertoire(new File("C:\\Users\\David\\Documents\\GitHub\\SAE-3.01"));

        // Création des modèles
        ModelDiagramme modelDiagramme = new ModelDiagramme();
        ModelArborescence modelArborescence = new ModelArborescence(rootDir);

        // Création des observateurs
        VueDiagramme vueDiagramme = new VueDiagramme();
        VueArborescence vueArborescence = new VueArborescence(rootDir);

        // Enregistrement des observateurs
        modelDiagramme.enregistrerObservateur(vueDiagramme);
        modelArborescence.enregistrerObservateur(vueArborescence);

        // tests
        modelArborescence.updateArborescence();

        // Affichage
        root.getChildren().addAll(vueArborescence);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
