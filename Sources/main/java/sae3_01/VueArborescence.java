package sae3_01;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class VueArborescence extends TreeView<FileComposite> implements Observateur {

    public VueArborescence(TreeItem<FileComposite> root, FileComposite parentFile) {
        super();
        buildTree(root, parentFile);
        this.setRoot(root);
    }

    @Override
    public void actualiser(Sujet s) {
        // Rien à faire
    }

    private void buildTree(TreeItem<FileComposite> parentItem, FileComposite parentFile) {
        for (FileComposite child : parentFile.getContenu()) {
            TreeItem<FileComposite> childItem = new TreeItem<>(child);
            parentItem.getChildren().add(childItem);
            if (child.isDirectory()) {
                buildTree(childItem, child);
            }
        }
    }
}