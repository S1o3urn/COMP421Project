import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MyCart {

    private String action;
    private Connection conn;
    private Scanner scanner;

    public MyCart(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void cartMenu() {

        boolean status = true;

        while (status) {
            System.out.println("\n" + Ressources.username + "'s cart");
            System.out.println("-h or help for available commands");
            System.out.println("Enter command:");

            action = scanner.nextLine();

            switch (action) {
                case "-h":
                case "help":
                    displayAvailableCommands();
                    break;

                case "-v":
                case "viewCart":
                    listItems();
                    break;

                case "-p":
                case "purchase":
                    purchaseCart();
                    break;

                case "back":
                    //Relinquish control back to Menu
                    System.out.println("\n" + Ressources.username + "'s menu");
                    status = false;
                    break;

                default:
                    //TODO implement modify cart as special command with arugments that need parsing
                    System.out.println("ERROR COMMAND INVALID\t please try again.\n");
                    break;
            }
        }
    }

    //TODO clear all items in cart and decrement item counts
    //TODO should add a constraint to remove consumable qty 0 from cart
    private void purchaseCart() {
    }

    //TODO display all items in cart
    private void listItems() {

        int rowCount = 1;

        try (PreparedStatement pst = conn.prepareStatement(Ressources.retrieveCartContentSQL + Ressources.username + "'");
             ResultSet rs = pst.executeQuery()) {

            System.out.println("Item\t\tConsumable_id\t\tConsumable_qty");

            while (rs.next()) {

                // Fetch all columns
                System.out.println(rowCount + "." + "\t\t" + rs.getString(1) + "\t\t\t\t\t" + rs.getInt(2));
                rowCount++;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void displayAvailableCommands() {
        System.out.println("\nAvailable actions in Cart:");
        System.out.println("viewCart(-v)\tmodifyCart(-m itemId qty)\tpurchase(-p)\tback");
    }
}
