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

        //Authentication challenge
        Login.authenticate(1);

        //All user actions accomplished through main menu
        Menu menu = new Menu();
    }
}
