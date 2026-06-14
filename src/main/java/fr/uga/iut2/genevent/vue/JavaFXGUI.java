package fr.uga.iut2.genevent.vue;
import fr.uga.iut2.genevent.controleur.Controleur;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.validator.routines.EmailValidator;

public class JavaFXGUI extends IHM {

    private final Controleur controleur;
    private final CountDownLatch eolBarrier;

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

    public JavaFXGUI(Controleur controleur) {
        this.controleur = controleur;
        this.eolBarrier = new CountDownLatch(1);
    }

//-----  Démarrage  ------------------------------------------------------------

    private void start(Stage primaryStage) throws IOException {
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());
        primaryStage.setTitle("GenEvent");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

//-----  Éléments du dialogue  -------------------------------------------------

    private void exitAction() {
        this.eolBarrier.countDown();
    }

    // menu principal
    @FXML
    private void connexionMenuItemAction() {
        this.controleur.saisirConnexion();
    }

    @FXML
    private void newOrganisateurMenuItemAction() {
        this.controleur.saisirOrganisateur();
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

    // vue connexion
    @FXML
    private void loginAction() {
        IHM.InfosConnexion data = new IHM.InfosConnexion(
                this.loginEmailTextField.getText().strip().toLowerCase(),
                this.loginMdpField.getText()
        );
        this.controleur.seConnecter(data);
        this.loginOkButton.getScene().getWindow().hide();
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

    // vue nouvel organisateur
    @FXML
    private void createNewOrganisateurAction() {
        IHM.InfosOrganisateur data = new IHM.InfosOrganisateur(
                this.newOrgSurnameTextField.getText().strip(),
                this.newOrgForenameTextField.getText().strip(),
                this.newOrgMdpTextField.getText(),
                this.newOrgEmailTextField.getText().strip().toLowerCase()
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
            Stage primaryStage = new Stage();
            primaryStage.setOnCloseRequest((WindowEvent t) -> this.exitAction());
            try {
                this.start(primaryStage);
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
        final Alert alert = new Alert(
                succes ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING
        );
        alert.setTitle("GenEvent");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void saisirConnexion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            Stage window = new Stage();
            window.setTitle("Connexion");
            window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
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
}