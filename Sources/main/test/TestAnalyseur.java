import org.junit.jupiter.api.Test;
import sae3_01.Analyseur;
import sae3_01.Classe;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe de test de la classe Analyseur
 */
public class TestAnalyseur {

    /**
     * Test de la méthode analyseClasse
     * @throws ClassNotFoundException si la classe n'existe pas
     */
    @Test
    public void test_analyserClasse() throws ClassNotFoundException {
        // Préparation des données
        String nomClasse = "sae3_01.Repertoire";
        String type = "public ";
        String nom = "Repertoire extends FileComposite";
        String packageName = "sae3_01";
        ArrayList<String> attributs = new ArrayList<>();
        attributs.add("- contenu : List");
        ArrayList<String> methodes = new ArrayList<>();
        methodes.add("+ Repertoire(File)");
        methodes.add("+ isDirectory() : boolean");
        methodes.add("+ getContenu() : List");
        int[] coordonnees = new int[]{0, 0};

        // Exécution de la méthode à tester
        Classe c = Analyseur.analyseClasse(nomClasse);

        // Vérification
        assertDoesNotThrow(() -> Analyseur.analyseClasse(nomClasse));
        assertEquals(type, c.getType());
        assertEquals(nom, c.getNom());
        assertEquals(packageName, c.getPackage());
        assertEquals(1, c.getAttributs().size());
        assertEquals(attributs, c.getAttributs());
        assertEquals(3, c.getMethodes().size());
        assertEquals(methodes, c.getMethodes());
        assertEquals(coordonnees[0], c.getCoordonnees()[0]);
        assertEquals(coordonnees[1], c.getCoordonnees()[1]);
    }
}
