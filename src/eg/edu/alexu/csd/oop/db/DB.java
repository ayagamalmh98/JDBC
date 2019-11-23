package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.util.ArrayList;

public class DB {
    private File directory;
    private ArrayList<Table> tables;

    public DB(String name, File parentDirectory) {
        tables = new ArrayList<>();
        directory = new File(parentDirectory, name);
        if (!directory.mkdir()) {
            File[] oldTables = directory.listFiles();
            if (oldTables != null) {
                for (File table : oldTables
                ) {
                    if (table.isFile() && table.getName().endsWith(".xml")) {
                        tables.add(new Table(table.getName().substring(0, table.getName().length() - 4).toLowerCase(), directory));
                    }
                }
            }
        }
    }
    public String getName(){
        return directory.getName();
    }
    public boolean deleteDatabase(){
        return directory.delete();
    }


}
