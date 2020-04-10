package application.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View/main.fxml"));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Түргэн хоол захиалга");
        primaryStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        DAO.init();
        launch(args);
    }
}
