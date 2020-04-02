import java.sql.Connection;
import java.util.Scanner;

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
    }

    private void displayAvailableCommands() {
        System.out.println("Available actions in HealthLog:");
        System.out.println("generateGraph(-g)\taddLog(-a date weight)\tback");
    }
}
