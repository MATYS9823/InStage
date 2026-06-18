package fr.uga.iut2.genevent.modele.optimisation;

import fr.uga.iut2.genevent.modele.Salle;

public class SimulateurFinancier {

    public ResultatOptimisation calculerOptimisationRevenu(Salle salle, int seuilRentabilite, int tarifStandard, int tarifVIP) {
        int jauge = salle.getNbPlaces();

        // Quota minimum imposé de 50% de places standards pour une configuration pleine
        int placesStandard = jauge / 2;
        int placesVIP = jauge - placesStandard;

        int revenuMax = (placesStandard * tarifStandard) + (placesVIP * tarifVIP);

        if (revenuMax < seuilRentabilite) {
            return null; // La jauge maximale ne permet pas d'atteindre le seuil
        }
        return new ResultatOptimisation(placesStandard, placesVIP, revenuMax);
    }

    public ResultatRentabiliteMin calculerMinimumBilletsPourRentabilite(Salle salle, int seuilRentabilite, int tarifStandard, int tarifVIP) {
        int jauge = salle.getNbPlaces();

        // 1. On calcule d'abord combien de places Standards sont nécessaires au minimum
        int placesStandard = (int) Math.ceil((double) seuilRentabilite / tarifStandard);
        int placesVIP = 0;

        // 2. Si on a besoin de plus de places que la capacité de la salle, on doit vendre du VIP
        if (placesStandard > jauge) {
            placesStandard = jauge;
            int revenuStandard = placesStandard * tarifStandard;
            int resteACombler = seuilRentabilite - revenuStandard;

            // On remplace des places Standards par des VIP (qui rapportent la différence de prix)
            int differencePrix = tarifVIP - tarifStandard;
            if (differencePrix > 0) {
                placesVIP = (int) Math.ceil((double) resteACombler / differencePrix);
                placesStandard = jauge - placesVIP;
            }
        }

        // 3. Vérification finale : est-ce que ça rentre toujours dans la salle ?
        if (placesVIP > jauge || placesStandard < 0) {
            return null; // Impossible d'être rentable même avec 100% de VIP
        }

        int revenuTotal = (placesStandard * tarifStandard) + (placesVIP * tarifVIP);
        return new ResultatRentabiliteMin(placesStandard, placesVIP, revenuTotal);
    }
}