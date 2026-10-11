package com.imc.beans;

import java.io.Serializable;

public class MonIMC implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nom;
    private String prenom;
    private double masse;
    private double taille;
    private double imc;
    private String interpretation;

    public MonIMC() {}

    public MonIMC(int id, String nom, String prenom, double masse, double taille) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.masse = masse;
        this.taille = taille;
        calculer();
    }

    /** Calcule l'IMC et son interprétation (normes OMS). */
    public void calculer() {
        this.imc = Math.round(masse / (taille * taille) * 100.0) / 100.0;
        if (imc < 18.5)      interpretation = "Insuffisance pondérale";
        else if (imc < 25)   interpretation = "Corpulence normale";
        else if (imc < 30)   interpretation = "Surpoids";
        else if (imc < 35)   interpretation = "Obésité modérée";
        else if (imc < 40)   interpretation = "Obésité sévère";
        else                 interpretation = "Obésité morbide";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public double getMasse() { return masse; }
    public void setMasse(double masse) { this.masse = masse; }
    public double getTaille() { return taille; }
    public void setTaille(double taille) { this.taille = taille; }
    public double getImc() { return imc; }
    public void setImc(double imc) { this.imc = imc; }
    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
}