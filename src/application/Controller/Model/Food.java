package application.Controller.Model;

import javafx.scene.image.Image;

public class Food {
    private String code;
    private String name;
    private String title;
    private String genre;
    private String measureUnit;
    private Double price;
    private Image image;

    public Food(String code, String name, String title, String genre, String measureUnit, Double price, Image image) {
        this.code = code;
        this.name = name;
        this.title = title;
        this.genre = genre;
        this.measureUnit = measureUnit;
        this.price = price;
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public Double getPrice() {
        return price;
    }

    public Image getImage() {
        return image;
    }
}
