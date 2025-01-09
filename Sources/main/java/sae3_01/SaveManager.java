package sae3_01;

import java.io.*;

/**
 * Gère la sérialisation du model
 */
public class SaveManager {

    /** Chemin de la sauvegarde */
    private final static String SAVE_PATH = "output/save/data.txt";

    /**
     * Permet de sauvegarder un model
     * @param model model à sauvegarder
     */
    public static void save(Model model) {
        File dossier = new File(SAVE_PATH);
        if (!dossier.exists()) {
            dossier.getParentFile().mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
            oos.writeObject(model);
        } catch (IOException e) {
            System.out.println("Erreur de sauvegarde " + e.getMessage());
        }
    }

    /**
     * Déserialise un model
     * @return objet Model
     * @throws Exception On gère l'exception au niveau au dessus, car un utilisateur n'as pas forcément de save
     */
    public static Model load() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_PATH));
        Model model = (Model) ois.readObject();
        ois.close();
        return model;
    }
}
