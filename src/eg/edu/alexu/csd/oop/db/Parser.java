package Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	public void parse(String query) throws SQLException {
		DatabaseImp d= DatabaseImp.getInstance();
		SQL sql =new SQL();
		String[] a = query.split(" ");
		
		if (a[0].toUpperCase().equals("USE")) {

			use(query);

		}

		else if (a[0].toUpperCase() .equals( "CREATE")) {
			if (create(query)) {
				int c=0; int i=0;
				for ( i=0;i<query.length();i++) {
					if (query.charAt(i)==' '&&query.charAt(i+1)!=' ') c++;
					if (c==2) break;
				}
				String q=query.substring(i+1); 
				Boolean DatabaseExist =sql.databaseExists(q);
				d.createDatabase(q, DatabaseExist);
				
				
			}
			
			
			
		}
		else if (a[0].toUpperCase() .equals("DROP")) {
			d.executeStructureQuery(query);
			//drop(query);

		}
		else if (a[0].toUpperCase().equals("INSERT") ){
			 insert(query);

		}
		else if (a[0].toUpperCase().equals("SELECT") ){
			 select(query);

		}
		else if (a[0].toUpperCase().equals("DELETE")) {
			 delete(query);

		}

		else if (a[0].toUpperCase().equals("UPDATE") ){
			 update(query);

		}
		else  System.out.println("not valid query1");

	}
	
	

	public void update(String query) {
		Pattern pattern = Pattern.compile("(?i)update\\s+\\w+\\s+(?i)set\\s+(\\s*\\w+\\s*=\\s*\\w+\\s*,)+\\s*(\\s*\\w+\\s*=\\s*\\w+\\s*)");
		Matcher matcher = pattern.matcher(query);
		if (matcher.find()) {
			System.out.println("done");
			
		}
		else
			System.out.println("not valid query2");
		
		
	}

	public void delete(String query) {
		Pattern pattern = Pattern.compile("(?i)delete\\s+(?i)from\\s+[\\w_]+\\s+(?i)where\\s+[\\w_]+\\s*(=|<|>)\\s*\\w+");
		Matcher matcher = pattern.matcher(query);
		if (matcher.find()) {
			System.out.println("done");
			
		}
		else
			System.out.println("not valid query3");
		
	}

	public void select(String query) {
		Pattern pattern1 = Pattern.compile("(?i)select\\s(\\w+,)+(\\w+)(?i)from\\s\\w+");
		
		Pattern pattern2 = Pattern.compile("(?i)select\\s[*]\\s(?i)from\\s\\w+");
		Matcher matcher1 = pattern1.matcher(query);
		Pattern pattern3 = Pattern.compile("(?i)select\\s+[\\w_]+\\s+(?i)from\\s+[\\w_]+\\s+(?i)where\\s+[\\w_]+\\s*(>|<|=)\\s*\\w+");
		Matcher matcher3 = pattern3.matcher(query);
		Matcher matcher2 = pattern2.matcher(query);
		if (matcher1.find()) {
			System.out.println("done");

			

		} else if (matcher2.find()) {
			System.out.println("done");
			
		}
		else if (matcher3.find()){
			System.out.println("done");
			
		}
		
		else
			System.out.println("not valid query4");
		
	}

	public void insert(String query) {
		Pattern pattern1 = Pattern.compile("(?i)insert\\s(?i)into\\s[\\w_]+\\s(?i)values\\s?[(](\\s?\\w+\\s?,)+(\\s?\\w+\\s?)[)]");
		Pattern pattern2 = Pattern.compile("(?i)insert\\s+(?i)into\\s+[\\w_]+\\s*[(](\\s*[\\w_]+\\s*,)*(\\s*[\\w_]+\\s*)[)]\\s*(?i)values\\s*[(](\\s*\\w+\\s?,)*(\\s*\\w+\\s*)[)]");

		Matcher matcher1 = pattern1.matcher(query);
		Matcher matcher2 = pattern2.matcher(query);

		if (matcher1.find()) {
			System.out.println("done");

			

		} else if (matcher2.find()) {
			System.out.println("done");
 
			
		}
		else System.out.println("not valid query 5");
		return;
		
		
	}

	public boolean drop(String query) throws SQLException {
		Pattern pattern = Pattern.compile("(?i)drop\\s(?i)(database|table)\\s\\w+");
		

		Matcher matcher = pattern.matcher(query);

		if (matcher.find()) {
			System.out.println("done");
			return true;

			// call for the required method

		} else {
			System.out.println("not valid query6 ");
			return false;
		}
		

	}

	public boolean create(String query) {
		Pattern pattern1 = Pattern.compile("(?i)create\\s(?i)database\\s\\w+");
	  
		Pattern pattern2 = Pattern.compile("(?i)create\\s+(?i)table\\s+[\\w_]+\\s*[(](\\s*[\\w_]+\\s+\\w+\\s*,)*(\\s*[\\w_]+\\s+\\w+\\s*)[)]"); //giving names for columns
		Matcher matcher1 = pattern1.matcher(query);

		Matcher matcher2 = pattern2.matcher(query);
		//System.out.println(matcher3);

		if (matcher1.find()) {
			System.out.println("done");
			return true;

		} else if (matcher2.find()) {
			System.out.println("done");
			return true;
			
		}
		 else {
			System.out.println("not valid query ");
			return false;
		}
		

	}

	public void use(String query) {
		Pattern pattern = Pattern.compile("(?i)use\\s(?i)database\\s\\w+");

		Matcher matcher = pattern.matcher(query);
		if (matcher.find()) {
			System.out.println("done");

			// call for the required method

		} else {
			System.out.println("not valid query , Enter a valid one please :|");

		}
		return;

	}

}
