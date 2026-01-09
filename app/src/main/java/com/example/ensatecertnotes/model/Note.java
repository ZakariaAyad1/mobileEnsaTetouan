
package com.example.ensatecertnotes.model;

public class Note {
    private int id;
    private int etudiantId;
    private int moduleId;
    private Double noteExamen;
    private Double noteTd;
    private Double noteTp;
    private Double noteFinale;
    private String statut;
    private String observation;

    public Note() {
    }

    public Note(int id, int etudiantId, int moduleId, Double noteExamen, Double noteTd, Double noteTp,
            Double noteFinale, String statut, String observation) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.moduleId = moduleId;
        this.noteExamen = noteExamen;
        this.noteTd = noteTd;
        this.noteTp = noteTp;
        this.noteFinale = noteFinale;
        this.statut = statut;
        this.observation = observation;
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

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public Double getNoteExamen() {
        return noteExamen;
    }

    public void setNoteExamen(Double noteExamen) {
        this.noteExamen = noteExamen;
    }

    public Double getNoteTd() {
        return noteTd;
    }

    public void setNoteTd(Double noteTd) {
        this.noteTd = noteTd;
    }

    public Double getNoteTp() {
        return noteTp;
    }

    public void setNoteTp(Double noteTp) {
        this.noteTp = noteTp;
    }

    public Double getNoteFinale() {
        return noteFinale;
    }

    public void setNoteFinale(Double noteFinale) {
        this.noteFinale = noteFinale;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
