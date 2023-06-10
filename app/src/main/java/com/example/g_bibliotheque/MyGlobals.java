package com.example.g_bibliotheque;

public class MyGlobals {

    private static String nom = "";
    private static String key = "";

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        MyGlobals.nom = nom;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        MyGlobals.key = key;
    }
}
