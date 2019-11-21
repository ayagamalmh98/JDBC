package eg.edu.alexu.csd.oop.db;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryValidation {
    public void updateValidation (String query) throws java.sql.SQLException {
        Map<String,String>columns = new TreeMap<>();
        String tableName = null;
        String condition = null;
        String condiionValue;
        Pattern choose = Pattern.compile("^(\\s+)?(([A-Z]|[a-z])+)(([\\s](.*))|(\\z))");
        Matcher chooseMatcher = choose.matcher(query);
        chooseMatcher.matches();
        Pattern delete = Pattern.compile("^(\\s+)?([d|D][e|E][l|L][e|E][t|T][e|E])(\\s+)([f|F][r|R][o|O][m|M])(\\s+)(([A-Z]|[a-z])+)(\\s+)?(([;])(\\s+)?|(([w|W][h|H][e|E][r|R][e|E])(\\s+)(([A-Z]|[a-z])+)(\\s+)?[=](\\s+)?(([']((.*)+)['])|(\\d+))(\\s+)?[;](\\s+)?))");
        Matcher deleteMatcher = delete.matcher(query);
        if(!deleteMatcher.matches()){
            throw new java.sql.SQLException();
        }
        Pattern  update = Pattern.compile("^(\\s+)?([u|U][p|P][d|D][a|A][t|T][e|E])(\\s+)(([A-Z]|[a-z])+)(\\s+)([s|S][e|E][t|T])(\\s+)((((([A-Z]|[a-z])+)(\\s+)?[=](\\s+)?(([']([^']*)['](\\s+)?)|(\\d+))(\\s+)?[,](\\s+)?)*)(\\s+)?(([A-Z]|[a-z])+)(\\s+)?[=](\\s+)?(([']([^']*)['](\\s+)?)|(\\d+)))(\\s+)?(([;])(\\s+)?|((\\s+)[w|W][h|H][e|E][r|R][e|E])(\\s+)(([A-Z]|[a-z])+)(\\s+)?[=](\\s+)?(([']([^']*)['])|(\\d+))(\\s+)?[;](\\s+)?)");
        Matcher updateMatcher = update.matcher(query);
        if(!updateMatcher.matches()){
            throw new java.sql.SQLException();
        }
        Pattern insert = Pattern.compile("");
        Matcher insertMatcher = insert.matcher(query);
        if(insertMatcher.matches()){
            throw new java.sql.SQLException();
        }
        switch (chooseMatcher.group(2).toLowerCase()){
            case "delete" : tableName = deleteMatcher.group(6);
            if(!deleteMatcher.group(15).equals("null")) {
                condition = deleteMatcher.group(15);
                condiionValue = deleteMatcher.group(19);
                if(condiionValue.charAt(0)=='\'')
                    condiionValue = condiionValue.substring(1,condiionValue.length()-1);
            }
                break;
            case "update" : tableName = updateMatcher.group(4);
                String s = updateMatcher.group(9);
                String [] split1 = s.split("(\\s+)?(,)(\\s+)?");
                String [] split2;
                for(int i=0;i<split1.length;i++){
                    split2= split1[i].split("(\\s+)?(=)(\\s)?");
                    columns.put(split2[0],split2[1]);
                }
            if(!updateMatcher.group(40).equals("null")){
                condition = updateMatcher.group(40);
                condiionValue = updateMatcher.group(44);
                if(condiionValue.charAt(0)=='\'')
                    condiionValue = condiionValue.substring(1,condiionValue.length()-1);
            }
                break;
            case "insert" :
                break;
        }

    }
}
