import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Chart {

    private static Connection conn;
    private String action;
    private Scanner scanner;

    // Constructor
    public Chart(Scanner scanner) {
        this.scanner = scanner;
    }

    public void chartMenu() {

        boolean status = true;

        while (status) {
            System.out.println("\n" + Ressources.username + "'s cart");
            System.out.println("-h or help for available commands");
            System.out.println("Enter command:");

            action = scanner.nextLine();

            // parse input
            List<String> argumentsList = Arrays.asList(action);
            ListIterator<String> iterator = argumentsList.listIterator();

            String argument = iterator.next();
            int count;

            switch (argument) {
                case "-h":
                case "help":
                    displayAvailableCommands();
                    break;

                case "1":
                case "top_ingredients":
                    if (!iterator.hasNext()) {
                        plotTopIngredientsChart(10);
                    } else {
                        try{
                            count = Integer.parseInt(iterator.next());
                            plotTopIngredientsChart(count);
                        } catch(IllegalArgumentException e){
                            System.out.println("COUNT INVALID\t please try again.\n");
                        }
                    }
                    break;

                case "2":
                case "top_discount_items":
                    if (!iterator.hasNext()) {
                        plotTopDiscountItemsChart(10);
                    } else {
                        try{
                            count = Integer.parseInt(iterator.next());
                            plotTopDiscountItemsChart(count);
                        } catch(IllegalArgumentException e){
                            System.out.println("COUNT INVALID\t please try again.\n");
                        }
                    }
                    break;

                case "3":
                case "account_spending":
                    if (!iterator.hasNext()) {
                        plotAccountSpendingsChart(10);
                    } else {
                        try{
                            count = Integer.parseInt(iterator.next());
                            plotAccountSpendingsChart(count);
                        } catch(IllegalArgumentException e){
                            System.out.println("COUNT INVALID\t please try again.\n");
                        }
                    }
                    break;

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

    private void plotAccountSpendingsChart(int count) {

        List<String> usernames = new ArrayList<>();
        List<Integer> amount = new ArrayList<>();

        // Fetch all user health data
        conn = Ressources.connectPSQL();
        try (PreparedStatement pst = conn.prepareStatement(Ressources.callAccountsSpendingsStoredProcedureSQL)) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                // Fetch all columns
                usernames.add(rs.getString(1));
                amount.add(rs.getInt(2));
            }

            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        conn = Ressources.closeConn(conn);

        DefaultCategoryDataset chart_dataset = new DefaultCategoryDataset();

        int index = 0;
        JFreeChart lineChartObject;
        int jpegWidth;
        int jpegHeight;
        File lineChart;
        int countIndex = count;

        while (countIndex > 0)
        {
            try{
                chart_dataset.addValue( amount.get(index++), "Users" , usernames.get(index++) );
                countIndex--;
            } catch (Exception e) {
                // Count bigger than dataset results
                break;
            }
        }

        lineChartObject = ChartFactory.createBarChart(
                "Top " + count + " account spendings","Usernames",
                "Amount($)",
                chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        // Width proportional to number of plot points
        jpegWidth = 30 * count;
        jpegHeight = 600;
        lineChart = new File( "Graphs/Management/AccountsSpendingsGraph.jpeg" );
        System.out.println("Chart can be found in: " + System.getProperty("user.dir") + "Graphs/Management/AccountsSpendingsGraph.jpeg");
        File testDir = new File (System.getProperty("user.dir") + "Graphs/Management");
        // Reset Index
        index = 0;

        try {
            if(testDir.exists()) {
                ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, jpegWidth, jpegHeight);
            }else{
                new File("Graphs/Management").mkdirs();
                ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, jpegWidth, jpegHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void plotTopDiscountItemsChart(int count) {
        List<String> consumable_id = new ArrayList<>();
        List<Integer> sum = new ArrayList<>();

        // Fetch all user health data
        conn = Ressources.connectPSQL();
        try (PreparedStatement pst = conn.prepareStatement(Ressources.callTopDiscountedItemsStoredProcedureSQL)) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                // Fetch all columns
                consumable_id.add(rs.getString(1));
                sum.add(rs.getInt(2));
            }

            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        conn = Ressources.closeConn(conn);

        DefaultCategoryDataset chart_dataset = new DefaultCategoryDataset();

        int index = 0;
        JFreeChart lineChartObject;
        int jpegWidth;
        int jpegHeight;
        File lineChart;
        int countIndex = count;

        while (countIndex > 0)
        {
            try{
                chart_dataset.addValue( sum.get(index++), "Users" , consumable_id.get(index++) );
                countIndex--;
            } catch (Exception e) {
                // Count bigger than dataset results
                break;
            }
        }

        lineChartObject = ChartFactory.createBarChart(
                "Top " + count + " popular discounted items","Consumable_ids",
                "Counts",
                chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        // Width proportional to number of plot points
        jpegWidth = 30 * count;
        jpegHeight = 600;
        lineChart = new File( "Graphs/Management/TopDiscountedItemsGraph.jpeg" );
        System.out.println("Chart can be found in: " + System.getProperty("user.dir") + "\\Graphs\\Management\\TopDiscountedItemsGraph.jpeg" );
        File testDir = new File (System.getProperty("user.dir") + "Graphs/Management");
        // Reset Index
        index = 0;

        try {
            if(testDir.exists()) {
                ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, jpegWidth, jpegHeight);
            }else{
                new File("Graphs/Management").mkdirs();
                ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, jpegWidth, jpegHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void plotTopIngredientsChart(int count) {

        List<String> ingredientName = new ArrayList<>();
        List<Integer> qty = new ArrayList<>();

        // Fetch all user health data
        conn = Ressources.connectPSQL();
        try (PreparedStatement pst = conn.prepareStatement(Ressources.callTopIngredientsStoredProcedureSQL)) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                // Fetch all columns
                ingredientName.add(rs.getString(1));
                qty.add(rs.getInt(2));
            }

            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        conn = Ressources.closeConn(conn);

        DefaultCategoryDataset chart_dataset = new DefaultCategoryDataset();

        int index = 0;
        JFreeChart lineChartObject;
        int jpegWidth;
        int jpegHeight;
        File lineChart;
        int countIndex = count;

        while (countIndex > 0)
        {
            try{
                chart_dataset.addValue( qty.get(index++), "Users" , ingredientName.get(index++) );
                countIndex--;
            } catch (Exception e) {
                // Count bigger than dataset results
                break;
            }
        }

        lineChartObject = ChartFactory.createBarChart(
                "Top " + count + " ingredients","ingredient_name",
                "Quantity",
                chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        // Width proportional to number of plot points
        jpegWidth = 30 * count;
        jpegHeight = 600;
        lineChart = new File( "Graphs/Management/TopIngredientsGraph.jpeg" );
        System.out.println("Chart can be found in: " + System.getProperty("user.dir") + "\\Graphs\\Management\\TopIngredientsGraph.jpeg");
        File testDir = new File (System.getProperty("user.dir") + "Graphs/Management");
        // Reset Index
        index = 0;

        try {
            if(testDir.exists()) {
                ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, jpegWidth, jpegHeight);
            }else{
                new File("Graphs/Management").mkdirs();
                ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, jpegWidth, jpegHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // User help
    private void displayAvailableCommands() {
        System.out.println("\nAvailable actions in Cart:");
        System.out.println("[top_ingredients | 1]\t[top_discount_items | 2]\t[account_spending | 3]\tback");
    }
}
