import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;
    private static Account user;

    
    /** 
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            ExpenseList.repopulateExpenseLists();   // Always first
            Account.repopulateAccounts();           // Always second
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Main.primaryStage = primaryStage;
        showWelcomeScreen();
    }

    public static void setUser(Account account) {
        user = account;
    }

    public static Account getUser() {
        return user;
    }

    @FXML
    private void handleLoginAction() {
        Main.setRoot("LoginView");
        Main.showLoginView();
    }

    @FXML
    private void handleRegisterAction() {
        Main.setRoot("RegisterView");
        Main.showRegisterView();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void showWelcomeScreen() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("WelcomeScreen.fxml"));
            primaryStage.setTitle("Split It");
            primaryStage.setScene(new Scene(root, 400, 450));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showLoginView() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("LoginView.fxml"));
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root, 400, 450));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showRegisterView() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("RegisterView.fxml"));
            primaryStage.setTitle("Register New Account");
            primaryStage.setScene(new Scene(root, 400, 450));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showListScreen() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("ListScreen.fxml"));
            primaryStage.setTitle("List 1");
            primaryStage.setScene(new Scene(root, 400, 450));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAccountView() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("AccountView.fxml"));
            primaryStage.setTitle("Account View");
            primaryStage.setScene(new Scene(root, 400, 450));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile + ".fxml"));
            primaryStage.setScene(new Scene(root, 400, 450));
            primaryStage.setTitle("Home Screen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
