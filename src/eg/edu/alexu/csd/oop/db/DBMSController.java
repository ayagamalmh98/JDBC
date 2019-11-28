package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBMSController {
	private DBMSController() throws SQLException {
		manager = new DBMS();
	}

	private static DBMSController instance;
	private static Database manager;

	static {
		try {
			instance = new DBMSController();

		} catch (SQLException s) {

		}
	}

	public static DBMSController getInstance() {
		return instance;
	}

	public String invoke(String query) throws SQLException {
		String[] splitted = query.trim().split("\\s+");
		switch (getFirstWord(query)) {
		case "CREATE":
			String test = manager.createDatabase(query, false);
			if (test == null) {
				new String(splitted[1].toUpperCase() + " wasn't Created Successfully.");
			} else {
				return new String(splitted[1].toUpperCase() + " Created Successfully.");
			}
		case "USE":
		case "DROP":
			boolean test1 = manager.executeStructureQuery(query);
			if (test1)
				return new String(splitted[1].toUpperCase() + " Dropped Successfully.");
			else
				return new String(splitted[1].toUpperCase() + " Wasn't Dropped Successfully.");
		case "SELECT":
			Object[][] test2 = manager.executeQuery(query);
			if (test2 == null) {
				return "wrong Selection!!";
			} else {
				StringBuilder st = new StringBuilder();
	        	for(int i = 0 ; i < test2.length ; i ++) {
	        		for(int j = 0 ; j < test2[0].length ; j++) {
	        			st.append(test2[i][j].toString() + " ");
	        		}
	        		st.append("\n");
	        	}
	        	return st.toString();
			}
		case "INSERT":
		case "DELETE":
		case "UPDATE":
			return manager.executeUpdateQuery(query) + " Table has been Updated.";
		default:
			throw new RuntimeException("Not a valid SQL query!");
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
