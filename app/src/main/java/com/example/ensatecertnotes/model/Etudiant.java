
package com.example.ensatecertnotes.model;

public class Etudiant {
    private int id;
    private int userId;
    private String cne;
    private String nom;
    private String prenom;
    private String filiere;
    private String anneeEtude;
    private String photoUrl;

    public Etudiant() {
    }

    public Etudiant(int id, int userId, String cne, String nom, String prenom, String filiere, String anneeEtude,
            String photoUrl) {
        this.id = id;
        this.userId = userId;
        this.cne = cne;
        this.nom = nom;
        this.prenom = prenom;
        this.filiere = filiere;
        this.anneeEtude = anneeEtude;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getAnneeEtude() {
        return anneeEtude;
    }

    public void setAnneeEtude(String anneeEtude) {
        this.anneeEtude = anneeEtude;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
