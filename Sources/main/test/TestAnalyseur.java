import org.junit.jupiter.api.Test;
import sae3_01.Analyseur;
import sae3_01.Classe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        String type = "<<class>>";
        String nomSimple = "Repertoire";
        String nomExtended = "Repertoire extends FileComposite";
        String packageName = "sae3_01";
        ArrayList<String> attributs = new ArrayList<>();
        attributs.add("- contenu : List");
        ArrayList<String> methodes = new ArrayList<>();
        methodes.add("+ Repertoire(File)");
        methodes.add("+ isDirectory() : boolean");
        methodes.add("+ getContenu() : List");
        int[] coordonnees = new int[]{0, 0};
        HashMap<String, ArrayList<String>> relations = new HashMap<>();
        relations.put("parent", new ArrayList<>(List.of("FileComposite")));
        relations.put("interface", new ArrayList<>());
        relations.put("associations", new ArrayList<>(List.of("FileComposite contenu")));

        // Exécution de la méthode à tester
        Classe c = Analyseur.analyseClasse(nomClasse);

        // Vérification
        assertDoesNotThrow(() -> Analyseur.analyseClasse(nomClasse));
        assertEquals(type, c.getType());
        assertEquals(nomSimple, c.getNomSimple());
        assertEquals(nomExtended, c.getNomExtended());
        assertEquals(packageName, c.getPackage());
        assertEquals(1, c.getAttributs().size());
        assertEquals(attributs, c.getAttributs());
        assertEquals(3, c.getMethodes().size());
        for (String m : c.getMethodes()) {
            assertTrue(methodes.contains(m));
        }
        assertEquals(coordonnees[0], c.getX());
        assertEquals(coordonnees[1], c.getY());
        assertEquals(relations.get("parent").getFirst(), c.getParent());
        assertEquals(relations.get("interface"), c.getInterfaces());
        assertEquals(relations.get("associations"), c.getAssociations());
    }
}
