package sae3_01;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * VueArborescence est une classe qui hérite de TreeView<FileComposite> et qui
 * permet d'afficher l'arborescence des fichiers et répertoires.
 */
public class VueArborescence extends TreeView<FileComposite> implements Observateur {

    /**
     * Constructeur de la classe VueArborescence.
     * @param root La racine de l'arborescence.
     * @param parentFile Le fichier parent.
     */
    public VueArborescence(TreeItem<FileComposite> root, FileComposite parentFile) {
        super();
        buildTree(root, parentFile);
        this.setRoot(root);
    }

    @Override
    public void actualiser(Sujet s) {
        // Rien à faire
    }

    /**
     * Construit l'arborescence des fichiers et répertoires.
     * @param parentItem L'élément parent.
     * @param parentFile Le fichier parent.
     */
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