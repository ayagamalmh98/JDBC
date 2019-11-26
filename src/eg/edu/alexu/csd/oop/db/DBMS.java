package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DBMS implements Database {

    private File workspace;
    private ArrayList<DB> databases;
    private QueryValidator validator;

    public DBMS() {
        databases = new ArrayList<>();
        workspace = new File("sample");
        validator = QueryValidator.getInstance();
        if (!workspace.mkdir()) {
            File[] oldDatabases = workspace.listFiles();
            if (oldDatabases != null) {
                for (File database : oldDatabases
                ) {
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
        String[] splitted = query.replaceAll("\\)", " ").replaceAll("\\(", " ").replaceAll("'", "").replaceAll("\\s+\\,", ",").split("\\s+|\\,\\s*|\\(|\\)|\\=");
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
      //          return databases.get(databases.size() - 1).addTable("test", new String[]{"col"});

            }
            if (splitted[0].equalsIgnoreCase("drop")) {
                return databases.get(databases.size() - 1).deleteTable("test", new String[]{"col"});
            }
        }
        return false;
    }

    public Object[][] executeQuery(String query) throws java.sql.SQLException {
        if (!validator.isValidReadQuery(query)) {
            throw new SQLException("Invalid query");
        }
        return null;
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

    private void addDatabase(String name) {
        databases.add(new DB(name, workspace));
    }
}
