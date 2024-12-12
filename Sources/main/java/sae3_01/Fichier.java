package sae3_01;
import java.io.File;
public class Fichier extends FileComposite{
    public Fichier(File file) {
        super(file);
    }
    @Override
    public void afficher(String s) {
        System.out.println(s + "|> " + this.file.getName() + " (" + this.file.length() + " bytes)");
    }
}
