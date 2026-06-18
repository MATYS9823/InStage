package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Merch implements Serializable {
    private String localisation;
    private String nom;
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

    public List<Produit> getProduits() {
        return produits;
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
