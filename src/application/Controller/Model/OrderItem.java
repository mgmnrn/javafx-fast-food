package application.Controller.Model;

import javafx.scene.image.Image;

public class OrderItem extends Food {
    private Double totalAmount;
    private Integer quantity;

    public OrderItem(String code, String name, String title, String genre, String measureUnit, Double price, Image image, Integer quantity) {
        super(code, name, title, genre, measureUnit, price, image);
        this.totalAmount = price * quantity;
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        totalAmount = super.getPrice() * quantity;
    }
}
