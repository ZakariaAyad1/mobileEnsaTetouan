
package com.example.ensatecertnotes.model;

public class DemandeDiplome_etudiant {
    private int id;
    private int etudiantId;
    private String dateDemande;
    private String statut; // EN_ATTENTE, APPROUVE, REJETE, PRET
    private String commentaire;
    private String dateTraitement;
    private String adresseLivraison;
    private String telephone;

    public DemandeDiplome_etudiant() {
    }

    public DemandeDiplome_etudiant(int id, int etudiantId, String dateDemande, String statut, 
                                   String commentaire, String dateTraitement, String adresseLivraison, String telephone) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.dateDemande = dateDemande;
        this.statut = statut;
        this.commentaire = commentaire;
        this.dateTraitement = dateTraitement;
        this.adresseLivraison = adresseLivraison;
        this.telephone = telephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(int etudiantId) {
        this.etudiantId = etudiantId;
    }

    public String getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(String dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

