package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.modele.optimisation.ResultatOptimisation;
import fr.uga.iut2.genevent.modele.optimisation.ResultatRentabiliteMin;
import fr.uga.iut2.genevent.modele.optimisation.SimulateurFinancier;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Concert implements Serializable {
    private static final long serialVersionUID = 1L;

    // CHANGEMENT ICI : Utilisation de LocalDate au lieu de String
    private LocalDate date_debut;
    private LocalDate date_fin;

    private String lieu;
    private String artistes;
    private String nom;
    private Salle salle = null;
    private Merch merch;
    private List<Materiel> materiels=new ArrayList<>();

    public Concert(LocalDate date_debut, LocalDate date_fin, String lieu, String nom,String artistes) {
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.lieu = lieu;
        this.nom = nom;
        this.artistes = artistes;
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

    public String getArtiste() {
        return artistes;
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

    /**
     * Calcule le coût de location de la salle affectée à ce concert.
     * @return Le coût total, ou 0 si aucune salle n'est affectée.
     */
    public int getCoutLocationSalle() {
        if (this.salle == null) {
            System.out.println("Aucune salle n'est affectée au concert " + this.nom);
            return 0;
        }
        // Le concert délègue le calcul à la salle en lui passant ses propres dates
        return this.salle.calculerPrixLocation(this.date_debut, this.date_fin);
    }

    /**
     * Utilise le simulateur pour générer un bilan de rentabilité pour ce concert.
     */
    public void afficherBilanFinancier(SimulateurFinancier simulateur, int tarifStandard, int tarifVIP) {
        if (this.salle == null) {
            System.out.println("Veuillez affecter une salle avant de lancer la simulation.");
            return;
        }

        System.out.println("=== Bilan Financier : " + this.nom + " ===");
        System.out.println("Salle : " + this.salle.getNom() + " (" + this.salle.getNbPlaces() + " places)");
        System.out.println("Date :  Du " + this.getDate_debut() + " au " + this.getDate_fin() + " ( "+ (ChronoUnit.DAYS.between(this.date_debut, this.date_fin) + 1)+" jours )");

        System.out.println("Coût de la location : " + this.getCoutLocationSalle() + " €");

        // 1. Calcul du revenu maximum possible (Sommet A)
        ResultatOptimisation opti = simulateur.calculerOptimisationRevenu(this.salle,this.getCoutLocationSalle(), tarifStandard, tarifVIP);
        if (opti != null) {
            System.out.println("[PROFIT MAXIMUM]");
            System.out.println("-> Places Standards : " + opti.getPlacesStandard());
            System.out.println("-> Places VIP : " + opti.getPlacesVIP());
            System.out.println("-> Chiffre d'affaires max : " + opti.getRevenuMax() + " €");
            System.out.println("-> Bénéfice net : " + (opti.getRevenuMax()-this.getCoutLocationSalle()) + " €");
        } else {
            System.out.println("⚠️ La jauge de la salle est trop petite pour atteindre l'objectif de " + this.getCoutLocationSalle() + " €.");
        }

        System.out.println("---------------------------------");

        // 2. Calcul du minimum syndical pour être rentable (Sommet B)
        ResultatRentabiliteMin min = simulateur.calculerMinimumBilletsPourRentabilite(this.salle, this.getCoutLocationSalle(), tarifStandard, tarifVIP);
        if (min != null) {
            System.out.println("[MINIMUM POUR RENTABILITÉ]");
            System.out.println("-> Minimum Places Standards : " + min.getPlacesStandard());
            System.out.println("-> Minimum Places VIP : " + min.getPlacesVIP());
            System.out.println("-> Total billets à vendre : " + min.getTotalBilletsAchetes() + " / " + this.salle.getNbPlaces());
        }
        System.out.println("===================================\n");
    }
}
