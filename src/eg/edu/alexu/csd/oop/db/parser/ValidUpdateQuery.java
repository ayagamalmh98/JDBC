package eg.edu.alexu.csd.oop.db.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUpdateQuery implements Parser {

	private String insertSomePattern = "(\\A)(?i)(\\s*)(insert)(\\s+)(into)(\\s+)(\\w+)(\\s*)([(](((\\s*)(\\w+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)))[)])(\\s*)(values)(\\s*)[(]((\\s*)(((\\s*)[']([^'])*['](\\s*)[,](\\s*))|((\\s*)([0-9])+(\\s*)[,](\\s*)))*(((\\s*)[']([^'])*['](\\s*))|((\\s*)([0-9])+(\\s*))))[)](\\s*)(?-i)(\\z)";
	private String insertAllPattern = "(\\A)(?i)(\\s*)(insert)(\\s+)(into)(\\s+)(\\w+)(\\s*)(values)(\\s*)[(]((\\s*)(((\\s*)[']([^'])*['](\\s*)[,](\\s*))|((\\s*)([0-9])+(\\s*)[,](\\s*)))*(((\\s*)[']([^'])*['](\\s*))|((\\s*)([0-9])+(\\s*))))[)](\\s*)(?-i)(\\z)";
	private String deleteAllPattern = "(\\A)(?i)(\\s*)(delete)(\\s+)(from)(\\s+)(\\w+)(\\s*)(?-i)(\\z)";
	private String deleteSomePattern = "(\\A)(?i)(\\s*)(delete)(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([']([^'])*['])|([0-9]+))(\\s*)(?-i)(\\z)";

	private String updatePattern = "(\\A)(?i)(\\s*)(update)(\\s+)(\\w+)(\\s+)(set)(\\s+)(((\\s*)(\\w+)(\\s*)[=](\\s*)(([']([^']*)['])|[0-9]+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)[=](\\s*)(([']([^']*)['])|[0-9]+)(\\s*)))(\\s*)(?-i)(\\z)";
	private String updateWherePattern = "(\\A)(?i)(\\s*)(update)(\\s+)(\\w+)(\\s+)(set)(\\s+)(((\\s*)(\\w+)(\\s*)[=](\\s*)(([']([^']*)['])|[0-9]+)(\\s*)[,](\\s*))*((\\s*)(\\w+)(\\s*)[=](\\s*)(([']([^']*)['])|[0-9]+)(\\s*)))(\\s+)(where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([']([^'])*['])|([0-9]+))(\\s*)(?-i)(\\z)";

	public boolean isValid(String query) {
		return insertIsValid(query) || deleteIsValid(query) || updateIsValid(query);
	}

	private boolean deleteIsValid(String query) {
		return regexMatcher(query, deleteAllPattern) || regexMatcher(query, deleteSomePattern);
	}

	private boolean insertIsValid(String query) {
		return regexMatcher(query, insertSomePattern) || regexMatcher(query, insertAllPattern);
	}

	private boolean updateIsValid(String query) {
		return regexMatcher(query, updatePattern) || regexMatcher(query, updateWherePattern);
	}

	private boolean regexMatcher(String input, String pattern) {
		Pattern pat = Pattern.compile(pattern);
		Matcher mat = pat.matcher(input);
		return mat.matches();
	}

}
