package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur JavaFX de la vue "Rechercher une salle" (recherche-salles-view.fxml).
 * Gère le filtrage dynamique des salles (lieu, capacité, tarif, dates de disponibilité),
 * l'affichage sous forme de liste, et l'intégration de la carte web interactive.
 */
public class RechercheSallesController {

    // --- Lien avec le reste de l'application ---
    private Controleur controleurPrincipal;
    private JavaFXGUI mainGUI;
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
    private final List<Salle> toutesLesSalles = new ArrayList<>();
    private final ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList();

    /**
     * @return La date de début saisie dans le filtre de recherche.
     */
    public LocalDate getDateDebut() { return date_debut.getValue(); }

    /**
     * @return La date de fin saisie dans le filtre de recherche.
     */
    public LocalDate getDateFin() { return date_fin.getValue(); }

    /**
     * Initialise le contrôleur.
     * Cette méthode est appelée manuellement par JavaFXGUI après le chargement du FXML
     * pour y injecter les dépendances nécessaires (Controleur métier et Navigation).
     *
     * @param controleur Le contrôleur principal (Modèle/MVC).
     * @param gui L'instance de l'interface graphique (pour la navigation).
     */
    public void initialiser(Controleur controleur, JavaFXGUI gui) {
        this.controleurPrincipal = controleur;
        this.mainGUI = gui;

        // Configuration de la liste visuelle
        listViewSalles.setCellFactory(lv -> new SalleListCell(this.mainGUI, this));
        listViewSalles.setItems(sallesFiltrees);
        listViewSalles.setFixedCellSize(120);

        // =========================================================================
        // --- GESTION ET VALIDATION AUTOMATIQUE DES DATES DE DÉBUT ET DE FIN ---
        // =========================================================================

        // 1. Écouteur sur la date de début : ajuste automatiquement la date de fin
        date_debut.valueProperty().addListener((observable, ancienneDate, nouvelleDate) -> {
            if (nouvelleDate != null) {
                LocalDate dateFinActuelle = date_fin.getValue();
                if (dateFinActuelle == null || dateFinActuelle.isBefore(nouvelleDate)) {
                    date_fin.setValue(nouvelleDate);
                }
            }
        });

        // 2. Écouteur sur la date de fin : bloque si l'utilisateur saisit une date illogique
        date_fin.valueProperty().addListener((observable, ancienneDate, nouvelleDate) -> {
            LocalDate dateDebutActuelle = date_debut.getValue();

            if (nouvelleDate != null && dateDebutActuelle != null) {
                if (nouvelleDate.isBefore(dateDebutActuelle)) {
                    if (this.mainGUI != null) {
                        this.mainGUI.informerUtilisateur("La date de fin ne peut pas être antérieure à la date de début !", false);
                    }
                    // Retour forcé à la date valide minimale (date de début)
                    javafx.application.Platform.runLater(() -> {
                        date_fin.setValue(dateDebutActuelle);
                    });
                }
            }
        });

        // 3. Calendrier visuel : Grise et désactive les dates antérieures à la date de début
        date_fin.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate debut = date_debut.getValue();
                setDisable(empty || (debut != null && date.isBefore(debut)));
            }
        });

        // =========================================================================
        // --- CHARGEMENT DES DONNÉES ET DE LA CARTE ---
        // =========================================================================

        if (this.controleurPrincipal != null && this.controleurPrincipal.getToutesLesSalles() != null) {
            toutesLesSalles.addAll(this.controleurPrincipal.getToutesLesSalles());
        }
        sallesFiltrees.setAll(toutesLesSalles);

        WebEngine webEngine = webViewCarte.getEngine();
        URL urlCarte = getClass().getResource("web/map.html");

        if (urlCarte != null) {
            webEngine.load(urlCarte.toExternalForm());
        } else {
            System.err.println("Erreur : Fichier map.html introuvable dans le dossier web/ !");
        }

        // Attend que la carte soit totalement chargée avant d'y placer les marqueurs
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                appliquerFiltres();
            }
        });
    }

    /**
     * Récupère les valeurs saisies dans les champs de recherche, filtre la liste globale
     * des salles, puis met à jour la ListView et les marqueurs de la WebView (Carte).
     */
    @FXML
    private void appliquerFiltres() {
        String lieuCherche = filtreLieu.getText() != null ? filtreLieu.getText().toLowerCase().trim() : "";
        String capStr = filtreCapacite.getText() != null ? filtreCapacite.getText().trim() : "";
        String tarifStr = filtreTarif.getText() != null ? filtreTarif.getText().trim() : "";
        LocalDate debut = date_debut.getValue();
        LocalDate fin = date_fin.getValue();

        List<Salle> resultats = toutesLesSalles.stream().filter(salle -> {
            // Filtre par lieu ou par nom de salle
            if (!lieuCherche.isEmpty() && !salle.getLieu().toLowerCase().contains(lieuCherche) && !salle.getNom().toLowerCase().contains(lieuCherche)) return false;

            // Filtre par capacité minimum
            if (!capStr.isEmpty()) {
                try { if (salle.getNbPlaces() < Integer.parseInt(capStr)) return false; } catch (NumberFormatException ignored){}
            }

            // Filtre par tarif horaire maximum
            if (!tarifStr.isEmpty()) {
                try { if (salle.getTarif() > Integer.parseInt(tarifStr)) return false; } catch (NumberFormatException ignored){}
            }

            // Filtre par disponibilité sur la plage de dates
            if (debut != null && fin != null) {
                if (!salle.estDisponible(debut, fin)) return false;
            }

            return true;
        }).collect(Collectors.toList());

        // Mise à jour de la liste JavaFX
        sallesFiltrees.setAll(resultats);

        // Mise à jour des marqueurs sur la carte via appel Javascript
        if (webViewCarte != null && webViewCarte.getEngine() != null) {
            String json = "[" + resultats.stream().map(Salle::toJson).collect(Collectors.joining(",")) + "]";
            try {
                webViewCarte.getEngine().executeScript("updateMarkers('" + json + "')");
            } catch (Exception ignored) {}
        }

        listViewSalles.refresh();
    }

    // =========================================================================
    // --- ACTIONS DE NAVIGATION ET MENUS ---
    // =========================================================================

    /**
     * Déconnecte l'utilisateur et retourne à l'écran de connexion.
     */
    @FXML
    private void connexionMenuItemActionNU() {
        if (this.mainGUI != null) {
            this.mainGUI.saisirConnexion();
        }
    }

    /**
     * Ferme proprement l'application.
     */
    @FXML
    private void quitterMenuBurgerAction() {
        javafx.application.Platform.exit();
        System.exit(0);
    }

    /**
     * Ouvre ou ferme le menu latéral.
     */
    @FXML
    public void toggleMenuD() {
        boolean isOpen = sideMenuD.isVisible();
        sideMenuD.setVisible(!isOpen);
        scrim.setVisible(!isOpen);
    }

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