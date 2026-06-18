package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.controleur.Controleur;

import fr.uga.iut2.genevent.modele.*;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.apache.commons.validator.routines.EmailValidator;
import java.util.List;

//import pour le format agenda
import javafx.stage.FileChooser;
import java.io.File;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;


public class JavaFXGUI extends IHM {

    private final Controleur controleur;
    private final CountDownLatch eolBarrier;

    // Stage principal, gardé pour changer de scène
    private Stage primaryStage;
    private final List<InfosNouveauConcert> concertsSauvegardes = new ArrayList<InfosNouveauConcert>();

    // champs vue connexion
    @FXML private TextField loginEmailTextField;
    @FXML private PasswordField loginMdpField;
    @FXML private Button loginOkButton;
    @FXML private Button loginCancelButton;

    // champs vue nouvel organisateur
    @FXML private TextField newOrgForenameTextField;
    @FXML private TextField newOrgSurnameTextField;
    @FXML private TextField newOrgEmailTextField;
    @FXML private PasswordField newOrgMdpTextField;
    @FXML private Button newOrgOkButton;
    @FXML private Button newOrgCancelButton;

    // champs vue accueil
    @FXML private Label welcomeLabel;
    @FXML private HBox sideMenu;
    @FXML private Region scrim;

    @FXML private TextField forgotEmailTextField;
    @FXML private Hyperlink inscritlink;
    @FXML private Button concertButton;
    @FXML private  Label gererconcertlabel;

    @FXML private TextField tfNomconcert;
    @FXML private TextField tflieuconcert;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private TextField tfArtiste;

    @FXML private Button creerconcertButton;
    @FXML private TilePane concertsTilePane;
    @FXML private Label artistelabel;

    @FXML private TextField tfNomMateriel;
    @FXML private TextField tfPrixMateriel;
    @FXML private TextField tfTypeEquipement;
    @FXML private TextField tfFournisseur;

    @FXML private VBox materielTilePane;

    @FXML private TextField tfLocalisationMerch;
    @FXML private TextField tfNomMerch;
    @FXML private VBox merchTilePane;


    @FXML private VBox sideMenuD;
    @FXML private Label dashboardConcertLabel;
    @FXML private Label dashboardArtisteLabel;
    @FXML private Label dashboardBuvetteNomLabel;
    @FXML private Label dashboardBuvetteThemeLabel;
    @FXML private Label dashboardMerchNomLabel;
    @FXML private Label dashboardStaffCountLabel;
    @FXML private Label dashboardStaffCategoriesLabel;
    @FXML private Label dashboardMaterielCategoriesLabel;

    @FXML private TextField tfNomEquipe;
    @FXML private TextField tfCategorieEquipe;
    @FXML private VBox staffTilePane;

    @FXML private VBox sideMenuCarte;
    @FXML private Region scrimCarte;
    @FXML private VBox carteTilePane;

    @FXML private TextField tfNomCarte;
    @FXML private TextField tfThemeCarte;

    @FXML private TextField tfNomBuvette;
    @FXML private TextField tfLocalisationBuvette;
    @FXML private VBox buvetteTilePane;
    @FXML private VBox sideMenuBuvette;
    @FXML private Region scrimBuvette;

    //pour collecter les dernière infos du concert recent
    @FXML private VBox paneDernierConcert;
    @FXML private Label lblDernierNom;
    @FXML private Label lblDernierArtiste;
    @FXML private Label lblDernierDate;
    @FXML private Label lblDernierLieu;

    public JavaFXGUI(Controleur controleur) {
        this.controleur = controleur;
        this.eolBarrier = new CountDownLatch(1);
    }

//-----  Démarrage  ------------------------------------------------------------

