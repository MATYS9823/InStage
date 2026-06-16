package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.modele.Salle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import java.io.IOException;

public class SalleListCell extends ListCell<Salle> {

    @FXML private HBox rootHBox;
    @FXML private Label lblNom;
    @FXML private Label lblAdresse;
    @FXML private Label lblCapacite;
    @FXML private Label lblTarif;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Salle salle, boolean empty) {
        super.updateItem(salle, empty);

        if (empty || salle == null) {
            // Si la cellule est vide (ex: à la fin de la liste), on n'affiche rien
            setText(null);
            setGraphic(null);
        } else {
            // 1. On charge le design de la carte une seule fois
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("ResultCard.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 2. On injecte les données de la salle dans les labels du FXML
            lblNom.setText(salle.getNom());
            lblAdresse.setText(salle.getLieu());
            lblCapacite.setText(String.valueOf(salle.getNbPlaces()));
            lblTarif.setText(String.valueOf(salle.getTarif()));

            // 3. On affiche la HBox graphique à la place du texte par défaut
            setText(null);
            setGraphic(rootHBox);
        }
    }
}