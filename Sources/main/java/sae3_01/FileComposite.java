package sae3_01;

import java.io.File;
import java.util.List;

/**
 * Classe abstraite représentant un fichier ou un répertoire
 */
public abstract class FileComposite {

    /** Fichier */
    protected File file;

    /**
     * Constructeur
     * @param file Fichier
     */
    public FileComposite(File file) {
        this.file = file;
    }

    /**
     * Récupère le nom du parent
     * @return Nom du parent
     */
    public String getParentFolderName() {
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            return parentFile.getName();
        } else {
            return null;
        }
    }

    /**
     * Récupère le nom du fichier
     * @return Nom du fichier
     */
    public String toString() {
        return this.file.getName();
    }

    /**
     * Récupère le fichier
     * @return Fichier
     */
    public abstract List<FileComposite> getContenu();

    /**
     * Indique si le fichier est un répertoire
     * @return Vrai si le fichier est un répertoire
     */
    public abstract boolean isDirectory();

}