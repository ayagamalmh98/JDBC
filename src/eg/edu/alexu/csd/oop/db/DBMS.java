package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.db.parser.ValidReadQuery;
import eg.edu.alexu.csd.oop.db.parser.ValidStructure;
import eg.edu.alexu.csd.oop.db.parser.ValidUpdateQuery;

public class DBMS implements Database {

    private File workspace;
    private ArrayList<DB> databases;


     DBMS()  {
        databases = new ArrayList<>();
        workspace = new File("workspace");
        
        if (!workspace.mkdir()) {
            deleteFile(workspace);
            workspace = new File("workspace");
            workspace.mkdir();

        }
    }

    public String createDatabase(String databaseName, boolean dropIfExists) {
        databaseName = databaseName.split(Pattern.quote("\\"))[databaseName.split(Pattern.quote("\\")).length - 1];
        String query;
        boolean createFlag = true;
        try {
			dropIfExists = DBExists(databaseName);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
        if (dropIfExists) {
            query = "Drop Database " + databaseName;
            query= query.toLowerCase();
            try {
                executeStructureQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        query = "Create Database " + databaseName;
        query= query.toLowerCase();

        try {
            createFlag = executeStructureQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (createFlag) {
            return databases.get(databases.size()-1).getPath();
        }
        return null;
    }

    public boolean executeStructureQuery(String query) throws java.sql.SQLException {
        query= query.toLowerCase();
        if (!new ValidStructure().isValid(query)) {
            throw new SQLException("Invalid query");
        }
        String LQuery = query.toLowerCase();
        DataCarrier carrier;

        if (LQuery.contains("create ")) {
            if (LQuery.contains("database")) {
                carrier = DataExtractor.getInstance().createDBData(query);
                addDatabase(carrier.DBName);
                return true;
            }
            if (LQuery.contains("table")) {
                if (databases.isEmpty()) {
                    throw new SQLException("there is no databases");
                }
                DB activeDB = databases.get(databases.size() - 1);
                carrier = DataExtractor.getInstance().createTableData(query);
                return activeDB.addTable(carrier);
            }
        }
        if (LQuery.contains("drop")) {

            if (LQuery.contains("database")) {
                carrier = DataExtractor.getInstance().dropDBData(query);
                return dropDatabase(carrier);
            }
            if (LQuery.contains("table")) {
                if (databases.isEmpty()) {
                    throw new SQLException("there is no databases");
                }
                DB activeDB = databases.get(databases.size() - 1);
                carrier = DataExtractor.getInstance().dropTableData(query);
                return activeDB.deleteTable(carrier);
            }
        }
        return false;
    }

    public Object[][] executeQuery(String query) throws java.sql.SQLException {
        query= query.toLowerCase();
        DataCarrier carrier;
        String LQuery = query.toLowerCase();
        if (!new ValidReadQuery().isValid(query)) {
            throw new SQLException("Invalid query");
        }
        if (databases.isEmpty()) {
            return null;
        }
        DB activeDB = databases.get(databases.size() - 1);
        if (LQuery.contains("*") && !LQuery.contains("where")) {
            carrier = DataExtractor.getInstance().selectAllData(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if(activeDB.getTableIndex(carrier.tableName)==-1){
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectAll();

        } else if (LQuery.contains("*") && LQuery.contains("where")) {
            carrier = DataExtractor.getInstance().selectAllWhereData(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if(activeDB.getTableIndex(carrier.tableName)==-1){
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectAllWhere(carrier);

        } else if (!LQuery.contains("*") && !LQuery.contains("where")) {
            carrier = DataExtractor.getInstance().selectSomeData(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if(activeDB.getTableIndex(carrier.tableName)==-1){
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectSome(carrier);

        } else if (!LQuery.contains("*") && LQuery.contains("where")) {
            carrier = DataExtractor.getInstance().selectSomeWhereData(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if(activeDB.getTableIndex(carrier.tableName)==-1){
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectSomeWhere(carrier);
        }
        return null;
    }

    public int executeUpdateQuery(String query) throws java.sql.SQLException {
        query= query.toLowerCase();
        if (!new ValidUpdateQuery().isValid(query)) {
            throw new SQLException("Invalid query");
        }
        DataCarrier carrier;
        String LQuery = query.toLowerCase();
        if (databases.isEmpty()) {
            return 0;
        }
        DB activeDB = databases.get(databases.size() - 1);
        if (LQuery.contains("insert")) {
            if (LQuery.split("values")[0].contains("(")) {
                carrier = DataExtractor.getInstance().insertSomeData(query);
                if(activeDB.getTableIndex(carrier.tableName)==-1){
                    throw new SQLException("No such a table");
                }
                return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).insertSome(carrier);
            } else {
                carrier = DataExtractor.getInstance().insertAllData(query);
                if(activeDB.getTableIndex(carrier.tableName)==-1){
                    throw new SQLException("No such a table");
                }
                return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).insertAll(carrier);

            }
        }
        if (LQuery.contains("update")) {
            if (LQuery.contains("where")) {
                carrier = DataExtractor.getInstance().updateWhereData(query);
                if(activeDB.getTableIndex(carrier.tableName)==-1){
                    throw new SQLException("No such a table");
                }
                return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).updateWhere(carrier);
            } else {
                carrier = DataExtractor.getInstance().updateData(query);
                if(activeDB.getTableIndex(carrier.tableName)==-1){
                    throw new SQLException("No such a table");
                }
                return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).update(carrier);

            }
        }
        if (LQuery.contains("delete")) {
            if (LQuery.contains("where")) {
                carrier = DataExtractor.getInstance().deleteSomeData(query);
                if(activeDB.getTableIndex(carrier.tableName)==-1){
                    throw new SQLException("No such a table");
                }
                return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).deleteWhere(carrier);
            } else {
                carrier = DataExtractor.getInstance().deleteAllData(query);
                if(activeDB.getTableIndex(carrier.tableName)==-1){
                    throw new SQLException("No such a table");
                }
                return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).deleteAll();
            }
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

    private boolean dropDatabase(DataCarrier carrier) {
        int index = findDatabaseByName(carrier.DBName);
        if (index == -1) {
            return false;
        }
        deleteFile(new File(databases.get(index).getPath()));
        databases.remove(index);
        return true;
    }
    private void deleteFile(File f){
        if(f.delete())
            return;
        File[] list= f.listFiles();
        if (list!=null){
            for (File inside:list
            ) {
                deleteFile(inside);
            }
        }

    }

    private void addDatabase(String name) throws SQLException {
        databases.add(new DB(name, workspace));
    }
    
    private boolean DBExists(String name) throws SQLException {
    	for (int i = 0; i < databases.size(); i++) {
            if (databases.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
    	return false;
    }

}