package sae3_01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
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

        // Panes
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(vueArborescence, vueDiagramme);

        BorderPane root = new BorderPane();
        Button[] zoomControls = configureZoomControls();
        ToolBar toolBar = new ToolBar(
                createMenuBar(stage),
                zoomControls[0],
                zoomControls[1],
                zoomControls[2]
        );

        // Placement dans la fenêtre
        root.setTop(toolBar);
        root.setCenter(splitPane);

        // Parametres du stage
        Scene scene = new Scene(root);
        stage.setTitle("SAE3-01");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        scene.setOnScroll(e -> {
            double zoomFactor = 1.05;
            double deltaY = e.getDeltaY();

            if (deltaY < 0){
                zoomFactor = 0.95;
            }
            zoomDiagramme(zoomFactor);
            e.consume();
        });

        configureDragAndDrop();
        splitPane.setDividerPositions(0.15);
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
    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                createFicherMenu(stage),
                createExportMenu(stage),
                createAddMenu()
        );
        return menuBar;
    }

    /**
     * Créer le menu fichier
     * @return Menu fichier
     */
    private Menu createFicherMenu(Stage stage) {
        Menu menu = new Menu("Fichier");

        // Sauvegarde
        MenuItem saveItem = new MenuItem("Sauvegarder");
        saveItem.setOnAction(e -> {
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

        MenuItem loadItem = new MenuItem("Ouvrir dossier");
        loadItem.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(vueArborescence.getRoot().getValue().getFile().getAbsolutePath()));
            directoryChooser.setTitle("Ouvrir un dossier dans l'arborescence");
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                Repertoire r = new Repertoire(selectedDirectory);
                TreeItem<FileComposite> treeItem = new TreeItem<>(r);
                vueArborescence.update(treeItem, r);
                model.supprimerToutesLesClasses();
            }
        });

        MenuItem reset = new MenuItem("Réinitialiser");
        reset.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        reset.setOnAction(e -> {
            model.supprimerToutesLesClasses();
            System.out.println("Toutes les classes ont été supprimées du modèle.");
        });

        menu.getItems().addAll(saveItem, loadItem, reset);

        return menu;
    }

    /**
     * Créer les items du menu d'exportation
     * @return Menu d'exportation
     */
    private Menu createExportMenu(Stage stage) {
        Menu menu = new Menu("Exporter");

        // PNG Export
        MenuItem pngItem = new MenuItem("PNG");
        ImageView pngIcon = new ImageView("file:image/png.png");
        pngIcon.setFitHeight(25);
        pngIcon.setFitWidth(25);
        pngItem.setGraphic(pngIcon);
        pngItem.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("output/export"));
            directoryChooser.setTitle("Choisir un dossier de sauvegarde");
            File selectedDirectory = directoryChooser.showDialog(stage);
            capturePane(vueDiagramme, selectedDirectory.getAbsolutePath() + "/diagramme.png");
        });

        // JPG Export
        MenuItem jpgItem = new MenuItem("JPG");
        ImageView jpgIcon = new ImageView("file:image/jpg.png");
        jpgIcon.setFitHeight(25);
        jpgIcon.setFitWidth(25);
        jpgItem.setGraphic(jpgIcon);
        jpgItem.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("output/export"));
            directoryChooser.setTitle("Choisir un dossier de sauvegarde");
            File selectedDirectory = directoryChooser.showDialog(stage);
            capturePane(vueDiagramme, selectedDirectory.getAbsolutePath() + "/diagramme.jpg");
        });

        // HTML Export
        MenuItem htmlItem = new MenuItem("HTML");
        ImageView htmlIcon = new ImageView("file:image/html.png");
        htmlIcon.setFitHeight(25);
        htmlIcon.setFitWidth(25);
        htmlItem.setGraphic(htmlIcon);
        htmlItem.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("output/export"));
            directoryChooser.setTitle("Choisir un dossier de sauvegarde");
            File selectedDirectory = directoryChooser.showDialog(stage);
            capturePaneAsHTML(vueDiagramme, selectedDirectory.getAbsolutePath());
        });

        // PUML Export
        MenuItem pumlItem = new MenuItem("PUML");
        ImageView pumlIcon = new ImageView("file:image/puml.png");
        pumlIcon.setFitHeight(25);
        pumlIcon.setFitWidth(25);
        pumlItem.setGraphic(pumlIcon);
        pumlItem.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("output/export"));
            directoryChooser.setTitle("Choisir un dossier de sauvegarde");
            File selectedDirectory = directoryChooser.showDialog(stage);
            exporterPlantUML(selectedDirectory.getAbsolutePath() + "/diagramme.puml");
        });

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
     * Active le drap and drop vers le diagramme
     */
    private void configureDragAndDrop() {
        vueDiagramme.getParent().setOnDragOver(event -> {
            if (event.getGestureSource() != vueDiagramme &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        vueDiagramme.getParent().setOnDragEntered(event -> {
            if (event.getGestureSource() != vueDiagramme && event.getDragboard().hasString()) {
                vueDiagramme.getParent().setStyle("-fx-opacity:.4;-fx-background-color: gray;");
            }
            event.consume();
        });

        vueDiagramme.getParent().setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                TreeItem<FileComposite> selectedItem = vueArborescence.getSelectionModel().getSelectedItem();
                Classe c;
                if (!selectedItem.getValue().getFile().getAbsolutePath().contains("sae3_01")) {
                    c = model.analyserClasseHorsClassPath(selectedItem.getValue().getFile());
                } else {
                    String nomFichier = db.getString().replace(".class", "");
                    String nomFichierAvecPackage = selectedItem.getValue().getParentFolderName() + "." + nomFichier;
                    c = model.analyserClasse(nomFichierAvecPackage);
                }
                c.setCoordonnees(event.getX(), event.getY());
                model.ajouterClasse(c);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
            vueDiagramme.getParent().setStyle("-fx-opacity:1;-fx-background-color: transparent;");
        });

        vueDiagramme.getParent().setOnDragExited(event -> {
            vueDiagramme.getParent().setStyle("-fx-opacity:1;-fx-background-color: transparent;");
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
    private void exporterPlantUML(String path) {
        try {
            String plantUMLCode = model.genererPlantUML();
            File fichierExport = new File(path);

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
    private void capturePaneAsHTML(Pane pane, String path) {
        try {
            // Capture de l'image
            SnapshotParameters params = new SnapshotParameters();
            WritableImage fxImage = pane.snapshot(params, null);

            // Sauvegarde de l'image temporaire
            File tempImageFile = new File(path + "/temp_image.png");
            BufferedImage bufferedImage = new BufferedImage(
                    (int) fxImage.getWidth(),
                    (int) fxImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            copyPixels(fxImage, bufferedImage);
            ImageIO.write(bufferedImage, "png", tempImageFile);

            // Création du fichier HTML
            File htmlFile = new File(path + "/diagramme.html");
            try (FileWriter writer = new FileWriter(htmlFile)) {
                writer.write("<!DOCTYPE html>\n<html>\n<head>\n");
                writer.write("<title>Exportation HTML</title>\n</head>\n<body>\n");
                writer.write("<h1>Votre diagramme !</h1>\n");
                writer.write("<img src=\"" + path + "/temp_image.png\" alt=\"Diagramme\">\n");
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
    /**
     * Configure les contrôles de zoom pour le diagramme.
     * @return HBox contenant les boutons de zoom
     */
    private Button[] configureZoomControls() {
        // Créer les boutons de zoom
        Button zoomInButton = new Button("+");
        Button zoomOutButton = new Button("-");
        Button zoomResetButton = new Button("100%");

        // Définir les actions des boutons de zoom
        zoomInButton.setOnAction(e -> zoomDiagramme(1.2)); // Zoom avant de 20%
        zoomOutButton.setOnAction(e -> zoomDiagramme(0.8)); // Zoom arrière de 20%
        zoomResetButton.setOnAction(e -> zoomDiagramme(1.0)); // Réinitialiser le zoom

        // Styliser les boutons de zoom
        zoomInButton.getStyleClass().add("zoom-button");
        zoomOutButton.getStyleClass().add("zoom-button");
        zoomResetButton.getStyleClass().add("zoom-button");

        return new Button[]{zoomInButton, zoomOutButton, zoomResetButton};
    }



    /**
     * Ajuste le zoom du diagramme.
     * @param scaleFactor Facteur de mise à l’échelle (1.2 pour zoom avant, 0.8 pour zoom arrière, 1.0 pour réinitialiser)
     */
    private void zoomDiagramme(double scaleFactor) {
        if (vueDiagramme != null) {
            // Si le facteur est exactement 1.0, réinitialiser complètement
            if (scaleFactor == 1.0) {
                vueDiagramme.setScaleX(1.0);
                vueDiagramme.setScaleY(1.0);
                return;
            }
            // Pour les zooms avant et arrière
            double currentScale = vueDiagramme.getScaleX();
            double newScale = Math.max(0.5, Math.min(2.0, currentScale * scaleFactor));
            vueDiagramme.setScaleX(newScale);
            vueDiagramme.setScaleY(newScale);
        }
    }

}