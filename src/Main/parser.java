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
		
		if (a[1].toUpperCase() == "CREATE") {
			create(query);
		}
		if (a[1].toUpperCase() == "DROP") {
			//drop(query);

		}
		if (a[1].toUpperCase() == "INSERT") {
			//insert(query);

		}
		if (a[1].toUpperCase() == "SELECT") {
			//select(query);

		}
		if (a[1].toUpperCase() == "DELETE") {
			//delete(query);

		}

		if (a[1].toUpperCase() == "UPDATE") {
			//update(query);

		}
		
	}

	private void create(String query) {
		// TODO Auto-generated method stub
		
	}

	private void use(String query) {
		Pattern pattern = Pattern
				.compile("(?i)use\\s(?i)database\\s\\w+");

		Matcher matcher = pattern.matcher(query);
		ArrayList<String> parts = new ArrayList<>();
		if (matcher.find()) {
			System.out.println("valid query :)");
			//call for the required method

		}
		else { System.out.println("not valid query ");  ;}
		return;
		
	}

}
