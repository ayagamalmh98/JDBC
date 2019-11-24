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
                        tables.add(new Table(table.getName().substring(0, table.getName().length() - 4), directory));
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

    public void addTable(String name,String[] columns){
        if(tableExist(name)){
            throw new RuntimeException("Found a table with same name !");
        }else {
            tables.add(new Table(name,directory));
        }

    }
    private boolean tableExist(String name){
        for (Table t:tables
             ) {
            if(name.equalsIgnoreCase(t.getName())){
                return true;
            }
        }
        return false;
    }



}
