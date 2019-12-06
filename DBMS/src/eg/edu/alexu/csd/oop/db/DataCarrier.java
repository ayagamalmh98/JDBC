package eg.edu.alexu.csd.oop.db;

public class DataCarrier {
 DataCarrier() {
 }

 String DBName;
 String tableName;
 String conditionColumn;
 String conditionValue;
 char conditionOperator;
 public String[] columns;
 public String[] columnsTypes;
 public String[] values;
}