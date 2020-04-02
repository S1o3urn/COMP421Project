import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class HealthLog {

    private String action;
    private Connection conn;
    private Scanner scanner;

    public HealthLog(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void healthLogMenu() {

        boolean status = true;

        while (status) {
            System.out.println("\n" + Ressources.username + "'s Health Log");
            System.out.println("-h or help for available commands in HealthLog");
            System.out.println("Enter command:");

            action = scanner.nextLine();

            switch (action) {
                case "-h":
                case "help":
                    displayAvailableCommands();
                    break;

                case "-g":
                case "generateGraph":
                    plotGraph();
                    break;

                case "back":
                    //Relinquish control back to Menu
                    System.out.println("\n" + Ressources.username + "'s menu");
                    status = false;
                    break;

                default:
                    //TODO implement add log as special command with arugments that need parsing
                    System.out.println("ERROR COMMAND INVALID\t please try again.\n");
                    break;
            }
        }
    }

    //TODO get data as csv and plot with matplotlib, export as png to a file location or open directly
    private void plotGraph() {

        List<Date> log_date = new ArrayList<Date>();
        List<Integer> height = new ArrayList<Integer>();
        List<Integer> weight = new ArrayList<Integer>();
        String sex;

        // Fetch all user health data
        try (PreparedStatement pst = conn.prepareStatement(Ressources.retrieveUserHealthLogsSQL + Ressources.username + "'");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                // Fetch all columns
                log_date.add(rs.getDate(0));
                height.add(rs.getInt(1));
                weight.add(rs.getInt(2));
                sex = rs.getString(3);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        // Weight over time graph
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();

        int index = 0;
        while (log_date.size() > index)
        {
            line_chart_dataset.addValue( weight.get(index++), "weight" , log_date.get(index++) );
        }

        JFreeChart lineChartObject = ChartFactory.createLineChart(
                "Weight over Log Dates","Log Date",
                "Weight (kg)",
                line_chart_dataset,PlotOrientation.VERTICAL,
                true,true,false);
        int jpegWidth = 640;    /* Width of the image */
        int jpegHeight = 480;   /* Height of the image */
        File lineChart = new File( "Graphs/WeightGraph.jpeg" );
        try {
            ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, jpegWidth ,jpegHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void displayAvailableCommands() {
        System.out.println("Available actions in HealthLog:");
        System.out.println("generateGraph(-g)\taddLog(-a date weight)\tback");
    }
}

class HealthLogs {
    List<Date> log_date;
    List<Integer> height;
    List<Integer> weight;
    String sex;

    public HealthLogs(List<Date> log_dates, List<Integer> heights, List<Integer> weights, String sex){
        this.log_date = log_dates;
        this.height = heights;
        this.weight = weights;
        this.sex = sex ;
    }
}
