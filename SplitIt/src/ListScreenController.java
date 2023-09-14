import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ListScreenController implements Initializable {

    @FXML
    private Label list1Label;
    @FXML
    private Label item1;
    @FXML
    private Label item2;
    @FXML
    private Label item3;
    @FXML
    private Label item4;
    @FXML
    private Label item5;
    @FXML
    private Label item6;

    @FXML
    private void handleAddNewExpenseButtonAction() {
        
    }

    @FXML
    private void handleBackButtonAction() {
        Main.setRoot("AccountView");
        Main.showAccountView();
    }

    
    /** 
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list1Label.setText("Welcome, " + Main.getUser().getName() + "!");

    }
}
