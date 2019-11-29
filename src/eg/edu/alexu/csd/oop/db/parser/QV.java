package eg.edu.alexu.csd.oop.db.parser;

public class QV {
	private Parser ValidReadQuery;
	private Parser ValidStructure;
	private Parser ValidUpdateQuery;
	
	public QV() {
		ValidReadQuery = new ValidReadQuery();
		ValidStructure = new ValidStructure();
		ValidUpdateQuery = new ValidUpdateQuery();
	}
	
	public boolean ValidReadQuery(String query){
		return ValidReadQuery.isValid(query);
	 }
	 public boolean ValidStructure(String query){
		 return ValidStructure.isValid(query);
	 }
	 public boolean ValidUpdateQuery(String query){
		 return ValidUpdateQuery.isValid(query);
	 }
	

}

