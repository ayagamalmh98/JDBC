package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static eg.edu.alexu.csd.oop.db.QueryValidator.*;


public class DataExtractor {

    public DataCarrier createTableData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(createTablePattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            String[] data = mat.group(9).split(",");
            toBeReturn.tableName = mat.group(7);
            toBeReturn.columns = new String[data.length];
            toBeReturn.columnsTypes = new String[data.length];
            Pattern isolateTwoWords = Pattern.compile("\\A\\s*(\\w+)\\s+(\\w+)\\s*\\z");
            Matcher isolateMatcher;
            for (int i = 0; i < data.length; i++) {
                isolateMatcher = isolateTwoWords.matcher(data[i]);
                if (isolateMatcher.matches()) {
                    toBeReturn.columns[i] = isolateMatcher.group(1);
                    toBeReturn.columnsTypes[i] = isolateMatcher.group(2).equalsIgnoreCase("varchar") ? "xs:string" : "xs:int";
                }
            }
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier insertSomeData(String query) throws SQLException {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(insertSomePattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            String[] columns = mat.group(10).split(",");
            String[] values = mat.group(23).split(",");
            toBeReturn.tableName = mat.group(7);
            if (columns.length != values.length) {
                throw new SQLException();
            }
            toBeReturn.columns = new String[columns.length];
            toBeReturn.values = new String[columns.length];
            fillColumns(toBeReturn, columns);
            fillValues(toBeReturn, values);
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier insertAllData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(insertAllPattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            String[] data = mat.group(11).split(",");
            toBeReturn.tableName = mat.group(7);
            toBeReturn.values = new String[data.length];
            fillValues(toBeReturn, data);
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier deleteAllData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(deleteAllPattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            toBeReturn.tableName = mat.group(7);
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier deleteSomeData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(deleteSomePattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            toBeReturn.tableName = mat.group(7);
            toBeReturn.conditionColumn = mat.group(11);
            toBeReturn.conditionValue = mat.group(15);
            if (toBeReturn.conditionValue.charAt(0) == '\'') {
                toBeReturn.conditionValue = toBeReturn.conditionValue.substring(1, toBeReturn.conditionValue.length() - 1);
            }
            toBeReturn.conditionOperator = mat.group(13).charAt(0);
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier selectAllData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(selectAllPattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            toBeReturn.tableName = mat.group(8);
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier selectAllWhereData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(selectAllWherePattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            toBeReturn.tableName = mat.group(8);
            toBeReturn.conditionColumn = mat.group(12);
            toBeReturn.conditionValue = mat.group(16);
            if (toBeReturn.conditionValue.charAt(0) == '\'') {
                toBeReturn.conditionValue = toBeReturn.conditionValue.substring(1, toBeReturn.conditionValue.length() - 1);
            }
            toBeReturn.conditionOperator = mat.group(14).charAt(0);
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier selectSomeData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(selectSomePattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            toBeReturn.tableName = mat.group(18);
            String[] data = mat.group(5).split(",");
            fillColumns(toBeReturn, data);
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier selectSomeWhereData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(selectSomeWherePattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            toBeReturn.tableName = mat.group(18);
            toBeReturn.conditionColumn = mat.group(22);
            toBeReturn.conditionValue = mat.group(26);
            if (toBeReturn.conditionValue.charAt(0) == '\'') {
                toBeReturn.conditionValue = toBeReturn.conditionValue.substring(1, toBeReturn.conditionValue.length() - 1);
            }
            toBeReturn.conditionOperator = mat.group(24).charAt(0);
            String[] data = mat.group(5).split(",");
            fillColumns(toBeReturn, data);
            return toBeReturn;
        }
        return null;
    }

    private void updateData(DataCarrier toBeReturn, Matcher mat) {
        toBeReturn.tableName = mat.group(5);
        String[] rawData = mat.group(9).split(",");
        String[] columnData = new String[rawData.length];
        String[] valuesData = new String[rawData.length];
        toBeReturn.values = new String[rawData.length];
        toBeReturn.columns = new String[rawData.length];
        for (int i = 0; i < rawData.length; i++) {
            String[] str = rawData[i].split("=");
            columnData[i] = str[0];
            valuesData[i] = str[1];
        }
        fillColumns(toBeReturn, columnData);
        fillValues(toBeReturn, valuesData);
    }

    public DataCarrier updateAllData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(updateAllPattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            updateData(toBeReturn, mat);
            return toBeReturn;
        }
        return null;
    }

    public DataCarrier updateSomeData(String query) {
        DataCarrier toBeReturn = new DataCarrier();
        Pattern pat = Pattern.compile(updateSomePattern);
        Matcher mat = pat.matcher(query);
        if (mat.matches()) {
            updateData(toBeReturn, mat);
            toBeReturn.conditionColumn = mat.group(32);
            toBeReturn.conditionValue = mat.group(36);
            if (toBeReturn.conditionValue.charAt(0) == '\'') {
                toBeReturn.conditionValue = toBeReturn.conditionValue.substring(1, toBeReturn.conditionValue.length() - 1);
            }
            toBeReturn.conditionOperator = mat.group(34).charAt(0);
            return toBeReturn;
        }
        return null;
    }


    private void fillColumns(DataCarrier toBeReturn, String[] data) {
        Pattern wordWithoutSingleQuotes = Pattern.compile("\\A\\s*(\\w+)\\s*\\z");
        Matcher matcher;
        toBeReturn.columns = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            matcher = wordWithoutSingleQuotes.matcher(data[i]);
            if (matcher.matches()) {
                toBeReturn.columns[i] = matcher.group(1);
            }
        }
    }


    private void fillValues(DataCarrier toBeReturn, String[] data) {
        Pattern withSingleQuotes = Pattern.compile("\\A\\s*[']([^']*)[']\\s*\\z");
        Pattern withoutSingleQuotes = Pattern.compile("\\A\\s*([0-9]+)\\s*\\z");
        for (int i = 0; i < data.length; i++) {

            Matcher matcher;
            matcher = withoutSingleQuotes.matcher(data[i]);
            if (matcher.matches()) {
                toBeReturn.values[i] = matcher.group(1);
            }
            matcher = withSingleQuotes.matcher(data[i]);
            if (matcher.matches()) {
                toBeReturn.values[i] = matcher.group(1);
            }
        }
    }


}
