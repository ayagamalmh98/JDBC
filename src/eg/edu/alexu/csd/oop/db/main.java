package eg.edu.alexu.csd.oop.db;

import java.util.Scanner;

public class main {

	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		while (true) {
			//start();
			Database imp = DatabaseImp.getInstance();
			SQL sql = new SQL();
			//imp.createDatabase("klam", sql.databaseExists("klam"));
			imp.executeStructureQuery("Drop Database klam");
			

		}

	}

	private static void start() {
		System.out.println("Enter sql query");
		System.out.print(">> ");
		String query = input.nextLine();
		parser p = new parser();
		p.parse(query);

	}
}
