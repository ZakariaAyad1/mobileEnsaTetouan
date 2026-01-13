
package com.example.ensatecertnotes.model;

public class Module {
    private int id;
    private String codeModule;
    private String nomModule;
    private int semestre;
    private double coefficient;
    private int professeurId;
    private String anneeUniversitaire;

    public Module() {
    }

    public Module(int id, String codeModule, String nomModule, int semestre, double coefficient, int professeurId,
            String anneeUniversitaire) {
        this.id = id;
        this.codeModule = codeModule;
        this.nomModule = nomModule;
        this.semestre = semestre;
        this.coefficient = coefficient;
        this.professeurId = professeurId;
        this.anneeUniversitaire = anneeUniversitaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeModule() {
        return codeModule;
    }

    public void setCodeModule(String codeModule) {
        this.codeModule = codeModule;
    }

    public String getNomModule() {
        return nomModule;
    }

    public void setNomModule(String nomModule) {
        this.nomModule = nomModule;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getProfesseurId() {
        return professeurId;
    }

    public void setProfesseurId(int professeurId) {
        this.professeurId = professeurId;
    }

    public String getAnneeUniversitaire() {
        return anneeUniversitaire;
    }

    public void setAnneeUniversitaire(String anneeUniversitaire) {
        this.anneeUniversitaire = anneeUniversitaire;
    }
}
