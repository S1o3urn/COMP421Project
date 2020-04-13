import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Menu {

    private static Connection conn;
    private String action;
    private Scanner scanner;
    private MyCart cart;
    private HealthLog log;
    private Chart chart;
    private Account account;

    public Menu() {

        System.out.println("\nWelcome Back " + Ressources.username + "!");
        System.out.println("Account status: " + Ressources.acocunt_type);
        this.scanner = new Scanner(System.in);
        this.cart = new MyCart(scanner);
        this.log = new HealthLog(scanner);
        this.chart = new Chart(scanner);
        this.account = new Account(scanner);
        mainMenuCommandParser();
    }

    // Handles user input on the main menu
    private void mainMenuCommandParser() {

        if(Ressources.acocunt_type.equals("admin")) {

            while (true) {
                System.out.println("\n-h or help for available commands in Menu");
                System.out.println("Enter command:");

                // Scanner to parse input
                action = scanner.nextLine();
                // parse input
                List<String> argumentsList = Arrays.asList(action.split(" "));

                ListIterator<String> iterator = argumentsList.listIterator();
                String argument = iterator.next();

                switch (argument) {
                    case "-h":
                    case "help":
                        displayAvailableCommands(Ressources.acocunt_type);
                        break;

                    case "-c":
                    case "chart":
                        chart.chartMenu();
                        break;

                    case "-ce":
                    case "checkExpiry":
                        conn = Ressources.connectPSQL();
                        try (PreparedStatement pst = conn.prepareStatement(Ressources.checkCloseToExpiringIngredients)) {

                            ResultSet rs = pst.executeQuery();

                            System.out.println("ingredient_id\t\t\tprice_per_unit");

                            while (rs.next()) {
                               System.out.println(rs.getString(1) + "\t\t\t" + rs.getInt(2));
                            }

                            rs.close();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                        conn = Ressources.closeConn(conn);
                        break;

                    case "-ad":
                    case "applyDiscount":
                        conn = Ressources.connectPSQL();
                        try (PreparedStatement pst = conn.prepareStatement(Ressources.applyDiscount)) {
                            Float discountValue = Float.parseFloat(iterator.next());
                            pst.setFloat(1, discountValue);
                            ResultSet rs = pst.executeQuery();

                            System.out.println("Action completed");

                            rs.close();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        } catch(NoSuchElementException ex) {
                            System.out.println("Have you forgotten an argument?");
                        }
                        conn = Ressources.closeConn(conn);
                        break;

                    case "exit":
                        exit();

                    default:
                        System.out.println("ERROR COMMAND INVALID\t please try again.\n");
                        break;
                }
            }
        } else if (Ressources.acocunt_type.equals("active")) {

            while (true) {
                System.out.println("\n-h or help for available commands in Menu");
                System.out.println("Enter command:");

                // Scanner to parse input
                action = scanner.nextLine();

                switch (action) {
                    case "-h":
                    case "help":
                        displayAvailableCommands(Ressources.acocunt_type);
                        break;

                    case "-a":
                    case "account" :
                        account.accountMenu();

                    case "-c":
                    case "cart":
                        cart.cartMenu();
                        break;

                    case "-hl":
                    case "healthLog":
                        log.healthLogMenu();
                        break;

                    case "exit":
                        exit();

                    default:
                        System.out.println("ERROR COMMAND INVALID\t please try again.\n");
                        break;
                }
            }
        }
            else {
            System.out.println("ERROR ACCOUNT TYPE: user's account might be disabled.");
            System.out.println("Shutting down application.");
        }
    }

    private void displayAvailableCommands(String mode) {
        System.out.println("\nAvailable actions in Menu:");
        if (mode.equals("admin")) {
            System.out.println("[-c|chart]\t[-ce|checkExpiry]\t[-ad|applyDiscount percentage]\texit\t[-h|help]");
        } else{
            System.out.println("[-c|cart]\t[-hl|healthLog]\texit\t[-h|help]");
        }
    }

    private void exit() {
        System.out.println("Logging off...");
        System.exit(0);
    }
}
