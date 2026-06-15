package fr.uga.iut2.genevent.modele;

import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Salle implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private List<Place> listePlaces=new ArrayList<>();
    private int nbPlaces;
    private String lieu;
    private int tarif;


    public Salle(String nom, List<Place> listePlaces, int nbPlaces, String lieu, int tarif) {
        this.nom = nom;
        this.listePlaces = listePlaces;
        this.nbPlaces = nbPlaces;
        this.lieu = lieu;
        this.tarif = tarif;
    }

    public Salle(String nom,String lieu,int tarif){
        this.nom=nom;
        this.lieu=lieu;
        this.tarif=tarif;
    }

    public String getNom() {
        return nom;
    }

    public List<Place> getListePlaces() {
        return listePlaces;
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
}
