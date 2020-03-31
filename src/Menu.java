import java.util.Scanner;

public class Menu {

    private static String action;

    public Menu() {
        System.out.println("Welcome Back " + Ressources.username + "!\n");

        commandParser();
    }

    private static void commandParser() {

        System.out.println("-h or help for available commands\n");
        System.out.println("Enter command:\n");

        // Scanner to parse input
        Scanner scanner = new Scanner(System.in);
        action = scanner.nextLine();

        switch (action) {
            case "-h":
            case "help":
                displayAvailableCommands();
                commandParser();

            case "exit" :
                exit();

            default:
                System.out.println("ERROR COMMAND INVALID\t please try again.");
                commandParser();
        }
    }

    private static void displayAvailableCommands() {
        System.out.println("Available actions\n");
        System.out.println("1.command1\t2.command2\t3.command3\t4.command4\t 5.command5\texit\n");
    }

    private static void exit() {
        System.out.println("Logging off...");
        System.exit(0);
    }
}
