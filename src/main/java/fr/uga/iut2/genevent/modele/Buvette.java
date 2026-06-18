package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Buvette implements Serializable {
    private String localisation;
    private String nom;
    private List<Equipe> equipes=new ArrayList<>();
    private Carte carte;
    private List<Carte> cartesAppliquees = new ArrayList<>();


    public Buvette( String localisation, String nom) {
        this.nom = nom;
        this.localisation = localisation;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getNom() {
        return nom;
    }

    public Carte getCarte() {
        return carte;
    }

    public void ajouterEquipe(Equipe equipe){
        if(equipes.contains(equipe)){
            System.out.println("Ce produit est deja dans votre liste");
        }
        else {equipes.add(equipe);}
    }

    public void supprimerEquipe(Equipe equipe){
        if (equipes.contains(equipe)){
            equipes.remove(equipe);
        }
        else {
            System.out.println("Ce produit n'est pas dans votre liste");
        }
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public List<Carte> getCartesAppliquees() {
        return cartesAppliquees;
    }

    public boolean isCarteAppliquee(Carte c) {
        return cartesAppliquees.contains(c);
    }

    public void toggleCarteAppliquee(Carte c) {
        if (cartesAppliquees.contains(c)) {
            cartesAppliquees.remove(c);
        } else {
            cartesAppliquees.add(c);
        }
    }
}
