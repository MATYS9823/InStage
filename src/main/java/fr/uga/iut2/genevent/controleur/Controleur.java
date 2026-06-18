package fr.uga.iut2.genevent.controleur;

import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.vue.CLI;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.JavaFXGUI;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe centrale du patron MVC.
 * Fait le lien entre l'interface utilisateur (Vue/IHM) et les données (Modèle/GenEvent).
 */
public class Controleur {

    private final GenEvent genevent;
    private final IHM ihm;
    private Organisateur organisateurConnecte;  // ← session
    private Concert concertSelectionne;


    public Controleur(GenEvent genevent) {
        this.genevent = genevent;
//        this.ihm = new CLI(this);
        this.ihm = new JavaFXGUI(this);
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
                infos.artistes,
                this.organisateurConnecte.getEmail()  // ← l'organisateur connecté
        );
        this.ihm.informerUtilisateur(
                "Nouveau concert : " + infos.nom + " à " + infos.lieu,
                true
        );
    }

    public void creerMateriel(IHM.InfosMateriel infosMateriel){
        this.genevent.nouveauMateriel(
                infosMateriel.prix,
                infosMateriel.typeEquipement,
                infosMateriel.fournisseur,
                infosMateriel.nom,
                this.concertSelectionne.getNom()
        );
        this.ihm.informerUtilisateur(
                "Nouveau matériel créer : "+infosMateriel.nom,
                true
        );
    }

    public void creerMerch(IHM.InfosMerch infosMerch){
        this.genevent.nouveauMerch(
                infosMerch.localisation,
                infosMerch.nom,
                this.concertSelectionne.getNom()
        );
        this.ihm.informerUtilisateur(
                "Nouveau merch créé : " + infosMerch.nom,
                true
        );
    }

    public void creerEquipe(IHM.InfosMerch infosMerch){
        this.genevent.nouvelEquipe(
                infosMerch.nom,
                infosMerch.localisation,
                this.concertSelectionne.getNom()
        );
        this.ihm.informerUtilisateur(
                "Nouvel equipe : " + infosMerch.nom,
                true
        );
    }

    public void creerBuvette(IHM.InfosBuvette infosBuvette){
        this.genevent.nouvelBuvette(
                infosBuvette.nom,          // CORRECTION : GenEvent attend le nom ici !
                infosBuvette.localisation, // CORRECTION : et la localisation ici !
                this.concertSelectionne.getNom()
        );
        this.ihm.informerUtilisateur(
                "Nouvelle buvette : " + infosBuvette.nom,
                true
        );
    }

    public void creercarte(IHM.InfosCarte infosCarte){
        this.genevent.nouvelcarte(
                infosCarte.localisation,
                infosCarte.nom,
                this.concertSelectionne.getNom()
        );
        this.ihm.informerUtilisateur(
                "Nouvel carte : " + infosCarte.nom,
                true
        );
    }

    public void supprimerConcert(Concert concert) {
        if (!this.estConnecte()) {
            this.ihm.informerUtilisateur("Vous devez être connecté pour supprimer un concert.", false);
            return;
        }
        if (!this.organisateurConnecte.getConcerts().contains(concert)) {
            this.ihm.informerUtilisateur("Ce concert ne vous appartient pas.", false);
            return;
        }

        this.genevent.supprimerConcert(concert,this.organisateurConnecte.getEmail());
        this.ihm.informerUtilisateur("Le concert \"" + concert.getNom() + "\" a été supprimé.", true);
    }

    public void supprimerMateriel(Materiel materiel) {
        this.genevent.supprimerMateriel(materiel,this.concertSelectionne.getNom());
        this.ihm.informerUtilisateur("Le matériel \"" + materiel.getNom() + "\" a été supprimé.", true);
    }

    public void supprimerBuvette(Buvette buvette) {
        this.genevent.supprimerBuvette(buvette,this.concertSelectionne.getNom());
        this.ihm.informerUtilisateur("La buvette \"" + buvette.getNom() + "\" a été supprimé.", true);
    }

    public void supprimerEquipe(Equipe equipe) {
        this.genevent.supprimerEquipe(equipe,this.concertSelectionne.getNom());
        this.ihm.informerUtilisateur("L'equipe'\"" + equipe.getNom() + "\" a été supprimé.", true);
    }

    public void supprimerCarte(Carte carte) {
        this.genevent.supprimerCarte(carte,this.concertSelectionne.getNom());
        this.ihm.informerUtilisateur("La carte '\"" + carte.getNom() + "\" a été supprimé.", true);
    }

    public void supprimerMerch(Merch merch) {
        this.genevent.supprimerMerch(merch,this.concertSelectionne.getNom());
        this.ihm.informerUtilisateur("Le merch '\"" + merch.getNom() + "\" a été supprimé.", true);
    }


    public void saisirConnexion() {
        this.ihm.saisirConnexion();
    }

    public void afficherconcerts() {
        this.ihm.afficherConcerts(this.genevent.getConcerts()); ;
    }

    public java.util.List<fr.uga.iut2.genevent.modele.Salle> getToutesLesSalles() {
        // IMPORTANT : Assurez-vous que votre classe GenEvent possède bien
        // une méthode getSalles() qui retourne la liste des salles existantes.
        return this.genevent.getSalles();
    }

    public void selectionnerConcertRecent(Concert concert) {
        this.concertSelectionne = concert;
        // NOUVEAU : On mémorise ce concert pour l'organisateur actuel
        if (this.organisateurConnecte != null) {
            this.organisateurConnecte.setDernierConcertConsulte(concert);
        }
    }

    public Organisateur getOrganisateur(String email) {
        return this.genevent.getOrganisateur(email);
    }


    public void selectionnerConcert(Concert concert) {
        this.concertSelectionne = concert;
    }

    public Concert getConcertSelectionne() {
        return this.concertSelectionne;
    }

    public Organisateur getOrganisateurConnecte() {
        return this.organisateurConnecte;
    }



}
