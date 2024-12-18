package sae3_01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
        Pane diagramme = new Pane();

        Repertoire rootDir = new Repertoire(new File("Target/"));
        TreeItem<FileComposite> treeRoot = new TreeItem<>(rootDir);

        // Création des modèles
        Model model = new Model(rootDir);

        // Création des observateurs
        VueDiagramme vueDiagramme = new VueDiagramme();
        VueArborescence vueArborescence = new VueArborescence(treeRoot, rootDir);
        VueDiagrammeConsole vueDiagrammeConsole = new VueDiagrammeConsole();

        // Enregistrement des observateurs
        model.enregistrerObservateur(vueDiagramme);
        model.enregistrerObservateur(vueArborescence);
        model.enregistrerObservateur(vueDiagrammeConsole);

        // Création des contrôleurs
        ControllerDiagramme controllerDiagramme = new ControllerDiagramme(model);
        ControllerArborescence controllerArborescence = new ControllerArborescence(model);

        // Assignation des contrôleurs
        vueArborescence.setOnMouseClicked(controllerArborescence);

        // Affichage
        root.getChildren().addAll(vueArborescence, diagramme);
        stage.setScene(new Scene(root));
        stage.show();
    }
}