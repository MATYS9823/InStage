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
    private final Map<String,Buvette> buvette;
    private  Map<String,Carte> carte = new HashMap<>();

    private List<Salle> salles = new ArrayList<>();

    public GenEvent() {
        this.organisateurs = new HashMap<>();
        this.concerts = new HashMap<>();
        this.materiel = new HashMap<>();
        this.merch = new HashMap<>();
        this.equipe = new HashMap<>();
        this.buvette = new HashMap<>();



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

    public void ajouterSalle(Salle nouvelleSalle){
        // Si la liste n'a pas été initialisée (à cause d'une ancienne sauvegarde)
        if (this.salles == null) {
            this.salles = new ArrayList<>();
        }

        // On supprime l'ancienne version de la salle si elle existe déjà
        // (Comme ça on met à jour les données de test à chaque lancement)
        this.salles.removeIf(s -> s.getNom().equals(nouvelleSalle.getNom()));

        // On ajoute la nouvelle salle toute propre
        this.salles.add(nouvelleSalle);
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

    public void nouveauConcert(String nom, LocalDate dateDebut, LocalDate dateFin, String lieu, String artistes, String emailOrganisateur) {
        assert !this.concerts.containsKey(nom);
        assert this.organisateurs.containsKey(emailOrganisateur);

        Concert concert = new Concert(dateDebut, dateFin, lieu, nom ,artistes);
        this.concerts.put(nom, concert);
        this.organisateurs.get(emailOrganisateur).creerConcert(concert);

        // On parcours les salles existantes pour voir si le lieu correspond
        for (Salle s : getSalles()) {
            if (s.getNom().equalsIgnoreCase(lieu) || s.getLieu().equalsIgnoreCase(lieu)) {
                // On associe la salle à ce concert
                concert.setSalle(s);
                // On ajoute l'occupation dans le planning de la salle
                s.ajouterOccupation(dateDebut, dateFin);
                break; // Salle trouvée et occupée, on arrête la recherche
            }
        }
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

    public void nouvelBuvette(String nom,String localisation,String nomConcert){
        Buvette buvette = new Buvette(localisation,nom);
        this.buvette.put(nom,buvette);
        this.concerts.get(nomConcert).ajouterBuvette(buvette);
    }

    public void nouvelcarte(String theme,String nom, String nomConcert){
        if (this.carte == null) {
            this.carte = new HashMap<>();
        }
        Carte carte = new Carte(theme,nom);
        this.carte.put(nom,carte);
        this.concerts.get(nomConcert).ajouterCarte(carte);
    }

    public void supprimerConcert(Concert concert, String emailOrganisateur) {
        assert this.concerts.containsKey(concert.getNom());
        assert this.organisateurs.containsKey(emailOrganisateur);

        this.concerts.remove(concert.getNom());
        this.organisateurs.get(emailOrganisateur).supprimerConcert(concert);
    }

    public void supprimerMateriel(Materiel materiel, String nomConcert) {
        this.materiel.remove(materiel.getNom());
        this.concerts.get(nomConcert).suprimerMateriel(materiel);
    }

    public void supprimerMerch(Merch merch, String nomConcert) {
        this.merch.remove(merch.getNom());
        this.concerts.get(nomConcert).suprimerMerch(merch);
    }

    public void supprimerCarte(Carte carte, String nomConcert) {
        this.carte.remove(carte.getNom());
        this.concerts.get(nomConcert).suprimerCarte(carte);
    }

    public void supprimerBuvette(Buvette buvette, String nomConcert) {
        this.buvette.remove(buvette.getNom());
        this.concerts.get(nomConcert).suprimerBuvette(buvette);
    }

    public void  supprimerEquipe(Equipe equipe, String nomConcert) {
        this.equipe.remove(equipe.getNom());
        this.concerts.get(nomConcert).suprimerEquipe(equipe);
    }

    public Organisateur connecter(String email, String mdp) {
        Organisateur org = this.organisateurs.get(email);
        if (org != null && org.getMdp().equals(mdp)) {
            return org;
        }
        return null;
    }
}
