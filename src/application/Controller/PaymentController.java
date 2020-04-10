package application.Controller;

import application.Controller.Model.Order;
import application.Controller.Model.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mn.mta.pos.exam.BridgePosAPI;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PaymentController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button btnPay;

    @FXML
    private ImageView close;

    @FXML
    private RadioButton rdBank;

    @FXML
    private RadioButton rdCart;

    @FXML
    private RadioButton rdQpay;

    @FXML
    void initialize() {
        close.setOnMouseClicked(e -> ((Stage) rootPane.getScene().getWindow()).close());
        ToggleGroup group = new ToggleGroup();
        rdBank.setToggleGroup(group);
        rdQpay.setToggleGroup(group);
        rdCart.setToggleGroup(group);

        btnPay.setOnMouseClicked(e -> {
            if (group.getSelectedToggle() != null) {
                String result = BridgePosAPI.put(Order.toJSON().toString());
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("success")) {
                    if (!jsonObject.getString("lottery").equals("")) {
                        Order.setQrData(jsonObject.getString("qrData"));
                        Order.setBillId(jsonObject.getString("billId"));
                        Order.setDateTime(jsonObject.getString("date"));
                        String[] regex = jsonObject.getString("date").split(" ");
                        Order.setDate(regex[0]);
                        Order.setLottery(jsonObject.getString("lottery"));
                        DAO.writeOrder();
                        if (Server.getStatus()) {
                            try (Socket socket = new Socket(Server.getAddress(), Server.getPort());
                                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                                dos.writeUTF(jsonObject.toString());
                            } catch (IOException ex) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Алдаа");
                                alert.setHeaderText("Server -тэй холбогдож чадсангүй");
                                alert.setContentText("Та сүлжээ болон ip хаяг, port -ийн дугаараа шалгана уу");
                                alert.showAndWait();
                            }
                        }
                        new Report().eBarimt("ebarimt");
                        FoodController.defaultValues();
                        ((Stage) rootPane.getScene().getWindow()).close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Алдаа");
                        alert.setHeaderText("Баримтыг бүртгэж чадсангүй");
                        alert.setContentText(jsonObject.getString("warningMsg"));
                        alert.setHeight(300);
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Алдаа");
                    alert.setHeaderText("Баримтыг бүртгэж чадсангүй");
                    alert.setContentText(jsonObject.getString("message"));
                    alert.showAndWait();
                }
            }
        });
    }
}