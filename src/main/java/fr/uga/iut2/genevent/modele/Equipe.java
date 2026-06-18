package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Equipe implements Serializable {
    private String categorie;
    private List<String> personnels=new ArrayList<>();
    private String nom;

    public Equipe(String nom, String categorie) {
        this.nom = nom;
        this.categorie = categorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<String> getPersonnels() {
        return personnels;
    }
    public void setPersonnels(List<String> personnels) {
        this.personnels = personnels;
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

        Equipe equipe = (Equipe) o;
        return Objects.equals(nom, equipe.nom) &&
                Objects.equals(categorie, equipe.categorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, categorie);
    }

    public void ajouterPersonnel(String nomPersonne) {
        if (nomPersonne != null && !nomPersonne.trim().isEmpty() && !this.personnels.contains(nomPersonne.trim())) {
            this.personnels.add(nomPersonne.trim());
        }
    }

    public void supprimerPersonnel(String nomPersonne) {
        this.personnels.remove(nomPersonne);
    }
}


