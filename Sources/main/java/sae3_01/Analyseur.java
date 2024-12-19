package sae3_01;

import java.lang.reflect.*;
import java.util.ArrayList;

/**
 * Classe permettant d'analyser une classe Java
 */
public class Analyseur {

    /**
     * Analyse une classe Java et retourne un objet Classe
     * @param nomClasse le nom de la classe à analyser
     * @return un objet Classe
     * @throws ClassNotFoundException si la classe n'existe pas
     */
    public static Classe analyseClasse(String nomClasse) throws ClassNotFoundException {
        Class<?> c = Class.forName(nomClasse);

        String type = getClassModifier(c);
        String nomSimple = c.getSimpleName();
        String nomExtended = getNom(c);
        String packageName = getPackage(c);
        ArrayList<String> attributs = getAttributs(c);
        ArrayList<String> methodes = getMethodes(c);

        return new Classe(type, nomSimple, nomExtended, packageName, attributs, methodes, new int[]{0, 0});
    }

    /**
     * Retourne le modificateur de la classe
     * @param c la classe
     * @return le modificateur de la classe
     */
    private static String getClassModifier(Class<?> c) {
        return getModifierString(c.getModifiers());
    }

    /**
     * Retourne le nom de la classe avec ses interfaces et sa classe mère
     * @param c la classe
     * @return le nom de la classe
     */
    private static String getNom(Class<?> c) {
        // Récupère le nom de la classe
        String[] classNameTab = c.getName().split("\\.");
        String className = classNameTab[classNameTab.length - 1];

        // Récupère le nom de la classe mère
        Class<?> superClass = c.getSuperclass();
        String superClassName = "";
        if (superClass != null) { // null dans le cas d'une interface
            if (!superClass.getName().contains("Object")) {
                String[] superClassNameTab = superClass.getName().split("\\.");
                superClassName = " extends " + superClassNameTab[superClassNameTab.length - 1];
            }
        }

        // Récupère les noms des interfaces
        Class<?>[] interfaces = c.getInterfaces();
        StringBuilder interfacesNames;
        String itfNames = "";
        if (interfaces.length != 0) {
            interfacesNames = new StringBuilder(" implements ");
            for (Class<?> i : interfaces) {
                String[] interfacesNamesTab = i.getName().split("\\.");
                interfacesNames.append(interfacesNamesTab[interfacesNamesTab.length - 1]).append(", ");
            }
            itfNames = interfacesNames.substring(0, interfacesNames.length() - 2);
        }


        return  className + superClassName + itfNames;
    }

    /**
     * Retourne le nom du package de la classe
     * @param c la classe
     * @return le nom du package de la classe
     */
    private static String getPackage(Class<?> c) {
        return c.getPackage().getName();
    }

