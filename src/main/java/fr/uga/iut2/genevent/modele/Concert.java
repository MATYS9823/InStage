package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Concert implements Serializable {
    private static final long serialVersionUID = 1L;

    // CHANGEMENT ICI : Utilisation de LocalDate au lieu de String
    private LocalDate date_debut;
    private LocalDate date_fin;

    private String lieu;
    private ArrayList<String> artiste = new ArrayList<>();
    private String nom;
    private Salle salle = null;
    private Merch merch;
    private List<Materiel> materiels=new ArrayList<>();

    // Constructeur mis à jour
    public Concert(LocalDate date_debut, LocalDate date_fin, String lieu, String nom) {
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.lieu = lieu;
        this.nom = nom;
    }

    /**
     * Recherche les salles disponibles parmi une liste donnée,
     * pour les dates du concert.
     */
    public List<Salle> rechercherSallesDisponibles(List<Salle> toutesLesSalles) {
        List<Salle> sallesDisponibles = new ArrayList<>();

        for (Salle s : toutesLesSalles) {
            // On demande à la salle si elle est libre à nos dates
            if (s.estDisponible(this.date_debut, this.date_fin)) {
                sallesDisponibles.add(s);
            }
        }

        return sallesDisponibles;
    }

    public LocalDate getDate_debut() {
        return date_debut;
    }

    public LocalDate getDate_fin() {
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

    public List<Materiel> getMateriels() {
        return materiels;
    }
     public void ajouterMateriel(Materiel materiel){
        if (materiels.contains(materiel)){
            System.out.println("Ce materiel est déja présent");
        }
        else {materiels.add(materiel);}
     }

    public void suprimerMateriel(Materiel materiel){
        if (materiels.contains(materiel)){
            materiels.remove(materiel);
        }
        else {System.out.println("Ce materiel n'est pas présent");}
    }
}
