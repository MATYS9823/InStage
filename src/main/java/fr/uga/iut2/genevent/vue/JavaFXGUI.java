package fr.uga.iut2.genevent.vue;
import fr.uga.iut2.genevent.controleur.Controleur;

import fr.uga.iut2.genevent.modele.*;
import javafx.event.ActionEvent;
import java.io.IOException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.apache.commons.validator.routines.EmailValidator;


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






    @FXML private TextField tfNomBuvette;
    @FXML private TextField tfLocalisationBuvette;
    @FXML private VBox buvetteTilePane;
    @FXML private VBox sideMenuBuvette;
    @FXML private Region scrimBuvette;

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
        primaryStage.setTitle("GenEvent");
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
            // Met à jour le label de bienvenue si présent dans le FXML
            if (welcomeLabel != null) {
                welcomeLabel.setText("Bienvenue " + controleur.getOrganisateurConnecte().getPrenom()
                        + " " + controleur.getOrganisateurConnecte().getNom());
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
            if (buvetteTilePane != null && controleur.getConcertSelectionne() != null) {
                buvetteTilePane.getChildren().clear();
                for (Buvette b : controleur.getConcertSelectionne().getBuvettes()) {
                    buvetteTilePane.getChildren().add(creerCarteBuvette(b));
                }
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

            Concert concert = controleur.getConcertSelectionne();
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

    private void allerVerscreerBuvette() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-buvette-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);

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
        }catch (IOException exc) {
            throw new RuntimeException(exc);
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
        allerVerscreerBuvette();
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
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);
        HBox footer = new HBox(10, date, footerSpacer, btnModifier);
        footer.setAlignment(Pos.BOTTOM_CENTER);

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
            // Logique de suppression
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
        Label nom = new Label(m.getNom());
        nom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label localisation = new Label("📍 " + m.getLocalisation());
        localisation.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        // CROIX DE SUPPRESSION
        Button btnSupprimer = new Button("✕");
        btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnSupprimer.setOnMouseEntered(e -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnMouseExited(e -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnAction(e -> {
            // Logique de suppression
        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimer);
        header.setAlignment(Pos.TOP_CENTER);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        VBox carte = new VBox(15, header, vSpacer, localisation);

        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #000000; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 18, 0, 0, 6);";

        carte.setStyle(defaultStyle);
        carte.setPrefSize(350, 200);
        carte.setOnMouseEntered(e -> carte.setStyle(hoverStyle));
        carte.setOnMouseExited(e -> carte.setStyle(defaultStyle));

        return carte;
    }

    private VBox creerCarteEquipe(Equipe e) {
        Label nom = new Label(e.getNom());
        nom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label categorie = new Label("🏷 " + e.getCategorie());
        categorie.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");

        HBox personnelBox = new HBox(8);
        personnelBox.setAlignment(Pos.CENTER_LEFT);
        if (e.getPersonnels().isEmpty()) {
            Label aucun = new Label("Aucun personnel assigné");
            aucun.setStyle("-fx-font-size: 13px; -fx-text-fill: #AAAAAA; -fx-font-style: italic;");
            personnelBox.getChildren().add(aucun);
        } else {
            for (String membre : e.getPersonnels()) {
                Label badge = new Label(membre);
                badge.setStyle("-fx-background-color: #F2F2F7; -fx-background-radius: 15; -fx-padding: 4 12 4 12; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #000000;");
                personnelBox.getChildren().add(badge);
            }
        }

        // CROIX DE SUPPRESSION
        Button btnSupprimer = new Button("✕");
        btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnSupprimer.setOnMouseEntered(ev -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #E74C3C; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnMouseExited(ev -> btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        btnSupprimer.setOnAction(ev -> {
            // Logique de suppression
        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimer);
        header.setAlignment(Pos.TOP_CENTER);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        VBox carte = new VBox(15, header, categorie, vSpacer, personnelBox);

        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #000000; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 18, 0, 0, 6);";

        carte.setStyle(defaultStyle);
        carte.setPrefSize(350, 200);
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
        btnSupprimer.setOnAction(ev -> {
            // Logique de suppression
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

        } catch (IOException exc) {
            System.err.println("Erreur lors du chargement de la page Recherche Salles !");
            exc.printStackTrace();
            throw new RuntimeException(exc);
        }
    }
}