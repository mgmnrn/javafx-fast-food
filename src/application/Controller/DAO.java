package application.Controller;

import application.Controller.Model.Food;
import application.Controller.Model.FoodContainer;
import application.Controller.Model.Order;
import application.Controller.Model.Server;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.*;

public class DAO {
    private static Connection conn = DBConnection.getConnection();

    public static void init() {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM 'food'");
            while (rs.next()) {
                InputStream inputStream = rs.getBinaryStream(7);
                Image image = new Image(inputStream);
                Food food = new Food(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), image);
                FoodContainer.addFood(food);
            }
            ResultSet rs2 = statement.executeQuery("SELECT * FROM 'settings' WHERE name = 'notification'");
            while (rs2.next()) {
                Server.setStatus(Boolean.parseBoolean(rs2.getString("status")));
                Server.setAddress(rs2.getString("ip"));
                Server.setPort(rs2.getInt("port"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void writeOrder() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("insert into 'order' values ('" + Order.getBillId() + "', '" + Order.getCashAmount() + "', '" + Order.getVat() + "', '" + Order.getAmount() + "', '" + Order.getDate() + "', '" + Order.getDateTime() + "', '" + Order.getLottery() + "', '" + Order.getQrData() + "')");
            Order.getStock().forEach(e -> {
                try {
                    statement.executeUpdate("insert into 'order_item' values ('" + Order.getBillId() + "', '" + e.getCode() + "', '" + e.getQuantity() + "', '" + e.getTotalAmount() + "') ");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getDate(String text) {
        String result = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT date_time FROM 'order' WHERE billId ='" + text + "'");
            while (rs.next()) {
                result = rs.getString("date_time");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void updateSettings() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE settings SET status ='" + Server.getStatus() + "', ip='" + Server.getAddress() + "', port='" + Server.getPort() + "' WHERE name = 'notification' ");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Boolean addFood(Food food, File file) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
            }
            PreparedStatement ps = conn.prepareStatement("insert into 'food' values ('" + food.getCode() + "', '" + food.getName() + "','" + food.getTitle() + "','" + food.getGenre() + "','" + food.getMeasureUnit() + "','" + food.getPrice() + "', ?) ");
            ps.setBytes(1, bos.toByteArray());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean removeFood(String searchByCode) {
        try {
            Statement statement = conn.createStatement();
            int resullt = statement.executeUpdate("DELETE FROM 'food' WHERE code ='" + searchByCode + "' ");
            if (resullt == 0)
                return false;
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
