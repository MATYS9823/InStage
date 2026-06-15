package fr.uga.iut2.genevent.modele;
import java.io.Serializable;
import java.time.LocalDate;
public class Occupation implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public Occupation(LocalDate dateDebut, LocalDate dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Méthode clé : vérifie si les dates recherchées chevauchent cette occupation
    public boolean chevauche(LocalDate debutRecherche, LocalDate finRecherche) {
        // Il y a chevauchement si la date de début recherchée est AVANT (ou égale à) la fin de l'occupation
        // ET que la date de fin recherchée est APRES (ou égale à) le début de l'occupation.
        return !debutRecherche.isAfter(this.dateFin) && !finRecherche.isBefore(this.dateDebut);
    }

    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
}
