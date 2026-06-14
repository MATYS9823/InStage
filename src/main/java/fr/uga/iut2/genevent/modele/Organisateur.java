package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.util.ArrayList;

public class Organisateur implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private String prenom;
    private String mdp;
    private String id;
    private String email;
    private ArrayList<Concert>  concerts = new ArrayList<Concert>();

    public Organisateur(String nom, String prenom, String mdp, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.mdp = mdp;
        this.email = email;
    }

    public void générer_id() {
        this.id = "id";
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMdp() {
        return mdp;
    }
    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }

    public ArrayList<Concert> getConcerts() {
        return concerts;
    }

    public void creerConcert(Concert concert) {
        this.concerts.add(concert);
    }
}


