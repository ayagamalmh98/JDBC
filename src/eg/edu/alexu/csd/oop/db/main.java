package Main;

import java.sql.SQLException;
import java.util.Scanner;

public class main {
	static Scanner input = new Scanner(System.in);
	


	public static void main(String[] args) throws Exception {
		
		while (true) {
			start();
		
		}

	}



	



	private static void start() throws SQLException {
		System.out.println("Enter sql query");
		System.out.print(">> ");
		String query = input.nextLine();
		Parser p=new Parser();
		
		p.parse(query);
		
	}

}
