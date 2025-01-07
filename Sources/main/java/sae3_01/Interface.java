package sae3_01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Classe Interface
 * Classe principale de l'application, elle gère l'interface graphique
 */
public class Interface extends Application {

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) {
        // Création des modèles
        Model model = new Model();

        // Création de la fenêtre
        stage.setTitle("SAE3-01");
        BorderPane root = new BorderPane();
        ComboBox<String> comboExport = new ComboBox<>();

        // Création de la vue diagramme
        VueDiagramme vueDiagramme = new VueDiagramme();

        // Création de la barre de menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Arborescence :");
        menuBar.getMenus().add(fileMenu);

        // Création du comboBox pour l'exportation
        comboExport.getItems().addAll("Exporter", "PDF", "JPEG", "PNG");
        comboExport.getSelectionModel().selectFirst();
        comboExport.setLayoutX(45);
        comboExport.setLayoutY(45);
        root.setTop(menuBar);
        root.setRight(comboExport);

        // Configuration du contenu principal
        Repertoire rootDir = new Repertoire(new File("Target/"));
        TreeItem<FileComposite> treeRoot = new TreeItem<>(rootDir);

        // Création des observateurs
        VueArborescence vueArborescence = new VueArborescence(treeRoot, rootDir);
        VueDiagrammeConsole vueDiagrammeConsole = new VueDiagrammeConsole();

        // Enregistrement des observateurs
        model.enregistrerObservateur(vueArborescence);
        model.enregistrerObservateur(vueDiagrammeConsole);
        model.enregistrerObservateur(vueDiagramme);

        // Création des contrôleurs
        ControllerArborescence controllerArborescence = new ControllerArborescence(model);
        ControllerContextMenu controllerContextMenu = new ControllerContextMenu(model);

        // Assignation des contrôleurs
        vueArborescence.setOnMouseClicked(controllerArborescence);
        vueArborescence.setOnContextMenuRequested(controllerContextMenu);
        vueDiagramme.setOnContextMenuRequested(controllerContextMenu);

        // Utilisation d'un SplitPane pour permettre le redimensionnement
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(vueArborescence, vueDiagramme);
        splitPane.setDividerPositions(0.2); // Position initiale du diviseur

        // Gestion du drag and drop avec l'arborescence et le diagramme
        vueDiagramme.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != vueDiagramme &&
                    dragEvent.getDragboard().hasString()) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
            dragEvent.consume();
        });

        vueDiagramme.setOnDragEntered(dragEvent -> {
            if (dragEvent.getGestureSource() != vueDiagramme && dragEvent.getDragboard().hasString()) {
                vueDiagramme.setStyle("-fx-opacity:.4;-fx-background-color: gray;");
            }
            dragEvent.consume();
        });

        vueDiagramme.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String nomFichier = db.getString().replace(".class", "");
                TreeItem<FileComposite> selectedItem = vueArborescence.getSelectionModel().getSelectedItem();
                String nomFichierAvecPackage = selectedItem.getValue().getParentFolderName() + "." + nomFichier;
                Classe c = model.analyserClasse(nomFichierAvecPackage);
                c.setCoordonnees(dragEvent.getX(), dragEvent.getY());
                model.ajouterClasse(c);
                success = true;
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
            vueDiagramme.setStyle("-fx-opacity:1;-fx-background-color: white;");
        });

        // Ajout des composants à l'interface
        root.setTop(menuBar);
        root.setCenter(splitPane);



        // Création de la scène
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
        javafx.scene.control.Button buttonExport = new javafx.scene.control.Button("Exporter");

        root.setBottom(buttonExport);
        buttonExport.setOnMouseClicked(event -> {
            capturePane(vueDiagramme, "image/diagramme.png");
        });

    }

    public void capturePane(Pane pane, String filePath) {
        try {
            // Créer les paramètres du snapshot
            SnapshotParameters params = new SnapshotParameters();

            // Prendre le screenshot du Pane
            WritableImage fxImage = pane.snapshot(params, null);

            // Convertir l'image JavaFX en BufferedImage
            BufferedImage bufferedImage = new BufferedImage(
                    (int) fxImage.getWidth(),
                    (int) fxImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            PixelReader pixelReader = fxImage.getPixelReader();

            // Copier les pixels
            for (int x = 0; x < fxImage.getWidth(); x++) {
                for (int y = 0; y < fxImage.getHeight(); y++) {
                    bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
                }
            }

            // Sauvegarder l'image
            ImageIO.write(bufferedImage, "png", new File(filePath));

            System.out.println("Screenshot sauvegardé avec succès : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du screenshot : " + e.getMessage());
        }
    }



}

