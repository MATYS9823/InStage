package fr.uga.iut2.genevent.modele.test;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.GenEvent;
import fr.uga.iut2.genevent.modele.Salle;

import java.util.ArrayList;

public class MainTestMap {
    public static void main(String[] args) {
        System.out.println("Lancement de GenEvent...");

        // 1. Initialisation du Modèle
        GenEvent modeleGenEvent = new GenEvent();

        // 2. Utilisateur de test
        modeleGenEvent.ajouteOrganisateur(
                "TestNom", "TestPrenom", "1234", "test@test.fr"
        );
        System.out.println("💡 Astuce : Connectez-vous avec test@test.fr / 1234");

        // --- 3. Ajout manuel des Salles de test AVANT de lancer l'interface ---
        Salle summum = new Salle("Le Summum", new ArrayList<>(), 3000, "Rue Henri Barbusse, Grenoble", 500, 45.1585, 5.7314);
        Salle belleElectrique2 = new Salle("La Belle Électrique 2", new ArrayList<>(), 1000, "Esplanade Andry Farcy, Grenoble", 150, 45.1865, 5.7042);
        Salle heureBleue = new Salle("L'Heure Bleue", new ArrayList<>(), 600, "Avenue Jean Vilar, Saint-Martin-d'Hères", 80, 45.1783, 5.7668);
        Salle grandAngle = new Salle("Le Grand Angle", new ArrayList<>(), 1700, "Rue du Moulin, Voiron", 250, 45.3672, 5.5881);

        // On ajoute nos salles de test directement dans la liste gérée par GenEvent
        modeleGenEvent.getSalles().add(summum);
        modeleGenEvent.getSalles().add(belleElectrique2);
        modeleGenEvent.getSalles().add(heureBleue);
        modeleGenEvent.getSalles().add(grandAngle);
        System.out.println("✅ Salles de test ajoutées au modèle.");

        // 4. Initialisation du Contrôleur
        Controleur controleur = new Controleur(modeleGenEvent);

        // 5. Démarrage de l'Interface Graphique (Bloquant)
        try {
            controleur.demarrer();
        } catch (Exception e) {
            System.err.println("Une erreur critique est survenue lors du lancement de l'interface.");
            e.printStackTrace();
        }
    }
}