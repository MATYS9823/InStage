package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.util.ArrayList;

public class Concert implements Serializable {
    private static final long serialVersionUID = 1L;
    private  String date_debut;
    private  String date_fin;
    private String lieu;
    private ArrayList<String> artiste =  new ArrayList<String>();
    private String nom;
    private Salle salle =null;

    public Concert(String date_debut, String date_fin, String lieu, String nom) {
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.lieu = lieu;
        this.nom = nom;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public String getLieu() {
        return lieu;
    }

    public ArrayList<String> getArtiste() {
        return artiste;
    }

    public void ajouterArtiste(String artiste) {
        this.artiste.add(artiste);
    }

    public String getNom() {
        return nom;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }
}
