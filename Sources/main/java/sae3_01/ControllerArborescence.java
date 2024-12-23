package sae3_01;

import javafx.event.EventHandler;
import javafx.scene.Scene;
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

    @Override
    public void handle(MouseEvent mouseEvent) {
        TreeView<FileComposite> treeView = (TreeView<FileComposite>) mouseEvent.getSource();
        if (mouseEvent.getClickCount() == 2) {
            TreeItem<FileComposite> selectedItem = treeView.getSelectionModel().getSelectedItem();
            FileComposite file = selectedItem.getValue();
            if (!selectedItem.getValue().isDirectory()) {
                String nomFichier = file.toString().substring(0, file.toString().length() - 6); // On enlève l'extension .class
                String nomFichierAvecPackage = file.getParentFolderName() + "." + nomFichier; // On ajoute le nom du package
                Classe c = model.analyserClasse(nomFichierAvecPackage);
                model.ajouterClasse(c);
            }
        }
    }
}
