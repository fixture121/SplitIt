import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField rePasswordField;
    
    @FXML
    private void handleRegisterButtonAction() {
        String username = nameField.getText();
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();

        if (!password.equals(rePassword)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Error");
            alert.setContentText("Your entered passwords do not match.");
            alert.showAndWait();
        } else {
            Main.setUser(new Account(username, password));
            Main.setRoot("WelcomeScreen");
            Main.showWelcomeScreen();
        }
    }

}
