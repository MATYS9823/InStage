package fr.uga.iut2.genevent.modele;

import java.io.Serializable;

public class Salle implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private int nbPlaces;
    private String lieu;
    private int tarif;
    private String type;

    public Salle(String nom,String lieu,int tarif,String type){
        this.nom=nom;
        this.lieu=lieu;
        this.tarif=tarif;
        this.type=type;
    }

    public String getNom() {
        return nom;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public String getLieu() {
        return lieu;
    }

    public int getTarif() {
        return tarif;
    }

    public String getType() {
        return type;
    }
}
