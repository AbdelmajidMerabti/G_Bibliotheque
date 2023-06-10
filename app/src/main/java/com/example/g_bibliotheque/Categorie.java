package com.example.g_bibliotheque;

public class Categorie {
    private String id;
    private String libelle;

    public Categorie() {
        // Default constructor required for calls to DataSnapshot.getValue(Categorie.class)
    }

    public Categorie(String id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }
}

