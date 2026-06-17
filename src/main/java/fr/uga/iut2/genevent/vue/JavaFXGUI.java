package fr.uga.iut2.genevent.vue;
import fr.uga.iut2.genevent.controleur.Controleur;

import fr.uga.iut2.genevent.modele.Organisateur;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import fr.uga.iut2.genevent.modele.Concert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.validator.routines.EmailValidator;
import java.util.List;


public class JavaFXGUI extends IHM {

    private final Controleur controleur;
    private final CountDownLatch eolBarrier;

    // Stage principal, gardé pour changer de scène
    private Stage primaryStage;
    private final List<IHM.InfosNouveauConcert> concertsSauvegardes = new ArrayList<InfosNouveauConcert>();

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


    // vhamp vue dashboard
    @FXML private VBox sideMenuD;

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
    public void allerVerscreerConcertsFromSalle(String lieu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-concert-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);

            // Une fois la vue chargée, on remplit le champ avec le lieu passé en paramètre
            if (tflieuconcert != null) {
                tflieuconcert.setText(lieu);
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
            if (controleur.getConcertSelectionne() != null) {
                welcomeLabel.setText(controleur.getConcertSelectionne().getNom());
                artistelabel.setText(controleur.getConcertSelectionne().getArtiste());
            }

        }catch (IOException exc){
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
        allerVersLogin();
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
                new java.util.ArrayList<>(javafx.stage.Window.getWindows()).forEach(window -> {
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
        IHM.InfosConnexion data = new IHM.InfosConnexion(
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
        IHM.InfosOrganisateur data = new IHM.InfosOrganisateur(
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

        IHM.InfosNouveauConcert data = new IHM.InfosNouveauConcert(
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

        IHM.InfosMateriel data = new IHM.InfosMateriel(
                prix,
                tfTypeEquipement.getText().strip(),
                tfFournisseur.getText().strip(),
                tfNomMateriel.getText().strip()
        );

        controleur.creerMateriel(data);
        allerVersMateriel();
    }

    private VBox creerCarteConcert(Concert c) {
        // --- ÉLÉMENTS DE TEXTE ---
        Label nom = new Label(c.getNom());
        nom.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        Label artiste = new Label(c.getArtiste());
        artiste.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: #7F8C8D;");

        Label lieu = new Label("📍 " + c.getLieu());
        lieu.setStyle("-fx-text-fill: #34495E; -fx-font-size: 13px;");

        String dateStr = c.getDate_debut() != null
                ? c.getDate_debut().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.FRANCE))
                : "Date inconnue";
        Label date = new Label("📅 " + dateStr);
        date.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2980B9;");

        // --- BOUTONS INTERACTIFS ---
        // Bouton Supprimer (Top Right)
        Button btnSupprimer = new Button("✕");
        btnSupprimer.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setContentText("Supprimer le concert \"" + c.getNom() + "\" ?");
            alert.showAndWait().ifPresent(reponse -> {
                if (reponse == ButtonType.OK) {
                    controleur.supprimerConcert(c);   // à ajouter dans Controleur/modèle
                    allerVersMesConcerts();           // recharge la vue pour rafraîchir les cartes
                }
            });
        });
        // Bouton Modifier (Bottom Right)
        Button btnModifier = new Button("Modifier");
        btnModifier.setOnAction(e -> {
            controleur.selectionnerConcert(c);
            allerVersdashboard();});
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, javafx.scene.layout.Priority.ALWAYS);
        HBox header = new HBox(10, nom, headerSpacer, btnSupprimer);
        header.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        // Contenu central : Artiste et Lieu
        VBox corpsContenu = new VBox(5, artiste, lieu);

        // Footer : Date à gauche, bouton modifier à droite
        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, javafx.scene.layout.Priority.ALWAYS);
        HBox footer = new HBox(10, date, footerSpacer, btnModifier);
        footer.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER);

        // Spacer vertical pour pousser le footer vers le bas
        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, javafx.scene.layout.Priority.ALWAYS);

        // Assemblage final de la carte
        VBox carte = new VBox(12, header, corpsContenu, vSpacer, footer);
        // Style de la carte : Fond blanc/ombragé plus moderne qu'un gris brut
        carte.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-background-radius: 18; " +
                        "-fx-padding: 20; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        carte.setPrefSize(360, 200);

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
        java.util.Optional<ButtonType> result = alert.showAndWait();
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
        // Ouvre la fenêtre/dialogue pour créer un nouveau Merch
        System.out.println("Ouverture du formulaire d'ajout d'un nouveau Merch...");
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

        java.util.Optional<ButtonType> result = alert.showAndWait();
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

    // Navigation vers la page Staff
    @FXML
    private void allerVersStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("staff-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

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

        java.util.Optional<ButtonType> result = alert.showAndWait();
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
        System.out.println("Ouverture du formulaire de création d'une équipe de staff...");
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