    private void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        loader.setController(this);
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("IN STAGE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//-----  Navigation  -----------------------------------------------------------

    /**
     * Charge home-view.fxml dans le stage principal après une connexion réussie.
     * Met à jour le label de bienvenue avec le prénom/nom de l'organisateur.
     */
    @FXML
    public void allerVersAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");

            if (welcomeLabel != null) {
                welcomeLabel.setText("Bienvenue " + controleur.getOrganisateurConnecte().getPrenom()
                        + " " + controleur.getOrganisateurConnecte().getNom());
            }

            // --- MISE À JOUR DE L'AFFICHAGE DIRECT DU CONCERT ---
            if (paneDernierConcert != null) {
                Organisateur org = controleur.getOrganisateurConnecte();

                if (org != null && org.getDernierConcertConsulte() != null) {
                    Concert dernier = org.getDernierConcertConsulte();

                    // 1. On injecte directement les informations du modèle dans les Labels
                    lblDernierNom.setText(dernier.getNom());
                    lblDernierArtiste.setText("Artiste : " + dernier.getArtiste());

                    String dateStr = dernier.getDate_debut() != null
                            ? dernier.getDate_debut().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.FRANCE))
                            : "Date inconnue";
                    lblDernierDate.setText("📅 " + dateStr);
                    lblDernierLieu.setText("📍 " + dernier.getLieu());

                    // 2. On s'assure que la carte est active et cliquable
                    paneDernierConcert.setDisable(false);
                    paneDernierConcert.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");
                } else {
                    // Si aucun concert n'a été visité, on bascule en mode "vide" (grisé et non cliquable)
                    lblDernierNom.setText("Aucun concert consulté récemment");
                    lblDernierArtiste.setText("Ouvrez un concert depuis l'onglet 'Mes concerts' pour le voir apparaître ici.");
                    lblDernierDate.setText("📅 -- ");
                    lblDernierLieu.setText("📍 -- ");

                    paneDernierConcert.setDisable(true); // Désactive le clic
                    paneDernierConcert.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 12; -fx-border-color: #DCDCDC; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-opacity: 0.6;");
                }
            }

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void allerVersMotdePasseOublie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("forgot-password-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    /** Revient à la vue de login (après déconnexion). */
    private void allerVersLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    public void allerVersMesConcerts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage-concerts-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");

            if (concertsTilePane != null) {
                concertsTilePane.getChildren().clear();
                for (Concert concert : controleur.getOrganisateurConnecte().getConcerts()) {
                    concertsTilePane.getChildren().add(creerCarteConcert(concert));
                }
            }
            if (welcomeLabel != null) {
                welcomeLabel.setText("Bienvenue " + controleur.getOrganisateurConnecte().getPrenom()
                        + " " + controleur.getOrganisateurConnecte().getNom());
            }

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }


    private void allerVerscreerConcerts(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-concert-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            // ---> APPEL NOUVELLE MÉTHODE pour initialiser les dates <---
            initialiserDatesConcert();
        }catch (IOException exc){
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void allerVersBuvette(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("buvette-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
            if (buvetteTilePane != null && controleur.getConcertSelectionne() != null) {
                buvetteTilePane.getChildren().clear();
                for (Buvette b : controleur.getConcertSelectionne().getBuvettes()) {
                    buvetteTilePane.getChildren().add(creerCarteBuvette(b));
                }
            }
            if (welcomeLabel != null) {
                welcomeLabel.setText(controleur.getConcertSelectionne().getNom());
            }

            if (artistelabel!= null) {
                artistelabel.setText(controleur.getConcertSelectionne().getArtiste());
            }


        }catch (IOException exc){
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void allerVerscreerMateriels(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-materiel-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
        }catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public void allerVerscreerConcertsFromSalle(String lieu, LocalDate debut, LocalDate fin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-concert-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");

            // ---> APPEL NOUVELLE MÉTHODE AVANT DE REMPLIR LES CHAMPS <---
            initialiserDatesConcert();

            // On remplit le lieu
            if (tflieuconcert != null) {
                tflieuconcert.setText(lieu);
            }
            // NOUVEAU : On remplit les dates
            if (dpDateDebut != null && debut != null) {
                dpDateDebut.setValue(debut);
            }
            if (dpDateFin != null && fin != null) {
                dpDateFin.setValue(fin);
            }
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }


    @FXML
    private void allerVersMerch(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("merch-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
            if (controleur.getConcertSelectionne() != null) {
                welcomeLabel.setText(controleur.getConcertSelectionne().getNom());
                artistelabel.setText(controleur.getConcertSelectionne().getArtiste());
            }
            if (merchTilePane != null && controleur.getConcertSelectionne() != null) {
                merchTilePane.getChildren().clear();
                for (Merch m : controleur.getConcertSelectionne().getMerchs()) {
                    merchTilePane.getChildren().add(creerCarteMerch(m));
                }
            }
        }catch (IOException exc){
            throw new RuntimeException(exc);
        }
    }

    private void allerVersdashboard(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");

            Concert concert = controleur.getConcertSelectionne();
            controleur.selectionnerConcertRecent(concert);

            if (concert != null) {
                // Mise à jour de l'en-tête
                if (dashboardConcertLabel != null) dashboardConcertLabel.setText(concert.getNom());
                if (dashboardArtisteLabel != null) dashboardArtisteLabel.setText(concert.getArtiste());

                // Mise à jour de la section Buvette (utilisation du premier merch comme buvette par défaut si besoin, ou adaptation future)
                // Pour l'instant, on laisse les valeurs par défaut si non géré explicitement dans le modèle Concert
                
                // Merch
                if (dashboardMerchNomLabel != null) {
                    if (!concert.getMerchs().isEmpty()) {
                        dashboardMerchNomLabel.setText(concert.getMerchs().get(0).getNom());
                    } else {
                        dashboardMerchNomLabel.setText("Aucun merchandising");
                    }
                }

                // Personnel
                if (dashboardStaffCountLabel != null) {
                    dashboardStaffCountLabel.setText("Nombre d'équipe: " + concert.getEquipes().size());
                }
                if (dashboardStaffCategoriesLabel != null) {
                    Set<String> categories = new HashSet<>();
                    for (Equipe e : concert.getEquipes()) {
                        categories.add(e.getNom()); // ou e.getLocalisation() selon l'usage
                    }
                    if (categories.isEmpty()) {
                        dashboardStaffCategoriesLabel.setText("Aucune équipe");
                    } else {
                        dashboardStaffCategoriesLabel.setText(String.join(", ", categories));
                    }
                }

                // Matériel
                if (dashboardMaterielCategoriesLabel != null) {
                    Set<String> types = new HashSet<>();
                    for (Materiel m : concert.getMateriels()) {
                        types.add(m.getTypeEquipement());
                    }
                    if (types.isEmpty()) {
                        dashboardMaterielCategoriesLabel.setText("Aucun matériel");
                    } else {
                        dashboardMaterielCategoriesLabel.setText(String.join(", ", types));
                    }
                }
            }

        }catch (IOException exc){
            throw new RuntimeException(exc);
        }
    }

    private void allerVerscreerMerch(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-merch-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void allerVerscreerStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-staff-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void allerVersStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("staff-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
            welcomeLabel.setText(controleur.getConcertSelectionne().getNom());

            if (staffTilePane != null && controleur.getConcertSelectionne() != null) {
                staffTilePane.getChildren().clear();
                for (Equipe e : controleur.getConcertSelectionne().getEquipes()) {
                    staffTilePane.getChildren().add(creerCarteEquipe(e));
                }
            }
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void allerVerscreerBuvette() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-buvette-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void allerVersCarte(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage-carte-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");

            if (carteTilePane != null && controleur.getConcertSelectionne() != null) {
                carteTilePane.getChildren().clear();
                for (Carte c : controleur.getConcertSelectionne().getCartes()) {
                    carteTilePane.getChildren().add(creerCarteCarte(c));
                }
            }
        }catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void  allerVerscreerCarte() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-carte-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");

        }catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }


    //fonction pour telecharger nos évènement en format calendar(ics)
    private void telechargerFichierIcs(Concert c) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le concert dans l'agenda");

        // On propose un nom de fichier par défaut (sans espaces)
        String nomFichierDefaut = c.getNom().replaceAll("\\s+", "_") + ".ics";
        fileChooser.setInitialFileName(nomFichierDefaut);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier iCalendar (*.ics)", "*.ics"));

        // Ouvre la fenêtre de sauvegarde
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Formatage de la date (iCal demande le format YYYYMMDD pour les dates complètes)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                String dateDebut = c.getDate_debut().format(formatter);

                // Pour les événements sur une journée entière (sans heure), iCal requiert que la date de fin soit exclusive (+1 jour)
                String dateFin = c.getDate_fin() != null
                        ? c.getDate_fin().plusDays(1).format(formatter)
                        : c.getDate_debut().plusDays(1).format(formatter);

                // Écriture du fichier au standard iCalendar
                writer.println("BEGIN:VCALENDAR");
                writer.println("VERSION:2.0");
                writer.println("PRODID:-//GenEvent//FR");
                writer.println("BEGIN:VEVENT");
                writer.println("SUMMARY:Concert " + c.getNom() + " - " + c.getArtiste());
                writer.println("LOCATION:" + c.getLieu());
                writer.println("DTSTART;VALUE=DATE:" + dateDebut);
                writer.println("DTEND;VALUE=DATE:" + dateFin);
                writer.println("DESCRIPTION:Concert de " + c.getArtiste() + " organisé via GenEvent.");
                writer.println("END:VEVENT");
                writer.println("END:VCALENDAR");

                informerUtilisateur("Le fichier calendrier a été téléchargé avec succès !", true);
            } catch (Exception ex) {
                informerUtilisateur("Erreur lors de la sauvegarde : " + ex.getMessage(), false);
            }
        }
    }





