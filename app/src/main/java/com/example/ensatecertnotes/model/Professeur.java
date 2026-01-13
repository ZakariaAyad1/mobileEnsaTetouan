
package com.example.ensatecertnotes.model;

public class Professeur {
    private int id;
    private int userId;
    private String nom;
    private String prenom;
    private String departement;
    private String telephone;
    private String photoUrl;

    public Professeur() {
    }

    public Professeur(int id, int userId, String nom, String prenom, String departement, String telephone,
            String photoUrl) {
        this.id = id;
        this.userId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.departement = departement;
        this.telephone = telephone;
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

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
