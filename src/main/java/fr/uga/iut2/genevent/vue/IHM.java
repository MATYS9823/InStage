package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.modele.Concert;
import fr.uga.iut2.genevent.modele.bases.Evenement;
import fr.uga.iut2.genevent.modele.bases.Utilisateur;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;


public abstract class IHM {
    /**
     * Classe conteneur pour les informations saisies à propos d'un
     * {@link Utilisateur}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosOrganisateur{
        public final String email;
        public final String nom;
        public final String prenom;
        public final String mdp;

        public InfosOrganisateur(final String email, final String nom, final String prenom,  final String mdp) {
            this.email = email;
            this.nom = nom;
            this.prenom = prenom;
            this.mdp = mdp;
        }
    }

    public static class InfosMateriel{
        public final int prix;
        public final String typeEquipement;
        public final String fournisseur;
        public final String nom;

        public InfosMateriel(final int prix, final String typeEquipement,final String fournisseur,final String nom){
            this.prix=prix;
            this.typeEquipement=typeEquipement;
            this.fournisseur=fournisseur;
            this.nom=nom;
        }
    }

    /**
     * Classe conteneur pour les informations saisies pour un nouvel
     * {@link Evenement}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosNouveauConcert {
        public final String nom;
        public final LocalDate dateDebut;
        public final LocalDate dateFin;
        public final String lieu;
        public final String artistes;

        public InfosNouveauConcert(String nom, LocalDate dateDebut, LocalDate dateFin, String lieu, String artistes) {
            this.nom = nom;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            this.lieu = lieu;
            this.artistes = artistes;
        }

        @Override
        public String toString() {
            return "le concert :"+ nom+ " "+ dateDebut+" "+ dateFin+" " +lieu;
        }
    }

    public static class InfosConnexion {
        public final String email;
        public final String mdp;

        public InfosConnexion(String email, String mdp) {
            this.email = email;
            this.mdp = mdp;
        }
    }

    /**
     * Rend actif l'interface Humain-machine.
     *
     * L'appel est bloquant : le contrôle est rendu à l'appelant une fois que
     * l'IHM est fermée.
     *
     */
    public abstract void demarrerInteraction();

    /**
     * Affiche un message d'information à l'attention de l'utilisa·teur/trice.
     *
     * @param msg Le message à afficher.
     *
     * @param succes true si le message informe d'une opération réussie, false
     *     sinon.
     */
    public abstract void informerUtilisateur(final String msg, final boolean succes);

    /**
     * Récupère les informations à propos d'un
     * {@link Utilisateur}.
     *
     */
    public abstract void saisirOrganisateur();

    /**
     * Récupère les informations nécessaires à la création d'un nouvel
     * {@link Evenement}.
     *
     * @param nomsExistants L'ensemble des noms d'évenements qui ne sont plus
     *     disponibles.
     *
     */
    public abstract void saisirNouveauConcert(final Set<String> nomsExistants);

    public abstract void saisirConnexion();
    public abstract void saisirInscription();

    public abstract void afficherConcerts(Map<String, Concert> concerts);



}
