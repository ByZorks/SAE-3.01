package sae3_01.test;

import org.junit.jupiter.api.Test;
import sae3_01.Repertoire;

import java.io.File;

public class TestComposite {
    @Test
    void afficher() {
        File rootFile = new File("test-root");
        if (!rootFile.exists()) {
            rootFile.mkdir();
        }

        File file1 = new File(rootFile, "File1.txt");
        File file2 = new File(rootFile, "File2.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Repertoire root = new Repertoire(rootFile);


        root.afficher("");

    }
}