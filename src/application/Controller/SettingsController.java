package application.Controller;

import application.Controller.Model.Food;
import application.Controller.Model.FoodContainer;
import application.Controller.Model.Server;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;
import mn.mta.pos.exam.BridgePosAPI;
import org.json.JSONObject;
import sample.MaskTextField;

import java.io.*;

public class SettingsController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private MenuItem productAdd;

    @FXML
    private MenuItem productMinus;

    @FXML
    private MenuItem productEdit;

    @FXML
    private MenuItem ebarimtSubmit;

    @FXML
    private MenuItem ebarimtReturn;

    @FXML
    private MenuItem depositReport;

    @FXML
    private MenuItem orderReport;

    @FXML
    private MenuItem chartReport;

    @FXML
    private JFXButton settings;

    @FXML
    private JFXButton exit;

    @FXML
    private Pane addPane;

    @FXML
    private ChoiceBox<String> addGenre;

    @FXML
    private ChoiceBox<String> addUnit;

    @FXML
    private JFXButton addChoose;

    @FXML
    private Button addButton;

    @FXML
    private MaskTextField addCode;

    @FXML
    private MaskTextField addName;

    @FXML
    private MaskTextField addNote;

    @FXML
    private MaskTextField addPrice;

    @FXML
    private Label addStatus;

    @FXML
    private Label editStatus;

    @FXML
    private Label minusStatus;

    @FXML
    private Pane minusPane;

    @FXML
    private MaskTextField minusNumber;

    @FXML
    private Button minusButton;

    @FXML
    private Pane editPane;

    @FXML
    private ChoiceBox<String> editGenre;

    @FXML
    private ChoiceBox<String> editUnit;

    @FXML
    private JFXButton editChoose;

    @FXML
    private Button editButton;

    @FXML
    private MaskTextField editNumber;

    @FXML
    private MaskTextField editName;

    @FXML
    private MaskTextField editNote;

    @FXML
    private MaskTextField editPrice;

    @FXML
    private Pane ebarimtPane;

    @FXML
    private MaskTextField ebarimtNumber;

    @FXML
    private Button ebarimtButton;

    @FXML
    private Pane settingsPane;

    @FXML
    private JFXToggleButton notification;

    @FXML
    private MaskTextField notificationIP;

    @FXML
    private MaskTextField notificationPort;

    @FXML
    private Button notificationButton;

    @FXML
    private Label notificationStatus;

    private File addfile = null;
    private File editFile = null;

    @FXML
    void initialize() {
        settingInit();
        addPane.toFront();
        ObservableList<MenuItem> reportList = FXCollections.observableArrayList(chartReport, depositReport, orderReport);
        ObservableList<String> genreList = FXCollections.observableArrayList("ПИЦЦА", "УНДАА", "НЭМЭЛТ", "БУРГЕР", "ТАХИА");
        ObservableList<String> unitList = FXCollections.observableArrayList("Л", "ГР");
        addGenre.setItems(genreList);
        addUnit.setItems(unitList);
        editGenre.setItems(genreList);
        editUnit.setItems(unitList);
        productAdd.setOnAction(e -> changeWindow(addPane));
        productMinus.setOnAction(e -> changeWindow(minusPane));
        productEdit.setOnAction(e -> changeWindow(editPane));
        ebarimtReturn.setOnAction(e -> changeWindow(ebarimtPane));
        settings.setOnAction(e -> changeWindow(settingsPane));

        editNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            Food food = FoodContainer.searchByCode(newValue);
            if (food != null) {
                editName.setText(food.getName());
                editNote.setText(food.getTitle());
                editGenre.getSelectionModel().select(food.getGenre());
                editUnit.getSelectionModel().select(food.getMeasureUnit());
                editPrice.setText(String.valueOf(food.getPrice()));
            }
        });

        editButton.setOnAction(e -> {
            String code = editNumber.getText();
            String name = editName.getText();
            String note = editNote.getText();
            String price = editPrice.getText();
            String genre = editGenre.getSelectionModel().getSelectedItem();
            String unit = editUnit.getSelectionModel().getSelectedItem();
            if (!name.isEmpty() & !note.isEmpty() & !price.isEmpty() & genre != null &
                    unit != null & editFile != null) {
                try (InputStream inputStream = new FileInputStream(editFile)) {
                    Food food = new Food(code, name, note, genre, unit, Double.parseDouble(price), new Image(inputStream));
                    if (DAO.removeFood(code) & DAO.addFood(food, editFile)) {
                        FoodContainer.removeFood(FoodContainer.searchByCode(code));
                        FoodContainer.addFood(food);
                        editStatus.setText("Амжилттай шинчлэгдлээ");
                    } else
                        editStatus.setText("Код давхардсан байна");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else editStatus.setText("Аль нэг талбарын утга хоосон байна");
        });

        reportList.forEach(e -> e.setOnAction(event -> {
            DatePicker startDate = new DatePicker();
            DatePicker endDate = new DatePicker();
            Label startLbl = new Label("Эхлэх огноо: ");
            Label endLbl = new Label("Дуусах огноо: ");
            HBox hBox1 = new HBox(startLbl, startDate);
            hBox1.setSpacing(10);
            hBox1.setAlignment(Pos.CENTER);
            HBox hBox2 = new HBox(endLbl, endDate);
            hBox2.setSpacing(10);
            hBox2.setAlignment(Pos.CENTER);
            Button button = new Button("ХАРАХ");
            button.setOnAction(be -> {
                if (startDate.getValue() != null & endDate.getValue() != null) {
                    new Report().showReport(e.getText(), startDate.getValue(), endDate.getValue());
                }
            });
            VBox root = new VBox(hBox1, hBox2, button);
            root.setSpacing(10);
            root.setAlignment(Pos.CENTER);
            Scene scene = new Scene(root, 400, 200);
            Stage stage = new Stage();
            stage.setTitle("Огноо сонох");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }));

        addButton.setOnAction(e -> {
            String code = addCode.getText();
            String name = addName.getText();
            String note = addNote.getText();
            String price = addPrice.getText();
            String genre = addGenre.getSelectionModel().getSelectedItem();
            String unit = addUnit.getSelectionModel().getSelectedItem();
            if (!name.isEmpty() & !note.isEmpty() & !price.isEmpty() & genre != null &
                    unit != null & addfile != null) {
                try (InputStream inputStream = new FileInputStream(addfile)) {
                    Food food = new Food(code, name, note, genre, unit, Double.parseDouble(price), new Image(inputStream));
                    if (DAO.addFood(food, addfile)) {
                        FoodContainer.addFood(food);
                        addStatus.setText("Амжилттай нэмэгдлээ");
                    } else
                        addStatus.setText("Код давхардсан байна");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else addStatus.setText("Аль нэг талбарын утга хоосон байна");
        });

        editChoose.setOnAction(e -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            configuringFileChooser(fileChooser);
            editFile = fileChooser.showOpenDialog(stage);
        });

        addChoose.setOnAction(e -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            configuringFileChooser(fileChooser);
            addfile = fileChooser.showOpenDialog(stage);
        });

        minusButton.setOnAction(e -> {
            if (!minusNumber.getText().isEmpty()) {
                if (DAO.removeFood(minusNumber.getText())) {
                    FoodContainer.removeFood(FoodContainer.searchByCode(minusNumber.getText()));
                    minusStatus.setText("Амжилттай хасагдлаа");
                } else {
                    minusStatus.setText("Дугаар буруу байна");
                }
            } else
                minusStatus.setText("Текст талбар хоосон байна");
        });

        notification.setOnAction(e -> {
            if (notification.isSelected()) {
                Server.setStatus(true);
                notificationIP.setDisable(false);
                notificationPort.setDisable(false);
                notificationButton.setDisable(false);
            } else {
                Server.setStatus(false);
                notificationIP.setDisable(true);
                notificationPort.setDisable(true);
                notificationButton.setDisable(true);
            }
            DAO.updateSettings();
        });

        notificationButton.setOnAction(e -> {
            if (!notificationPort.getText().isEmpty() & !notificationIP.getText().isEmpty()) {
                Server.setAddress(notificationIP.getText());
                Server.setPort(Integer.parseInt(notificationPort.getText()));
                DAO.updateSettings();
                notificationStatus.setText("Амжилттай");
            }
        });

        ebarimtButton.setOnAction(e -> {
            if (!ebarimtNumber.getText().isEmpty()) {
                String date = DAO.getDate(ebarimtNumber.getText());
                JSONObject jo = new JSONObject();
                jo.put("returnBillId", ebarimtNumber.getText());
                jo.put("date", date);
                String result = BridgePosAPI.returnBill(jo.toString());
                JSONObject res = new JSONObject(result);
                if (res.getBoolean("success")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Амжилттай буцаагдлаа");
                    alert.setContentText(null);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Баримт буцаахад алдаа гараа");
                    alert.setContentText(res.getString("message"));
                    alert.showAndWait();
                }
            }
        });

        ebarimtSubmit.setOnAction(e -> {
            String result = BridgePosAPI.sendData();
            JSONObject jo = new JSONObject(result);
            if (jo.getBoolean("success")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Амжилттай илгээгдлээ");
                alert.setContentText(null);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Баримт илгээхэд алдаа гараа");
                alert.setContentText(jo.getString("message"));
                alert.showAndWait();
            }
        });

        exit.setOnAction(event -> {
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
    }

    private void configuringFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select Pictures");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
    }

    private void settingInit() {
        if (Server.getStatus()) {
            notification.setSelected(true);
        } else {
            notification.setSelected(false);
            notificationIP.setDisable(true);
            notificationPort.setDisable(true);
            notificationButton.setDisable(true);
        }
        notificationIP.setText(Server.getAddress());
        notificationPort.setText(String.valueOf(Server.getPort()));
    }

    private void changeWindow(Pane addPane) {
        addPane.toFront();
    }
}
