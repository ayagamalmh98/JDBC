package Main;

import java.util.Scanner;

public class main {
	static Scanner input = new Scanner(System.in);
	


	public static void main(String[] args) throws Exception {
		
		while (true) {
			start();
		
		}

	}



	



	private static void start() {
		System.out.println("Enter sql query");
		System.out.print(">> ");
		String query = input.nextLine();
		parser p=new parser();
		p.parse(query);
		
	}

}
