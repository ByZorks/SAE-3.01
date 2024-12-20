package sae3_01;

import java.io.File;
import java.util.List;

/**
 * Classe représentant un fichier
 */
public class Fichier extends FileComposite{

    /**
     * Constructeur
     * @param file Fichier
     */
    public Fichier(File file) {
        super(file);
    }

    @Override
    public List<FileComposite> getContenu() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

}
