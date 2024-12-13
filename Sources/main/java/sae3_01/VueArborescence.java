package sae3_01;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class VueArborescence extends TreeView<FileComposite> implements Observateur {

    public VueArborescence(TreeView<FileComposite> treeView) {
        super(treeView.getRoot());
    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;
        if (model.getRootDir() == null) {
            return;
        }
        TreeItem<FileComposite> rootItem = new TreeItem<>(model.getRootDir());
        buildTree(rootItem, model.getRootDir());
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