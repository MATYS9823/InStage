package fr.uga.iut2.genevent.modele.optimisation;

public class ResultatRentabiliteMin {
    private final int placesStandard;
    private final int placesVIP;
    private final int totalBilletsAchetes;
    private final int revenuGenere;

    public ResultatRentabiliteMin(int placesStandard, int placesVIP, int revenuGenere) {
        this.placesStandard = placesStandard;
        this.placesVIP = placesVIP;
        this.totalBilletsAchetes = placesStandard + placesVIP;
        this.revenuGenere = revenuGenere;
    }

    public int getPlacesStandard() { return placesStandard; }
    public int getPlacesVIP() { return placesVIP; }
    public int getTotalBilletsAchetes() { return totalBilletsAchetes; }
    public int getRevenuGenere() { return revenuGenere; }
}
