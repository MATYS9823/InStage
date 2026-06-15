package fr.uga.iut2.genevent.modele.optimisation;

public class ResultatOptimisation {
    private final int placesStandard;
    private final int placesVIP;
    private final int revenuMax;

    public ResultatOptimisation(int placesStandard, int placesVIP, int revenuMax) {
        this.placesStandard = placesStandard;
        this.placesVIP = placesVIP;
        this.revenuMax = revenuMax;
    }

    public int getPlacesStandard() { return placesStandard; }
    public int getPlacesVIP() { return placesVIP; }
    public int getRevenuMax() { return revenuMax; }
}
