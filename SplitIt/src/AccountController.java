import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class AccountController implements Initializable {

    @FXML
    private Label accountLabel;

    @FXML
    private void handleCreateNewListButtonAction() {
        Main.getUser().addExpenseList(new ExpenseList("New List", Main.getUser()));
        Main.setRoot("ListScreen");
        Main.showListScreen();
    }

    @FXML
    private void handleJoinListButtonAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feature Under Construction");
        alert.setContentText("Feature to be implemented in the future.");
        alert.show();
    }

    @FXML
    private Label yourListsLabel;

    @FXML
    private void handleList1ButtonAction() {
        Main.setRoot("ListScreen");
        Main.showListScreen();
    }

    @FXML
    private void handleLogoutButtonAction() {
        Main.setRoot("WelcomeScreen");
        Main.showWelcomeScreen();
    }

    
    /** 
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountLabel.setText("Welcome, " + Main.getUser().getName() + "!");
    }
}
