package application.Controller;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import javax.swing.*;
import application.Controller.Model.Order;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

public class Report extends JFrame {
    public void showReport(String reportName, LocalDate startDate, LocalDate endDate){
        try {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("startDate", startDate.toString());
            parameters.put("endDate", endDate.toString());
            Connection conn = DBConnection.getConnection();
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("View/"+reportName+".jrxml"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(1920, 1080);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void eBarimt(String reportName){
        try {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("order_id", Order.getBillId());
            Connection conn = DBConnection.getConnection();
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("View/"+reportName+".jrxml"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(600, 850);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}