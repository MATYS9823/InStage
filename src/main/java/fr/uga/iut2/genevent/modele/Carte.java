package fr.uga.iut2.genevent.modele;

import java.util.ArrayList;
import java.util.List;

public class Carte {
    private String theme;
    private String nom;
    private List<Produit> produits=new ArrayList<>();


    public Carte(String theme, String nom) {
        this.theme = theme;
        this.nom = nom;
    }

    public String getTheme() {
        return theme;
    }

    public String getNom() {
        return nom;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void ajouterProduit(Produit produit){
    }

    public void supprimererProduit(Produit produit){

    }


}
