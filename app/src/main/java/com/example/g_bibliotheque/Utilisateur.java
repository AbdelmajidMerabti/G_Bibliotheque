package com.example.g_bibliotheque;

import java.io.Serializable;

public class Utilisateur  implements Serializable {
    private String key;
    private String nom;
    private String cin;
    private String email;
    private String dateNaissance;
    private String telephone;
    private String type;
    private String password;
    private int status;

    public Utilisateur() {
        // Constructeur vide requis pour Firebase Realtime Database
    }

    public Utilisateur(String key, String nom, String cin, String email, String dateNaissance, String type, String password, String telephone) {
        this.key = key;
        this.nom = nom;
        this.cin = cin;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.type = type;
        this.password = password;
        this.telephone = telephone;
        this.status = 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
