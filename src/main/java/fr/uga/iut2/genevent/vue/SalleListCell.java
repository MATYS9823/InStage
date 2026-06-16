package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.modele.Salle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
    @FXML private Button btnSelectionner;

    private FXMLLoader mLLoader;
    private JavaFXGUI mainGUI;

    public SalleListCell(JavaFXGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void updateSelected(boolean selected) {
        // Empêche la cellule de changer d'état visuel lors du clic
    }

    @Override
    protected void updateItem(Salle salle, boolean empty) {
        super.updateItem(salle, empty);

        if (empty || salle == null) {
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fr/uga/iut2/genevent/vue/ResultCard.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                btnSelectionner.setOnAction(event -> {
                    if (this.mainGUI != null) {
                        this.mainGUI.allerVerscreerConcertsFromSalle(lblAdresse.getText());
                    }
                });
            }

            // Mise à jour des textes
            lblNom.setText(salle.getNom());
            lblAdresse.setText(salle.getLieu());
            lblCapacite.setText(salle.getNbPlaces() + " pers.");
            lblTarif.setText(salle.getTarif() + " €/h");

            setGraphic(rootHBox);
        }
    }
}