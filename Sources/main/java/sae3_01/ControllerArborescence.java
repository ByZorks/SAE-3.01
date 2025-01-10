package sae3_01;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

/**
 * Contrôleur de l'arborescence
 */
public class ControllerArborescence implements EventHandler<MouseEvent> {

    /** Modèle */
    private Model model;

    /**
     * Constructeur
     * @param model Modèle
     */
    public ControllerArborescence(Model model) {
        this.model = model;
    }

    /**
     * Méthode déclenchée lors d'un clic sur l'arborescence des fichiers.
     * Un double clic sur un fichier non répertoire permet d'analyser la classe
     * et de l'ajouter au modèle.
     * @param mouseEvent L'événement de clic de souris détecté.
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        TreeView<FileComposite> treeView = (TreeView<FileComposite>) mouseEvent.getSource();
        TreeItem<FileComposite> selectedItem = treeView.getSelectionModel().getSelectedItem();
        FileComposite file = selectedItem.getValue();
        if (mouseEvent.getClickCount() == 2 && !selectedItem.getValue().isDirectory()) {
            Classe c;
            if (!file.getFile().getAbsolutePath().contains("sae3_01")) {
                c = model.analyserClasseHorsClassPath(file.getFile());
            } else {
                String nomFichier = file.toString().replace(".class", "");
                String nomFichierAvecPackage = file.getParentFolderName() + "." + nomFichier; // On ajoute le nom du package
                c = model.analyserClasse(nomFichierAvecPackage);
            }
            model.ajouterClasse(c);
        }
    }
}
