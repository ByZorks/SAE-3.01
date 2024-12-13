package sae3_01;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Analyseur {

    public static void main(String[] args) {
        try {
            Classe c = analyseClasse("sae3_01.Fichier");
            System.out.println("-- nom --");
            System.out.println(c.getNom());
            System.out.println("-- type --");
            System.out.println(c.getType());
            System.out.println("-- package --");
            System.out.println(c.getPackage());
            System.out.println("-- attributs --");
            System.out.println(c.getAttributs());
            System.out.println("-- methodes --");
            System.out.println(c.getMethodes());
            System.out.println("-- coordonnees --");
            System.out.println(Arrays.toString(c.getCoordonnees()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Classe analyseClasse(String nomClasse) throws ClassNotFoundException {
        Class<?> c = Class.forName(nomClasse);

        String type = getClassModifier(c);
        String nom = getNom(c);
        String packageName = getPackage(c);
        ArrayList<String> attributs = getAttributs(c);
        ArrayList<String> methodes = getMethodes(c);

        return new Classe(type, nom, packageName, attributs, methodes, new int[]{0, 0});
    }

    private static String getClassModifier(Class<?> c) {
        return getModifierString(c.getModifiers());
    }

    private static String getNom(Class<?> c) {
        // Récupère le nom de la classe
        String[] classNameTab = c.getName().split("\\.");
        String className = classNameTab[classNameTab.length - 1];

        // Récupère le nom de la classe mère
        Class<?> superClass = c.getSuperclass();
        String superClassName;
        String[] superClassNameTab = superClass.getName().split("\\.");
        superClassName = " extends " + superClassNameTab[superClassNameTab.length - 1];
        if (superClassName.equals(" extends Object")) {
            superClassName = "";
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

    private static String getPackage(Class<?> c) {
        return c.getPackage().getName();
    }

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
            for (Parameter p : parameters) {
                String typeName = p.getType().getSimpleName();
                methodes.add(getModifierUMLSymbol(con.getModifiers()) + name + "(" + typeName + ")");
            }
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
            for (Parameter p : parameters) {
                String typeName = p.getType().getSimpleName();
                methodes.add(getModifierUMLSymbol(m.getModifiers()) + name + "(" + typeName + ") : " + returnTypeName);
            }
        }

        return methodes;
    }

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
        if (Modifier.isFinal(modifiers)) {
            modifierString.append("final ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            modifierString.append("synchronized ");
        }

        return modifierString.toString();
    }

    public static String getModifierUMLSymbol(int modifiers) {
        if (Modifier.isPublic(modifiers)) {
            return "+ ";
        } else if (Modifier.isPrivate(modifiers)) {
            return "- ";
        } else if (Modifier.isProtected(modifiers)) {
            return "# ";
        }
        return "";
    }
}
