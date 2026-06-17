package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

public class Organisateur implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private String prenom;
    private String mdp;
    private String id;
    private String email;
    private ArrayList<Concert>  concerts = new ArrayList<>();
    private Concert dernierConcertConsulte = null;

    public Organisateur(String nom, String prenom, String mdp, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.mdp = mdp;
        this.email = email;
    }

    public BigInteger générer_id(String email) {
        email= email.toLowerCase();
        StringBuilder idTemp=new StringBuilder();
        for (int i = 0; i < email.length(); i++) {
            char caractere = email.charAt(i);
            if (caractere >= 'a' && caractere <= 'z') {
                int positionAlphabet = caractere - 'a' + 1;
                idTemp.append(positionAlphabet);
            }
        }
        BigInteger idFinal = new BigInteger(idTemp.toString());
        return idFinal;

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

    public Concert getDernierConcertConsulte() {
        return this.dernierConcertConsulte;
    }
    public void setDernierConcertConsulte(Concert concert) {
        this.dernierConcertConsulte = concert;
    }



    public ArrayList<Concert> getConcerts() {
        return concerts;
    }

    public void creerConcert(Concert concert) {
        this.concerts.add(concert);
    }

    public void supprimerConcert(Concert concert) {
        this.concerts.remove(concert);
    }
}


