package com.example.g_bibliotheque;

public class Demande {
    private String demandeId;
    private String livreId;
    private String utilisateurId;
    private int qteDemande;
    private String dateDemande;
    private int statusDemande;

    public Demande() {
        // Required empty constructor for Firebase
    }

    public Demande(String demandeId, String livreId, String utilisateurId, int qteDemande, String dateDemande, int statusDemande) {
        this.demandeId = demandeId;
        this.livreId = livreId;
        this.utilisateurId = utilisateurId;
        this.qteDemande = qteDemande;
        this.dateDemande = dateDemande;
        this.statusDemande = statusDemande;
    }

    public String getDemandeId() {
        return demandeId;
    }

    public void setDemandeId(String demandeId) {
        this.demandeId = demandeId;
    }

    public String getLivreId() {
        return livreId;
    }

    public void setLivreId(String livreId) {
        this.livreId = livreId;
    }

    public String getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getQteDemande() {
        return qteDemande;
    }

    public void setQteDemande(int qteDemande) {
        this.qteDemande = qteDemande;
    }

    public String getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(String dateDemande) {
        this.dateDemande = dateDemande;
    }

    public int getStatusDemande() {
        return statusDemande;
    }

    public void setStatusDemande(int statusDemande) {
        this.statusDemande = statusDemande;
    }
}
