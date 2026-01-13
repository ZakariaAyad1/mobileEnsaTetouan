
package com.example.ensatecertnotes.model;

public class Certificat {
    private int id;
    private int etudiantId;
    private String type; // ATTESTATION_SCOLAIRE, ATTESTATION_STAGE
    private String dateDemande;
    private String statut; // EN_ATTENTE, APPROUVE, REJETE, DELIVRE
    private String dateTraitement;
    private String motif;
    private String commentaire;

    public Certificat() {
    }

    public Certificat(int id, int etudiantId, String type, String dateDemande, String statut,
                      String dateTraitement, String motif, String commentaire) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.type = type;
        this.dateDemande = dateDemande;
        this.statut = statut;
        this.dateTraitement = dateTraitement;
        this.motif = motif;
        this.commentaire = commentaire;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
