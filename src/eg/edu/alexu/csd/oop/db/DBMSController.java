package eg.edu.alexu.csd.oop.db;

import eg.edu.alexu.csd.oop.db.parser.QV;

import java.sql.SQLException;

public class DBMSController {
	private DBMSController() throws SQLException {
		manager = new DBMS();
	}

	private static DBMSController instance;
	private static Database manager;
	private static QV validator;

	static {
		try {
			instance = new DBMSController();
			validator = new QV();
		} catch (SQLException s) {

		}
	}

	public static DBMSController getInstance() {
		return instance;
	}

	public String invoke(String query) throws SQLException {
		int operation = validator.isValidQuery(query);
		if(operation==0){
			boolean test = manager.executeStructureQuery(query);
			if (test) {
				return ("Database is active successfully.");
			} else {
				return ( "No such a database");

			}
		}
		if (operation == 1 || operation == 2) {
			boolean test = manager.executeStructureQuery(query);
			if (test) {
				return ((operation == 1 ? "Database" : "Table") + " created successfully.");
			} else {
				return ((operation == 1 ? "Database" : "Table") + " wasn't created successfully.");

			}
		} else if (operation == 3 || operation == 4) {
			boolean test1 = manager.executeStructureQuery(query);
			if (test1) {
				return ((operation == 3 ? "Database" : "Table") + " dropped successfully.");
			} else {
				return ((operation == 3 ? "Database" : "Table") + " wasn't dropped successfully.");
			}
		} else if ((operation >= 5 && operation <= 8) || operation == 15) {
			Object[][] test2 = manager.executeQuery(query);
			if (test2 == null) {
				return "Wrong selection!!";
			} else {
				StringBuilder st = new StringBuilder();
				for (Object[] objects : test2) {
					for (int j = 0; j < test2[0].length; j++) {
						//st.append(objects[j].toString()).append(" ");
						if (objects[j]==null){
							st.append("|");
						}else {
							st.append(objects[j].toString()).append("|");
						}
					}
					st.append("\n");
				}
				return st.toString();
			}
		} else if (operation >= 9 && operation <= 14) {
			return manager.executeUpdateQuery(query) + " rows has been Updated.";

		} else {
			throw new SQLException("Not a valid SQL query!");
		}
	}

	public Object[][] invokee(String query) throws SQLException {
		int operation = validator.isValidQuery(query);
		if ((operation >= 5 && operation <= 8) || operation == 15) {
			Object[][] test2 = manager.executeQuery(query);
			if (test2 == null) {
				return null;
			} else {
				return test2;
			}
		} else {
			throw new SQLException("Not a valid SQL query!");
		}
	}

	public DataCarrier getColumnsInfo(String query) throws SQLException {
		DB activeDB = ((DBMS) manager).getactiveDB();
		if (activeDB == null) {
			return null;
		}
		Table required = activeDB.getTable(getTableName(query));
		DataCarrier carrier;
		int operation = validator.isValidQuery(query);
		if (operation < 0) {
			throw new SQLException("Invalid query");
		}
		if (operation == 15) {
			carrier = DataExtractor.getInstance().selectAllData(query);
			carrier.columns = required.columnsNames();
			carrier.columnsTypes = required.columnsTypes();
			return carrier;
		} else if (operation == 6) {
			carrier = DataExtractor.getInstance().selectAllWhereData(query);
			carrier.columns = required.columnsNames();
			carrier.columnsTypes = required.columnsTypes();
			return carrier;
		} else if (operation == 7) {
			carrier = DataExtractor.getInstance().selectSomeData(query);
			return getDataCarrier(required, carrier);

		} else if (operation == 8) {
			carrier = DataExtractor.getInstance().selectSomeWhereData(query);
			return getDataCarrier(required, carrier);
		} else if (operation == 5) {
			carrier = DataExtractor.getInstance().selectAs(query);
			return getDataCarrier(required, carrier);
		}
		return null;
	}

	private DataCarrier getDataCarrier(Table required, DataCarrier carrier) throws SQLException {
		String[] allcolumnNames = required.columnsNames();
		String[] allcolumnTypes = required.columnsTypes();
		String[] somecolumnNames = carrier.columns;
		String[] somecolumnTypes = new String[somecolumnNames.length];
		for (int i = 0; i < somecolumnNames.length; i++) {
			for (int j = 0; j < allcolumnNames.length; j++) {
				if (allcolumnNames[j].equalsIgnoreCase(somecolumnNames[i])) {
					somecolumnTypes[i] = allcolumnTypes[j];
				}
			}
		}
		carrier.columnsTypes = somecolumnTypes;
		return carrier;
	}

	public String getTableName(String query) throws SQLException {
		DataCarrier carrier;
		int operation = validator.isValidQuery(query);
		if (operation < 0) {
			throw new SQLException("Invalid query");
		}
		if (operation == 15) {
			carrier = DataExtractor.getInstance().selectAllData(query);
			return carrier.tableName;
		} else if (operation == 6) {
			carrier = DataExtractor.getInstance().selectAllWhereData(query);
			return carrier.tableName;
		} else if (operation == 7) {
			carrier = DataExtractor.getInstance().selectSomeData(query);
			return carrier.tableName;
		} else if (operation == 8) {
			carrier = DataExtractor.getInstance().selectSomeWhereData(query);
			return carrier.tableName;
		} else if (operation == 5) {
			carrier = DataExtractor.getInstance().selectAs(query);
			return carrier.tableName;
		}
		return null;
	}

}