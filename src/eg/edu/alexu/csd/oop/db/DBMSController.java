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
		query = query.toLowerCase();
		String[] splitted = query.trim().split("\\s+");
		if (splitted[0].contains("create")) {
			boolean test = manager.executeStructureQuery(query);
			if (test) {
				return new String(splitted[1] + " Created Successfully.");
			} else {
				return new String(splitted[1] + " wasn't Created Successfully.");

			}
		} else if (splitted[0].contains("use") || splitted[0].contains("drop")) {
			boolean test1 = manager.executeStructureQuery(query);
			if (test1)
				return new String(splitted[1] + " Dropped Successfully.");
			else
				return new String(splitted[1] + " Wasn't Dropped Successfully.");
		} else if (splitted[0].contains("select")) {
			Object[][] test2 = manager.executeQuery(query);
			if (test2 == null) {
				return "wrong Selection!!";
			} else {
				StringBuilder st = new StringBuilder();
				for (int i = 0; i < test2.length; i++) {
					for (int j = 0; j < test2[0].length; j++) {
						st.append(test2[i][j].toString() + " ");
					}
					st.append("\n");
				}
				return st.toString();
			}
		} else if (splitted[0].contains("insert") || splitted[0].contains("delete") || splitted[0].contains("update")) {
			int test3 = manager.executeUpdateQuery(query);
			if (test3 == 0) {
				return "rows hasn't been Updated.";
			} else {
				return manager.executeUpdateQuery(query) + " rows has been Updated.";
			}
		} else {
			throw new SQLException("Not a valid SQL query!");
		}
	}

	
}