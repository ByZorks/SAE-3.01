package sae3_01;

import java.lang.reflect.*;
import java.util.*;

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
        String packageName = getPackage(c);
        ArrayList<String> attributs = getAttributs(c);
        ArrayList<String> methodes = getMethodes(c);
        HashMap<String, ArrayList<String>> relations = getRelations(c);

        return new Classe(type, nomSimple, packageName, attributs, methodes, new double[]{0, 0}, relations);
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
            String typeName;

            // On vérifie qu'un type générique est utilisé
            Type type = f.getGenericType();
            if (type instanceof ParameterizedType paramType) {
                String rawType = ((Class<?>) paramType.getRawType()).getSimpleName(); // Type (e.g., ArrayList, Map)
                StringBuilder genericTypes = getGenericTypes(paramType); // Sous-type (e.g., String, Integer)
                typeName = rawType + "<" + genericTypes + ">";
            } else {
                typeName = f.getType().getSimpleName();
            }

            attributs.add(getModifierUMLSymbol(f.getModifiers()) + name + " : " + typeName);
        }
        return attributs;
    }

    /**
     * Retourne les types génériques d'un paramètre
     * @param paramType le type du paramètre
     * @return les types génériques d'un paramètre
     */
    private static StringBuilder getGenericTypes(ParameterizedType paramType) {
        Type[] typeArguments = paramType.getActualTypeArguments();
        StringBuilder genericTypes = new StringBuilder();

        // Récupère les types génériques
        for (Type typeArg : typeArguments) {
            if (!genericTypes.isEmpty()) {
                genericTypes.append(", ");
            }

            // Si le type est une classe, on récupère son nom simple
            if (typeArg instanceof Class) {
                genericTypes.append(((Class<?>) typeArg).getSimpleName());
            // Si le type est un type paramétré, on récupère ses types génériques
            } else if (typeArg instanceof ParameterizedType genericType) {
                String rawType = ((Class<?>) genericType.getRawType()).getSimpleName();
                genericTypes.append(rawType)
                        .append("<")
                        .append(getGenericTypes(genericType)) // Récursif pour les types génériques imbriqués
                        .append(">");
            }
        }

        return genericTypes;
    }

    /**
     * Retourne les méthodes de la classe (constructeurs et méthodes)
     * @param c la classe
     * @return les méthodes de la classe
     */
    private static ArrayList<String> getMethodes(Class<?> c) {
        ArrayList<String> methodes = new ArrayList<>();

        // Constructeurs
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
                // Get generic parameter type if it exists
                String typeName;
                Type paramType = p.getParameterizedType();
                if (paramType instanceof ParameterizedType) {
                    String rawType = ((Class<?>) ((ParameterizedType) paramType).getRawType()).getSimpleName();
                    StringBuilder genericTypes = getGenericTypes((ParameterizedType) paramType);
                    typeName = rawType + "<" + genericTypes + ">";
                } else {
                    typeName = p.getType().getSimpleName();
                }
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
            if (name.startsWith("lambda$")) continue; // On ne prend pas en compte les méthodes lambda

            // Type de return avec gestion des génériques
            String returnTypeName;
            Type returnType = m.getGenericReturnType();
            if (returnType instanceof ParameterizedType) {
                String rawType = ((Class<?>) ((ParameterizedType) returnType).getRawType()).getSimpleName();
                StringBuilder genericTypes = getGenericTypes((ParameterizedType) returnType);
                returnTypeName = rawType + "<" + genericTypes + ">";
            } else {
                returnTypeName = m.getReturnType().getSimpleName();
            }

            // Parametres
            Parameter[] parameters = m.getParameters();
            String initParams = getModifierUMLSymbol(m.getModifiers()) + name + "(";
            StringBuilder params = new StringBuilder(initParams);

            for (Parameter p : parameters) {
                // Get generic parameter type if it exists
                String typeName;
                Type paramType = p.getParameterizedType();
                if (paramType instanceof ParameterizedType) {
                    String rawType = ((Class<?>) ((ParameterizedType) paramType).getRawType()).getSimpleName();
                    StringBuilder genericTypes = getGenericTypes((ParameterizedType) paramType);
                    typeName = rawType + "<" + genericTypes + ">";
                } else {
                    typeName = p.getType().getSimpleName();
                }
                params.append(typeName).append(", ");
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
     * Retourne les relations de la classe (hors classes du package java)
     * @param c la classe
     * @return les relations de la classe avec leurs types
     */
    private static HashMap<String, ArrayList<String>> getRelations(Class<?> c) {
        HashMap<String, ArrayList<String>> relations = new HashMap<>();

        // Classe mère
        Type superClassType = c.getGenericSuperclass();
        if (superClassType instanceof ParameterizedType) {
            String genericTypes = new StringBuilder(getGenericTypes((ParameterizedType) superClassType)).toString();
            relations.put("parent", new ArrayList<>(List.of(((Class<?>) ((ParameterizedType) superClassType).getRawType()).getSimpleName() + "<" + genericTypes + ">")));
        } else if (superClassType instanceof Class) {
            relations.put("parent", new ArrayList<>(List.of(((Class<?>) superClassType).getSimpleName())));
        } else {
            relations.put("parent", new ArrayList<>(List.of("Object")));
        }

        // Interfaces
        Class<?>[] interfaces = c.getInterfaces();
        ArrayList<String> interfacesNames = new ArrayList<>();
        for (Class<?> i : interfaces) {
            interfacesNames.add(i.getSimpleName());
        }
        relations.put("interface", interfacesNames);

        // Attributs
        ArrayList<String> attributsNames = new ArrayList<>();
        for (Field f : c.getDeclaredFields()) {
            if (f.getType().isPrimitive()) continue;

            // Détermine le type de relation basé sur le type de l'attribut
            if (f.getType().getName().startsWith("java.util.List") ||
                    f.getType().getName().startsWith("java.util.Map") ||
                    f.getType().getName().startsWith("java.util.Set") ||
                    f.getType().getName().startsWith("java.util.Array")) {

                // Récupération des types génériques
                ParameterizedType pType = (ParameterizedType) f.getGenericType();
                for (Type argType : pType.getActualTypeArguments()) {
                    String typeName = ((Class<?>) argType).getName();
                    if (!typeName.startsWith("java.")) {
                        // Format: type nom [type-relation]
                        attributsNames.add(((Class<?>) argType).getSimpleName() + " " +
                                f.getName() + " [*]"); // * indique multiple
                    }
                }
            }
            // Type simple non-Java
            else if (!f.getType().getName().startsWith("java.")) {
                // Format: type nom [type-relation]
                attributsNames.add(f.getType().getSimpleName() + " " +
                        f.getName() + " [1]"); // 1 indique simple
            }
        }
        relations.put("associations", attributsNames);

        return relations;
    }

    /**
     * Retourne une chaine de caractères représentant les modificateurs de la classe
     * @param modifiers les modificateurs
     * @return une chaine de caractères représentant les modificateurs de la classe
     */
    private static String getModifierString(int modifiers) {
        String modifierString;

        if (Modifier.isInterface(modifiers)) {
            modifierString = "<<interface>>";
        } else if (Modifier.isAbstract(modifiers)) {
            modifierString = "<<abstract>>";
        } else {
            modifierString = "<<class>>";
        }

        return modifierString;
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
        if (Modifier.isSynchronized(modifiers)) {
            modifierString += "{synchronized} ";
        }
        return modifierString;
    }
}
