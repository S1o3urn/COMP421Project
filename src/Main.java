import java.sql.*;

public class Main {

    public static void main(String[] args) {
	    System.out.println(
	              " ______    __      ______   ___    ____   __    __   __    __  ____    \n"
                + "|   ___|  /  \\    |  __  \\ |   \\  /    | |  |  |  | |  |  |  ||  _ \\  \n"
                + "|  |__   /  _ \\   | |__\\ | |    \\/     | |  |__|  | |  |  |  || |/  /  \n"
                + "|   __| /  /_\\ \\  |      / |  |\\   /|  | |   __   | |  |  |  ||  _  \\ \n"
                + "|  |   /  ____\\ \\ |  |\\ \\  |  | \\_/ |  | |  |  |  | \\  \\_/  / | |_|  | \n"
                + "|__|  /__/     \\_\\|__| \\_\\ |__|     |__| |__|  |__|  \\____ /  |_____/  \n"
                );
	    System.out.println("WELCOME TO FARMHUB");
        System.out.println("LOG IN TO PROCEED...");

        //Establish single database connection
        Main app = new Main();
        Connection conn = app.connectPSQL();

        //Authentication challenge
        Login.authenticate(1, conn);

        //All user actions accomplished through main menu
        Menu menu = new Menu(conn);

        // Close connection
        app.closeConn(conn);
    }

    /// This method creates a connection object to the database.
    public Connection connectPSQL() {
        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL DataSource unable to load PostgreSQL JDBC Driver");
            System.exit(1);
        }

        try {
            conn = DriverManager.getConnection(Ressources.url, Ressources.user, Ressources.password);
            System.out.println("Connected to the PostgreSQL server successfully.\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return conn;
    }


    public void closeConn(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
