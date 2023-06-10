package com.example.g_bibliotheque;

public class DemandeLivre {
    String nom,cin,titre,categorie,dateDemande,telephone,email;

    public DemandeLivre(){}

    public DemandeLivre(String nom, String cin, String titre, String categorie, String dateDemande,String telephone,String email) {
        this.nom = nom;
        this.cin = cin;
        this.titre = titre;
        this.categorie = categorie;
        this.dateDemande = dateDemande;
        this.telephone = telephone;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(String dateDemande) {
        this.dateDemande = dateDemande;
    }
}
