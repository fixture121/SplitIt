import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    
    /** 
     * @throws Exception
     */
    @FXML
    private void handleLoginButtonAction() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Main.setUser(Account.login(username, password));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (Main.getUser() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Error");
            alert.setContentText("Your credentials do not match any stored on our system.");
            alert.showAndWait();
        } else {
            Main.setRoot("AccountView");
        }
    }
}
