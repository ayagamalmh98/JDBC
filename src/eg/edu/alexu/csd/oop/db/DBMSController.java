package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBMSController {
    private DBMSController() {
        manager = new DBMS();
    }

    private static DBMSController instance = new DBMSController();
    private static Database manager;

    public DBMSController getInstance() {
        return instance;
    }

    public void invoke(String query) throws SQLException {

        switch (getFirstWord(query)) {
            case "CREATE":
                manager.createDatabase(query, false);
                break;
            case "USE":
            case "DROP":
                manager.executeStructureQuery(query);
                break;
            case "SELECT":
                manager.executeQuery(query);
                break;
            case "INSERT":
            case "DELETE":
            case "UPDATE":
                manager.executeUpdateQuery(query);
                break;
            default:
                throw new RuntimeException("Not a valid SQL query !");
        }
    }

    private String getFirstWord(String query) {
        Pattern firstWord = Pattern.compile("(\\s*)([A-Za-z]+)(\\s+)");
        Matcher firstWordMatcher = firstWord.matcher(query);
        if (firstWordMatcher.matches() && firstWordMatcher.group(2) != null) {
            return firstWordMatcher.group(2).toUpperCase();
        }
        return "";
    }
}
