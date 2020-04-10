package application.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView main;

    @FXML
    private TextField username;

    @FXML
    private Label status;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    void initialize() {
        main.setOnMouseClicked(event -> {
            try {
                Stage newStage = ((Stage) rootPane.getScene().getWindow());
                Parent root = FXMLLoader.load(getClass().getResource("View/main.fxml"));
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()));
                newStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        login.setOnAction(e -> checkLogin());

        rootPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                checkLogin();
        });
    }

    private void checkLogin() {
        status.setText("");
        username.setStyle("");
        password.setStyle("");
        if (username.getText().isEmpty())
            username.setStyle("-fx-border-color: red");
        else if (password.getText().isEmpty())
            password.setStyle("-fx-border-color: red");
        else {
            if (username.getText().equals("admin") && password.getText().equals("admin")) {
                try {
                    Stage newStage = ((Stage) rootPane.getScene().getWindow());
                    Parent root = FXMLLoader.load(getClass().getResource("View/settings.fxml"));
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()));
                    newStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                status.setText("Нэвтрэх нэр эсвэл нууц үг буруу байна.");
            }
        }
    }
}