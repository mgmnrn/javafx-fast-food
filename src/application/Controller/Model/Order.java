package application.Controller.Model;

import javafx.beans.property.SimpleStringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Order {
    private static SimpleStringProperty cashAmount = new SimpleStringProperty();
    private static double vat;
    private static double amount;
    private static double nonCashAmount;
    private static double cityTax;
    private static int districtCode;
    private static String customerNo;
    private static String qrData;
    private static String billId;
    private static String lottery;
    private static String date;
    private static String dateTime;
    private static int billType;
    private static ArrayList<OrderItem> stock;

    public static void setQrData(String qr) {
        qrData = qr;
    }

    public static void setBillId(String bID) {
        billId = bID;
    }

    public static void setDateTime(String date) {
        dateTime = date;
    }

    public static void setLottery(String lot) {
        lottery = lot;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        Order.date = date;
    }

    public static double getAmount() {
        return amount;
    }

    public static double getVat() {
        return vat;
    }

    public static String getQrData() {
        return qrData;
    }

    public static String getBillId() {
        return billId;
    }

    public static String getLottery() {
        return lottery;
    }

    public static String getDateTime() {
        return dateTime;
    }

    public static ArrayList<OrderItem> getStock() {
        return stock;
    }

    public static double getCashAmount() {
        String[] strings = cashAmount.get().split("₮");
        return Double.parseDouble(strings[0]);
    }

    public static SimpleStringProperty cashAmountPropery() {
        return cashAmount;
    }

    public static void addItem(OrderItem orderItem) {
        String[] strings = cashAmount.get().split("₮");
        double old = Double.parseDouble(strings[0]);
        cashAmount.set((old + orderItem.getTotalAmount()) + "₮");
        stock.add(orderItem);
    }

    public static void removeItem(OrderItem orderItem) {
        String[] strings = cashAmount.get().split("₮");
        double old = Double.parseDouble(strings[0]);
        cashAmount.set((old - orderItem.getTotalAmount()) + "₮");
        stock.remove(orderItem);
    }

    public static void defaultOrder() {
        cashAmount.set(String.valueOf(0));
        vat = 0.0;
        amount = 0.0;
        nonCashAmount = 0.0;
        cityTax = 1.0;
        districtCode = 25;
        customerNo = "";
        qrData = "";
        billId = "";
        lottery = "";
        dateTime = "";
        billType = 1;
        stock = new ArrayList<>();
    }

    public static JSONObject toJSON() {
        String[] strings = cashAmount.get().split("₮");
        double old = Double.parseDouble(strings[0]);
        vat = old * 0.1;
        nonCashAmount = vat;
        amount = old + vat;
        JSONArray stockArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            stock.forEach(e -> {
                JSONObject stockObject = new JSONObject();
                stockObject.put("code", e.getCode());
                stockObject.put("name", e.getName());
                stockObject.put("measureUnit", e.getMeasureUnit());
                stockObject.put("qty", String.format("%.2f", Double.valueOf(e.getQuantity())));
                stockObject.put("unitPrice", String.format("%.2f", e.getPrice()));
                stockObject.put("totalAmount", String.format("%.2f", e.getTotalAmount()));
                stockObject.put("cityTax", "1.00");
                stockObject.put("vat", String.format("%.2f", e.getTotalAmount() * 0.1));
                stockArray.put(stockObject);
            });
            jsonObject.put("amount", String.format("%.2f", amount));
            jsonObject.put("vat", String.format("%.2f", vat));
            jsonObject.put("cashAmount", String.format("%.2f", old));
            jsonObject.put("nonCashAmount", String.format("%.2f", nonCashAmount));
            jsonObject.put("cityTax", String.format("%.2f", cityTax));
            jsonObject.put("districtCode", String.valueOf(districtCode));
            jsonObject.put("customerNo", customerNo);
            jsonObject.put("billType", String.valueOf(billType));
            jsonObject.put("stocks", stockArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
