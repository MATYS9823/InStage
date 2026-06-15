package fr.uga.iut2.genevent.modele.optimisation;

import fr.uga.iut2.genevent.modele.Salle;

public class SimulateurFinancier {

    /**
     * Calcule la répartition optimale (Sommet A) pour maximiser le revenu.
     */
    public ResultatOptimisation calculerOptimisationRevenu(Salle salle, int seuilRentabilite, int tarifStandard, int tarifVIP) {
        int jauge = salle.getNbPlaces();

        // Quota minimum imposé de 50%
        int placesStandard = jauge / 2;
        int placesVIP = jauge - placesStandard;

        int revenuMax = (placesStandard * tarifStandard) + (placesVIP * tarifVIP);

        if (revenuMax < seuilRentabilite) {
            return null; // La jauge maximale ne permet pas d'atteindre le seuil
        }
        return new ResultatOptimisation(placesStandard, placesVIP, revenuMax);
    }

    /**
     * Calcule le nombre minimum de billets à vendre (Sommet B) pour dépasser le seuil.
     */
    public ResultatRentabiliteMin calculerMinimumBilletsPourRentabilite(Salle salle, int seuilRentabilite, int tarifStandard, int tarifVIP) {
        int jauge = salle.getNbPlaces();

        // On vend d'abord le quota minimum de places standards (n/2)
        int placesStandard = jauge / 2;
        int revenuStandard = placesStandard * tarifStandard;

        int placesVIP = 0;
        // Si le revenu standard ne suffit pas, on complète avec le strict nécessaire de VIP
        if (revenuStandard < seuilRentabilite) {
            int resteACombler = seuilRentabilite - revenuStandard;
            placesVIP = (int) Math.ceil((double) resteACombler / tarifVIP);
        }

        // Vérification physique : est-ce que ça dépasse la capacité de la salle ?
        if ((placesStandard + placesVIP) > jauge) {
            return null; // Impossible d'être rentable dans cette salle
        }

        int revenuTotal = revenuStandard + (placesVIP * tarifVIP);
        return new ResultatRentabiliteMin(placesStandard, placesVIP, revenuTotal);
    }
}
