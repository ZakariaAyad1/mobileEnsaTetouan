
package com.example.ensatecertnotes.model;


public class Notification {
    /*salma*/
    private int id;
    private int userId;
    private String titre;
    private String message;
    private String type; // INFO, SUCCES, ALERTE, URGENT
    private int lu; // 0 or 1
    private String dateCreation;

    public Notification() {
    }

    public Notification(int userId, String titre, String message, String type) {
        this.userId = userId;
        this.titre = titre;
        this.message = message;
        this.type = type;
        this.lu = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getLu() { return lu; }
    public void setLu(int lu) { this.lu = lu; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }
    /*salma*/
}
