package sae3_01;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ArrayList;

public class Model implements Sujet {

    private ArrayList<Classe> classes;
    private Repertoire root;
    private TreeView<FileComposite> treeView;
    private ArrayList<Observateur> observateurs;

    public Model(Repertoire root) {
        this.classes = new ArrayList<>();
        this.root = root;
        this.treeView = new TreeView<>(new TreeItem<>(root));
        this.observateurs = new ArrayList<>();
    }

    public void setTreeView(TreeView<FileComposite> treeView) {
        this.treeView = treeView;
    }

    public void setRootTreeView() {
        TreeItem<FileComposite> rootItem = new TreeItem<>(root);
        treeView.setRoot(rootItem);
        notifierObservateurs();
    }

    public Repertoire getRootTreeView() {
        return root;
    }

    public TreeView<FileComposite> getTreeView() {
        return treeView;
    }

    public Classe analyserClasse(String nomClasse) {
        Classe c = null;
        try {
            c = Analyseur.analyseClasse(nomClasse);
        } catch (ClassNotFoundException e) {
            System.out.println("Classe non trouvée");
        }
        return c;
    }

    public void ajouterClasse(Classe classe) {
        this.classes.add(classe);
        notifierObservateurs();
    }

    public void supprimerClasse(Classe classe) {
        this.classes.remove(classe);
        notifierObservateurs();
    }

    public ArrayList<Classe> getClasses() {
        return this.classes;
    }

    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateurs() {
        for (Observateur o : this.observateurs) o.actualiser(this);
    }
}
