package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.modele.bases.Evenement;
import fr.uga.iut2.genevent.modele.bases.Utilisateur;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class GenEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Map<String, Organisateur> organisateurs;  // qualifié par email
    private final Map<String, Concert> concerts;            // qualifié par nom

    public GenEvent() {
        this.organisateurs = new HashMap<>();
        this.concerts = new HashMap<>();
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

    public void nouveauConcert(String nom, String dateDebut, String dateFin, String lieu, String emailOrganisateur) {
        assert !this.concerts.containsKey(nom);
        assert this.organisateurs.containsKey(emailOrganisateur);

        Concert concert = new Concert(dateDebut, dateFin, lieu, nom);
        this.concerts.put(nom, concert);
        this.organisateurs.get(emailOrganisateur).creerConcert(concert);
    }

    public Organisateur connecter(String email, String mdp) {
        Organisateur org = this.organisateurs.get(email);
        if (org != null && org.getMdp().equals(mdp)) {
            return org;
        }
        return null;
    }
}
