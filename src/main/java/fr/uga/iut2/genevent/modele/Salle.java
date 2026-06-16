package fr.uga.iut2.genevent.modele;

import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Salle implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private List<Place> listePlaces=new ArrayList<>();
    private int nbPlaces;
    private String lieu;
    private int tarif;
    private List<Occupation> occupations = new ArrayList<>();
    private double latitude;
    private double longitude;

    public Salle(String nom, List<Place> listePlaces, int nbPlaces, String lieu, int tarif, double latitude, double longitude) {
        this.nom = nom;
        this.listePlaces = listePlaces;
        this.nbPlaces = nbPlaces;
        this.lieu = lieu;
        this.tarif = tarif;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // --- NOUVELLE MÉTHODE POUR LA CARTE (JSON) ---
    public String toJson() {
        return String.format("{\"nom\":\"%s\",\"adresse\":\"%s\",\"latitude\":%f,\"longitude\":%f}",
                nom.replace("\"", "\\\""), lieu.replace("\"", "\\\""), latitude, longitude);
    }



    // NOUVEAU : Méthode pour vérifier si la salle est libre pour des dates données
    public boolean estDisponible(LocalDate debut, LocalDate fin) {
        for (Occupation occ : occupations) {
            if (occ.chevauche(debut, fin)) {
                return false; // La salle est occupée sur cette période
            }
        }
        return true; // Aucune occupation ne chevauche, la salle est libre
    }

    // NOUVEAU : Méthode pour réserver la salle
    public void ajouterOccupation(LocalDate debut, LocalDate fin) {
        this.occupations.add(new Occupation(debut, fin));
    }

    /**
     * Calcule le coût total de la location de la salle pour une période donnée.
     * * @param debut La date de début du concert
     * @param fin La date de fin du concert
     * @return Le prix total en euros
     * @throws IllegalArgumentException si la date de fin est antérieure à la date de début
     */
    public int calculerPrixLocation(LocalDate debut, LocalDate fin) {
        // Calcul du nombre de jours (inclusif)
        long nbJours = ChronoUnit.DAYS.between(debut, fin) + 1;

        // Sécurité : on vérifie que les dates sont cohérentes
        if (nbJours <= 0) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }

        // Calcul du prix total (tarif de la salle * nombre de jours)
        return (int) (nbJours * this.tarif);
    }



}
