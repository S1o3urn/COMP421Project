import java.sql.Connection;
import java.util.Scanner;

public class MyCart {

    private String action;
    private Connection conn;
    private Scanner scanner;

    public MyCart(Connection conn, Scanner scanner) {
        System.out.println(Ressources.username + "'s cart\n");
        this.conn = conn;
        this.scanner = scanner;
    }

    public void cartMenu() {

        System.out.println("-h or help for available commands");
        System.out.println("Enter command:");

        action = scanner.nextLine();

        switch (action) {
            case "-h":
            case "help":
                displayAvailableCommands();
                cartMenu();

            case "-v":
            case "viewCart":
                listItems();
                cartMenu();

            case "-p":
            case "purchase":
                purchaseCart();
                cartMenu();

            case "back":
                //Do nothing, relinquish control back to Menu

            default:
                //TODO implement modify cart as special command with arugments that need parsing
                System.out.println("ERROR COMMAND INVALID\t please try again.");
                cartMenu();
        }
    }

    //TODO clear all items in cart and decrement item counts
    private void purchaseCart() {
    }

    //TODO display all items in cart
    private void listItems() {
    }

    private void displayAvailableCommands() {
        System.out.println("Available actions:\n");
        System.out.println("viewCart(-v)\tmodifyCart(-m itemId qty)\tpurchase(-p)\tback\n");
    }
}
