package eg.edu.alexu.csd.oop.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Validator;

public class Table {
    private File dataFile;
    private File schemaFile;

    public Table(File databasepath, DataCarrier carrier) throws SQLException {
        dataFile = new File(databasepath, carrier.tableName + ".xml");
        schemaFile = new File(databasepath, carrier.tableName + ".xsd");
        try {
            if (dataFile.createNewFile() && schemaFile.createNewFile()) {
                intializeXML(carrier);
                schemaFile = SchemaGenerator.getInstance(schemaFile).createSchema(carrier);
            }

        } catch (IOException e) {
            System.out.println("Error loading old databases.");
        }

    }

    public int insertSome(DataCarrier carrier) throws SQLException {
        Document doc = DOMFactory.getDomObj(dataFile);
        checkDataFile(doc, "Bad data File !");
        if (doc != null) {
            Element newRow = doc.createElement("row");
            for (int i = 0; i < carrier.columns.length; i++) {
                newRow.setAttribute(carrier.columns[i], carrier.values[i]);
            }
            doc.getDocumentElement().appendChild(newRow);
            doc.getDocumentElement().appendChild(newRow);
            checkDataFile(doc, "Bad data entered !");
            DOMFactory.writeDOMtoFile(doc, dataFile);
            return 1;
        } else {
            throw new SQLException("Error loading data file !");
        }
    }

    public int insertAll(DataCarrier carrier) throws SQLException {
        Document doc = DOMFactory.getDomObj(dataFile);
        checkDataFile(doc, "Bad data File !");
        if (doc != null) {
            Element newRow = doc.createElement("row");
            for (int i = 0; i < carrier.columns.length; i++) {
                newRow.setAttribute(carrier.columns[i], carrier.values[i]);
            }
            doc.getDocumentElement().appendChild(newRow);
            checkDataFile(doc, "Bad data entered !");
            DOMFactory.writeDOMtoFile(doc, dataFile);
            return 1;
        } else {
            throw new SQLException("Error loading data file !");
        }
    }

    public int update(String[] columns, String[] values, String column, String value) throws SQLException {
        int counter = 0;
        Document doc = DOMFactory.getDomObj(dataFile);
        checkDataFile(doc, "Bad data File !");
        if (doc != null) {
            NodeList rows = doc.getElementsByTagName("row");
            for (int i = 0; i < rows.getLength(); i++) {
                if (rows.item(i).getAttributes().getNamedItem(column).getNodeValue().equals(value)) {
                    for (int j = 0; j < columns.length; j++) {
                        rows.item(i).getAttributes().getNamedItem(columns[j]).setNodeValue(values[j]);
                    }
                    counter++;
                }
            }
            checkDataFile(doc, "Bad data entered !");
            DOMFactory.writeDOMtoFile(doc, dataFile);
        } else {
            throw new SQLException("Error loading data file !");
        }
        return counter;
    }

    String getName() {
        return dataFile.getName().substring(0, dataFile.getName().length() - 4);
    }

