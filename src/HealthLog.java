import java.sql.Connection;
import java.util.Scanner;

public class HealthLog {

    private String action;
    private Connection conn;
    private Scanner scanner;

    public HealthLog(Connection conn, Scanner scanner) {
        System.out.println(Ressources.username + "'s Health Log\n");
        this.conn = conn;
        this.scanner = scanner;
    }

    public void healthLogMenu() {

        System.out.println("-h or help for available commands");
        System.out.println("Enter command:");

        action = scanner.nextLine();

        switch (action) {
            case "-h":
            case "help":
                displayAvailableCommands();
                healthLogMenu();

            case "-g":
            case "generateGraph":
                plotGraph();
                healthLogMenu();

            case "back":
                //Do nothing, relinquish control back to Menu

            default:
                //TODO implement add log as special command with arugments that need parsing
                System.out.println("ERROR COMMAND INVALID\t please try again.");
                healthLogMenu();
        }
    }

    //TODO get data as csv and plot with matplotlib, export as png to a file location or open directly
    private void plotGraph() {
    }

    private void displayAvailableCommands() {
        System.out.println("Available actions:\n");
        System.out.println("generateGraph(-g)\taddLog(-a date weight)back\n");
    }
}
