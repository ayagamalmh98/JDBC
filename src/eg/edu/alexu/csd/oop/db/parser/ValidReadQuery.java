package eg.edu.alexu.csd.oop.db.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidReadQuery implements Parser {
	private String selectAllPattern = "(\\A)(?i)(\\s*)(select)(\\s*)[*](\\s*)(from)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
	private String selectAllWherePattern = "(\\A)(?i)(\\s*)(select)(\\s*)[*](\\s*)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([']([^'])*['])|([0-9]+))(\\s*)(?-i)(\\z)";
	private String selectSomePattern = "(\\A)(?i)(\\s*)(select)(\\s+)(((\\s*)(\\w+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)))(\\s+)(from)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
	private String selectSomeWherePattern = "(\\A)(?i)(\\s*)(select)(\\s+)(((\\s*)(\\w+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)))(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([']([^'])*['])|([0-9]+))(\\s*)(?-i)(\\z)";

	public boolean isValid(String query) {
		 return selectIsValid(query);
    }
	 private boolean selectIsValid(String query) {
	        return regexMatcher(query, selectAllPattern) || regexMatcher(query, selectAllWherePattern) || regexMatcher(query, selectSomePattern) || regexMatcher(query, selectSomeWherePattern);
	    }
	private boolean regexMatcher(String input, String pattern) {
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(input);
        return mat.matches();
    }

}
