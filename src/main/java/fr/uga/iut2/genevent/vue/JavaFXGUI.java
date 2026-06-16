package fr.uga.iut2.genevent.vue;
import fr.uga.iut2.genevent.controleur.Controleur;

import fr.uga.iut2.genevent.modele.Organisateur;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
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
    @FXML private  Label gererconcertlabel;

    @FXML private TextField tfNomconcert;
    @FXML private TextField tflieuconcert;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private TextField tfArtiste;

    @FXML private Button creerconcertButton;


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
            if (gererconcertlabel != null) {
                gererconcertlabel.setText("Gérer vos concerts " + controleur.getOrganisateurConnecte().getPrenom()
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
        Platform.runLater(() -> {
            // 1. On récupère TOUTES les fenêtres ouvertes (principale + popups) et on les ferme
            new java.util.ArrayList<>(javafx.stage.Window.getWindows()).forEach(window -> {
                if (window instanceof Stage) {
                    ((Stage) window).close();
                }
            });

            // 2. On libère la barrière pour réveiller ton Main
            // Le programme va exécuter ton code de sauvegarde automatique puis s'éteindre
            this.exitAction();
        });
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
    private void createNewConcertAction(){
        IHM.InfosNouveauConcert data = new IHM.InfosNouveauConcert(
            this.tfNomconcert.getText().strip(),
                this.dpDateDebut.getValue(),
                this.dpDateDebut.getValue(),
                this.tflieuconcert.getText().strip(),
                this.tfArtiste.getText().strip()
        );

        this.controleur.creerConcert(data);
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
    @FXML private VBox sideMenuMerch;
    @FXML private Region scrimMerch;

    // Navigation vers la page Merch (à lier à tes autres boutons de menu)
    @FXML
    private void allerVersMerch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("merch-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    // Gestion du volet latéral de la page Merch
    @FXML
    public void toggleMenuMerch() {
        boolean isOpen = sideMenuMerch.isVisible();
        sideMenuMerch.setVisible(!isOpen);
        scrimMerch.setVisible(!isOpen);
    }

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
        System.out.println("Ouverture du formulaire d'ajout d'un nouvel équipement...");
        // Tu pourras appeler ici une méthode qui ouvre un Stage en mode "Modality.APPLICATION_MODAL"
        // comme tu l'as fait pour la méthode saisirOrganisateur()
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new-org-view.fxml"));
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
