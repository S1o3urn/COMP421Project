import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class MyCart {

    private static Connection conn;
    private String action;
    private Scanner scanner;

    // Constructor
    public MyCart(Scanner scanner) {
        this.scanner = scanner;
    }

    // Handles user cart inputs
    public void cartMenu() {

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
            switch (argument) {
                case "-h":
                case "help":
                    displayAvailableCommands();
                    break;

                case "-v":
                case "viewCart":
                    listItems();
                    break;

                case "-m":
                case "modify":
                    String consumable_id = iterator.next();
                    String new_consumable_qty = iterator.next();

                    if (Integer.parseInt(new_consumable_qty) == 0) {
                        // Delete record from table
                        conn = Ressources.connectPSQL();
                        try(PreparedStatement preparedStmt = conn.prepareStatement(Ressources.deleteCartRecordSQL)) {

                            preparedStmt.setInt   (1, Integer.parseInt(consumable_id));

                            preparedStmt.executeUpdate();

                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                        conn = Ressources.closeConn(conn);
                    }

                    // Update record
                    else {
                        // Modify qty
                        conn = Ressources.connectPSQL();
                        try(PreparedStatement preparedStmt = conn.prepareStatement(Ressources.updateCartRecordSQL)) {

                            preparedStmt.setInt   (1, Integer.parseInt(new_consumable_qty));
                            preparedStmt.setString(2, consumable_id);

                            preparedStmt.executeUpdate();

                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                        conn = Ressources.closeConn(conn);
                    }

                case "-p":
                case "purchase":
                    purchaseCart();
                    break;

                case "back":
                    //Relinquish control back to Menu
                    System.out.println("\n" + Ressources.username + "'s menu");
                    status = false;
                    break;
                
                case "-l":
                case "view_consumables":
                	viewConsumables();
                	break;
                	
                default:
                    System.out.println("ERROR COMMAND INVALID\t please try again.\n");
                    break;
            }
        }
    }

    //TODO clear all items in cart and decrement item counts
    //TODO should add a constraint to remove consumable qty 0 from cart
    private void purchaseCart() {
    }

    // Displays all items in cart along with their quantity
    private void listItems() {

        int rowCount = 1;

        conn = Ressources.connectPSQL();
        try (PreparedStatement pst = conn.prepareStatement(Ressources.retrieveCartContentSQL)) {
            pst.setString(1, Ressources.username);
            ResultSet rs = pst.executeQuery();

            System.out.println("Item\t\tConsumable_id\t\tConsumable_qty");

            while (rs.next()) {
                // Fetch all columns
                System.out.println(rowCount + "." + "\t\t" + rs.getString(1) + "\t\t\t\t\t" + rs.getInt(2));
                rowCount++;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        conn = Ressources.closeConn(conn);
    }
    
  //List all available items for sale
    private void viewConsumables(){
        conn = Ressources.connectPSQL();
        try{
            PreparedStatement pst = conn.prepareStatement(Ressources.listconsumablesnameSQL);
            ResultSet rs = pst.executeQuery();
            System.out.println("List of consumables:(id name) \n");

            while(rs.next()){
                String meal = rs.getString("consumable_name");
                int id = rs.getInt("consumable_id");
                System.out.format("%s\t%s \n", id, meal);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        conn = Ressources.closeConn(conn);

    }

    // User help
    private void displayAvailableCommands() {
        System.out.println("\nAvailable actions in Cart:");
        System.out.println("viewCart(-v)\tmodifyCart(-m itemId qty)\tpurchase(-p)\tback\tview_consumables(-l)");
    }
}