//-----  Éléments du dialogue  -------------------------------------------------

    private void exitAction() {
        this.eolBarrier.countDown();
    }

    // ---- Navbar home ----

    @FXML
    private void connexionMenuItemAction() {
        // Depuis la home, ce bouton sert à se déconnecter
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de fermeture");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

        // 2. Affichage de la fenêtre et attente du choix de l'utilisateur
        Optional<ButtonType> resultat = alert.showAndWait();

        // 3. Vérification de la réponse
        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            allerVersLogin();
        };
        // Si l'utilisateur clique sur "Annuler" ou ferme la fenêtre,
        // le code s'arrête ici et l'application reste ouverte naturellement.
    }

    @FXML
    private void connexionMenuItemActionNU() {
        // Depuis la home, ce bouton sert à se déconnecter
        allerVersLogin();
        inscritlink.getScene().getWindow().hide();
    }

    @FXML
    private void connexionMenuItemActionFP() {
        // Depuis mdp oublié, ce bouton sert à aller sur la page de déconnecter
        allerVersLogin();
    }



    @FXML
    private void quitterMenuBurgerAction() {
        // 1. Création de la fenêtre de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de fermeture");
        alert.setHeaderText(null); // Optionnel : null pour un affichage plus épuré sans en-tête
        alert.setContentText("Êtes-vous sûr de vouloir quitter l'application ?");

        // 2. Affichage de la fenêtre et attente du choix de l'utilisateur
        Optional<ButtonType> resultat = alert.showAndWait();

        // 3. Vérification de la réponse
        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            // L'utilisateur a cliqué sur "OK", on lance la procédure de fermeture
            Platform.runLater(() -> {
                // On récupère TOUTES les fenêtres ouvertes (principale + popups) et on les ferme
                new ArrayList<>(Window.getWindows()).forEach(window -> {
                    if (window instanceof Stage) {
                        ((Stage) window).close();
                    }
                });

                // On libère la barrière pour réveiller ton Main
                // Le programme va exécuter ton code de sauvegarde automatique puis s'éteindre
                this.exitAction();
            });
        }
        // Si l'utilisateur clique sur "Annuler" ou ferme la fenêtre,
        // le code s'arrête ici et l'application reste ouverte naturellement.
    }


    @FXML
    private void newOrganisateurMenuItemAction() {
        this.controleur.saisirOrganisateur();

    }

    @FXML
    private void donnermotdepasse(){
        String email = forgotEmailTextField.getText().strip().toLowerCase();
        Organisateur org = this.controleur.getOrganisateur(email);

        if (org !=null){
            informerUtilisateur("Votre mot de passe est :"+ org.getMdp(),true);
        }
        else {
            informerUtilisateur("Aucun compte trouvé pour cet email.",false);
        }
    }

    @FXML
    private void motdepasseOublierAction() {
        allerVersMotdePasseOublie();
    }

    @FXML
    private void newConcertMenuItemAction() {
        allerVerscreerConcerts();
    }

    @FXML
    private void exitMenuItemAction() {
        Platform.exit();
        this.exitAction();
    }

    // ---- Vue connexion (login-view.fxml) ----

    @FXML
    private void loginAction() {
        InfosConnexion data = new InfosConnexion(
                this.loginEmailTextField.getText().strip().toLowerCase(),
                this.loginMdpField.getText()
        );
        this.controleur.seConnecter(data);
        // Si la connexion a réussi, on navigue vers l'accueil
        if (this.controleur.estConnecte()) {
            allerVersAccueil();
        }
        // Sinon, informerUtilisateur() a déjà affiché une alerte d'erreur
    }

    @FXML
    private void cancelLoginAction() {
        this.loginCancelButton.getScene().getWindow().hide();
    }

    @FXML
    private void validateLoginFields() {
        boolean isValid = validateEmailTextField(this.loginEmailTextField)
                && this.loginMdpField.getText().length() > 0;
        this.loginOkButton.setDisable(!isValid);
    }



    // ---- Vue nouvel organisateur ----

    @FXML
    private void createNewOrganisateurAction() {
        InfosOrganisateur data = new InfosOrganisateur(
                this.newOrgEmailTextField.getText().strip().toLowerCase(),  // email
                this.newOrgSurnameTextField.getText().strip(),              // nom
                this.newOrgForenameTextField.getText().strip(),             // prenom
                this.newOrgMdpTextField.getText()                          // mdp
        );
        this.controleur.creerOrganisateur(data);
        this.newOrgOkButton.getScene().getWindow().hide();
    }



    @FXML
    private void createNewConcertAction() {
        // Validation basique
        if (tfNomconcert.getText().isBlank() || dpDateDebut.getValue() == null) {
            informerUtilisateur("Merci de remplir au moins le nom et la date de début.", false);
            return;
        }

        InfosNouveauConcert data = new InfosNouveauConcert(
                tfNomconcert.getText().strip(),
                dpDateDebut.getValue(),
                dpDateFin.getValue() != null ? dpDateFin.getValue() : dpDateDebut.getValue(),
                tflieuconcert.getText().strip(),
                tfArtiste.getText().strip()
        );

        concertsSauvegardes.add(data);        // ← on mémorise
        this.controleur.creerConcert(data);   // ← logique métier existante
        allerVersMesConcerts();               // ← on navigue directement
    }

    @FXML
    private void createNewMaterielAction() {
        if (tfNomMateriel.getText().isBlank()) {
            informerUtilisateur("Merci de remplir au moins le nom.", false);
            return;
        }

        int prix;
        try {
            prix = Integer.parseInt(tfPrixMateriel.getText().strip());
        } catch (NumberFormatException e) {
            informerUtilisateur("Le prix doit être un nombre entier.", false);
            return;
        }

        InfosMateriel data = new InfosMateriel(
                prix,
                tfTypeEquipement.getText().strip(),
                tfFournisseur.getText().strip(),
                tfNomMateriel.getText().strip()
        );

        controleur.creerMateriel(data);
        allerVersMateriel();
    }

    @FXML
    private void createNewMerchAction() {
        if (tfNomMerch.getText().isBlank()) {
            informerUtilisateur("Merci de remplir au moins le nom.", false);
            return;
        }

        InfosMerch data = new InfosMerch(
                tfLocalisationMerch.getText().strip(),
                tfNomMerch.getText().strip()
        );

        controleur.creerMerch(data);
        allerVersMerch();
    }

    @FXML
    private void createNewStaffAction() {
        if (tfNomEquipe.getText().isBlank()) {
            informerUtilisateur("Merci de renseigner le nom de l'équipe.", false);
            return;
        }

        InfosMerch data = new InfosMerch(
                tfCategorieEquipe.getText().strip(),  // localisation = catégorie
                tfNomEquipe.getText().strip()          // nom
        );

        controleur.creerEquipe(data);
        allerVersStaff();
    }

    @FXML
    private void creerCarteAction() {
        allerVerscreerCarte();
    }

    @FXML
    private void createNewBuvetteAction() {
        if (tfNomBuvette.getText().isBlank()) {
            informerUtilisateur("Merci de renseigner le nom de la buvette.", false);
            return;
        }
        InfosBuvette data = new InfosBuvette(
                tfLocalisationBuvette.getText().strip(),
                tfNomBuvette.getText().strip()
        );
        controleur.creerBuvette(data);
        allerVersBuvette();
    }

    @FXML
    private void createNewCarteAction() {
        if (tfNomCarte.getText().isBlank()) {
            informerUtilisateur("Merci de renseigner le nom de la carte.", false);
            return;
        }
        InfosCarte data = new InfosCarte(
                tfThemeCarte.getText().strip(),
                tfNomCarte.getText().strip()
        );
        controleur.creercarte(data);
        allerVersCarte();
    }

    // Gestion du volet latéral spécifique à la page Buvette
    @FXML
    public void toggleMenuBuvette() {
        boolean isOpen = sideMenuBuvette.isVisible();
        sideMenuBuvette.setVisible(!isOpen);
        scrimBuvette.setVisible(!isOpen);
    }

    @FXML
    public void toggleMenuCarte() {
        boolean isOpen = sideMenuCarte.isVisible();
        sideMenuCarte.setVisible(!isOpen);
        scrimCarte.setVisible(!isOpen);
    }


    private VBox creerCarteConcert(Concert c) {
        // --- TEXTES ---
        Label nom = new Label(c.getNom());
        nom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label artiste = new Label("Artiste : " + c.getArtiste());
        artiste.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");

        Label lieu = new Label("📍 " + c.getLieu());
        lieu.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        String dateStr = c.getDate_debut() != null
                ? c.getDate_debut().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRANCE))
                : "Date inconnue";
        Label date = new Label("📅 " + dateStr);
        date.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        // --- CROIX DE SUPPRESSION (EN HAUT À DROITE) ---
        Button btnSupprimer = new Button("✕");
        btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnSupprimer.setOnMouseEntered(e -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;")); // Devient rouge
        btnSupprimer.setOnMouseExited(e -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setContentText("Supprimer le concert \"" + c.getNom() + "\" ?");
            alert.showAndWait().ifPresent(reponse -> {
                if (reponse == ButtonType.OK) {
                    controleur.supprimerConcert(c);
                    allerVersMesConcerts();
                }
            });
        });

        // --- BOUTON CALENDRIER (iCal) ---
        Button btnCalendrier = new Button("📅 Exporter");
        btnCalendrier.setStyle("-fx-background-color: #2C2C2E; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        btnCalendrier.setOnMouseEntered(e -> btnCalendrier.setStyle("-fx-background-color: #3A3A3C; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;"));
        btnCalendrier.setOnMouseExited(e -> btnCalendrier.setStyle("-fx-background-color: #2C2C2E; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;"));
        btnCalendrier.setOnAction(e -> telechargerFichierIcs(c)); // Appel de la méthode d'export

        // --- BOUTON MODIFIER ---
        Button btnModifier = new Button("Modifier ✍");
        btnModifier.setStyle("-fx-background-color: #2C2C2E; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        btnModifier.setOnMouseEntered(e -> btnModifier.setStyle("-fx-background-color: #3A3A3C; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;"));
        btnModifier.setOnMouseExited(e -> btnModifier.setStyle("-fx-background-color: #2C2C2E; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;"));
        btnModifier.setOnAction(e -> {
            controleur.selectionnerConcert(c);
            allerVersdashboard();
        });

        // --- ASSEMBLAGE DE LA CARTE ---
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimer);
        header.setAlignment(Pos.TOP_CENTER);

        VBox corpsContenu = new VBox(5, artiste, lieu);

        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, javafx.scene.layout.Priority.ALWAYS);

        // On intègre le bouton calendrier juste avant le bouton modifier dans le footer
        HBox footer = new HBox(10, date, footerSpacer, btnCalendrier, btnModifier);
        footer.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        VBox carte = new VBox(15, header, corpsContenu, vSpacer, footer);

        // --- STYLE DE LA CARTE (Fond blanc, bordure dynamique) ---
        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #000000; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 18, 0, 0, 6);";

        carte.setStyle(defaultStyle);
        carte.setPrefSize(350, 200);

        carte.setOnMouseEntered(e -> carte.setStyle(hoverStyle));
        carte.setOnMouseExited(e -> carte.setStyle(defaultStyle));

        return carte;
    }

    private VBox creerCarteMateriel(Materiel m) {
        Label nom = new Label(m.getNom());
        nom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label type = new Label(m.getTypeEquipement());
        type.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");

        Label fournisseur = new Label("🏭 " + m.getFournisseur());
        fournisseur.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        Label prix = new Label("💶 " + m.getPrix() + " €");
        prix.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        // CROIX DE SUPPRESSION
        Button btnSupprimer = new Button("✕");
        btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnSupprimer.setOnMouseEntered(e -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnMouseExited(e -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setContentText("Supprimer le materiel \"" + m.getNom() + "\" ?");
            alert.showAndWait().ifPresent(reponse -> {
                if (reponse == ButtonType.OK) {
                    controleur.supprimerMateriel(m);
                    allerVersMateriel();
                }
            });

        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimer);
        header.setAlignment(Pos.TOP_CENTER);

        VBox corps = new VBox(5, type, fournisseur);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        VBox carte = new VBox(15, header, corps, vSpacer, prix);

        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #000000; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 18, 0, 0, 6);";

        carte.setStyle(defaultStyle);
        carte.setPrefSize(350, 200);
        carte.setOnMouseEntered(e -> carte.setStyle(hoverStyle));
        carte.setOnMouseExited(e -> carte.setStyle(defaultStyle));

        return carte;
    }

    private VBox creerCarteMerch(Merch m) {
        // En-tête de la carte
        Label nom = new Label(m.getNom());
        nom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label detail = new Label("📍 " + m.getLocalisation());
        detail.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");

        // --- AFFICHAGE DES PRODUITS EXISTANTS ---
        FlowPane produitsBox = new FlowPane();
        produitsBox.setHgap(8);
        produitsBox.setVgap(8);

        if (m.getProduits().isEmpty()) {
            Label aucun = new Label("Aucun produit en vente");
            aucun.setStyle("-fx-font-size: 13px; -fx-text-fill: #AAAAAA; -fx-font-style: italic;");
            produitsBox.getChildren().add(aucun);
        } else {
            for (Produit p : m.getProduits()) {
                HBox badge = new HBox(5);
                badge.setAlignment(Pos.CENTER);
                badge.setStyle("-fx-background-color: #F2F2F7; -fx-background-radius: 15; -fx-padding: 4 10 4 12;");

                // Affichage du nom, du type et du prix
                Label lblProd = new Label(p.getNom() + " (" + p.getType() + ") - " + p.getPrix() + "€");
                lblProd.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #000000;");

                Button btnRemove = new Button("✕");
                btnRemove.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 10px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 0 0 5;");
                btnRemove.setOnAction(ev -> {
                    m.supprimerProduit(p); // Appelle votre méthode du modèle
                    allerVersMerch(); // Rafraîchissement de la page
                });

                badge.getChildren().addAll(lblProd, btnRemove);
                produitsBox.getChildren().add(badge);
            }
        }

        // --- ZONE D'AJOUT D'UN NOUVEAU PRODUIT (Adaptée à votre Produit.java) ---
        VBox zoneAjout = new VBox(8);
        zoneAjout.setStyle("-fx-border-color: #EAEAEA; -fx-border-width: 1 0 0 0; -fx-padding: 15 0 0 0;");

        // Champ 1 : Nom du produit
        TextField tfNomProduit = new TextField();
        tfNomProduit.setPromptText("Nom du produit (ex: T-Shirt)");
        tfNomProduit.getStyleClass().add("input-field");
        tfNomProduit.setPrefHeight(35);

        // Ligne avec Type et Prix côte à côte
        HBox ligneDetails = new HBox(10);

        TextField tfType = new TextField();
        tfType.setPromptText("Type (ex: Vêtement)");
        tfType.getStyleClass().add("input-field");
        tfType.setPrefHeight(35);
        HBox.setHgrow(tfType, Priority.ALWAYS);

        TextField tfPrix = new TextField();
        tfPrix.setPromptText("Prix (€)");
        tfPrix.getStyleClass().add("input-field");
        tfPrix.setPrefHeight(35);
        HBox.setHgrow(tfPrix, Priority.ALWAYS);

        ligneDetails.getChildren().addAll(tfType, tfPrix);

        // Bouton d'ajout
        Button btnAdd = new Button("Ajouter le produit +");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: #2C2C2E; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 8;");

        btnAdd.setOnAction(ev -> {
            try {
                String nomProd = tfNomProduit.getText();
                String typeProd = tfType.getText();

                // Vérification que tous les champs sont bien remplis
                if (nomProd != null && !nomProd.trim().isEmpty() &&
                        typeProd != null && !typeProd.trim().isEmpty() &&
                        !tfPrix.getText().isEmpty()) {

                    // Conversion du prix en int (comme attendu dans Produit.java)
                    int prix = Integer.parseInt(tfPrix.getText().trim());

                    // Création avec votre constructeur Produit(int prix, String nom, String type)
                    Produit nouveauProduit = new Produit(prix, nomProd.trim(), typeProd.trim());
                    m.ajouterProduit(nouveauProduit);

                    allerVersMerch(); // Rafraîchit l'affichage
                } else {
                    informerUtilisateur("Veuillez remplir tous les champs.", false);
                }
            } catch (NumberFormatException e) {
                // Si l'utilisateur a tapé des lettres dans le champ prix
                informerUtilisateur("Veuillez entrer un nombre entier valide pour le prix.", false);
            }
        });

        // Validation possible avec la touche Entrée depuis n'importe quel champ
        tfNomProduit.setOnAction(btnAdd.getOnAction());
        tfType.setOnAction(btnAdd.getOnAction());
        tfPrix.setOnAction(btnAdd.getOnAction());

        zoneAjout.getChildren().addAll(tfNomProduit, ligneDetails, btnAdd);


        // --- CROIX DE SUPPRESSION DU STAND MERCH ---
        Button btnSupprimerMerch = new Button("✕");
        btnSupprimerMerch.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnSupprimerMerch.setOnMouseEntered(ev -> btnSupprimerMerch.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimerMerch.setOnMouseExited(ev -> btnSupprimerMerch.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimerMerch.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setContentText("Supprimer le merch \"" + m.getNom() + "\" ?");
            alert.showAndWait().ifPresent(reponse -> {
                if (reponse == ButtonType.OK) {
                    controleur.supprimerMerch(m);
                    allerVersMerch();
                }
            });

        });

        // --- ASSEMBLAGE DE LA CARTE ---
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimerMerch);
        header.setAlignment(Pos.TOP_CENTER);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        VBox carte = new VBox(15, header, detail, produitsBox, vSpacer, zoneAjout);

        // Style de la carte
        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #000000; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 18, 0, 0, 6);";

        carte.setStyle(defaultStyle);
        carte.setPrefWidth(350);
        carte.setMinHeight(320); // Carte un peu plus haute pour accueillir les 3 champs

        carte.setOnMouseEntered(ev -> carte.setStyle(hoverStyle));
        carte.setOnMouseExited(ev -> carte.setStyle(defaultStyle));

        return carte;
    }
    private VBox creerCarteEquipe(Equipe e) {
        Label nom = new Label(e.getNom());
        nom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label categorie = new Label("🏷 " + e.getCategorie());
        categorie.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");

        // --- AFFICHAGE DES MEMBRES ---
        // Utilisation d'un FlowPane pour que les badges aillent à la ligne s'ils sont nombreux
        FlowPane personnelBox = new FlowPane();
        personnelBox.setHgap(8);
        personnelBox.setVgap(8);

        if (e.getPersonnels().isEmpty()) {
            Label aucun = new Label("Aucun personnel assigné");
            aucun.setStyle("-fx-font-size: 13px; -fx-text-fill: #AAAAAA; -fx-font-style: italic;");
            personnelBox.getChildren().add(aucun);
        } else {
            for (String membre : e.getPersonnels()) {
                // Création d'un petit badge contenant le nom et un bouton supprimer
                HBox badge = new HBox(5);
                badge.setAlignment(Pos.CENTER);
                badge.setStyle("-fx-background-color: #F2F2F7; -fx-background-radius: 15; -fx-padding: 4 10 4 12;");

                Label lblMembre = new Label(membre);
                lblMembre.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #000000;");

                // Bouton miniature pour retirer ce membre spécifique
                Button btnRemove = new Button("✕");
                btnRemove.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 10px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 0 0 5;");
                btnRemove.setOnAction(ev -> {
                    e.supprimerPersonnel(membre); // Suppression du modèle
                    allerVersStaff(); // Rafraîchissement de la page
                });

                badge.getChildren().addAll(lblMembre, btnRemove);
                personnelBox.getChildren().add(badge);
            }
        }

        // --- ZONE D'AJOUT D'UN NOUVEAU MEMBRE ---
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER_LEFT);

        TextField tfNouveauMembre = new TextField();
        tfNouveauMembre.setPromptText("Nom du membre...");
        tfNouveauMembre.getStyleClass().add("input-field");
        tfNouveauMembre.setPrefHeight(35);
        HBox.setHgrow(tfNouveauMembre, Priority.ALWAYS); // Le champ prend l'espace disponible

        Button btnAdd = new Button("+");
        btnAdd.setStyle("-fx-background-color: #2C2C2E; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 15; -fx-cursor: hand; -fx-padding: 5 15 5 15;");
        btnAdd.setOnAction(ev -> {
            String nouveauNom = tfNouveauMembre.getText();
            if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
                e.ajouterPersonnel(nouveauNom); // Ajout au modèle
                allerVersStaff(); // Rafraîchissement de la page
            }
        });

        // Ajouter la validation avec la touche "Entrée" sur le clavier
        tfNouveauMembre.setOnAction(btnAdd.getOnAction());

        addBox.getChildren().addAll(tfNouveauMembre, btnAdd);


        // --- CROIX DE SUPPRESSION DE L'ÉQUIPE (Entière) ---
        Button btnSupprimerEquipe = new Button("✕");
        btnSupprimerEquipe.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnSupprimerEquipe.setOnMouseEntered(ev -> btnSupprimerEquipe.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimerEquipe.setOnMouseExited(ev -> btnSupprimerEquipe.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimerEquipe.setOnAction(ev -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setContentText("Supprimer l'equipe' \"" + e.getNom() + "\" ?");
            alert.showAndWait().ifPresent(reponse -> {
                if (reponse == ButtonType.OK) {
                    controleur.supprimerEquipe(e);
                    allerVersStaff();
                }
            });

        });

        // --- ASSEMBLAGE DE LA CARTE ---
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimerEquipe);
        header.setAlignment(Pos.TOP_CENTER);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        // Intégration de la zone d'ajout (addBox) tout en bas de la carte
        VBox carte = new VBox(15, header, categorie, personnelBox, vSpacer, addBox);

        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #000000; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 18, 0, 0, 6);";

        carte.setStyle(defaultStyle);
        carte.setPrefWidth(350);
        carte.setMinHeight(250); // Hauteur minimale dynamique pour que la carte s'allonge si beaucoup de membres sont ajoutés

        carte.setOnMouseEntered(ev -> carte.setStyle(hoverStyle));
        carte.setOnMouseExited(ev -> carte.setStyle(defaultStyle));

        return carte;
    }

    private VBox creerCarteBuvette(Buvette b) {
        Label nom = new Label(b.getNom());
        nom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label localisation = new Label("📍 " + b.getLocalisation());
        localisation.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");

        HBox equipesBox = new HBox(8);
        equipesBox.setAlignment(Pos.CENTER_LEFT);
        if (b.getEquipes().isEmpty()) {
            Label aucune = new Label("Aucune équipe assignée");
            aucune.setStyle("-fx-font-size: 13px; -fx-text-fill: #AAAAAA; -fx-font-style: italic;");
            equipesBox.getChildren().add(aucune);
        } else {
            for (Equipe equipe : b.getEquipes()) {
                Label badge = new Label(equipe.getNom());
                badge.setStyle("-fx-background-color: #F2F2F7; -fx-background-radius: 15; -fx-padding: 4 12 4 12; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #000000;");
                equipesBox.getChildren().add(badge);
            }
        }

        // CROIX DE SUPPRESSION
        Button btnSupprimer = new Button("✕");
        btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnSupprimer.setOnMouseEntered(ev -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnMouseExited(ev -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setContentText("Supprimer la buvette \"" + b.getNom() + "\" ?");
            alert.showAndWait().ifPresent(reponse -> {
                if (reponse == ButtonType.OK) {
                    controleur.supprimerBuvette(b);
                    allerVersBuvette();
                }
            });

        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimer);
        header.setAlignment(Pos.TOP_CENTER);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        // BOUTON "APPLIQUER CARTE"
        Button btnAppliquerCarte = new Button("Appliquer carte");
        btnAppliquerCarte.setMaxWidth(Double.MAX_VALUE);
        btnAppliquerCarte.setStyle("-fx-background-color: #2C2C2E; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        btnAppliquerCarte.setOnMouseEntered(ev -> btnAppliquerCarte.setStyle("-fx-background-color: #3A3A3C; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;"));
        btnAppliquerCarte.setOnMouseExited(ev -> btnAppliquerCarte.setStyle("-fx-background-color: #2C2C2E; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 16 8 16;"));
        btnAppliquerCarte.setOnAction(ev -> {
            allerVersCarte();
        });

        VBox carte = new VBox(15, header, localisation, vSpacer, equipesBox, btnAppliquerCarte);

        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #000000; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 18, 0, 0, 6);";

        carte.setStyle(defaultStyle);
        carte.setPrefSize(350, 220);
        carte.setOnMouseEntered(ev -> carte.setStyle(hoverStyle));
        carte.setOnMouseExited(ev -> carte.setStyle(defaultStyle));

        return carte;
    }

    private VBox creerCarteCarte(Carte c) {
        Label nom = new Label("Carte : \"" + c.getNom() + "\"");
        nom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label theme = new Label("Thème : " + c.getTheme());
        theme.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");

        // CROIX DE SUPPRESSION
        Button btnSupprimer = new Button("✕");
        btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnSupprimer.setOnMouseEntered(ev -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnMouseExited(ev -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setContentText("Supprimer la carte \"" + c.getNom() + "\" ?");
            alert.showAndWait().ifPresent(reponse -> {
                if (reponse == ButtonType.OK) {
                    controleur.supprimerCarte(c);
                    allerVersCarte();
                }
            });

        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimer);
        header.setAlignment(Pos.TOP_CENTER);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        VBox carte = new VBox(15, header, vSpacer, theme);

        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #000000; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 18, 0, 0, 6);";

        carte.setStyle(defaultStyle);
        carte.setPrefSize(350, 200);
        carte.setOnMouseEntered(ev -> carte.setStyle(hoverStyle));
        carte.setOnMouseExited(ev -> carte.setStyle(defaultStyle));

        return carte;
    }


    @FXML
    private void cancelNewOrganisateurAction() {
        this.newOrgCancelButton.getScene().getWindow().hide();
    }

    @FXML
    private void validateOrgTextFields() {
        boolean isValid = validateNonEmptyTextField(this.newOrgForenameTextField)
                && validateNonEmptyTextField(this.newOrgSurnameTextField)
                && validateEmailTextField(this.newOrgEmailTextField)
                && this.newOrgMdpTextField.getText().length() > 0;
        this.newOrgOkButton.setDisable(!isValid);
    }

    public void toggleMenu() {
        boolean isOpen = sideMenu.isVisible();
        sideMenu.setVisible(!isOpen);
        scrim.setVisible(!isOpen);
    }

    public void toggleMenuD() {
        boolean isOpen = sideMenuD.isVisible();
        sideMenuD.setVisible(!isOpen);
        scrim.setVisible(!isOpen);
    }

    //Pour Merch
    // champs vue Merch
    @FXML private Region scrimMerch;

    // Protection contre les erreurs : Alerte de confirmation de suppression
    @FXML
    private void supprimerMerchAction() {
        // Création de l'alerte de confirmation (Bonne pratique UX)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression du Merch");
        alert.setContentText("Attention, voulez-vous vraiment supprimer ce merch ? Cette action est irréversible.");

        // On capture le choix de l'utilisateur
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // ---> METS ICI TON CODE POUR SUPPRIMER LE MERCH DU MODÈLE <---

            informerUtilisateur("Le merch a été supprimé avec succès.", true);
            // Tu pourras ensuite rafraîchir l'affichage ici
        } else {
            // L'utilisateur a cliqué sur "Annuler" ou fermé la fenêtre, on ne fait rien (Protection réussie !)
        }
    }

    // Action pour le gros bouton "Ajout merch +"
    @FXML
    private void ajouterMerchAction() {
        allerVerscreerMerch();
    }

    //Pour Matériel
    // champs vue Matériel
    @FXML private VBox sideMenuMateriel;
    @FXML private Region scrimMateriel;

    // Navigation vers la page Matériel
    @FXML
    private void allerVersMateriel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("materiel-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");
            welcomeLabel.setText(controleur.getConcertSelectionne().getNom());

            if (materielTilePane != null) {
                materielTilePane.getChildren().clear();
                for (Materiel m : controleur.getConcertSelectionne().getMateriels()) {
                    materielTilePane.getChildren().add(creerCarteMateriel(m));
                }
            }
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    // Gestion du volet latéral spécifique à la page Matériel
    @FXML
    public void toggleMenuMateriel() {
        boolean isOpen = sideMenuMateriel.isVisible();
        sideMenuMateriel.setVisible(!isOpen);
        scrimMateriel.setVisible(!isOpen);
    }

    // Protection contre les erreurs : Alerte de confirmation de suppression
    @FXML
    private void supprimerMaterielAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression du Matériel");
        alert.setContentText("Attention, voulez-vous vraiment supprimer cet équipement de la liste ? Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // ---> METS ICI TON CODE POUR SUPPRIMER LE MATÉRIEL DU MODÈLE <---

            informerUtilisateur("L'équipement a été supprimé avec succès.", true);
            // Pense à rafraîchir ta vue ici
        }
    }

    // Action pour le bouton "Ajout matériel +"

    @FXML
    private void ajouterMaterielAction() {
        allerVerscreerMateriels();
    }


    //Pour Staff
    // champs vue Staff
    @FXML private VBox sideMenuStaff;
    @FXML private Region scrimStaff;




    // Gestion de la visibilité du volet latéral Staff (Inactif par défaut)
    @FXML
    public void toggleMenuStaff() {
        boolean isOpen = sideMenuStaff.isVisible();
        sideMenuStaff.setVisible(!isOpen);
        scrimStaff.setVisible(!isOpen);
    }

    // Gestion des Erreurs : Fenêtre de confirmation avant suppression irréversible
    @FXML
    private void supprimerStaffAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression d'une équipe de staff");
        alert.setContentText("Attention, voulez-vous vraiment supprimer cette équipe ? Cette action retirera tous les membres assignés.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // ---> Insérer ici le code pour mettre à jour la liste dans le modèle <---

            informerUtilisateur("L'équipe a été supprimée avec succès.", true);
        } else {
            // L'utilisateur a cliqué sur "Annuler" ou fermé la boîte : l'action est avortée sans risque.
            System.out.println("Suppression annulée.");
        }
    }

    // Action pour le bouton global "Nouvelle équipe +"
    @FXML
    private void ajouterStaffAction() {
        allerVerscreerStaff();
    }


    public void voirconcertaction(){
        allerVersMesConcerts();
    }

