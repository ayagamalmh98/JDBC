package eg.edu.alexu.csd.oop.db;


import java.io.File;
import java.util.List;

public class Main {


	public static void main(String[] args) throws Exception {
		File test = new File("test");
		Table testTable = new Table("test",test);
		testTable.insert(new String[]{"id","price"},new String[]{"cataflam","dsd"});
	}

}
