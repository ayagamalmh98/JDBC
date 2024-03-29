package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.db.parser.QV;

public class DBMS implements Database {

    private File workspace;
    private ArrayList<DB> databases;
    private QV Validator;
    private DB activeDB;
     DBMS() {
        databases = new ArrayList<>();
        workspace = new File("workspace");
        Validator = new QV();
        activeDB=null;
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
        if (dropIfExists) {
            query = "Drop Database " + databaseName;
            query = query.toLowerCase();
            try {
                executeStructureQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        query = "Create Database " + databaseName;
        query = query.toLowerCase();

        try {
            createFlag = executeStructureQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (createFlag) {
            activeDB = databases.get(databases.size() - 1);
            return databases.get(databases.size() - 1).getPath();
        }
        return null;
    }

    public boolean executeStructureQuery(String query) throws java.sql.SQLException {
        int operation = Validator.validStructure(query);
        if (operation < 0) {
            throw new SQLException("Invalid query");
        }
        DataCarrier carrier;
        if (operation==0){
            carrier = DataExtractor.getInstance().useDBData(query);
            int index = findDatabaseByName(carrier.DBName);
            if(index==-1){
                return false;
            }
            activeDB = databases.get(index);
            return true;
        }
        if (operation == 1) {
            carrier = DataExtractor.getInstance().createDBData(query);
            addDatabase(carrier.DBName);
            activeDB = databases.get(databases.size() - 1);
            return true;
        }

        if (operation == 2) {
            if (databases.isEmpty()) {
                throw new SQLException("there is no databases");
            }
            carrier = DataExtractor.getInstance().createTableData(query);
            return activeDB.addTable(carrier);
        }

        if (operation == 3) {
            carrier = DataExtractor.getInstance().dropDBData(query);
            return dropDatabase(carrier);
        }
        
        if (operation == 4) {
            if (databases.isEmpty()) {
                throw new SQLException("there is no databases");
            }
            carrier = DataExtractor.getInstance().dropTableData(query);
            return activeDB.deleteTable(carrier);

        }
        return false;
    }
    
    DB getactiveDB()   {
    		return activeDB;
    }

    public Object[][] executeQuery(String query) throws java.sql.SQLException {
        DataCarrier carrier;
        int operation = Validator.validReadQuery(query);
        if (operation < 0) {
            throw new SQLException("Invalid query");
        }
        if (databases.isEmpty()) {
            return null;
        }
        if (operation == 15) {
            carrier = DataExtractor.getInstance().selectAllData(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectAll();

        } else if (operation == 6) {
            carrier = DataExtractor.getInstance().selectAllWhereData(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectAllWhere(carrier);

        } else if (operation == 7) {
            carrier = DataExtractor.getInstance().selectSomeData(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectSome(carrier);

        } else if (operation == 8) {
            carrier = DataExtractor.getInstance().selectSomeWhereData(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectSomeWhere(carrier);
        } else if(operation == 5){
            carrier = DataExtractor.getInstance().selectAs(query);
            if (!activeDB.tableExist(carrier.tableName)) {
                throw new SQLException("Table " + carrier.tableName + " does not exists in " + activeDB.getName());
            }
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).selectAs(carrier);
        }
        return null;
    }

    public int executeUpdateQuery(String query) throws java.sql.SQLException {
        int operation = Validator.validUpdateQuery(query);
        if (operation < 0) {
            throw new SQLException("Invalid query");
        }
        DataCarrier carrier;
        if (databases.isEmpty()) {
            return 0;
        }
        if (operation == 11) {
            carrier = DataExtractor.getInstance().insertSomeData(query);
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).insertSome(carrier);
        }
        if (operation == 12) {
            carrier = DataExtractor.getInstance().insertAllData(query);
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).insertAll(carrier);

        }

        if (operation == 14) {
            carrier = DataExtractor.getInstance().updateWhereData(query);
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).updateWhere(carrier);
        }
        if (operation == 13) {
            carrier = DataExtractor.getInstance().updateData(query);
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).update(carrier);

        }
        if (operation == 10) {
            carrier = DataExtractor.getInstance().deleteSomeData(query);
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).deleteWhere(carrier);
        }
        if (operation == 9) {
            carrier = DataExtractor.getInstance().deleteAllData(query);
            if (activeDB.getTableIndex(carrier.tableName) == -1) {
                throw new SQLException("No such a table");
            }
            return activeDB.getTables().get(activeDB.getTableIndex(carrier.tableName)).deleteAll();
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
        activeDB=null;
        return true;
    }

    private void deleteFile(File f) {
        if (f.delete())
            return;
        File[] list = f.listFiles();
        if (list != null) {
            for (File inside : list
            ) {
                deleteFile(inside);
            }
        }

    }

    private void addDatabase(String name) throws SQLException {
        databases.add(new DB(name, workspace));
    }


}