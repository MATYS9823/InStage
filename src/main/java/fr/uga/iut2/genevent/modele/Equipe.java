package fr.uga.iut2.genevent.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Equipe {
    private String categorie;
    private List<String> personnels=new ArrayList<>();
    private String nom;

    public Equipe(String nom, String categorie) {
        this.nom = nom;
        this.categorie = categorie;
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
}
