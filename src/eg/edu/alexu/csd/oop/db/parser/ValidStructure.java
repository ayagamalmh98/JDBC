package eg.edu.alexu.csd.oop.db.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidStructure implements Parser {
	 private String createDBPattern = "(\\A)(?i)(\\s*)(create)(\\s+)(database)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
	 private String createTablePattern = "(\\A)(?i)(\\s*)(create)(\\s+)(table)(\\s+)(\\w+)(\\s*)[(](((\\s*)(\\w+)(\\s+)((varchar)|(int))(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s+)((varchar)|(int))(\\s*)))[)](\\s*)(?-i)(\\z)";
	 private String  dropDBPattern = "(\\A)(?i)(\\s*)(drop)(\\s+)(database)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
	 private String  dropTablePattern = "(\\A)(?i)(\\s*)(drop)(\\s+)(table)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";

	public boolean isValid(String query) {
        return createIsValid(query) || dropIsValid(query);
    }
	private boolean createIsValid(String query) {
        return regexMatcher(query, createDBPattern) || regexMatcher(query, createTablePattern);
    }
	private boolean dropIsValid(String query) {
        return regexMatcher(query, dropDBPattern) || regexMatcher(query, dropTablePattern);
    }
	private boolean regexMatcher(String input, String pattern) {
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(input);
        return mat.matches();
    }

}
