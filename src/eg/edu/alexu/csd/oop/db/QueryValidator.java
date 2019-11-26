package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QueryValidator {

    private static QueryValidator instance = new QueryValidator();
     static final String createDBPattern;
     static final String createTablePattern;
     static final String dropDBPattern;
     static final String dropTablePattern;
     static final String insertSomePattern;
     static final String insertAllPattern;
     static final String deleteSomePattern;
     static final String deleteAllPattern;
     static final String selectAllPattern;
     static final String selectAllWherePattern;
     static final String selectSomePattern;
     static final String selectSomeWherePattern;
     static final String updateAllPattern;
     static final String updateSomePattern;

    static {
        createDBPattern = "(\\A)(?i)(\\s*)(create)(\\s+)(database)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
        createTablePattern = "(\\A)(?i)(\\s*)(create)(\\s+)(table)(\\s+)(\\w+)(\\s*)[(](((\\s*)(\\w+)(\\s+)((varchar)|(int))(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s+)((varchar)|(int))(\\s*)))[)](\\s*)(?-i)(\\z)";
        dropDBPattern = "(\\A)(?i)(\\s*)(drop)(\\s+)(database)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
        dropTablePattern = "(\\A)(?i)(\\s*)(drop)(\\s+)(table)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
        insertSomePattern = "(\\A)(?i)(\\s*)(insert)(\\s+)(into)(\\s+)(\\w+)(\\s*)([(](((\\s*)(\\w+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)))[)])(\\s*)(values)(\\s*)[(]((\\s*)(((\\s*)[']([^'])*['](\\s*)[,](\\s*))|((\\s*)([0-9])+(\\s*)[,](\\s*)))*(((\\s*)[']([^'])*['](\\s*))|((\\s*)([0-9])+(\\s*))))[)](\\s*)(?-i)(\\z)";
        insertAllPattern = "(\\A)(?i)(\\s*)(insert)(\\s+)(into)(\\s+)(\\w+)(\\s*)(values)(\\s*)[(]((\\s*)(((\\s*)[']([^'])*['](\\s*)[,](\\s*))|((\\s*)([0-9])+(\\s*)[,](\\s*)))*(((\\s*)[']([^'])*['](\\s*))|((\\s*)([0-9])+(\\s*))))[)](\\s*)(?-i)(\\z)";
        deleteAllPattern = "(\\A)(?i)(\\s*)(delete)(\\s+)(from)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
        deleteSomePattern = "(\\A)(?i)(\\s*)(delete)(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([']([^'])*['])|([0-9]+))(\\s*)(?-i)(\\z)";
        selectAllPattern = "(\\A)(?i)(\\s*)(select)(\\s*)[*](\\s*)(from)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
        selectAllWherePattern = "(\\A)(?i)(\\s*)(select)(\\s*)[*](\\s*)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([']([^'])*['])|([0-9]+))(\\s*)(?-i)(\\z)";
        selectSomePattern = "(\\A)(?i)(\\s*)(select)(\\s+)(((\\s*)(\\w+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)))(\\s+)(from)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
        selectSomeWherePattern = "(\\A)(?i)(\\s*)(select)(\\s+)(((\\s*)(\\w+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)))(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([']([^'])*['])|([0-9]+))(\\s*)(?-i)(\\z)";
        updateAllPattern = "(\\A)(?i)(\\s*)(update)(\\s+)(\\w+)(\\s+)(set)(\\s+)(((\\s*)(\\w+)(\\s*)[=](\\s*)(([']([^']*)['])|[0-9]+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)[=](\\s*)(([']([^']*)['])|[0-9]+)(\\s*)))(\\s*)(?-i)(\\z)";
        updateSomePattern = "(\\A)(?i)(\\s*)(update)(\\s+)(\\w+)(\\s+)(set)(\\s+)(((\\s*)(\\w+)(\\s*)[=](\\s*)(([']([^']*)['])|[0-9]+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)[=](\\s*)(([']([^']*)['])|[0-9]+)(\\s*)))(\\s+)(where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([']([^'])*['])|([0-9]+))(\\s*)(?-i)(\\z)";

    }
    private QueryValidator() {


    }

    public static QueryValidator getInstance() {
        return instance;
    }

    public boolean isValidStructureQuery(String query) {
        return createIsValid(query) || dropIsValid(query);
    }

    public boolean isValidReadQuery(String query) {
        return selectIsValid(query);
    }

    public boolean isValidUpdateQuery(String query) {
        return insertIsValid(query) || deleteIsValid(query) || updateIsValid(query);
    }

    private boolean deleteIsValid(String query) {
        return regexMatcher(query, deleteAllPattern) || regexMatcher(query, deleteSomePattern);
    }

    private boolean createIsValid(String query) {
        return regexMatcher(query, createDBPattern) || regexMatcher(query, createTablePattern);
    }


    private boolean insertIsValid(String query) {
        return regexMatcher(query, insertSomePattern) || regexMatcher(query, insertAllPattern);
    }

    private boolean dropIsValid(String query) {
        return regexMatcher(query, dropDBPattern) || regexMatcher(query, dropTablePattern);
    }

    private boolean selectIsValid(String query) {
        return regexMatcher(query, selectAllPattern) || regexMatcher(query, selectAllWherePattern) || regexMatcher(query, selectSomePattern) || regexMatcher(query, selectSomeWherePattern);
    }

    private boolean updateIsValid(String query) {
        return regexMatcher(query, updateAllPattern) || regexMatcher(query, updateSomePattern);
    }

    private boolean regexMatcher(String input, String pattern) {
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(input);
        return mat.matches();
    }


}
