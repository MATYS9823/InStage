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
    @FXML private Button btnSelectionner; // NOUVEAU BOUTON

    private FXMLLoader mLLoader;
    private JavaFXGUI mainGUI; // Lien pour pouvoir changer de page
    private boolean isExpanded = false; // L'état actuel de la carte

    // NOUVEAU CONSTRUCTEUR : on lui passe l'interface graphique
    public SalleListCell(JavaFXGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    protected void updateItem(Salle salle, boolean empty) {
        super.updateItem(salle, empty);

        if (empty || salle == null) {
            setText(null);
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

                // ÉVÉNEMENT 1 : Clic n'importe où sur la carte pour afficher/cacher le bouton
                rootHBox.setOnMouseClicked(event -> {
                    isExpanded = !isExpanded; // On inverse l'état
                    btnSelectionner.setVisible(isExpanded);
                    btnSelectionner.setManaged(isExpanded);
                });

                // ÉVÉNEMENT 2 : Clic sur le bouton vert "Sélectionner"
                btnSelectionner.setOnAction(event -> {
                    event.consume(); // Empêche le clic de refermer la carte par accident
                    if (this.mainGUI != null) {
                        this.mainGUI.allerVersCreerConcert(); // On lance le changement de page
                    }
                });
            }

            lblNom.setText(salle.getNom());
            lblAdresse.setText(salle.getLieu());
            lblCapacite.setText(String.valueOf(salle.getNbPlaces()));
            lblTarif.setText(String.valueOf(salle.getTarif()));

            // On s'assure que le bouton garde le bon état quand on scrolle dans la liste
            btnSelectionner.setVisible(isExpanded);
            btnSelectionner.setManaged(isExpanded);

            setText(null);
            setGraphic(rootHBox);
        }
    }
}