package fr.uga.iut2.genevent;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.util.Persisteur;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static final int EXIT_ERR_LOAD = 2;
    public static final int EXIT_ERR_SAVE = 3;

    public static void main(String[] args) {
        GenEvent genevent = null;

        try {
            genevent = Persisteur.lireEtat();
        }
        catch (ClassNotFoundException | IOException ignored) {
            System.err.println("Erreur irrécupérable pendant le chargement de l'état : fin d'exécution !");
            System.err.flush();
            System.exit(Main.EXIT_ERR_LOAD);
        }

        // --- 1. CRÉATION DES SALLES DE TEST ---
        Salle summum = new Salle("Le Summum", new ArrayList<>(), 3000, "Rue Henri Barbusse, Grenoble", 500, 45.1585, 5.7314);
        Salle belleElectrique2 = new Salle("La Belle Électrique 2", new ArrayList<>(), 1000, "Esplanade Andry Farcy, Grenoble", 150, 45.1865, 5.7042);
        Salle heureBleue = new Salle("L'Heure Bleue", new ArrayList<>(), 600, "Avenue Jean Vilar, Saint-Martin-d'Hères", 80, 45.1783, 5.7668);
        Salle grandAngle = new Salle("Le Grand Angle", new ArrayList<>(), 1700, "Rue du Moulin, Voiron", 250, 45.3672, 5.5881);

        LocalDate dateDebutSummum = LocalDate.of(2026, 10, 15);
        LocalDate dateFinSummum = LocalDate.of(2026, 10, 20);
        summum.ajouterOccupation(dateDebutSummum, dateFinSummum);

        LocalDate dateDebutBelle = LocalDate.of(2026, 11, 5);
        LocalDate dateFinBelle = LocalDate.of(2026, 11, 10);
        belleElectrique2.ajouterOccupation(dateDebutBelle, dateFinBelle);

        genevent.ajouterSalle(summum);
        genevent.ajouterSalle(belleElectrique2);
        genevent.ajouterSalle(heureBleue);
        genevent.ajouterSalle(grandAngle);

        // =================================================================================
        // --- 2. GÉNÉRATION DES DONNÉES DE TEST (LOGIN RAPIDE ET CONCERTS COMPLETS) ---
        // =================================================================================

        // On vérifie si le compte de test existe déjà pour ne pas tout recréer en boucle
        if (genevent.getOrganisateur("test@test.com") == null) {

            // Création du compte de connexion rapide
            genevent.ajouteOrganisateur("Organisateur", "Test", "1234", "test@test.com");
            System.out.println("✅ COMPTE TEST CRÉÉ -> Email : test@test.com | Mdp : 1234");

            // ---------------------------------------------------------
            // CONCERT 1 : Rock Festival
            // ---------------------------------------------------------
            genevent.nouveauConcert("Rock Festival 2026", LocalDate.of(2026, 10, 15), LocalDate.of(2026, 10, 17), "Le Summum", "The Rockers", "test@test.com");
            Concert c1 = genevent.getConcertParNom("Rock Festival 2026");

            if (c1 != null) {
                // a. Salle et Matériel
                c1.setSalle(summum);
                genevent.nouveauMateriel(5000, "Son", "AudioPro", "Enceintes Façade", c1.getNom());
                genevent.nouveauMateriel(2500, "Lumière", "LightShow", "Projecteurs LED", c1.getNom());

                // b. Équipe (avec plusieurs membres)
                genevent.nouvelEquipe("Sécurité", "Toute la salle", c1.getNom());
                if (!c1.getEquipes().isEmpty()) {
                    Equipe secu = c1.getEquipes().get(0);
                    secu.ajouterPersonnel("Paul Dupont");
                    secu.ajouterPersonnel("Marc Leblanc");
                    secu.ajouterPersonnel("Sophie Martin");
                }

                // c. Merch Classique (avec plusieurs produits)
                genevent.nouveauMerch("Hall Principal", "Boutique Officielle", c1.getNom());
                Merch boutique = c1.getMerchs().stream().filter(m -> m.getNom().equals("Boutique Officielle")).findFirst().orElse(null);
                if (boutique != null) {
                    boutique.ajouterProduit(new Produit(25, "T-Shirt Tournée", "Vêtement"));
                    boutique.ajouterProduit(new Produit(10, "Affiche Dédicacée", "Goodies"));
                }

                // d. Carte & ses Produits (Liés par le même nom dans ton système)
                genevent.nouvelcarte("VIP", "Pass Premium", c1.getNom());
                genevent.nouveauMerch("Virtuel", "Pass Premium", c1.getNom()); // On crée le Merch fantôme pour stocker les produits de la carte
                Merch passPremium = c1.getMerchs().stream().filter(m -> m.getNom().equals("Pass Premium")).findFirst().orElse(null);
                if (passPremium != null) {
                    passPremium.ajouterProduit(new Produit(50, "Carnet Boissons x5", "Conso"));
                    passPremium.ajouterProduit(new Produit(150, "Rencontre Artiste", "Service"));
                }
            }

            // ---------------------------------------------------------
            // CONCERT 2 : Electro Night
            // ---------------------------------------------------------
            genevent.nouveauConcert("Electro Night", LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 6), "La Belle Électrique 2", "DJ Khaled", "test@test.com");
            Concert c2 = genevent.getConcertParNom("Electro Night");

            if (c2 != null) {
                // a. Salle et Matériel
                c2.setSalle(belleElectrique2);
                genevent.nouveauMateriel(1500, "Scène", "DJ Setup", "Platines Pioneer", c2.getNom());
                genevent.nouveauMateriel(800, "Effets", "SmokeCorp", "Machine à fumée", c2.getNom());

                // b. Équipe
                genevent.nouvelEquipe("Bar", "Fosse", c2.getNom());
                if (!c2.getEquipes().isEmpty()) {
                    Equipe bar = c2.getEquipes().get(0);
                    bar.ajouterPersonnel("Alice Dubois");
                    bar.ajouterPersonnel("Lucas Moreau");
                }

                // c. Buvette
                genevent.nouvelBuvette("Mezzanine", "Bar VIP", c2.getNom());

                // d. Merch Classique
                genevent.nouveauMerch("Entrée", "ElectroStore", c2.getNom());
                Merch electroStore = c2.getMerchs().stream().filter(m -> m.getNom().equals("ElectroStore")).findFirst().orElse(null);
                if (electroStore != null) {
                    electroStore.ajouterProduit(new Produit(35, "Sweat à capuche", "Vêtement"));
                    electroStore.ajouterProduit(new Produit(5, "Bracelet Fluorescent", "Accessoire"));
                }

                // e. Carte (Cashless)
                genevent.nouvelcarte("Cashless", "Carte Electro", c2.getNom());
                genevent.nouveauMerch("Virtuel", "Carte Electro", c2.getNom());
                Merch carteElectro = c2.getMerchs().stream().filter(m -> m.getNom().equals("Carte Electro")).findFirst().orElse(null);
                if (carteElectro != null) {
                    carteElectro.ajouterProduit(new Produit(20, "Recharge 20€", "Crédit"));
                    carteElectro.ajouterProduit(new Produit(50, "Recharge 50€", "Crédit"));
                }
            }
        }


        // --- 3. Initialisation et démarrage de l'interface graphique ---
        Controleur controleur = new Controleur(genevent);
        controleur.demarrer();

        // --- 4. Sauvegarde de l'état une fois l'interface fermée ---
        try {
            Persisteur.sauverEtat(genevent);
        }
        catch (IOException ignored) {
            System.err.println("Erreur irrécupérable pendant la sauvegarde de l'état : fin d'exécution !");
            System.err.flush();
            System.exit(Main.EXIT_ERR_SAVE);
        }
    }
}