package application.Controller;

import application.Controller.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;

public class FoodController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane scrollPn;

    @FXML
    private ScrollPane scrolCart;

    @FXML
    private Label chicken;

    @FXML
    private Label pizza;

    @FXML
    private Label sides;

    @FXML
    private Label drink;

    @FXML
    private Label burger;

    @FXML
    private ImageView main;

    @FXML
    private Button confirm;

    private static Map<String, List<VBox>> VBoxFood = new HashMap<>();
    private static VBox cart;
    private static Button myButton;
    private ImageView oldGraphic;
    private VBox tempVBox;

    @FXML
    void scrollPnDragDone() {
        rootPane.getChildren().remove(oldGraphic);
    }

    @FXML
    void scrollPnDragOver(DragEvent event) {
        oldGraphic.setTranslateX(event.getSceneX());
        oldGraphic.setTranslateY(event.getSceneY());
    }

    @FXML
    void scrolCartDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            rootPane.getChildren().remove(oldGraphic);
            addCartItem(Objects.requireNonNull(FoodContainer.searchByCode(db.getString())), tempVBox);
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    void scrolCartDragExited() {
        scrolCart.setStyle("-fx-border-color:  #67daff");
    }

    @FXML
    void scrolCartDragOver(DragEvent event) {
        if (event.getGestureSource() != scrolCart && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            scrolCart.setStyle("-fx-border-color: green; -fx-border-width: 3px; -fx-background-insets: 0, 1 ;");
        }
        event.consume();
    }

    @FXML
    void confirmAction() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("View/payment.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setTitle("Төлбөр төлөх");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        Order.defaultOrder();
        createCartLayout();
        myButton = confirm;
        myButton.textProperty().bindBidirectional(Order.cashAmountPropery());
        myButton.setDisable(true);
        TilePane tilePane = createTilePane();
        scrollPn.setContent(tilePane);
        scrollPn.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPn.setStyle("-fx-background-color: green");
        scrolCart.setContent(cart);
        scrolCart.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ObservableList<Label> lblList = createLabelList();
        lblList.forEach(lbl -> {
            List<VBox> VBoxes = new ArrayList<>();
            FoodContainer.getFoods().forEach(fo -> {
                if (fo.getGenre().equals(lbl.getText())) {
                    VBoxes.add(createCard(fo));
                }
            });
            VBoxFood.put(lbl.getText(), VBoxes);
        });

        chicken.setStyle("-fx-text-fill: red");
        tilePane.getChildren().setAll(VBoxFood.get(chicken.getText()));

        lblList.forEach(lbl -> lbl.setOnMouseClicked(e -> {
            if (lbl.getStyle().equals("")) {
                defaultStyle(lblList);
                lbl.setStyle("-fx-text-fill: red");
                tilePane.getChildren().setAll(VBoxFood.get(lbl.getText()));
            }
        }));

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
    }

    private VBox createCard(Food fo) {
        ImageView imageView = new ImageView(fo.getImage());
        imageView.setFitHeight(150);
        imageView.setFitWidth(250);
        Label name = new Label(fo.getName());
        Label title = new Label(fo.getTitle());
        Button button = new Button("Сонгох " + fo.getPrice() + "₮");
        button.setPrefSize(250.0, 50.0);
        button.setStyle("-fx-background-color: #8aacc8");
        VBox vBoxs = new VBox(name, title, button);
        vBoxs.setPadding(new Insets(10, 10, 10, 10));
        VBox vBox = new VBox(imageView, vBoxs);
        button.setOnAction(event -> addCartItem(fo, vBox));
        vBox.setSpacing(5);
        vBox.setMaxSize(250.0, 150.0);
        vBox.setStyle("-fx-background-color: #67daff");
        vBox.setOnDragDetected(event -> {
            tempVBox = vBox;
            onDragDetected(event, imageView, fo, vBox);
        });
        return vBox;
    }

    private TilePane createTilePane() {
        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        TilePane tilePane = new TilePane();
        tilePane.setVgap(20);
        tilePane.setHgap(20);
        tilePane.setPadding(new Insets(25));
        tilePane.setMaxWidth(rectangle2D.getWidth() / 1.5);
        return tilePane;
    }

    private void createCartLayout() {
        cart = new VBox();
        cart.setStyle("-fx-background-color: #bbdefb;");
        cart.setPadding(new Insets(10));
        cart.setSpacing(5);
    }

    private ObservableList<Label> createLabelList() {
        return FXCollections.observableArrayList(
                chicken, burger, pizza, sides, drink
        );
    }

    private void addCartItem(Food fo, VBox vBox) {
        OrderItem orderItem = new OrderItem(fo.getCode(), fo.getName(), fo.getTitle(), fo.getGenre(), fo.getMeasureUnit(), fo.getPrice(), fo.getImage(), 1);
        Order.addItem(orderItem);
        Label price = new Label(fo.getPrice() + "₮");
        Label cartName = new Label(fo.getName());
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spinner.setValueFactory(valueFactory);
        spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        spinner.setPrefWidth(70);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            price.setText(String.valueOf(fo.getPrice() * newValue));
            Order.removeItem(orderItem);
            orderItem.setQuantity(newValue);
            Order.addItem(orderItem);
        });
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        Button btnRemove = new Button("Устгах");
        btnRemove.setOnAction(events -> {
            vBox.setDisable(false);
            Order.removeItem(orderItem);
            cart.getChildren().remove(gridPane);
            if (cart.getChildren().isEmpty())
                confirm.setDisable(true);
        });
        gridPane.setPadding(new Insets(10));
        gridPane.add(cartName, 0, 0);
        gridPane.add(price, 1, 0);
        gridPane.add(spinner, 0, 1);
        gridPane.add(btnRemove, 1, 1);
        gridPane.setStyle("-fx-border-color: black; -fx-border-width: 0 0 1 0;");
        cart.getChildren().add(gridPane);
        vBox.setDisable(true);
        confirm.setDisable(false);
    }

    private void onDragDetected(MouseEvent event, ImageView imageView, Food fo, VBox vBox) {
        oldGraphic = new ImageView();
        oldGraphic.setImage(imageView.getImage());
        oldGraphic.setFitHeight(100);
        oldGraphic.setFitWidth(150);
        oldGraphic.setOpacity(0.5);
        rootPane.getChildren().add(oldGraphic);
        ClipboardContent content = new ClipboardContent();
        content.putString(fo.getCode());
        Dragboard db = vBox.startDragAndDrop(TransferMode.COPY);
        db.setContent(content);
        event.consume();
    }

    /**
     * Программыг анхны төлөвт оруулна.
     */
    static void defaultValues() {
        Order.defaultOrder();
        myButton.setDisable(true);
        cart.getChildren().clear();
        VBoxFood.forEach((k, v) -> v.forEach(e -> e.setDisable(false)));
    }

    private void defaultStyle(ObservableList<Label> obList) {
        for (Label lbl : obList) {
            lbl.setStyle("");
        }
    }
}