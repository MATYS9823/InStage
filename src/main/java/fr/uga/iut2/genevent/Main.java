package fr.uga.iut2.genevent;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.GenEvent;
import fr.uga.iut2.genevent.modele.Salle;
import fr.uga.iut2.genevent.util.Persisteur;
import java.io.IOException;
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

        // 1. Création des salles de test
        Salle summum = new Salle("Le Summum", new ArrayList<>(), 3000, "Rue Henri Barbusse, Grenoble", 500, 45.1585, 5.7314);
        Salle belleElectrique2 = new Salle("La Belle Électrique 2", new ArrayList<>(), 1000, "Esplanade Andry Farcy, Grenoble", 150, 45.1865, 5.7042);
        Salle heureBleue = new Salle("L'Heure Bleue", new ArrayList<>(), 600, "Avenue Jean Vilar, Saint-Martin-d'Hères", 80, 45.1783, 5.7668);
        Salle grandAngle = new Salle("Le Grand Angle", new ArrayList<>(), 1700, "Rue du Moulin, Voiron", 250, 45.3672, 5.5881);

        summum.ajouterOccupation(java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(5));
        // 2. Ajout des salles AVANT de démarrer le contrôleur
        // Utilisation de la méthode ajouterSalle() définie dans GenEvent
        genevent.ajouterSalle(summum);
        genevent.ajouterSalle(belleElectrique2);
        genevent.ajouterSalle(heureBleue);
        genevent.ajouterSalle(grandAngle);
        System.out.println("✅ Salles de test ajoutées au modèle.");

        // 3. Initialisation et démarrage de l'interface graphique
        Controleur controleur = new Controleur(genevent);
        // `Controleur.demarrer` garde le contrôle de l'exécution tant que
        // l'utilisateur/trice n'a pas saisi la commande QUITTER.
        controleur.demarrer();

        // 4. Sauvegarde de l'état une fois l'interface fermée
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