//-----  Utilitaires de validation  --------------------------------------------

    private static void markTextFieldErrorStatus(TextField textField, boolean isValid) {
        if (isValid) {
            textField.setStyle(null);
        } else {
            textField.setStyle("-fx-control-inner-background: f8d7da");
        }
    }

    private static boolean validateNonEmptyTextField(TextField textField) {
        boolean isValid = textField.getText().strip().length() > 0;
        markTextFieldErrorStatus(textField, isValid);
        return isValid;
    }

    private static boolean validateEmailTextField(TextField textField) {
        EmailValidator validator = EmailValidator.getInstance(false, false);
        boolean isValid = validator.isValid(textField.getText().strip().toLowerCase());
        markTextFieldErrorStatus(textField, isValid);
        return isValid;
    }

//-----  Implémentation des méthodes abstraites  -------------------------------

    @Override
    public void demarrerInteraction() {
        Platform.startup(() -> {
            Stage stage = new Stage();
            stage.setOnCloseRequest((WindowEvent t) -> this.exitAction());
            try {
                this.start(stage);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        });
        try {
            this.eolBarrier.await();
        } catch (InterruptedException exc) {
            System.err.println("Erreur d'exécution de l'interface.");
            System.err.flush();
        }
    }

    @Override
    public void informerUtilisateur(String msg, boolean succes) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(
                    succes ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING
            );
            alert.setTitle("GenEvent");
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }

    @Override
    public void saisirConnexion() {
        // Dans le flux principal on navigue directement via la scène de login ;
        // cette méthode reste utile si le CLI ou un autre composant l'appelle.
        allerVersLogin();
    }


    @Override
    public void saisirOrganisateur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new-user-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            Stage window = new Stage();
            window.setTitle("Créer un organisateur");
            window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void saisirNouveauConcert(Set<String> nomsExistants) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saisirInscription() {
        saisirOrganisateur();
    }

    @Override
    public void afficherConcerts(Map<String, Concert> concerts) {
        // À implémenter selon la vue concerts
    }

    @FXML
    private void allerVersRechercheSalles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("recherche-salles-view.fxml"));

            // On charge d'abord la vue (le FXML)
            Scene scene = new Scene(loader.load());

            // On récupère le contrôleur de la page qui a été créé automatiquement par le FXML
            RechercheSallesController controller = loader.getController();

            // On l'initialise avec les bonnes données !
            // (this.controleur pour les données de l'application, et this pour pouvoir revenir en arrière plus tard)
            controller.initialiser(this.controleur, this);

            // On affiche la page
            primaryStage.setScene(scene);
            primaryStage.setTitle("IN STAGE");

        } catch (IOException exc) {
            System.err.println("Erreur lors du chargement de la page Recherche Salles !");
            exc.printStackTrace();
            throw new RuntimeException(exc);
        }
    }
    //gestion des dates
    private void initialiserDatesConcert() {
        // Sécurité si la page n'a pas les composants chargés
        if (dpDateDebut == null || dpDateFin == null) return;

        // 1. Ajustement automatique de la date de fin quand la date de début change
        dpDateDebut.valueProperty().addListener((observable, ancienneDate, nouvelleDate) -> {
            if (nouvelleDate != null) {
                LocalDate dateFinActuelle = dpDateFin.getValue();
                if (dateFinActuelle == null || dateFinActuelle.isBefore(nouvelleDate)) {
                    dpDateFin.setValue(nouvelleDate);
                }
            }
        });

        // 2. Blocage si la date de fin est antérieure à la date de début
        dpDateFin.valueProperty().addListener((observable, ancienneDate, nouvelleDate) -> {
            LocalDate dateDebutActuelle = dpDateDebut.getValue();
            if (nouvelleDate != null && dateDebutActuelle != null) {
                if (nouvelleDate.isBefore(dateDebutActuelle)) {
                    informerUtilisateur("La date de fin ne peut pas être antérieure à la date de début !", false);
                    javafx.application.Platform.runLater(() -> dpDateFin.setValue(dateDebutActuelle));
                }
            }
        });

        // 3. Griser visuellement les jours impossibles dans le calendrier de fin
        dpDateFin.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate debut = dpDateDebut.getValue();
                setDisable(empty || (debut != null && date.isBefore(debut)));
            }
        });
    }

    @FXML
    private void reprendreDernierConcertAction() {
        Organisateur org = controleur.getOrganisateurConnecte();
        if (org != null && org.getDernierConcertConsulte() != null) {
            // Sélection automatique et redirection
            controleur.selectionnerConcert(org.getDernierConcertConsulte());
            allerVersdashboard();
        }
    }



}