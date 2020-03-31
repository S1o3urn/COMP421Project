public class Ressources {

    public static final String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
    public static final String user = "cs421g39";
    public static final String password = "WeHaveChangedOurPassword123!";

    /// SQL script to fetch password based on username
    public static final String loginCheckSQL = "SELECT username, password FROM cs421g39.accounts WHERE username = '";
}
