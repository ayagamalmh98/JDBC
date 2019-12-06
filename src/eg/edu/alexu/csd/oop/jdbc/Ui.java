package eg.edu.alexu.csd.oop.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Ui {
	public static void main(String[] args) throws SQLException {
		
		Driver driver = new DriverImp();
		File dbDir = new File("jdbc:xmldb://localhost");
		Properties info = new Properties();
		info.put("path", dbDir.getAbsoluteFile());
		Connection con = driver.connect("jdbc:xmldb://localhost", info); 
		Statement stat = con.createStatement();
		Scanner scan = new Scanner(System.in);
		System.out.println("type '.help' for help");
		while (true) {
			System.out.println("SQL >> ");
			String query = scan.nextLine();
			if (query.equalsIgnoreCase(".quit")){
				System.exit(0);
			}
			if (query.equalsIgnoreCase(".help")){
				System.out.println("Following queries can be handled:\nCreate database database_name\nCreate table table_name\nDrop database database_name\nDrop table table_name\nInsert into table_name (column1, column2,...) values (value1, value2,...)\nInsert into table_name values (value1, value2,...)\nDelete from table_name\nDelete from table_name where column [><=] value\nSelect * from table_name\nSelect * from table_name where column [><=] value\nSelect column1, column2,.... from table_name\nSelect column1, column2,.... from table_name where column [><=] value\nUpdate table_name set column1 = value1, column2 = value2,...\nUpdate table_name set column1 = value1, column2 = value2,... where column [><=] value");
				continue;
			}
			if (query.isEmpty()) {
				query = scan.nextLine();
			}
			try {
			stat.execute(query);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}


}