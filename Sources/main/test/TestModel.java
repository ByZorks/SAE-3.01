import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae3_01.Classe;
import sae3_01.Model;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de la classe Model
 */
public class TestModel {

    /** Modèle */
    private Model model;

    /**
     * Initialisation des données de test
     */
    @BeforeEach
    public void setUp() {
        this.model = new Model();
    }

    /**
     * Test de la méthode checkAssociation
     */
    @Test
    public void test_checkAssociation() {
        // Préparation des données
        Classe c1 = model.analyserClasse("sae3_01.Repertoire");
        Classe c2 = model.analyserClasse("sae3_01.FileComposite");

        // Exécution de la méthode à tester
        String result = this.model.checkAssociation(c1, c2);

        // Vérification
        assertNotNull(result);
    }

    @Test
    public void test_checkAssociationNull() {
        // Prépartion des données
        Classe c1 = model.analyserClasse("sae3_01.Repertoire");
        Classe c2 = model.analyserClasse("sae3_01.Classe");

        // Exécution de la méthode à tester
        String result = this.model.checkAssociation(c1, c2);

        // Vérification
        assertNull(result);
    }

    /**
     * Test de la méthode genererPlantUML avec une classe abstraite
     */
    @Test
    public void test_genererPlantUMLAbstract() {
        // Préparation des données
        Classe c = model.analyserClasse("sae3_01.FileComposite");
        model.ajouterClasse(c);
        String[] expected = {"@startuml\nabstract FileComposite {\n",
        "\t# file : File\n",
        "\t+ FileComposite(File)\n",
        "\t+ toString() : String\n",
        "\t+ {abstract} isDirectory() : boolean\n",
        "\t+ getParentFolderName() : String\n",
        "\t+ {abstract} getContenu() : List\n",
        "}\n\n@enduml"};

        // Exécution de la méthode à tester
        String result = this.model.genererPlantUML();

        // Vérification
        for (String s : expected) {
            assertTrue(result.contains(s));
        }
    }

    /**
     * Test de la méthode genererPlantUML avec une interface
     */
    @Test
    public void test_genererPlantUMLInterface() {
        // Préparation des données
        Classe c = model.analyserClasse("sae3_01.Observateur");
        model.ajouterClasse(c);
        String[] expected = {"@startuml\ninterface Observateur {\n",
        "\t+ {abstract} actualiser(Sujet) : void\n",
        "}\n\n@enduml"};

        // Exécution de la méthode à tester
        String result = this.model.genererPlantUML();

        // Vérification
        for (String s : expected) {
            assertTrue(result.contains(s));
        }
    }

    /**
     * Test de la méthode genererPlantUML avec une classe et une interface qui ont une association
     */
    @Test
    public void test_genererPlantUMLClassAvecAssociations() {
        // Préparation des données
        Classe c1 = model.analyserClasse("sae3_01.FileComposite");
        Classe c2 = model.analyserClasse("sae3_01.Repertoire");
        model.ajouterClasse(c1);
        model.ajouterClasse(c2);
        String[] expected = {"@startuml\nabstract FileComposite {\n",
                "\t# file : File\n",
                "\t+ FileComposite(File)\n",
                "\t+ toString() : String\n",
                "\t+ {abstract} isDirectory() : boolean\n",
                "\t+ getParentFolderName() : String\n",
                "\t+ {abstract} getContenu() : List\n",
                "}\n",
                "class Repertoire extends FileComposite {\n",
                "\t- contenu : List\n",
                "\t+ Repertoire(File)\n",
                "\t+ isDirectory() : boolean\n",
                "\t+ getContenu() : List\n",
                "}\n",
                "Repertoire --> FileComposite : contenu\n",
                "\n@enduml"};

        // Exécution de la méthode à tester
        String result = this.model.genererPlantUML();

        // Vérification
        for (String s : expected) {
            assertTrue(result.contains(s));
        }
    }
}
