package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.util.Objects;

public class Materiel implements Serializable {
    private int prix;
    private String typeEquipement;
    private String fournisseur;
    private String nom;

    public Materiel(int prix, String typeEquipement, String fournisseur, String nom) {
        this.prix = prix;
        this.typeEquipement = typeEquipement;
        this.fournisseur = fournisseur;
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getTypeEquipement() {
        return typeEquipement;
    }

    public void setTypeEquipement(String typeEquipement) {
        this.typeEquipement = typeEquipement;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        else if (o == null || getClass() != o.getClass()){ return false;}

        Materiel materiel = (Materiel) o;
        return Objects.equals(nom, materiel.nom) &&
                Objects.equals(typeEquipement, materiel.typeEquipement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, typeEquipement);
    }
}
