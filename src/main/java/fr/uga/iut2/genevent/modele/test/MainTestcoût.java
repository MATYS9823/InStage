package fr.uga.iut2.genevent.modele.test;
import fr.uga.iut2.genevent.modele.Concert;
import fr.uga.iut2.genevent.modele.Salle;
import fr.uga.iut2.genevent.modele.optimisation.SimulateurFinancier;

import java.time.LocalDate;

public class MainTestcoût {
    public static void main(String[] args) {
        // 1. On instancie le service mathématique
        SimulateurFinancier simulateur = new SimulateurFinancier();

        // 2. On crée une salle (ex: Le Zénith, 500 places, 2500€ par jour)
        Salle zenith = new Salle("Le Zénith", "Paris", 12400);
        zenith.setNbPlaces(8000);

        // 3. On crée le concert (du 12 au 14 Mai)
        LocalDate debut = LocalDate.of(2026, 5, 12);
        LocalDate fin = LocalDate.of(2026, 6, 3);
        Concert monConcert = new Concert(debut, fin, "Paris", "Festival Rock 2026","travis");

        // 4. On affecte la salle au concert
        monConcert.setSalle(zenith);

        // 5. On lance l'analyse financière !
        // Disons qu'on veut un seuil de rentabilité de 35 000€, avec des billets à 50€ et 100€
        monConcert.afficherBilanFinancier(simulateur, 50, 100);
    }
}
