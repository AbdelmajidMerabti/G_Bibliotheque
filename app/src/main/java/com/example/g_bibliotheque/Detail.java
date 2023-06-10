package com.example.g_bibliotheque;

public class Detail {
    String key,date,livreId;
    int qte;
    double prix;

    public Detail(){}

    public Detail(String key, String date, int qte, String livreId, double prix) {
        this.key = key;
        this.date = date;
        this.qte = qte;
        this.livreId = livreId;
        this.prix = prix;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getLivreId() {
        return livreId;
    }

    public void setLivreId(String livreId) {
        this.livreId = livreId;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
