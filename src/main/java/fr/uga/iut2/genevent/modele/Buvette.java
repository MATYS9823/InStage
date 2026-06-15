package fr.uga.iut2.genevent.modele;

import java.util.ArrayList;

public class Buvette {
    private String localisation;
    private String nom;
    private List<Equipe> equipes=new ArrayList<>();
    private Carte carte;


    public String getLocalisation() {
        return localisation;
    }

    public String getNom() {
        return nom;
    }

    public Buvette(String nom, String localisation) {
        this.nom = nom;
        this.localisation = localisation;
    }
}
