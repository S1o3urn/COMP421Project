import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class HealthLog {

    private static Connection conn;
    private String action;
    private Scanner scanner;

    // Constructor
    public HealthLog(Scanner scanner) {
        this.scanner = scanner;
    }

    // Health Log menu. Parses user inputs and calls appropriate methods.
    public void healthLogMenu() {

        //exit for healthLog menu
        boolean status = true;

        while (status) {
            System.out.println("\n" + Ressources.username + "'s Health Log");
            System.out.println("-h or help for available commands in HealthLog");
            System.out.println("Enter command:");

            action = scanner.nextLine();

            // parse input
            List<String> argumentsList = Arrays.asList(action.split(" "));
            ListIterator<String> iterator = argumentsList.listIterator();

            // Loop through arguments list and populate attributes.
                String argument = iterator.next();
                switch(argument) {
                    case "-h":
                    case "help":
                        displayAvailableCommands();
                        break;

                    case "-g":
                    case "generateGraph":
                        String specifiedGraph = "";
                        try{
                            specifiedGraph = iterator.next();
                        } catch (NoSuchElementException e) {
                            e.getMessage();
                        }
                        switch(specifiedGraph) {
                            case "-w":
                            case "weight":
                                fetchGraphData(1);
                                break;

                            case "-h":
                            case "height":
                                fetchGraphData(2);
                                break;

                            case "-b":
                            case "bmi":
                                fetchGraphData(3);
                                break;
                            default:
                                System.out.println("ERROR Input Invalid. Please try again.");
                        }
                        break;

                    case "-a":
                    case "addPlot":

                        //Insert data
                        conn = Ressources.connectPSQL();
                        try (PreparedStatement statement = conn.prepareStatement(Ressources.insertHealthLogRecordSQL)) {

                            String date = iterator.next();
                            String weight = iterator.next();
                            String height = iterator.next();

                            // Allows for optional sex input
                            String sex;
                            try{
                                sex = iterator.next();
                            } catch(Exception e) {
                                sex = fetchSex();
                            }

                            statement.setString(1, Ressources.username);
                            statement.setDate(2, Date.valueOf(date));
                            statement.setInt(3, Integer.parseInt(weight));
                            statement.setInt(4, Integer.parseInt(height));
                            statement.setString(5, sex);

                            statement.addBatch();
                            statement.executeBatch();

                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        } catch (NoSuchElementException ex) {
                            System.out.println(ex.getMessage());
                        }
                        conn = Ressources.closeConn(conn);

                    case "back":
                        //Relinquish control back to Menu
                        System.out.println("\n" + Ressources.username + "'s menu");
                        status = false;
                        break;

                    default:
                        System.out.println("ERROR COMMAND INVALID\t please try again.\n");
                        break;
                }
            }
        }

    // Fetch all data concerning a single user.
    private void fetchGraphData(int mode) {

        List<Date> log_date = new ArrayList<>();
        List<Integer> height = new ArrayList<>();
        List<Integer> weight = new ArrayList<>();
        String sex = "";

        // Fetch all user health data
        conn = Ressources.connectPSQL();
        try (PreparedStatement pst = conn.prepareStatement(Ressources.retrieveUserHealthLogsSQL)) {

            pst.setString(1, Ressources.username);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                // Fetch all columns
                log_date.add(rs.getDate(1));
                height.add(rs.getInt(2));
                weight.add(rs.getInt(3));
                sex = rs.getString(4);
            }

            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        }
        conn = Ressources.closeConn(conn);

        // Create desired graph
        plotGraph(mode, log_date, height, weight, sex);
    }

    // Return the last sex plot point data
    private String fetchSex() {

        String sex = "";

        conn = Ressources.connectPSQL();
        try (PreparedStatement pst = conn.prepareStatement(Ressources.retrieveSexSQL)) {

            pst.setString(1, Ressources.username);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                // Fetch all columns
                sex = rs.getString(1);
            }

            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        conn = Ressources.closeConn(conn);

        return sex;
    }

    // Plots graphs as jpeg for specified mode
    private void plotGraph(int mode, List<Date> log_date, List<Integer> height, List<Integer> weight, String sex) {
        // Weight over time graph of all log dates
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();

        int index;
        JFreeChart lineChartObject;
        int jpegWidth;
        int jpegHeight;
        File lineChart;

        CategoryPlot plot;
        CategoryAxis domainAxis;

        switch (mode) {

            // Weight
            case 1:
                index = 0;
                while (log_date.size() > index)
                {
                    line_chart_dataset.addValue( weight.get(index), "weight" , log_date.get(index) );
                    index++;
                }

                lineChartObject = ChartFactory.createLineChart(
                        "Weight over Log Dates","Log Date",
                        "Weight (kg)",
                        line_chart_dataset,PlotOrientation.VERTICAL,
                        true,true,false);

                plot = (CategoryPlot) lineChartObject.getPlot();
                domainAxis = plot.getDomainAxis();
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                // Change line width
                plot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));

                // Width proportional to number of plot points
                jpegWidth = 50 * weight.size();
                jpegHeight = 600;
                lineChart = new File( "Graphs/HealthGraphs/WeightGraph.jpeg" );
                System.out.println("Chart can be found in: " + System.getProperty("user.dir") + "Graphs/HealthGraphs/WeightGraph.jpeg");

                // Reset Index
                index = 0;

                try {
                    ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, jpegWidth ,jpegHeight);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchElementException ex) {
                    ex.getMessage();
                }
                break;

            // Height
            case 2:
                index = 0;
                while (log_date.size() > index)
                {
                    line_chart_dataset.addValue( height.get(index), "weight" , log_date.get(index) );
                    index++;
                }

                lineChartObject = ChartFactory.createLineChart(
                        "Height over Log Dates","Log Date",
                        "Height (cm)",
                        line_chart_dataset,PlotOrientation.VERTICAL,
                        true,true,false);

                plot = (CategoryPlot) lineChartObject.getPlot();
                domainAxis = plot.getDomainAxis();
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                // Change line width
                plot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));

                // Width proportional to number of plot points
                jpegWidth = 50 * height.size();
                jpegHeight = 1000;
                lineChart = new File( "Graphs/HealthGraphs/HeightGraph.jpeg" );
                System.out.println("Chart can be found in: " + System.getProperty("user.dir") + "Graphs/HealthGraphs/HeightGraph.jpeg");

                // Reset index
                index = 0;

                try {
                    ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, jpegWidth ,jpegHeight);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchElementException ex) {
                    ex.getMessage();
                }
                break;

            // BMI
            case 3:
                index = 0;
                while (log_date.size() > index)
                {
                    line_chart_dataset.addValue( weight.get(index)/Math.pow((height.get(index)/100),2), "bmi" , log_date.get(index) );
                    index++;
                }

                lineChartObject = ChartFactory.createLineChart(
                        "bmi over Log Dates","Log Date",
                        "Bmi (kg/m*m)",
                        line_chart_dataset,PlotOrientation.VERTICAL,
                        true,true,false);

                plot = (CategoryPlot) lineChartObject.getPlot();
                domainAxis = plot.getDomainAxis();
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

                // Change line width
                plot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));

                // Width proportional to number of plot points
                jpegWidth = 50 * height.size();
                jpegHeight = 1000;
                lineChart = new File( "Graphs/HealthGraphs/BmiGraph.jpeg" );
                System.out.println("Chart can be found in: " + System.getProperty("user.dir") + "Graphs/HealthGraphs/BmiGraph.jpeg");

                // Reset index
                index = 0;

                try {
                    ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, jpegWidth ,jpegHeight);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchElementException ex) {
                    ex.getMessage();
                }
                break;
        }
    }

    // Use help
    private void displayAvailableCommands() {
        System.out.println("Available actions in HealthLog:");
        System.out.println("[-g|generateGraph] mode\t(mode: [-w|weight],[-h|height],[-b|bmi])");
        System.out.println("[-a|addLog] date weight height sex(optional)\t(date: [YYYY-MM-DD])\t weight: kg\theight: cm\t sex: [female|male]");
        System.out.println("back");
    }
}
