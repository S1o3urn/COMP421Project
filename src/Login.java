import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {

    // Claimed credentials to process login
    private static String claimedUsername;
    private static String claimedPassword;

    public static Boolean authenticate(int trial, Connection conn) {

        claimedUsername = "";
        claimedPassword = "";

        // 3 login trial condition
        if (trial > 3) {
            //return false;
           System.out.println("Too many login attempts.");
           System.out.println("Exiting...");
           System.exit(1);
        }

        // Scanner to parse input
        Scanner scanner = new Scanner(System.in);

        System.out.println("Username:");
        claimedUsername = scanner.nextLine();

        System.out.println("Password:");
        claimedPassword = scanner.nextLine();

        // Sanity checks
        if (claimedUsername == "" || claimedPassword == "") {
            System.out.println("ERROR LOGIN FAILED\tattempt " + trial + " of 3 - Username or Password blank.\n");
            authenticate(++trial, conn);
        }

        else {

            String password = "";
            String mode = "";

            //verify claim against database
            try (PreparedStatement pst = conn.prepareStatement(Ressources.loginCheckSQL)) {
                pst.setString(1, claimedUsername);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {

                    // Fetch password and account_status
                    password = rs.getString(2);
                    mode = rs.getString(3);

                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            if (password == "") {
                System.out.println("ERROR LOGIN FAILED\tattempt " + trial + " of 3 - Username invalid.\n");
                authenticate(++trial, conn);
            }

            else {
                if (password.equals(claimedPassword)) {

                    // Store username for reference
                    Ressources.username = claimedUsername;

                    // Store account_type to display appropriate menu
                    Ressources.acocunt_type = mode;
                    return true;
                } else {
                    System.out.println("ERROR LOGIN FAILED\tattempt " + trial + " of 3 - Password invalid.\n");
                    authenticate(++trial, conn);
                }
            }
        }
        return false;
    }
}
