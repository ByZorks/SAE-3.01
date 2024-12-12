package sae3_01;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Repertoire extends FileComposite {

    private List<FileComposite> contenu =  new ArrayList<FileComposite>();

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
}
