package sae3_01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static jdk.xml.internal.SecuritySupport.getResourceAsStream;

public class Interface extends Application {

    public static void main(String[] args) {
        Application.launch();
    }



    @Override
    public void start(Stage stage) throws Exception {
        // Création de la fenêtre
        stage.setTitle("SAE3-01");
        BorderPane root = new BorderPane();
        HBox content = new HBox();
        Pane diagramme = new Pane();
        ComboBox comboExport = new ComboBox();






        // Création de la barre de menu
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("Arborescence :");


        menuBar.getMenus().add(fileMenu);


        // Ajout d'une icône de poubelle (plus tard)
        Button buttonSuppr = new Button("Supprimer");
        buttonSuppr.setLayoutX(50);
        buttonSuppr.setLayoutY(10);
        root.setBottom(buttonSuppr);





        //Création du comboBox pour l'exportation
        comboExport.getItems().addAll("Exporter","PDF","JPEG","PNG");
        comboExport.getSelectionModel().selectFirst();
        comboExport.setLayoutX(45);
        comboExport.setLayoutY(45);
        root.setTop(menuBar);
        root.setCenter(diagramme);
        root.setRight(comboExport);

        // Configuration du contenu principal
        Repertoire rootDir = new Repertoire(new File("Target/"));
        TreeItem<FileComposite> treeRoot = new TreeItem<>(rootDir);

        // Création des modèles
        Model model = new Model();

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

        // Ajout des composants à l'interface
        content.getChildren().addAll(vueArborescence, diagramme);
        root.setTop(menuBar);
        root.setCenter(content);

        // Création de la scène
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

    }
    
}
