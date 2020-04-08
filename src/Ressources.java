public class Ressources {

    //TODO implement safer way to store db credentials
    public static final String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
    public static final String user = "cs421g39";
    public static final String password = "WeHaveChangedOurPassword123!";

    // Holds the username for reference
    public static String username;

    // Holds the application operating mode
    public static String acocunt_type;

    //Test user logins:
    //tjiang9   #12345
    //cbibb1g   o1PreJwLgWC

    //Test admin login for statistics charts:
    //admin     admin2020
    //TODO: create admin account to test stats graphs


    /// SQL script to fetch password based on username
    public static final String loginCheckSQL = "SELECT username, password, account_status FROM cs421g39.accounts WHERE username = ?";

    // SQL script to fetch all items in cart for an user
    public static final String retrieveCartContentSQL = "SELECT consumable_id, consumable_qty FROM cs421g39.cart_contents WHERE username = ?";

    // SQL script to fetch sex on username
    public static final String retrieveSexSQL = "SELECT sex FROM cs421g39.customer_health_logs WHERE username = ?";

    // SQL script to fetch all health data for an user
    public static final String retrieveUserHealthLogsSQL = "SELECT log_date, height, weight, sex FROM cs421g39.customer_health_logs WHERE username = ?";

    // SQL script to insert a healthLog record
    public static final String insertHealthLogRecordSQL = "INSERT INTO cs421g39.customer_health_logs (username, log_date, height, weight, sex) VALUES(?,?,?,?,?)";

    // SQL script to update a cart table record
    public static final String updateCartRecordSQL = "UPDATE cs421g39.cart_contents SET consumable_qty = ? WHERE consumable_id = ?";

    // SQL script to delete a record from cart table
    public static final String deleteCartRecordSQL = "DELETE FROM cs421g39.cart_contents WHERE consumable_id = ?";

    // SQL script to get accountsSpendings stored procedure data, used to circumvent stored procedure calling limitations
    public static final String callAccountsSpendingsStoredProcedureSQL = "SELECT * FROM cs421g39.\"accountsSpendings\"";

    // SQL script to get popular_discounted_items stored procedure data, used to circumvent stored procedure calling limitations
    public static final String callTopDiscountedItemsStoredProcedureSQL = "SELECT * FROM cs421g39.\"popular_discounted_items\"";

    // SQL script to get top_ingredients stored procedure data, used to circumvent stored procedure calling limitations
    public static final String callTopIngredientsStoredProcedureSQL = "SELECT * FROM cs421g39.\"top_ingredients\"";
}
