package sae3_01;

import java.io.File;
import java.util.List;

public class Fichier extends FileComposite{

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
