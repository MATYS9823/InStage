package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.Concert;
import fr.uga.iut2.genevent.modele.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RechercheSallesController {

    // --- Lien avec le reste de l'application ---
    private Controleur controleurPrincipal;
    private JavaFXGUI mainGUI; // Permet de naviguer vers d'autres pages si besoin
    // Stage principal, gardé pour changer de scène
    private Stage primaryStage;

    // --- Éléments FXML ---
    @FXML private TextField filtreLieu;
    @FXML private TextField filtreCapacite;
    @FXML private TextField filtreTarif;
    @FXML private ComboBox<String> comboTypeSalle;
    @FXML private DatePicker date_debut;
    @FXML private DatePicker date_fin;
    @FXML private ListView<Salle> listViewSalles;
    @FXML private WebView webViewCarte;
    @FXML private HBox sideMenuD;
    @FXML private Region scrim;


    // --- Données ---
    private List<Salle> toutesLesSalles = new ArrayList<>();
    private ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList();


    public LocalDate getDateDebut() { return date_debut.getValue(); }
    public LocalDate getDateFin() { return date_fin.getValue(); }

    /**
     * Cette méthode remplace notre ancien initialiserRechercheSalles().
     * Elle est appelée par JavaFXGUI au moment de charger la page.
     */
    public void initialiser(Controleur controleur, JavaFXGUI gui) {
        this.controleurPrincipal = controleur;
        this.mainGUI = gui;

        listViewSalles.setCellFactory(lv -> new SalleListCell(this.mainGUI));
        listViewSalles.setItems(sallesFiltrees);
        listViewSalles.setFixedCellSize(120);

        // Récupération des vraies données
        if (this.controleurPrincipal != null && this.controleurPrincipal.getToutesLesSalles() != null) {
            toutesLesSalles.addAll(this.controleurPrincipal.getToutesLesSalles());
        }
        sallesFiltrees.setAll(toutesLesSalles);


        // Chargement de la carte
        WebEngine webEngine = webViewCarte.getEngine();
        URL urlCarte = getClass().getResource("web/map.html");
        if (urlCarte != null) {
            webEngine.load(urlCarte.toExternalForm());
        } else {
            System.err.println("Erreur : map.html introuvable !");
        }

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                appliquerFiltres();
            }
        });
    }

    @FXML
    private void appliquerFiltres() {
        String lieuCherche = filtreLieu.getText() != null ? filtreLieu.getText().toLowerCase().trim() : "";
        String capStr = filtreCapacite.getText() != null ? filtreCapacite.getText().trim() : "";
        String tarifStr = filtreTarif.getText() != null ? filtreTarif.getText().trim() : "";
        // On ne récupère plus les dates ici pour filtrer les salles

        List<Salle> resultats = toutesLesSalles.stream().filter(salle -> {
            if (!lieuCherche.isEmpty() && !salle.getLieu().toLowerCase().contains(lieuCherche) && !salle.getNom().toLowerCase().contains(lieuCherche)) return false;
            if (!capStr.isEmpty()) {
                try { if (salle.getNbPlaces() < Integer.parseInt(capStr)) return false; } catch (NumberFormatException e){}
            }
            if (!tarifStr.isEmpty()) {
                try { if (salle.getTarif() > Integer.parseInt(tarifStr)) return false; } catch (NumberFormatException e){}
            }
            // 👇 On a retiré le filtre sur les dates ici pour ne pas masquer les salles
            return true;
        }).collect(Collectors.toList());

        sallesFiltrees.setAll(resultats);

        if (webViewCarte != null && webViewCarte.getEngine() != null) {
            String json = "[" + resultats.stream().map(Salle::toJson).collect(Collectors.joining(",")) + "]";
            try { webViewCarte.getEngine().executeScript("updateMarkers('" + json + "')"); }
            catch (Exception e) {}
        }

        // 👇 NOUVEAU : On force l'actualisation des cartes
        listViewSalles.refresh();
    }


    @FXML
    private void connexionMenuItemActionNU() {
        if (this.mainGUI != null) {
            // On utilise la méthode publique de JavaFXGUI pour retourner à l'écran de login
            this.mainGUI.saisirConnexion();
        }
    }

    @FXML
    private void quitterMenuBurgerAction() {
        // Ferme proprement l'application JavaFX
        javafx.application.Platform.exit();
        System.exit(0);
    }


    @FXML
    public void toggleMenuD() {
        boolean isOpen = sideMenuD.isVisible();
        sideMenuD.setVisible(!isOpen);
        scrim.setVisible(!isOpen);
    }

    //-----  Navigation  -----------------------------------------------------------

    @FXML
    private void allerVersAccueil() {
        if (this.mainGUI != null) {
            this.mainGUI.allerVersAccueil();
        }
    }

    @FXML
    private void allerVersMesConcerts() {
        if (this.mainGUI != null) {
            this.mainGUI.allerVersMesConcerts();
        }
    }
}