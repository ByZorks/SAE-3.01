package sae3_01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Interface extends Application {

    private Model model;
    private VueDiagramme vueDiagramme;
    private VueArborescence vueArborescence;

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) {
        // Initialisation
        creerDossierOutput();
        initialiserMVC();
        configureDragAndDrop();

        // Panes
        BorderPane root = new BorderPane();
        ToolBar toolBar = new ToolBar(createMenuBar(), createClearButton(), createSaveButton());
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(vueArborescence, vueDiagramme);
        splitPane.setDividerPositions(0.2);
        splitPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            splitPane.setDividerPositions(0.2); // Empeche le diviseur de bouger lorsqu'on redimensionne la fenetre
        });

        // Placement dans la fenêtre
        root.setTop(toolBar);
        root.setCenter(splitPane);

        // Parametres du stage
        Scene scene = new Scene(root);
        stage.setTitle("SAE3-01");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Initialise les attributs et les classes du MVC
     */
    private void initialiserMVC() {
        model = new Model();
        try {
            Model save = SaveManager.load();
            model.loadSave(save);
        } catch (Exception e) {
            // Rien à faire car l'utilisateur n'as pas forcément de save
        }

        vueDiagramme = new VueDiagramme();

        // Arborescence
        Repertoire rootDir = new Repertoire(new File("Target/"));
        TreeItem<FileComposite> treeRoot = new TreeItem<>(rootDir);
        vueArborescence = new VueArborescence(treeRoot, rootDir);

        // Observateurs
        model.enregistrerObservateur(vueArborescence);
        model.enregistrerObservateur(new VueDiagrammeConsole());
        model.enregistrerObservateur(vueDiagramme);

        // Controllers
        ControllerArborescence controllerArborescence = new ControllerArborescence(model);
        ControllerContextMenu controllerContextMenu = new ControllerContextMenu(model);

        vueArborescence.setOnMouseClicked(controllerArborescence);
        vueArborescence.setOnContextMenuRequested(controllerContextMenu);
        vueDiagramme.setOnContextMenuRequested(controllerContextMenu);

        model.notifierObservateurs(); // Utile en cas de chargement de save
    }

    /**
     * Créer le MenuBar
     * @return MenuBar
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                createExportMenu(),
                createAddMenu()
        );
        return menuBar;
    }

    /**
     * Créer les items du menu d'exportation
     * @return Menu d'exportation
     */
    private Menu createExportMenu() {
        Menu menu = new Menu("Exporter");

        // PNG Export
        MenuItem pngItem = new MenuItem("PNG");
        ImageView pngIcon = new ImageView("file:image/png.png");
        pngIcon.setFitHeight(25);
        pngIcon.setFitWidth(25);
        pngItem.setGraphic(pngIcon);
        pngItem.setOnAction(e -> capturePane(vueDiagramme, "output/export/diagramme.png"));

        // JPG Export
        MenuItem jpgItem = new MenuItem("JPG");
        ImageView jpgIcon = new ImageView("file:image/jpg.png");
        jpgIcon.setFitHeight(25);
        jpgIcon.setFitWidth(25);
        jpgItem.setGraphic(jpgIcon);
        jpgItem.setOnAction(e -> capturePane(vueDiagramme, "output/export/diagramme.jpg"));

        // HTML Export
        MenuItem htmlItem = new MenuItem("HTML");
        ImageView htmlIcon = new ImageView("file:image/html.png");
        htmlIcon.setFitHeight(25);
        htmlIcon.setFitWidth(25);
        htmlItem.setGraphic(htmlIcon);
        htmlItem.setOnAction(e -> capturePaneAsHTML(vueDiagramme));

        // PUML Export
        MenuItem pumlItem = new MenuItem("PUML");
        ImageView pumlIcon = new ImageView("file:image/puml.png");
        pumlIcon.setFitHeight(25);
        pumlIcon.setFitWidth(25);
        pumlItem.setGraphic(pumlIcon);
        pumlItem.setOnAction(e -> exporterPlantUML());

        menu.getItems().addAll(pngItem, jpgItem, htmlItem, pumlItem);
        return menu;
    }

    /**
     * Créer les items du menu d'ajout de classe
     * @return Menu d'ajout
     */
    private Menu createAddMenu() {
        Menu menu = new Menu("Ajouter");
        MenuItem interfaceItem = new MenuItem("Interface");
        interfaceItem.setOnAction(e -> addNewClass("interface"));

        MenuItem classeConcreteItem = new MenuItem("Classe concrète");
        classeConcreteItem.setOnAction(e -> addNewClass("class"));

        MenuItem classeAbstraiteItem = new MenuItem("Classe abstraite");
        classeAbstraiteItem.setOnAction(e -> addNewClass("abstract"));

        menu.getItems().addAll(interfaceItem, classeConcreteItem, classeAbstraiteItem);
        return menu;
    }

    /**
     * Créer les items du menu de suppression
     * @return Menu de suppression
     */
    private Button createClearButton() {
        Button reset = new Button("Réinitialiser");
        reset.setOnAction(e -> {
            model.supprimerToutesLesClasses();
            System.out.println("Toutes les classes ont été supprimées du modèle.");
        });
        return reset;
    }

    /**
     * Créer un bouton de sauvegarde
     * @return Bouton de sauvegarde
     */
    private Button createSaveButton() {
        Button save = new Button("Sauvegarder");
        save.setOnAction(e -> {
            // Sauvegarde la position des vuesClasses
            for (Classe c : model.getClasses() ) {
                VueClasse vc = vueDiagramme.getVueClasse(c.getNomSimple());
                if (vc != null) {
                    c.setCoordonnees(vc.getLayoutX(), vc.getLayoutY());
                }
            }
            SaveManager.save(model);
            System.out.println("Sauvegarde effectué avec succès");
        });
        return save;
    }

    /**
     * Active le drap and drop vers le diagramme
     */
    private void configureDragAndDrop() {
        vueDiagramme.setOnDragOver(event -> {
            if (event.getGestureSource() != vueDiagramme &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        vueDiagramme.setOnDragEntered(event -> {
            if (event.getGestureSource() != vueDiagramme &&
                    event.getDragboard().hasString()) {
                vueDiagramme.setStyle("-fx-opacity:.4;-fx-background-color: gray;");
            }
            event.consume();
        });

        vueDiagramme.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String nomFichier = db.getString().replace(".class", "");
                TreeItem<FileComposite> selectedItem = vueArborescence.getSelectionModel().getSelectedItem();
                String nomFichierAvecPackage = selectedItem.getValue().getParentFolderName() + "." + nomFichier;
                Classe c = model.analyserClasse(nomFichierAvecPackage);
                c.setCoordonnees(event.getX(), event.getY());
                model.ajouterClasse(c);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
            vueDiagramme.setStyle("-fx-opacity:1;-fx-background-color: white;");
        });

        vueDiagramme.setOnDragExited(event -> {
            vueDiagramme.setStyle("-fx-opacity:1;-fx-background-color: white;");
            event.consume();
        });
    }

    /**
     * Ajoute une nouvelle classe
     * @param type type de classe
     **/
    private void addNewClass(String type) {
        Dialog<Classe> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle Classe");
        dialog.setHeaderText("Ajouter une nouvelle classe de type : " + type);

        ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Classe(type, "DefaultClassName", "defaultPackage", new ArrayList<>(), new ArrayList<>(), new double[0], new HashMap<>());
            }
            return null;
        });
        Optional<Classe> result = dialog.showAndWait();
        result.ifPresent(model::ajouterClasse);
    }

    /**
     * Fais une capture d'écran du diagramme
     * @param pane Pane à capturer
     * @param filePath chemin de sortie
     */
    private void capturePane(Pane pane, String filePath) {
        try {
            SnapshotParameters params = new SnapshotParameters();
            WritableImage fxImage = pane.snapshot(params, null);

            BufferedImage bufferedImage = new BufferedImage(
                    (int) fxImage.getWidth(),
                    (int) fxImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            copyPixels(fxImage, bufferedImage);
            ImageIO.write(bufferedImage, "png", new File(filePath));
            Desktop.getDesktop().open(new File(filePath));

            System.out.println("Screenshot sauvegardé avec succès : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du screenshot : " + e.getMessage());
        }
    }

    /**
     * Convertis une writableImage en BufferedImage en copiant les pixels 1 par 1
     * @param fxImage image en entrée
     * @param bufferedImage image en sortie
     */
    private void copyPixels(WritableImage fxImage, BufferedImage bufferedImage) {
        PixelReader pixelReader = fxImage.getPixelReader();
        for (int x = 0; x < fxImage.getWidth(); x++) {
            for (int y = 0; y < fxImage.getHeight(); y++) {
                bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
            }
        }
    }

    /**
     * Exporte le code plantUML dans un fichier
     */
    private void exporterPlantUML() {
        try {
            String plantUMLCode = model.genererPlantUML();
            File fichierExport = new File("output/export/Model_PlantUML.puml");

            try (FileWriter writer = new FileWriter(fichierExport)) {
                writer.write(plantUMLCode);
                System.out.println("Exportation PlantUML réussie : " + fichierExport.getAbsolutePath());
                Desktop.getDesktop().open(fichierExport);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'exportation du fichier PlantUML : " + e.getMessage());
        }
    }

    /**
     * Exporte un pane au format HTML
     * @param pane Pane à exporter
     */
    private void capturePaneAsHTML(Pane pane) {
        try {
            // Capture de l'image
            SnapshotParameters params = new SnapshotParameters();
            WritableImage fxImage = pane.snapshot(params, null);

            // Sauvegarde de l'image temporaire
            File tempImageFile = new File("output/export/temp_image.png");
            BufferedImage bufferedImage = new BufferedImage(
                    (int) fxImage.getWidth(),
                    (int) fxImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            copyPixels(fxImage, bufferedImage);
            ImageIO.write(bufferedImage, "png", tempImageFile);

            // Création du fichier HTML
            File htmlFile = new File("output/export/diagramme.html");
            try (FileWriter writer = new FileWriter(htmlFile)) {
                writer.write("<!DOCTYPE html>\n<html>\n<head>\n");
                writer.write("<title>Exportation HTML</title>\n</head>\n<body>\n");
                writer.write("<h1>Votre diagramme !</h1>\n");
                writer.write("<img src=\"../../output/export/temp_image.png\" alt=\"Diagramme\">\n");
                writer.write("</body>\n</html>");
            }

            System.out.println("HTML exporté avec succès : " + htmlFile.getAbsolutePath());
            Desktop.getDesktop().open(htmlFile);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'export en HTML : " + e.getMessage());
        }
    }

    /**
     * Créer les dossiers nécessaires au bon fonctionnement du projet
     */
    private void creerDossierOutput() {
        File dossierOutput = new File("output");
        if (!dossierOutput.exists()) {
            dossierOutput.mkdir();
        }
        File dossierExport = new File("output/export");
        if (!dossierExport.exists()) {
            dossierExport.mkdir();
        }
        File dossierSave = new File("output/save");
        if (!dossierSave.exists()) {
            dossierSave.mkdir();
        }
    }
}