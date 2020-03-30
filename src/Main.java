import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
	    System.out.println("WELCOME TO FARMHUB\n");
        System.out.println("LOG IN TO PROCEED...\n");

        //Establish single database connection
        Main app = new Main();
        Connection conn = app.connectPSQL();

        //Authentication
        Login.authenticate(0, conn);




    }

    /// This method creates a connection object to the database.
    public Connection connectPSQL() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Ressources.url, Ressources.user, Ressources.password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}
