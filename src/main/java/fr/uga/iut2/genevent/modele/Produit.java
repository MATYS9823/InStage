package fr.uga.iut2.genevent.modele;

public class Produit {
    private String fournisseur;
    private int prix;
    private String nom;
    private String type;

    public Produit(int prix, String nom, String type) {
        this.prix = prix;
        this.nom = nom;
        this.type = type;
    }

    public Produit(String fournisseur, int prix, String nom, String type) {
        this.fournisseur = fournisseur;
        this.prix = prix;
        this.nom = nom;
        this.type = type;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public int getPrix() {
        return prix;
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
    }
}