    private void intializeXML(DataCarrier carrier) throws SQLException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("table");
            rootElement.setAttribute("name", carrier.tableName);
            doc.appendChild(rootElement);
            DOMFactory.writeDOMtoFile(doc, dataFile);
        } catch (ParserConfigurationException e) {
            throw new SQLException("Error loading data file !");
        }

    }

    public int delete(String condition, String operator, String value) {
        int counter = 0;
        Document doc = DOMFactory.getDomObj(dataFile);
        if (doc != null) {
            NodeList rows = doc.getElementsByTagName("row");
            if (operator.equals("=")) {
                for (int i = 0; i < rows.getLength(); i++) {
                    if (rows.item(i).getAttributes().getNamedItem(condition).getNodeValue().equals(value)) {
                        doc.getDocumentElement().removeChild(rows.item(i));
                        counter++;
                    }
                }

            }
            if (operator.equals(">")) {
                for (int i = 0; i < rows.getLength(); i++) {
                    if (rows.item(i).getAttributes().getNamedItem(condition).getNodeValue().compareTo(value)>0) {
                        doc.getDocumentElement().removeChild(rows.item(i));
                        counter++;
                    }
                }
            }
            if (operator.equals("<")) {
                for (int i = 0; i < rows.getLength(); i++) {
                    if (rows.item(i).getAttributes().getNamedItem(condition).getNodeValue().compareTo(value)<0 ) {
                        doc.getDocumentElement().removeChild(rows.item(i));
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

    public String[][] selectAll() throws SQLException {
        Document doc = DOMFactory.getDomObj(dataFile);
        if (doc != null) {
            checkDataFile(doc, "Bad data File !");
            NodeList rows = doc.getElementsByTagName("row");
            String[][] table = new String[rows.getLength()][rows.item(0).getAttributes().getLength()];
            for (int i = 0; i < rows.getLength(); i++) {
                for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
                    table[i][j] = rows.item(i).getAttributes().item(j).getNodeValue();
                }
            }
            return table;
        } else {
            throw new SQLException("Error loading data file !");
        }
    }

    public String[][] selectSome(String[] columnsName) throws SQLException {
        Document doc = DOMFactory.getDomObj(dataFile);
        if (doc != null) {
            checkDataFile(doc, "Bad data File !");
            NodeList rows = doc.getElementsByTagName("row");
            String[][] table = new String[rows.getLength()][columnsName.length];
            for (int i = 0; i < rows.getLength(); i++) {
                int count = 0;
                for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
                    if (rows.item(i).getAttributes().item(j).getNodeName().equals(columnsName[count])) {
                        table[i][count] = rows.item(i).getAttributes().item(j).getNodeValue();
                        count++;
                    }
                }
                if (count != columnsName.length) {
                    return null;
                }
            }
            return table;
        } else {
            throw new SQLException("Error loading data file !");
        }

    }

    public String[][] getColumns() {
        Document readtable = DOMFactory.getDomObj(dataFile);
        NodeList rows = readtable.getElementsByTagName("row");
        String[][] table = new String[rows.getLength()][rows.item(0).getAttributes().getLength()];
        /*
         * for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
         * table[0][j] = rows.item(0).getAttributes().item(j).getNodeName(); }
         */
        for (int i = 0; i < rows.getLength(); i++) {
            for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
                table[i][j] = rows.item(i).getAttributes().item(j).getNodeValue();
            }
        }
        return table;
    }

    public String[][] getCertainColumns(String[] columnsName) {
        Document readtable = DOMFactory.getDomObj(dataFile);
        NodeList rows = readtable.getElementsByTagName("row");
        String[][] table = new String[rows.getLength()][columnsName.length];
        /*
         * for (int j = 0; j < columnsName.length; j++) { table[0][j] = columnsName[j];
         * }
         */
        for (int i = 0; i < rows.getLength(); i++) {
            int count = 0;
            for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
                if (rows.item(i).getAttributes().item(j).getNodeName().equals(columnsName[count])) {
                    table[i][count] = rows.item(i).getAttributes().item(j).getNodeValue();
                    count++;
                }
            }
            if (count != columnsName.length) {
                return null;
            }
        }
        return table;
    }

    public String[][] getOneColumn(String columnName) {
        Document readtable = DOMFactory.getDomObj(dataFile);
        NodeList rows = readtable.getElementsByTagName("row");
        String[][] table = new String[rows.getLength()][1];
        // table[0][0] = columnName;
        for (int i = 0; i < rows.getLength(); i++) {
            int count = 0;
            for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
                if (rows.item(i).getAttributes().item(j).getNodeName().equals(columnName)) {
                    table[i][count] = rows.item(i).getAttributes().item(j).getNodeValue();
                    count++;
                }
            }
            if (count != 1) {
                return null;
            }
        }
        return table;
    }

    public boolean columnExists(String columnName) {
        Document doc = DOMFactory.getDomObj(dataFile);
        NodeList rows = doc.getElementsByTagName("row");
        for (int i = 0; i < rows.getLength(); i++) {
            for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
                if (rows.item(i).getAttributes().item(j).getNodeName().equals(columnName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Object[][] WherePart(Object[][] cols, String conditionColumn, char conditionOperator, String conditionValue,
                                Table table) {
        if (cols == null) {
            throw new RuntimeException("Error invalid columns");
        }
        Object[][] filtered = new Object[cols.length][cols[0].length];

        if (!table.columnExists(conditionColumn)) {
            throw new RuntimeException("Error in where clause, Column " + conditionColumn + " doesn't exist.");
        }

        int rowIndex = 0;
        if (isInt(conditionValue)) {
            if (getType(conditionColumn).equals("xs:int")) {
                Integer value = Integer.parseInt(conditionValue);
                try {
                    int i = 0;
                    for (Object[] row : cols) {
                        ArrayList<Object> filteredCells = new ArrayList<>();
                        i = 0;
                        for (Object cell : row) {
                            Object x = getCell(i, conditionColumn);
                            if (compare(x, value, conditionOperator))
                                filteredCells.add(cell);
                            i++;
                        }
                        filtered[rowIndex++] = filteredCells.toArray();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (getType(conditionColumn).equals("xs:varchar")) {
                String value = conditionValue.toString();
                int i = 0;
                rowIndex = 0;
                for (Object[] row : cols) {
                    ArrayList<Object> filteredCells = new ArrayList<>();
                    i = 0;
                    for (Object cell : row) {
                        Object x = getCell(i, conditionColumn);
                        if (compare(x, value, conditionOperator))
                            filteredCells.add(cell);
                        i++;
                    }
                    filtered[rowIndex++] = filteredCells.toArray();
                }
            }
        } else if (isLetter(conditionValue)) {
            if (getType(conditionColumn).equals("xs:varchar")) {
                String value = conditionValue.toString();
                int i = 0;
                rowIndex = 0;
                for (Object[] row : cols) {
                    ArrayList<Object> filteredCells = new ArrayList<>();
                    i = 0;
                    for (Object cell : row) {
                        Object x = getCell(i, conditionColumn);
                        if (compare(x, value, conditionOperator))
                            filteredCells.add(cell);
                        i++;
                    }
                    filtered[rowIndex++] = filteredCells.toArray();
                }
            } else if (getType(conditionColumn).equals("xs:int")) {
                Integer value = Integer.parseInt(conditionValue);
                try {
                    int i = 0;
                    for (Object[] row : cols) {
                        ArrayList<Object> filteredCells = new ArrayList<>();
                        i = 0;
                        for (Object cell : row) {
                            Object x = getCell(i, conditionColumn);
                            if (compare(x, value, conditionOperator))
                                filteredCells.add(cell);
                            i++;
                        }
                        filtered[rowIndex++] = filteredCells.toArray();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return filtered;
    }

    private static boolean isInt(String strNum) {
        strNum = strNum.replaceAll("\'", "");
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean isLetter(String s) {
        s = s.replaceAll("\'", "");
        if (s == null) {
            return false;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (Character.isLetter(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean compare(Object ColumnValue, Object conditionValue, char operator) {
        String conditionOperator = Character.toString(operator);
        if (ColumnValue instanceof Integer && conditionValue instanceof Integer) {
            Integer value1 = (Integer) ColumnValue;
            Integer value2 = (Integer) conditionValue;
            if (conditionOperator.equals("=")) {
                return Integer.compare(value1.intValue(), value2.intValue()) == 0 ? true : false;
            } else if (conditionOperator.equals(">")) {
                return Integer.compare(value1.intValue(), value2.intValue()) > 0 ? true : false;
            } else if (conditionOperator.equals("<")) {
                return Integer.compare(value1.intValue(), value2.intValue()) < 0 ? true : false;
            }
        } else if (ColumnValue instanceof String && conditionValue instanceof String) {
            String value1 = (String) ColumnValue;
            String value2 = (String) conditionValue;
            if (conditionOperator.equals("=")) {
                return value1.compareToIgnoreCase(value2) == 0 ? true : false;
            } else if (conditionOperator.equals(">")) {
                return value1.compareTo(value2) > 0 ? true : false;
            } else if (conditionOperator.equals("<")) {
                return value1.compareTo(value2) < 0 ? true : false;
            }
        }
        return false;
    }

    private String getType(String columnName) {
        Document doc = DOMFactory.getDomObj(schemaFile);
        NodeList elements = doc.getElementsByTagName("xs:attribute");
        String type = null;
        for (int i = 0; i < elements.getLength(); i++) {
            if (elements.item(i).getAttributes().item(0).getNodeName().equals(columnName)) {
                type = elements.item(i).getAttributes().item(1).getNodeValue();
            }
        }
        return type;
    }

    public Object getCell(int index, String ColumnName) {
        Document doc = DOMFactory.getDomObj(dataFile);
        NodeList rows = doc.getElementsByTagName("row");
        Object cell = new Object();
        for (int i = 0; i < rows.getLength(); i++) {
            for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
                if (i == index && rows.item(i).getAttributes().item(j).getNodeName().equals(ColumnName)) {
                    cell = rows.item(i).getAttributes().item(j).getNodeValue();
                }
            }
        }
        return cell;
    }

    private void checkDataFile(Document doc, String error) throws SQLException {
        if (!DOMFactory.validateXML(doc, schemaFile)) {
            throw new SQLException(error);
        }
    }

}