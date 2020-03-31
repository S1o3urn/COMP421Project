import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private String action;
    private Connection conn;

    public Menu(Connection conn) {

        System.out.println("Welcome Back " + Ressources.username + "!\n");
        this.conn = conn;
    }


    public void mainMenuCommandParser() {

        System.out.println("-h or help for available commands");
        System.out.println("Enter command:");

        // Scanner to parse input
        Scanner scanner = new Scanner(System.in);
        action = scanner.nextLine();

        switch (action) {
            case "-h":
            case "help":
                displayAvailableCommands();
                mainMenuCommandParser();

            case "cart":
                MyCart cart = new MyCart(conn, scanner);
                cart.cartMenu();
                mainMenuCommandParser();

            case "healthLog":
                HealthLog log = new HealthLog(conn, scanner);
                log.healthLogMenu();
                mainMenuCommandParser();

            case "exit" :
                exit();

            default:
                System.out.println("ERROR COMMAND INVALID\t please try again.");
                mainMenuCommandParser();
        }
    }

    private void displayAvailableCommands() {
        System.out.println("Available actions:\n");
        System.out.println("1.command1\t2.command2\t3.command3\t4.command4\thealthLog\texit\n");
    }

    private void exit() {
        System.out.println("Logging off...");
        System.exit(0);
    }
}
