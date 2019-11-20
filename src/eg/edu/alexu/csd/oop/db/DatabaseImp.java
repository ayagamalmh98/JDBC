package Main;


import java.sql.SQLException;
import java.util.ArrayList;
import java.io.File;

public class DatabaseImp implements Database {

	private static DatabaseImp Instance = null;
	private ArrayList<File> database;
	private SQL sql;
	private Parser parser=new Parser();;
	

	private DatabaseImp() {
		this.database = new ArrayList<>();
		this.sql = new SQL();
		

	}
	

	public static DatabaseImp getInstance() {
		if (Instance == null) {
			Instance = new DatabaseImp();
		}
		return Instance;
	}

	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		databaseName = databaseName.toLowerCase();
		String query = ""; 
		if (dropIfExists) {
			query = "Drop Database " + databaseName;
			try {
				executeStructureQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			query = "Create Database " + databaseName;
			try {
				executeStructureQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			query = "Create Database " + databaseName;
			try {
				executeStructureQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return databaseName;
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		query = query.toLowerCase();
		boolean dropMatch=false;
		boolean createMatch=false;
		if (query.startsWith("drop")) {
			 dropMatch=parser.drop(query);
		}
		else if (query.startsWith("create")) {
			 createMatch=parser.create(query);
		}
		
		if (dropMatch||createMatch) {
		
		String[] splitted = query.replaceAll("\\)", " ").replaceAll("\\(", " ").replaceAll("'", "")
				.replaceAll("\\s+\\,", ",").split("\\s+|\\,\\s*|\\(|\\)|\\=");
		String databaseName = splitted[2].toLowerCase();
		if (splitted[1].equalsIgnoreCase("database")) {
			if (splitted[0].equalsIgnoreCase("create")) {
				if (sql.databaseExists(databaseName)) {
					int index = Index(databaseName);
					if (index != -1) {
						database.remove(index);
					}
				}
				File data = sql.createDatabase(databaseName);
				database.add(data);
			} else if (splitted[0].equalsIgnoreCase("drop")) {
				if (sql.databaseExists(databaseName)) {
					int index = Index(databaseName);
					if (index != -1) {
						database.remove(index);
					}
				}
				sql.dropDatabase(databaseName);
			}
		} else if (splitted[1].equalsIgnoreCase("table")) {
			if (splitted[0].equalsIgnoreCase("create")) {

			} else if (splitted[0].equalsIgnoreCase("drop")) {

			}
		}
	}
		return true;
	}

	private int Index(String databaseName) {
		int i = -1;
		for (File flag : database) {
			i++;
			if (flag.getName().equalsIgnoreCase(databaseName)) {
				return i;
			}

		}
		return -1;
	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {

		return null;
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {

		return 0;
	}

}
