import java.sql.*;
import java.time.Instant;
import java.util.Scanner;

public class Login {

    // Claimed credentials to process login
    private static String claimedUsername;
    private static String claimedPassword;
    private static Connection conn;

    // New account creation variables
    private static String newUsername;
    private static String newPassword;
    private static String firstName;
    private static String lastName;
    private static Date dateOfBirth;
    private static String province;
    private static String postalCode;
    private static String address;
    private static int category;

    public static Boolean authenticate(int trial) {

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
            authenticate(++trial);
        }

        else {

            String password = "";
            String mode = "";

            //verify claim against database
            conn = Ressources.connectPSQL();
            try (PreparedStatement pst = conn.prepareStatement(Ressources.loginCheckSQL)) {
                pst.setString(1, claimedUsername);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {

                    // Fetch password and account_status
                    password = rs.getString(2);
                    mode = rs.getString(3);

                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            conn = Ressources.closeConn(conn);

            if (password == "") {
                System.out.println("ERROR LOGIN FAILED\tattempt " + trial + " of 3 - Username invalid.\n");
                authenticate(++trial);
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
                    authenticate(++trial);
                }
            }
        }
        return false;
    }

    public static boolean createAccount() {
        System.out.println("To Sign-in: 1   -   To create a new account: 2");
        Scanner scanner = new Scanner(System.in);

        int createAccountInput = Integer.parseInt(scanner.nextLine());

        if(createAccountInput == 1) {
            return true;
        }

        else if(createAccountInput == 2) {
            // Create account
            System.out.println("Please create an username:");
            System.out.println("username must be between 5 and 10 characters");

            newUsername = scanner.nextLine();

            System.out.println("Please create a password:");
            System.out.println("Password should be 10 to 15 characters");

            newPassword = scanner.nextLine();

            while(!passwordCreationCheck(newPassword, scanner));

            System.out.println("Please enter your first name:");
            firstName = scanner.nextLine();

            System.out.println("Please enter your last name:");
            lastName = scanner.nextLine();

            System.out.println("Please enter your date of birth with the format [YYYY-MM-DD]");
            while (!dateOfBirthInput(scanner));

            System.out.println("Please enter the province you are residing in:");
            province = scanner.nextLine();

            System.out.println("Please enter your postal code:");
            postalCode = scanner.nextLine();

            System.out.println("Please enter your address:");
            address = scanner.nextLine();

            while(!chooseCategory(scanner));

            System.out.println("Press any key to create an account");
            scanner.nextLine();

            conn = Ressources.connectPSQL();
            try(PreparedStatement preparedStmt = conn.prepareStatement(Ressources.createAccountSQL)) {

                preparedStmt.setString   (1, newUsername);
                preparedStmt.setString   (2, newPassword);
                preparedStmt.setTimestamp(3, Timestamp.from(Instant.now()));
                preparedStmt.setString   (4, firstName);
                preparedStmt.setString   (5, lastName);
                preparedStmt.setDate     (6, dateOfBirth);
                preparedStmt.setString   (7, province);
                preparedStmt.setString   (8,postalCode);
                preparedStmt.setString   (9,address);
                preparedStmt.setString   (10, String.valueOf(category));
                preparedStmt.setString   (11,"active");

                preparedStmt.executeUpdate();

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            conn = Ressources.closeConn(conn);

            return true;
        }

        else {
            System.out.println("ERROR INVALID INPUT     Please try again");
            return false;
        }
    }

    private static boolean chooseCategory(Scanner scanner) {
        System.out.println("Please choose your desired fitness category. You may change this at any time.");
        System.out.println("Select 1 for Fat Loss | 2 for Muscle Gain | 3 for Stay Healthy | 4 for Cardio");

        String input;

        input = scanner.nextLine();

        if(input.length() != 1) {
            System.out.println("ERROR INVALID INPUT. Please try again.");
            return false;
        }

        try {
            if(Integer.parseInt(input) != 1 && Integer.parseInt(input) != 2 && Integer.parseInt(input) != 3 && Integer.parseInt(input) != 4) {
                System.out.println("ERROR INVALID INPUT. Please try again.");
                return false;
            } else {
                category = Integer.parseInt(input);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean passwordCreationCheck(String aNewPassword, Scanner scanner) {

        System.out.println("Please confirm your password by retyping it:");

            if (!aNewPassword.equals(scanner.nextLine())) {

            System.out.println("Password does not match. Please try again.");
            return false;
        }
        return true;
    }

    private static boolean dateOfBirthInput(Scanner scanner){

        String input = scanner.nextLine();

        try{
            dateOfBirth = Date.valueOf(input);
        } catch (Exception e) {
            System.out.println("ERROR INVALID INPUT. Please try again.");
            return false;
        }
        return true;
    }
}
