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

    /**
     * Retourne le contenu du répertoire sous forme d'une liste d'objets FileComposite.
     * Chaque élément peut être un fichier ou un sous-répertoire.
     * @return Liste des fichiers et sous-répertoires du répertoire.
     */
    @Override
    public List<FileComposite> getContenu() {
        return this.contenu;
    }

    /**
     * Indique si cet objet est un répertoire.
     * Cette méthode retourne toujours true pour cette classe.
     * @return true, car cet objet représente un répertoire.
     */
    @Override
    public boolean isDirectory() {
        return true;
    }
}
