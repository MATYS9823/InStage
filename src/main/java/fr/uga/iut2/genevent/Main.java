package fr.uga.iut2.genevent;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.GenEvent;
import fr.uga.iut2.genevent.modele.Salle;
import fr.uga.iut2.genevent.util.Persisteur;
import java.io.IOException;
import java.time.LocalDate; // N'oublie pas d'importer LocalDate !
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

        Salle summum = new Salle("Le Summum", new ArrayList<>(), 3000, "Rue Henri Barbusse, Grenoble", 500, 45.1585, 5.7314);
        Salle belleElectrique2 = new Salle("La Belle Électrique 2", new ArrayList<>(), 1000, "Esplanade Andry Farcy, Grenoble", 150, 45.1865, 5.7042);
        Salle heureBleue = new Salle("L'Heure Bleue", new ArrayList<>(), 600, "Avenue Jean Vilar, Saint-Martin-d'Hères", 80, 45.1783, 5.7668);
        Salle grandAngle = new Salle("Le Grand Angle", new ArrayList<>(), 1700, "Rue du Moulin, Voiron", 250, 45.3672, 5.5881);

        // --- DÉBUT DES DONNÉES DE TEST POUR LES DATES ---

        // Le Summum est occupé du 15 Octobre 2026 au 20 Octobre 2026
        LocalDate dateDebutSummum = LocalDate.of(2026, 10, 15);
        LocalDate dateFinSummum = LocalDate.of(2026, 10, 20);
        summum.ajouterOccupation(dateDebutSummum, dateFinSummum);

        // La Belle Électrique 2 est occupée du 5 Novembre 2026 au 10 Novembre 2026
        LocalDate dateDebutBelle = LocalDate.of(2026, 11, 5);
        LocalDate dateFinBelle = LocalDate.of(2026, 11, 10);
        belleElectrique2.ajouterOccupation(dateDebutBelle, dateFinBelle);

        // L'Heure Bleue et Le Grand Angle restent libres tout le temps
        // --- FIN DES DONNÉES DE TEST ---

        // 2. Ajout des salles
        // Grâce à notre nouvelle méthode ajouterSalle, il n'y aura plus de doublons !
        genevent.ajouterSalle(summum);
        genevent.ajouterSalle(belleElectrique2);
        genevent.ajouterSalle(heureBleue);
        genevent.ajouterSalle(grandAngle);
        System.out.println("✅ Salles de test ajoutées au modèle avec des occupations de test.");

        // 3. Initialisation et démarrage de l'interface graphique
        Controleur controleur = new Controleur(genevent);
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