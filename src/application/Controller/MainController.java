package application.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {
    @FXML
    StackPane rootPane;

    @FXML
    private Label user;

    @FXML
    private Label settings;

    @FXML
    void initialize() {
        user.setOnMouseClicked(e -> {
            try {
                Stage newStage = ((Stage) rootPane.getScene().getWindow());
                Parent root = FXMLLoader.load(getClass().getResource("View/food.fxml"));
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()));
                newStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        settings.setOnMouseClicked(e -> {
            try {
                Stage newStage = ((Stage) rootPane.getScene().getWindow());
                Parent root = FXMLLoader.load(getClass().getResource("View/login.fxml"));
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()));
                newStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}

