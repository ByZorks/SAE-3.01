package sae3_01;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

public class ControllerArborescence implements EventHandler<MouseEvent> {

    private Model model;

    public ControllerArborescence(Model model) {
        this.model = model;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 2) {
                TreeItem<FileComposite> selectedItem = model.getTreeView().getSelectionModel().getSelectedItem();
                FileComposite file = selectedItem.getValue();
                if (!selectedItem.getValue().isDirectory()) {
                    String nomFichier = file.toString().substring(0, file.toString().length() - 6);
                    Classe c = model.analyserClasse(file.getParentFolderName() + "." + nomFichier); // On enlève l'extension .class
                    model.ajouterClasse(c);
                }
            }
    }
}
