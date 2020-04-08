import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private String action;
    private Connection conn;
    private Scanner scanner;
    private MyCart cart;
    private HealthLog log;
    private Chart chart;

    public Menu(Connection conn) {

        System.out.println("\nWelcome Back " + Ressources.username + "!");
        System.out.println("Account status: " + Ressources.acocunt_type);
        this.conn = conn;
        this.scanner = new Scanner(System.in);
        this.cart = new MyCart(conn, scanner);
        this.log = new HealthLog(conn, scanner);
        this.chart = new Chart(conn, scanner);
        mainMenuCommandParser();
    }


    private void mainMenuCommandParser() {

        if(Ressources.acocunt_type.equals("admin")) {

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

                    case "-c":
                    case "chart":
                        chart.chartMenu();
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
            System.out.println("[-c|chart]\texit\t[-h|help]");
        } else{
            System.out.println("[-c|cart]\t[-hl|healthLog]\texit\t[-h|help]");
        }
    }

    private void exit() {
        System.out.println("Logging off...");
        System.exit(0);
    }
}
