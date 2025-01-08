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

    /**
     * Retourne le contenu du fichier.
     * Comme un fichier simple ne peut pas contenir d'autres fichiers,
     * @return Toujours null, car un fichier n'a pas de contenu.
     */
    @Override
    public List<FileComposite> getContenu() {
        return null;
    }

    /**
     * Indique si cet élément est un répertoire.
     * Cette méthode retourne toujours false car un fichier n'est
     * jamais un répertoire.
     * @return false, car un fichier n'est pas un répertoire.
     */
    @Override
    public boolean isDirectory() {
        return false;
    }

}
