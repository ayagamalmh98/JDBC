package eg.edu.alexu.csd.oop.db;


import java.util.Scanner;

public class UI {

	private static Scanner sc;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		DBMSController executor = DBMSController.getInstance();
		System.out.println("type '.help' for help");
		while (true) {
			System.out.println("SQL >> ");
			String query = sc.nextLine();
			if (query.equalsIgnoreCase(".quit")){
				System.exit(0);
			}
			if (query.equalsIgnoreCase(".help")){
				System.out.println("Following queries can be handled:\nCreate database database_name\nCreate table table_name\nDrop database database_name\nDrop table table_name\nInsert into table_name (column1, column2,...) values (value1, value2,...)\nInsert into table_name values (value1, value2,...)\nDelete from table_name\nDelete from table_name where column [><=] value\nSelect * from table_name\nSelect * from table_name where column [><=] value\nSelect column1, column2,.... from table_name\nSelect column1, column2,.... from table_name where column [><=] value\nUpdate table_name set column1 = value1, column2 = value2,...\nUpdate table_name set column1 = value1, column2 = value2,... where column [><=] value");
				continue;
			}
			if (query.isEmpty()) {
				query = sc.nextLine();
			}
			try {
				System.out.println(executor.invoke(query));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
