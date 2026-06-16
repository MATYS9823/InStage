package fr.uga.iut2.genevent.vue;
import fr.uga.iut2.genevent.controleur.Controleur;

import fr.uga.iut2.genevent.modele.Organisateur;
import javafx.event.ActionEvent;
import java.io.IOException;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.validator.routines.EmailValidator;

public class JavaFXGUI extends IHM {

    private final Controleur controleur;
    private final CountDownLatch eolBarrier;

    // Stage principal, gardé pour changer de scène
    private Stage primaryStage;

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
    private void allerVersAccueil() {
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
    private void allerVersMesConcerts(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage-concerts-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (IOException exc) {
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
        this.controleur.saisirConcert();
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






}
