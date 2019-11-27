package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DBMS implements Database {

    private File workspace;
    private ArrayList<DB> databases;
    private QueryValidator validator;

    public DBMS() throws SQLException {
        databases = new ArrayList<>();
        workspace = new File("sample");
        validator = QueryValidator.getInstance();
        if (!workspace.mkdir()) {
            File[] oldDatabases = workspace.listFiles();
            if (oldDatabases != null) {
                for (File database : oldDatabases) {
                    if (database.isDirectory()) {
                        databases.add(new DB(database.getName(), workspace));
                    }
                }
            }
        }
    }

    public String createDatabase(String databaseName, boolean dropIfExists) {
        databaseName = databaseName.split(Pattern.quote("\\"))[databaseName.split(Pattern.quote("\\")).length - 1];
        String query;
        boolean dropFlag = true, createFlag = true;
        if (dropIfExists) {
            query = "Drop Database " + databaseName;
            try {
                dropFlag = executeStructureQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        query = "Create Database " + databaseName;
        try {
            createFlag = executeStructureQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dropFlag && createFlag) {
            return workspace.getAbsolutePath() + "\\" + databaseName;
        }
        return null;
    }

    public boolean executeStructureQuery(String query) throws java.sql.SQLException {
        if (!validator.isValidStructureQuery(query)) {
            throw new SQLException("Invalid query");

        }
        String[] splitted = query.replaceAll("\\)", " ").replaceAll("\\(", " ").replaceAll("'", "")
                .replaceAll("\\s+\\,", ",").split("\\s+|\\,\\s*|\\(|\\)|\\=");
        String databaseName = splitted[2].toLowerCase();
        if (splitted[1].equalsIgnoreCase("database")) {
            if (splitted[0].equalsIgnoreCase("create")) {
                int index = findDatabaseByName(databaseName);
                if (index != -1) {
                    dropDatabase(index);
                }
                addDatabase(databaseName);
                return true;
            }
            if (splitted[0].equalsIgnoreCase("drop")) {
                int index = findDatabaseByName(databaseName);
                if (index != -1) {
                    dropDatabase(index);
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (splitted[1].equalsIgnoreCase("table")) {
            if (databases.isEmpty()) {
                throw new SQLException();
            }
            if (splitted[0].equalsIgnoreCase("create")) {
                 return databases.get(databases.size() - 1).addTable(DataExtractor.getInstance().createTableData(query));

            }
            if (splitted[0].equalsIgnoreCase("drop")) {
                return databases.get(databases.size() - 1).deleteTable("test", new String[] { "col" });
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public Object[][] executeQuery(String query) throws java.sql.SQLException {
        if (!validator.isValidReadQuery(query)) {
            throw new SQLException("Invalid query");
        }
        query = query.toLowerCase();
        String[] splitted = query.replaceAll("\\)", " ").replaceAll("\\(", " ").replaceAll("\\s+\\,", ",")
                .replaceAll("\\s+\\,", ",").replaceAll("\\,\\s+", ",").replaceAll("\\s+\\,\\s+", ",")
                .replaceAll("\\s*\"\\s*", "\"").replaceAll("\\s*'\\s*", "'").replaceAll("=", " = ")
                .replaceAll("<", " < ").replaceAll(">", " > ").split("\\s+");
        String ColumnName = splitted[1];
        String TableName = splitted[3];
        boolean where = false;
        if (splitted.length > 4) {
            where = true;
        }
        DB currentDB = databases.get(databases.size() - 1);

        if (!currentDB.tableExist(TableName)) {
            throw new RuntimeException("Table " + TableName + " does not exists in " + currentDB.getName());
        }

        Table currentTable = currentDB.getTables().get(currentDB.getTableIndex(TableName));

        if (ColumnName.equals("*")) {
            String[][] columns = currentTable.selectAll();
            Object[][] Output = new Object[columns.length][columns[0].length];
            for (int i = 0; i < columns.length; i++) {
                for (int j = 0; j < columns[0].length; j++) {
                    if (isInt(columns[i][j])) {
                        Output[i][j] = Integer.parseInt(columns[i][j]);
                    } else if (isLetter(columns[i][j])) {
                        Output[i][j] = columns[i][j].toString();
                    } else {
                        Output[i][j] = null;
                    }
                }
            }
            if (where) {
                return currentTable.WherePart(Output, splitted, currentTable);
            }
            return Output;

        } else if (ColumnName.contains(",")) {
            String[] columnsName = ColumnName.split("\\s*,\\s*");
            String[][] columns = currentTable.selectSome(columnsName);
            Object[][] Output = new Object[columns.length][columns[0].length];
            if (columns == null) {
                throw new RuntimeException("Error invalid columns");
            } else {
                for (int i = 0; i < columns.length; i++) {
                    for (int j = 0; j < columns[0].length; j++) {
                        if (isInt(columns[i][j])) {
                            Output[i][j] = Integer.parseInt(columns[i][j]);
                        } else if (isLetter(columns[i][j])) {
                            Output[i][j] = columns[i][j].toString();
                        } else {
                            Output[i][j] = null;
                        }
                    }
                }

            }
            if (where) {
                return currentTable.WherePart(Output, splitted, currentTable);
            }
            return Output;

        } else {
            String[][] columns = currentTable.getOneColumn(ColumnName);
            Object[][] Output = new Object[columns.length][1];
            if (columns == null) {
                throw new RuntimeException("Error invalid columns");
            } else {
                for (int i = 0; i < columns.length; i++) {
                    if (isInt(columns[i][0])) {
                        Output[i][0] = Integer.parseInt(columns[i][0]);
                    } else if (isLetter(columns[i][0])) {
                        Output[i][0] = columns[i][0].toString();
                    } else {
                        Output[i][0] = null;
                    }
                }
            }
            if (where) {
                return currentTable.WherePart(Output, splitted, currentTable);
            }
            return Output;
        }
    }

    private static boolean isInt(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean isLetter(String s) {
        if (s == null) {
            return false;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if ((Character.isLetter(s.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public int executeUpdateQuery(String query) throws java.sql.SQLException {
        if (!validator.isValidUpdateQuery(query)) {
            throw new SQLException("Invalid query");
        }
        return 0;
    }

    private int findDatabaseByName(String name) {
        for (int i = 0; i < databases.size(); i++) {
            if (databases.get(i).getName().endsWith(name)) {
                return i;
            }
        }
        return -1;
    }

    private void dropDatabase(int index) {
        databases.get(index).deleteDatabase();
        databases.remove(index);
    }

    private void addDatabase(String name) throws SQLException {
        databases.add(new DB(name, workspace));
    }

}