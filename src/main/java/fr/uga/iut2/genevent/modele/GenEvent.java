package fr.uga.iut2.genevent.modele;

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
    private final Map<String,Merch> merch;
    private final Map<String,Equipe> equipe;

    private List<Salle> salles = new ArrayList<>();

    public GenEvent() {
        this.organisateurs = new HashMap<>();
        this.concerts = new HashMap<>();
        this.materiel = new HashMap<>();
        this.merch = new HashMap<>();
        this.equipe = new HashMap<>();



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
        if (this.salles==null){
            this.salles=new ArrayList<>();
        }
        return this.salles;
    }

    public void ajouterSalle(Salle salle){
        // Si la liste n'a pas été initialisée (à cause d'une ancienne sauvegarde)
        if (this.salles == null) {
            this.salles = new ArrayList<>();
        }
        if (!this.salles.contains(salle))
        {this.salles.add(salle);}
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

    public Concert getConcertParNom(String nom) {
        return this.concerts.get(nom);
    }

    public Map<String, Concert> getConcerts() {
        return this.concerts;
    }

    public Map<String, Organisateur> getOrganisateurs() {
        return this.organisateurs;
    }

    public void nouveauConcert(String nom, LocalDate dateDebut, LocalDate dateFin, String lieu,String artistes, String emailOrganisateur) {
        assert !this.concerts.containsKey(nom);
        assert this.organisateurs.containsKey(emailOrganisateur);

        Concert concert = new Concert(dateDebut, dateFin, lieu, nom ,artistes);
        this.concerts.put(nom, concert);
        this.organisateurs.get(emailOrganisateur).creerConcert(concert);
    }

    public void nouveauMateriel(int prix, String typeEquipement, String fournisseur, String nom, String nomConcert){
        Materiel materiel = new Materiel(prix,typeEquipement,fournisseur,nom);
        this.materiel.put(nom,materiel);
        this.concerts.get(nomConcert).ajouterMateriel(materiel);
    }

    public void nouveauMerch(String localisation, String nom, String nomConcert){
        Merch merch = new Merch(localisation, nom);
        this.merch.put(nom, merch);
        this.concerts.get(nomConcert).ajouterMerch(merch);
    }

    public void  nouvelEquipe(String nom,String categorie,String nomConcert){
        Equipe equipe = new Equipe(nom,categorie);
        this.equipe.put(nom,equipe);
        this.concerts.get(nomConcert).ajouterEquipe(equipe);
    }

    public void supprimerConcert(Concert concert, String emailOrganisateur) {
        assert this.concerts.containsKey(concert.getNom());
        assert this.organisateurs.containsKey(emailOrganisateur);

        this.concerts.remove(concert.getNom());
        this.organisateurs.get(emailOrganisateur).supprimerConcert(concert);
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
