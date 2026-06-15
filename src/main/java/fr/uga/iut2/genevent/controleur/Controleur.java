package fr.uga.iut2.genevent.controleur;

import fr.uga.iut2.genevent.modele.Organisateur;
import fr.uga.iut2.genevent.modele.GenEvent;
import fr.uga.iut2.genevent.vue.CLI;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.JavaFXGUI;

import java.util.Set;
import java.util.stream.Collectors;

public class Controleur {

    private final GenEvent genevent;
    private final IHM ihm;
    private Organisateur organisateurConnecte;  // ← session

    public Controleur(GenEvent genevent) {
        this.genevent = genevent;
        this.ihm = new CLI(this);
//        this.ihm = new JavaFXGUI(this);
        this.organisateurConnecte = null;
    }

    public void demarrer() {
        this.ihm.demarrerInteraction();
    }

    // --- Connexion ---

    public void seConnecter(IHM.InfosConnexion infos) {
        Organisateur org = this.genevent.connecter(infos.email, infos.mdp);
        if (org != null) {
            this.organisateurConnecte = org;
            this.ihm.informerUtilisateur(
                    "Connecté en tant que " + org.getPrenom() + " " + org.getNom(),
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "Email ou mot de passe incorrect.",
                    false
            );
        }
    }

    public void seDeconnecter() {
        this.organisateurConnecte = null;
        this.ihm.informerUtilisateur("Déconnecté.", true);
    }

    public boolean estConnecte() {
        return this.organisateurConnecte != null;
    }

    // --- Organisateur ---

    public void saisirOrganisateur() {
        this.ihm.saisirOrganisateur();
    }

    public void creerOrganisateur(IHM.InfosOrganisateur infos) {
        boolean nouveau = this.genevent.ajouteOrganisateur(
                infos.nom,
                infos.prenom,
                infos.mdp,
                infos.email
        );
        if (nouveau) {
            this.ihm.informerUtilisateur(
                    "Nouvel organisateur : " + infos.prenom + " " + infos.nom + " <" + infos.email + ">",
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "L'organisateur " + infos.email + " existe déjà.",
                    false
            );
        }
    }

    // --- Concert ---

    public void saisirConcert() {
        if (!this.estConnecte()) {
            this.ihm.informerUtilisateur("Vous devez être connecté pour créer un concert.", false);
            return;
        }
        // on passe uniquement les noms des concerts de l'organisateur connecté
        Set<String> nomsExistants = this.organisateurConnecte.getConcerts()
                .stream()
                .map(c -> c.getNom())
                .collect(java.util.stream.Collectors.toSet());
        this.ihm.saisirNouveauConcert(nomsExistants);
    }

    public void creerConcert(IHM.InfosNouveauConcert infos) {
        if (!this.estConnecte()) {
            this.ihm.informerUtilisateur("Vous devez être connecté.", false);
            return;
        }
        this.genevent.nouveauConcert(
                infos.nom,
                infos.dateDebut,
                infos.dateFin,
                infos.lieu,
                this.organisateurConnecte.getEmail()  // ← l'organisateur connecté
        );
        this.ihm.informerUtilisateur(
                "Nouveau concert : " + infos.nom + " à " + infos.lieu,
                true
        );
    }

    public void saisirConnexion() {
        this.ihm.saisirConnexion();
    }

    public void afficherconcerts() {
        this.ihm.afficherConcerts(this.genevent.getConcerts()); ;
    }

    public Organisateur getOrganisateur(String email) {
        return this.genevent.getOrganisateur(email);
    }


}
