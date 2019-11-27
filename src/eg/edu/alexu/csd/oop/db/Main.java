package eg.edu.alexu.csd.oop.db;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class Main {


	public static void main(String[] args)throws IOException,SQLException {
		File testtss = new File("testttss");
		testtss.mkdir();
		DataCarrier carrier = new DataCarrier();
		carrier.columns = new String[]{"name","price"};
		carrier.columnsTypes = new String[]{"string","int"};
		carrier.tableName="pharmacy";

		Table testss = new Table(testtss,carrier);
		carrier.values = new String[]{"catflam","50"};
		testss.insertSome(carrier);

	}

}
