package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.io.File;

public class DatabaseImp implements Database {

	private static DatabaseImp Instance = null;
	private ArrayList<File> database;
	private SQL sql;

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
		return sql.PathOfDatabase(databaseName);
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		query = query.toLowerCase();
		boolean dropMatch = false;
		boolean createMatch = false;
		if (query.startsWith("drop")) {
			dropMatch = (boolean) parser.drop(query);
		} else if (query.startsWith("create")) {
			createMatch = parser.create(query);
		}

		if (dropMatch || createMatch) {

			String[] splitted = query.replaceAll("\\)", " ").replaceAll("\\(", " ").replaceAll("'", "")
					.replaceAll("\\s+\\,", ",").split("\\s+|\\,\\s*|\\(|\\)|\\=");
			if (splitted[1].equalsIgnoreCase("database")) {
				String databaseName = splitted[2].toLowerCase();
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
				String TableName = splitted[2].toLowerCase();
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
		String [] splitted = query.replaceAll("\\)", " ").replaceAll("\\(", " ").replaceAll("\\s*'\\s*","'")
				.replaceAll("\\s+\\,", ",").replaceAll("\\s*\"\\s*","\"").replaceAll("=", " = ")
				.split("\\s+|\\(|\\)");
		String columnName = splitted[1];
		String tableName = splitted[3];
		String DatabaseName = database.get(database.size()-1).getName();
		
		if(! sql.TableExists(DatabaseName, tableName)){
			throw new RuntimeException("Table " + tableName +
					" does not exists in " + DatabaseName);
		}
		
		if(columnName.equals("*")){
			
		}
		if(columnName.contains(",")){
			String[] columnsName = columnName.split("\\s*,\\s*");
		
		}
		else{
		
		}

		return null;
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		QueryValidation valid = new QueryValidation();
		valid.updateValidation(query);
		int rowsCount = 0;
		return rowsCount;
	}

}
