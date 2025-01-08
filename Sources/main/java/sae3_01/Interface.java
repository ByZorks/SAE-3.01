package sae3_01;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;

import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
        Menu menu = new Menu("Exporter");

        //Exportation en format PNG
        MenuItem item = new MenuItem("PNG");
        //Ajout du logo PNG
        ImageView pngIcon = new ImageView("file:image/png.png");
        pngIcon.setFitHeight(25);
        pngIcon.setFitWidth(25);
        item.setGraphic(pngIcon);
        item.setOnAction(event -> {
            capturePane(vueDiagramme, "image/diagramme.png");
        });

        //Exportation en JPG
        MenuItem item2 = new MenuItem("JPG");
        //Ajout du logo JPG
        ImageView jpgIcon = new ImageView("file:image/jpg.png");
        jpgIcon.setFitHeight(25);
        jpgIcon.setFitWidth(25);
        item2.setGraphic(jpgIcon);
        item2.setOnAction(event -> {
            capturePane(vueDiagramme, "image/diagramme.jpg");
        });

        // Exportation en html
        MenuItem item5 = new MenuItem("HTML");
        ImageView htmlIcon = new ImageView("file:image/html.png");
        htmlIcon.setFitWidth(25);
        htmlIcon.setFitHeight(25);
        item5.setGraphic(htmlIcon);
        item5.setOnAction(event -> {
            capturePaneAsHTML(vueDiagramme, "diagramme.html");
        });
        menu.getItems().add(item5);


        //Exportation en PlantUML
        MenuItem item4 = new MenuItem("PUML");
        //Ajout du logo PlantUML
        ImageView pumlIcon = new ImageView("file:image/puml.png");
        pumlIcon.setFitHeight(25);
        pumlIcon.setFitWidth(25);
        item4.setGraphic(pumlIcon);
        item4.setOnAction(event -> {
            capturePane(vueDiagramme, "image/diagramme.jpg");
        });

        menu.getItems().addAll(item, item2,item4);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        root.setTop(menuBar);

        // Gestion de l'exportation en fonction du choix du ComboBox
        item4.setOnAction(event -> {
                exporterPlantUML(model);
        });

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

        vueDiagramme.setOnDragExited(dragEvent -> {
            vueDiagramme.setStyle("-fx-opacity:1;-fx-background-color: white;");
            dragEvent.consume();
        });

        // Ajout des composants à l'interface
        root.setTop(menuBar);
        root.setCenter(splitPane);

        // Création de la scène
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Capture un Pane et sauvegarde le screenshot dans un fichier.
     * @param pane Pane à capturer
     * @param filePath Chemin du fichier de sauvegarde
     */
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
            Desktop.getDesktop().open(new File(filePath));
            System.out.println("Screenshot sauvegardé avec succès : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du screenshot : " + e.getMessage());
        }
    }

    /**
     * Exporte le modèle sous forme de fichier texte contenant le code PlantUML.
     * @param model Le modèle contenant les classes à exporter.
     */
    private void exporterPlantUML(Model model) {
        String plantUMLCode = model.genererPlantUML();
        File fichierExport = new File("Model_PlantUML.puml");

        try (FileWriter writer = new FileWriter(fichierExport)) {
            writer.write(plantUMLCode);
            System.out.println("Exportation PlantUML réussie : " + fichierExport.getAbsolutePath());
            Desktop.getDesktop().open(new File(String.valueOf(fichierExport)));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'exportation du fichier PlantUML : " + e.getMessage());
        }
    }

    /**
     * Capture un Pane et exporte son contenu dans un fichier HTML.
     * @param pane Pane à capturer
     * @param filePath Chemin du fichier HTML de sauvegarde
     */
    public void capturePaneAsHTML(Pane pane, String filePath) {
        try {
            // Prendre un snapshot du Pane
            SnapshotParameters params = new SnapshotParameters();
            WritableImage fxImage = pane.snapshot(params, null);

            // Sauvegarder l'image en tant que fichier temporaire PNG
            File tempImageFile = new File("temp_image.png");
            BufferedImage bufferedImage = new BufferedImage(
                    (int) fxImage.getWidth(),
                    (int) fxImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            PixelReader pixelReader = fxImage.getPixelReader();
            for (int x = 0; x < fxImage.getWidth(); x++) {
                for (int y = 0; y < fxImage.getHeight(); y++) {
                    bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
                }
            }
            ImageIO.write(bufferedImage, "png", tempImageFile);

            // Créer un fichier HTML et intègre l'image
            File htmlFile = new File(filePath);
            try (FileWriter writer = new FileWriter(htmlFile)) {
                writer.write("<!DOCTYPE html>\n");
                writer.write("<html>\n");
                writer.write("<head>\n");
                writer.write("<title>Exportation HTML</title>\n");
                writer.write("</head>\n");
                writer.write("<body>\n");
                writer.write("<h1>Votre diagramme !</h1>\n");
                writer.write("<img src=\"temp_image.png\" alt=\"Diagramme\">\n");
                writer.write("</body>\n");
                writer.write("</html>");
            }

            System.out.println("HTML exporté avec succès : " + filePath);
            Desktop.getDesktop().open(htmlFile);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'export en HTML : " + e.getMessage());
        }
    }


}





