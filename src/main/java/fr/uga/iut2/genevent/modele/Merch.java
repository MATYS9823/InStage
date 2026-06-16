package fr.uga.iut2.genevent.modele;

import java.util.ArrayList;
import java.util.List;

public class Merch {
    private String localisation;
    private String nom;
    private List<Equipe> equipes=new ArrayList<>();
    private List<Produit> produits=new ArrayList<>();


    public Merch(String localisation, String nom) {
        this.localisation = localisation;
        this.nom = nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getNom() {
        return nom;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void ajouterEquipe(Equipe equipe){
        if(equipes.contains(equipe)){
            System.out.println("Cette equipe est deja dans votre liste");
        }
        else {equipes.add(equipe);}
    }

    public void supprimerEquipe(Equipe equipe){
        if (equipes.contains(equipe)){
            equipes.remove(equipe);
        }
        else {
            System.out.println("Cette equipe n'est pas dans votre liste");
        }
    }

    public void ajouterProduit(Produit produit){
        if(produits.contains(produit)){
            System.out.println("Ce produit est deja dans votre liste");
        }
        else {produits.add(produit);}
    }

    public void supprimerProduit(Produit produit){
        if (produits.contains(produit)){
            produits.remove(produit);
        }
        else {
            System.out.println("Ce produit n'est pas dans votre liste");
        }
    }

}
