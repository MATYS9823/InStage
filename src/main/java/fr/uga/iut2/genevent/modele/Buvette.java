package fr.uga.iut2.genevent.modele;

import java.util.ArrayList;
import java.util.List;

public class Buvette {
    private String localisation;
    private String nom;
    private List<Equipe> equipes=new ArrayList<>();
    private Carte carte;




    public Buvette(String nom, String localisation) {
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
}
