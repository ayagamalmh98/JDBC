package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

public class DB {
    private File directory;
    private ArrayList<Table> tables;

    public DB(String name, File parentDirectory) throws SQLException {
        tables = new ArrayList<>();
        directory = new File(parentDirectory, name);
        if (!directory.mkdir()) {
            File[] oldTables = directory.listFiles();
            if (oldTables != null) {
                for (File table : oldTables) {
                    if (table.isFile() && table.getName().endsWith(".xml")) {
                        DataCarrier temp = new DataCarrier();
                        temp.tableName = table.getName().substring(0, table.getName().length() - 4);
                        tables.add(
                                new Table( directory, temp));
                    }
                }
            }
        }
    }

    public String getName() {
        return directory.getName();
    }

    public boolean deleteDatabase() {
        return directory.delete();
    }

    public boolean addTable( DataCarrier carrier) throws SQLException {
        if (tableExist(carrier.tableName)) {
            return false;
        } else {
            tables.add(new Table(directory, carrier));
            return true;
        }

    }

    public boolean tableExist(String name) {
        for (Table t : tables) {
            if (name.equalsIgnoreCase(t.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteTable(String test, String[] strings) {
        return true;
    }

    public int getTableIndex(String name) {
        int i = 0;
        for (Table table : tables) {
            if (table.getName().equalsIgnoreCase(name))
                return i;
            i++;
        }
        return -1;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

}