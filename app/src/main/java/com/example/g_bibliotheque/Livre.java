package com.example.g_bibliotheque;

public class Livre {
    String key,image,titre,description,categorie;
    int qte,qteR;


    public Livre(){}

    public Livre(String key, String image, String titre, String description, String categorie, int qte, int qteR) {
        this.key = key;
        this.image = image;
        this.titre = titre;
        this.description = description;
        this.categorie = categorie;
        this.qte = qte;
        this.qteR = qteR;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public int getQteR() {
        return qteR;
    }

    public void setQteR(int qteR) {
        this.qteR = qteR;
    }
}
