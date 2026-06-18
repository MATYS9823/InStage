package fr.uga.iut2.genevent.modele;

import java.util.Objects;
import java.io.Serializable;
import java.util.Objects;

public class Produit implements Serializable {
    private static final long serialVersionUID = 1L; // Bonne pratique pour la sérialisation

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


    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        else if (o == null || getClass() != o.getClass()){ return false;}

        Produit produit = (Produit) o;
        return Objects.equals(nom, produit.nom) &&
                Objects.equals(type, produit.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, type);
    }
}