    /**
     * Retourne les attributs de la classe
     * @param c la classe
     * @return les attributs de la classe
     */
    private static ArrayList<String> getAttributs(Class<?> c) {
        ArrayList<String> attributs = new ArrayList<>();
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            String name = f.getName();
            String typeName = f.getType().getSimpleName();
            attributs.add(getModifierUMLSymbol(f.getModifiers()) + name + " : " + typeName);
        }
        return attributs;
    }

    /**
     * Retourne les méthodes de la classe (constructeurs et méthodes)
     * @param c la classe
     * @return les méthodes de la classe
     */
    private static ArrayList<String> getMethodes(Class<?> c) {
        // Constructeurs
        ArrayList<String> methodes = new ArrayList<>();
        Constructor<?>[] cons = c.getDeclaredConstructors();
        for (Constructor<?> con : cons) {
            // Nom
            String[] nameTab = con.getName().split("\\.");
            String name = nameTab[nameTab.length - 1];

            // Parametres
            Parameter[] parameters = con.getParameters();
            String initParams = getModifierUMLSymbol(con.getModifiers()) + name + "(";
            StringBuilder params = new StringBuilder(initParams);
            for (Parameter p : parameters) {
                String typeName = p.getType().getSimpleName();
                params.append(typeName).append(", ");
            }

            // Empeche la suppression de la dernière virgule si il n'y a pas de paramètres
            if (!params.toString().equals(initParams)) {
                params.deleteCharAt(params.length() - 1); // Supprime l'espace
                params.deleteCharAt(params.length() - 1); // Supprime la dernière virgule
            }
            params.append(")");
            methodes.add(params.toString());
        }

        // Méthodes
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            // Nom
            String[] nameTab = m.getName().split("\\.");
            String name = nameTab[nameTab.length - 1];

            // Type de return
            String returnTypeName = m.getReturnType().getSimpleName();

            // Parametres
            Parameter[] parameters = m.getParameters();
            String initParams = getModifierUMLSymbol(m.getModifiers()) + name + "(";
            StringBuilder params = new StringBuilder(initParams);
            for (Parameter p : parameters) {
                String argName = p.getType().getSimpleName();
                params.append(argName).append(", ");
            }
            // Empeche la suppression de la dernière virgule si il n'y a pas de paramètres
            if (!params.toString().equals(initParams)) {
                params.deleteCharAt(params.length() - 1); // Supprime l'espace
                params.deleteCharAt(params.length() - 1); // Supprime la dernière virgule
            }
            params.append(")").append(" : ").append(returnTypeName);
            methodes.add(params.toString());
        }

        return methodes;
    }

    /**
     * Retourne le type de l'attribut
     * @param c le nom de la classe
     * @return le type de l'attribut en entier et son nom sous la forme "type nom"
     */
    public static String[] getDetailledFieldType(String c) {
        Class<?> cl;
        String[] types = null;
        try {
            cl = Class.forName(c);
            Field[] fields = cl.getDeclaredFields();
            types = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                AnnotatedType type = fields[i].getAnnotatedType(); // Récupère le type complet de l'attribut
                String name = fields[i].getName();
                types[i] = type.getType().getTypeName() + " " + name;
            }
        } catch (ClassNotFoundException e) {
            // Ne devrait jamais arriver car la classe a déjà été chargée une fois avant d'appeler cette méthode
        }

        return types;
    }

    /**
     * Retourne une chaine de caractères représentant les modificateurs de la classe
     * @param modifiers les modificateurs
     * @return une chaine de caractères représentant les modificateurs de la classe
     */
    private static String getModifierString(int modifiers) {
        StringBuilder modifierString = new StringBuilder();

        if (Modifier.isPublic(modifiers)) {
            modifierString.append("public ");
        } else if (Modifier.isPrivate(modifiers)) {
            modifierString.append("private ");
        } else if (Modifier.isProtected(modifiers)) {
            modifierString.append("protected ");
        }

        if (Modifier.isStatic(modifiers)) {
            modifierString.append("static ");
        }
        if (Modifier.isAbstract(modifiers)) {
            modifierString.append("abstract ");
        }
        if (Modifier.isInterface(modifiers)) {
            modifierString.append("interface ");
        }
        if (Modifier.isFinal(modifiers)) {
            modifierString.append("final ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            modifierString.append("synchronized ");
        }

        return modifierString.toString();
    }

    /**
     * Retourne le symbole UML du modificateur
     * @param modifiers les modificateurs
     * @return le symbole UML du modificateur
     */
    private static String getModifierUMLSymbol(int modifiers) {
        String modifierString = "";
        if (Modifier.isPublic(modifiers)) {
            modifierString = "+ ";
        } else if (Modifier.isPrivate(modifiers)) {
            modifierString = "- ";
        } else if (Modifier.isProtected(modifiers)) {
            modifierString = "# ";
        }

        if (Modifier.isStatic(modifiers)) {
            modifierString += "{static} ";
        }
        if (Modifier.isAbstract(modifiers)) {
            modifierString += "{abstract} ";
        }
        if (Modifier.isFinal(modifiers)) {
            modifierString += "{final} ";
        }
        if (Modifier.isSynchronized(modifiers)) {
            modifierString += "{synchronized} ";
        }
        return modifierString;
    }
}
