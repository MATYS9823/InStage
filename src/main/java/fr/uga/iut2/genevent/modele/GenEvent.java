package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.modele.bases.Evenement;
import fr.uga.iut2.genevent.modele.bases.Utilisateur;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList; // NOUVEAU
import java.util.HashMap;
import java.util.List;      // NOUVEAU
import java.util.Map;

public class GenEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Map<String, Organisateur> organisateurs;
    private final Map<String, Concert> concerts;
    private final Map<String,Materiel> materiel;

    // --- NOUVEAU : La liste des salles ---
    private final List<Salle> salles;

    public GenEvent() {
        this.organisateurs = new HashMap<>();
        this.concerts = new HashMap<>();
        this.materiel = new HashMap<>();
        this.salles = new ArrayList<>(); // Initialisation

        // TEMPORAIRE : On ajoute des salles par défaut au démarrage pour pouvoir tester l'interface
        initialiserSallesParDefaut();
    }

    // --- NOUVELLES MÉTHODES POUR LES SALLES ---

    private void initialiserSallesParDefaut() {
        // Ces données seront créées une seule fois à la création du GenEvent.
        // Assurez-vous que les paramètres correspondent bien au constructeur de Salle que nous avons fait !
        this.salles.add(new Salle("Accor Arena", new ArrayList<Place>(), 12000, "12 Bis Rue des Arts, Paris", 1100, 48.8386, 2.3784));
        this.salles.add(new Salle("La Belle Electrique", new ArrayList<Place>(), 12000, "12 Esp. Andry Farcy, Grenoble", 1100,  45.1852, 5.7077));
        this.salles.add(new Salle("Palais des Sports", new ArrayList<Place>(), 5000, "Boulevard Clemenceau, Grenoble", 800,  45.1866, 5.7412));
    }

    public List<Salle> getSalles() {
        return this.salles;
    }

    public boolean ajouteOrganisateur(String nom, String prenom, String mdp, String email) {
        if (this.organisateurs.containsKey(email)) {
            return false;
        }
        this.organisateurs.put(email, new Organisateur(nom, prenom, mdp, email));
        return true;
    }

    public Organisateur getOrganisateur(String email) {
        return this.organisateurs.get(email);
    }

    public Map<String, Concert> getConcerts() {
        return this.concerts;
    }

    public Map<String, Organisateur> getOrganisateurs() {
        return this.organisateurs;
    }

    public void nouveauConcert(String nom, LocalDate dateDebut, LocalDate dateFin, String lieu, String emailOrganisateur) {
        assert !this.concerts.containsKey(nom);
        assert this.organisateurs.containsKey(emailOrganisateur);

        Concert concert = new Concert(dateDebut, dateFin, lieu, nom);
        this.concerts.put(nom, concert);
        this.organisateurs.get(emailOrganisateur).creerConcert(concert);
    }

 /*   public void nouveauMateriel(String nom, int Prix, String Fournisseur, String TypeEquipement,String nomConcert){
        assert  !this.materiel.containsKey(nom);
        assert this.concerts.containsKey(nomConcert);

        Materiel materiel = new Materiel(Prix,TypeEquipement,Fournisseur,nom);
        this.materiel.put(nom,materiel);
        this.concerts.get(nomConcert).
    }*/



    public Organisateur connecter(String email, String mdp) {
        Organisateur org = this.organisateurs.get(email);
        if (org != null && org.getMdp().equals(mdp)) {
            return org;
        }
        return null;
    }
}
