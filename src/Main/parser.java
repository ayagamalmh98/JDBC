package Main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parser {
	public void parse(String query) {
		
		String[] a = query.split(" ");
		
		if (a[0].toUpperCase().equals("USE")) {

			use(query);

		}

		if (a[0].toUpperCase() .equals( "CREATE")) {
			
			create(query);
		}
		if (a[0].toUpperCase() .equals("DROP")) {
			drop(query);

		}
		if (a[0].toUpperCase().equals("INSERT") ){
			 insert(query);

		}
		if (a[0].toUpperCase().equals("SELECT") ){
			// select(query);

		}
		if (a[0].toUpperCase().equals("DELETE")) {
			// delete(query);

		}

		if (a[0].toUpperCase().equals("UPDATE") ){
			// update(query);

		}

	}

	private void insert(String query) {
		Pattern pattern1 = Pattern.compile("(?i)insert\\s(?i)into\\s\\w+\\s(?i)values\\s?[(](\\s?\\w+\\s?,)+(\\s?\\w+\\s?)[)]");
		Pattern pattern2 = Pattern.compile("(?i)insert\\s(?i)into\\s\\w+\\s?[(](\\s?\\w+\\s,)+(\\s?\\w+\\s)[)]\\s?(?i)values\\s?[(](\\s?\\w+\\s?,)+(\\s?\\w+\\s?)[)]");

		Matcher matcher1 = pattern1.matcher(query);
		Matcher matcher2 = pattern2.matcher(query);

		if (matcher1.find()) {

			

		} else if (matcher2.find()) {
 
			
		}
		else System.out.println("not valid query ");
		return;
		
		
	}

	private void drop(String query) {
		Pattern pattern = Pattern.compile("(?i)drop\\s(?i)database\\s\\w+");

		Matcher matcher = pattern.matcher(query);

		if (matcher.find()) {

			// call for the required method

		} else {
			System.out.println("not valid query ");
		}
		return;

	}

	private void create(String query) {
		Pattern pattern1 = Pattern.compile("(?i)create\\s(?i)database\\s\\w+");
		Pattern pattern2 = Pattern.compile("(?i)create\\s(?i)table\\s\\w+");   
		Pattern pattern3 = Pattern.compile("(?i)create\\s[(](\\w+\\s\\w+,)+(\\w+\\s\\w+)[)]"); //giving names for columns
		Matcher matcher1 = pattern1.matcher(query);
		Matcher matcher2 = pattern2.matcher(query);
		Matcher matcher3 = pattern3.matcher(query);
		//System.out.println(matcher3);

		if (matcher1.find()) {

			// call for the required method

		} else if (matcher2.find()) {

		} else if (matcher3.find()) {
			

		} else {
			System.out.println("not valid query ");
		}
		return;

	}

	private void use(String query) {
		Pattern pattern = Pattern.compile("(?i)use\\s(?i)database\\s\\w+");

		Matcher matcher = pattern.matcher(query);
		if (matcher.find()) {

			// call for the required method

		} else {
			System.out.println("not valid query , Enter a valid one please :|");

		}
		return;

	}

}
