import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private String action;
    private Connection conn;
    private Scanner scanner;
    private MyCart cart;
    private HealthLog log;

    public Menu(Connection conn) {

        System.out.println("\nWelcome Back " + Ressources.username + "!");
        this.conn = conn;
        this.scanner = new Scanner(System.in);
        this.cart = new MyCart(conn, scanner);
        this.log = new HealthLog(conn, scanner);
        mainMenuCommandParser();
    }


    private void mainMenuCommandParser() {

        while (true) {
            System.out.println("\n-h or help for available commands in Menu");
            System.out.println("Enter command:");

            // Scanner to parse input
            action = scanner.nextLine();

            switch (action) {
                case "-h":
                case "help":
                    displayAvailableCommands();
                    break;

                case "-c":
                case "cart":
                    cart.cartMenu();
                    break;

                case "-hl":
                case "healthLog":
                    log.healthLogMenu();
                    break;

                case "exit" :
                    exit();

                default:
                    System.out.println("ERROR COMMAND INVALID\t please try again.\n");
                    break;
            }
        }
    }

    private void displayAvailableCommands() {
        System.out.println("\nAvailable actions in Menu:");
        System.out.println("cart\t2.command2\t3.command3\t4.command4\thealthLog\texit");
    }

    private void exit() {
        System.out.println("Logging off...");
        System.exit(0);
    }
}
