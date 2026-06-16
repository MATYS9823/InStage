package fr.uga.iut2.genevent.modele.test;
import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.GenEvent;
public class MainTestMap {
    public static void main(String[] args) {
        System.out.println("Lancement de GenEvent...");

        // 1. Initialisation du Modèle (Le Cœur des données)
        // C'est ici que vos salles par défaut ("Accor Arena", etc.) sont créées en mémoire.
        GenEvent modeleGenEvent = new GenEvent();

        // --- OPTIONNEL : Création d'un utilisateur de test ---
        // Pour vous éviter de devoir créer un compte à chaque fois que vous lancez l'application
        // pour tester votre nouvelle page, on injecte un organisateur par défaut :
        modeleGenEvent.ajouteOrganisateur(
                "TestNom",
                "TestPrenom",
                "1234", // Mot de passe
                "test@test.fr" // Email
        );
        System.out.println("💡 Astuce : Connectez-vous avec test@test.fr / 1234");

        // 2. Initialisation du Contrôleur (Le Chef d'Orchestre)
        // En se créant, le contrôleur va automatiquement générer la vue (JavaFXGUI)
        Controleur controleur = new Controleur(modeleGenEvent);

        // 3. Démarrage de l'Interface Graphique
        try {
            controleur.demarrer();
        } catch (Exception e) {
            System.err.println("Une erreur critique est survenue lors du lancement de l'interface.");
            e.printStackTrace();
        }
    }
}
