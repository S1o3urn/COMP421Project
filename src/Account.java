import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Account {

    private static Connection conn;
    private String action;
    private Scanner scanner;

    private String newPassword;
    private String newCategory;

    // Constructor
    public Account(Scanner scanner) {
        this.scanner = scanner;
    }

    public void accountMenu() {
        boolean status = true;

        while (status) {
            System.out.println("\n" + Ressources.username + "'s account");
            System.out.println("-h or help for available commands");
            System.out.println("Enter command:");

            action = scanner.nextLine();

            // parse input
            List<String> argumentsList = Arrays.asList(action.split(" "));

            ListIterator<String> iterator = argumentsList.listIterator();

            String argument = iterator.next();
            switch (argument) {
                case "-h":
                case "help":
                    displayAvailableCommands();
                    break;

                case "-v":
                case"view":
                    conn = Ressources.connectPSQL();
                    try(PreparedStatement preparedStmt = conn.prepareStatement(Ressources.viewAccountSettingsSQL)) {

                        preparedStmt.setString(1, Ressources.username);

                        ResultSet rs = preparedStmt.executeQuery();

                        System.out.println("Username: " + rs.getString(1));
                        System.out.println("Password: " + rs.getString(2));
                        System.out.println("Account Creation Date: " + rs.getTimestamp(3));
                        System.out.println("First Name: " + rs.getString(4));
                        System.out.println("Last Name: " + rs.getString(5));
                        System.out.println("Date Of Birth: " + rs.getDate(6));
                        System.out.println("Province: " + rs.getString(7));
                        System.out.println("Postal Code: " + rs.getString(8));
                        System.out.println("Address: " + rs.getString(9));
                        System.out.println("Category: " + rs.getString(10));
                        System.out.println("Account Status: " + rs.getString(11));

                        rs.close();
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    conn = Ressources.closeConn(conn);
                    break;

                case "-p":
                case "password":
                    while(!changePassword());

                    conn = Ressources.connectPSQL();
                    try(PreparedStatement preparedStmt = conn.prepareStatement(Ressources.updatePasswordSQL)) {

                        preparedStmt.setString(1, newPassword);
                        preparedStmt.setString(2, Ressources.username);

                        preparedStmt.executeUpdate();

                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    conn = Ressources.closeConn(conn);

                case "-c":
                case "category":
                    while(!changeCategory());

                    conn = Ressources.connectPSQL();
                    try(PreparedStatement preparedStmt = conn.prepareStatement(Ressources.updateCategorySQL)) {

                        preparedStmt.setString(1, newCategory);
                        preparedStmt.setString(2, Ressources.username);

                        preparedStmt.executeUpdate();

                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    conn = Ressources.closeConn(conn);

                case "back":
                    //Relinquish control back to Menu
                    System.out.println("\n" + Ressources.username + "'s menu");
                    status = false;
                    break;

                default:
                    System.out.println("ERROR COMMAND INVALID\t please try again.\n");
                    break;
            }
        }
    }


    private boolean changeCategory() {
        System.out.println("Select 1 for Fat Loss | 2 for Muscle Gain | 3 for Stay Healthy | 4 for Cardio");
        String aNewCategory = scanner.nextLine();

        if(aNewCategory.length() != 1) {
            System.out.println("ERROR INVALID INPUT. Please try again.");
            return false;
        }

        else if(Integer.parseInt(aNewCategory) != 1 && Integer.parseInt(aNewCategory) != 2 && Integer.parseInt(aNewCategory) != 3 && Integer.parseInt(aNewCategory) != 4) {
            System.out.println("ERROR INVALID INPUT. Please try again.");
            return false;
        } else {
            newCategory = aNewCategory;
            return true;
        }
    }

    private boolean changePassword(){
        System.out.println("Enter new password");
        newPassword = scanner.nextLine();

        if(!Login.passwordCreationCheck(newPassword, scanner)){
            return false;
        } else {
            return true;
        }
    }

    // User help
    private void displayAvailableCommands() {
        System.out.println("\nAvailable actions in Cart:");
        System.out.println("View account settings(-v)\tback");
    }
}
