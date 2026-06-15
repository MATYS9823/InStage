package fr.uga.iut2.genevent.modele;

public class Place {
    private String type;
    private int prix;

    public Place(String type, int prix) {
        this.type = type;
        this.prix = prix;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }
}
