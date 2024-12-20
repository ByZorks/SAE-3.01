package sae3_01;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un répertoire.
 */
public class Repertoire extends FileComposite {

    /**
     * Contenu du répertoire.
     */
    private List<FileComposite> contenu =  new ArrayList<>();

    /**
     * Constructeur.
     * @param f Répertoire à représenter.
     */
    public Repertoire(File f) {
        super(f);
        File[] files = this.file.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                contenu.add(new Fichier(file));
            } else if (file.isDirectory()) {
                contenu.add(new Repertoire(file));
            }
        }
    }

    public List<FileComposite> getContenu() {
        return this.contenu;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
