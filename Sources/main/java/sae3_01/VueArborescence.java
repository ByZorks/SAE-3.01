package sae3_01;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class VueArborescence extends TreeView<FileComposite> implements Observateur {

    public VueArborescence(Repertoire r) {
        TreeItem<FileComposite> rootItem = new TreeItem<>(r);
        this.setRoot(rootItem);
    }

    @Override
    public void actualiser(Sujet s) {
        ModelArborescence model = (ModelArborescence) s;
        TreeItem<FileComposite> rootItem = new TreeItem<>(model.getArborescence());
        buildTree(rootItem, model.getArborescence());
        this.setRoot(rootItem);
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