package api.allegro.controller;

import api.allegro.app.App;
import api.allegro.exception.AllegroAccessDeniedException;
import api.allegro.exception.AllegroAuthorizationPendingException;
import api.allegro.exception.AllegroInvalidRefreshTokenException;
import api.allegro.exception.AllegroSlowDownException;
import api.allegro.service.AuthorizationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;

import java.io.IOException;

public class AuthorizationController {

    private final AuthorizationService service = new AuthorizationService("allegro-pu");

    @FXML
    private Button connectBtn;
    @FXML
    private Button goToDashboardBtn;
    @FXML
    private TextField verificationUriTxt;
    @FXML
    private Button copyBtn;
    @FXML
    private Label introLbl;


    @FXML
    public void initialize() {
        App.getStage().setTitle("Authorize application");
        try {
            if (service.getAccessToken() != null) {
                service.refreshTokens();
                copyBtn.setVisible(false);
                goToDashboardBtn.setVisible(true);
                verificationUriTxt.setVisible(false);
                connectBtn.setVisible(false);
                introLbl.setText("Already logged in. Go to Dashboard.");
            } else {
                verificationUriTxt.setText(service.getVerificationUri());
                goToDashboardBtn.setVisible(false);
                connectBtn.setVisible(true);
            }
        } catch (IOException | InterruptedException e) {
            Alert alert = new Alert(AlertType.WARNING, "IO/Network error. Please try again.");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                initialize();
            }
        } catch (IllegalStateException e) {
            Alert alert = new Alert(AlertType.ERROR, "Unknown error. " + e.getMessage());
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                initialize();
            }
        } catch (AllegroInvalidRefreshTokenException e) {
            service.clearTokens();
            Alert alert = new Alert(AlertType.WARNING, "Token has expired. Please connect app.");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                initialize();
            }
        }
    }

    @FXML
    public void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    public void copyUri() {
        ClipboardContent cc = new ClipboardContent();
        cc.putString(verificationUriTxt.getText());
        App.getClipboard().setContent(cc);
    }

    @FXML
    public void connectApp() {
        try {
            //service.clearTokens();
            service.getTokens();
            App.setRoot("dashboard");
        } catch (AllegroSlowDownException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(AlertType.WARNING, "IO/Network error. Please try again.");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                connectApp();
            }
        } catch (AllegroAccessDeniedException e) {
            Alert alert = new Alert(AlertType.ERROR, "Authorization failed. Please try again.");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                initialize();
            }
        } catch (AllegroAuthorizationPendingException e) {
            showAlertWindow(AlertType.WARNING, "Authorization pending.");
        } catch (IllegalStateException e) {
            Alert alert = new Alert(AlertType.ERROR, "Unknown error. " + e.getMessage());
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                initialize();
            }
        }
    }

    @FXML
    public void showAlertWindow(AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
