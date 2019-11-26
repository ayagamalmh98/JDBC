package eg.edu.alexu.csd.oop.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Table {
	private File dataFile;
	private File schemaFile;

	public Table(String name, File databasepath, String[][] columns) {
		dataFile = new File(databasepath, name + ".xml");
		schemaFile = new File(databasepath, name + ".xsd");
		try {
			if (dataFile.createNewFile() && schemaFile.createNewFile()) {
				intializeXML();

			}

		} catch (IOException e) {
			System.out.println("Error loading old databases.");
		}

	}

	public int insert(String[] columns, String[] values) {

		Document doc = DOMFactory.getDomObj(dataFile);
		if (doc != null) {
			Element newRow = doc.createElement("row");
			for (int i = 0; i < columns.length; i++) {
				newRow.setAttribute(columns[i], values[i]);
			}
			doc.getDocumentElement().appendChild(newRow);
			if (DOMFactory.validateXML(doc, schemaFile)) {
				DOMFactory.writeDOMtoFile(doc, dataFile);
			} else {
				System.out.println("Not a valid query");
				return 0;
			}
			return columns.length;
		}
		return 0;
	}

	public int update(String[] columns, String[] values, String column, String value) {
		int counter = 0;
		Document doc = DOMFactory.getDomObj(dataFile);
		if (doc != null) {
			NodeList rows = doc.getElementsByTagName("row");
			for (int i = 0; i < rows.getLength(); i++) {
				if (rows.item(i).getAttributes().getNamedItem(column).getNodeValue().equals(value)) {
					for (int j = 0; j < columns.length; j++) {
						rows.item(i).getAttributes().getNamedItem(columns[j]).setNodeValue(values[j]);
					}
					counter++;
				}
			}
			if (DOMFactory.validateXML(doc, schemaFile)) {
				DOMFactory.writeDOMtoFile(doc, dataFile);
			} else {
				System.out.println("Not a valid query");
				return 0;
			}
		}
		return counter;
	}

	String getName() {
		return dataFile.getName().substring(0, dataFile.getName().length() - 4);
	}

	private void intializeXML() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Element rootElement = doc.createElement("table");
			doc.appendChild(rootElement);
			DOMFactory.writeDOMtoFile(doc, dataFile);
		} catch (ParserConfigurationException e) {
		}

	}

	public int delete(String condition, String operand, String value) {
		int counter = 0;
		Document doc = DOMFactory.getDomObj(dataFile);
		if (doc != null) {
			NodeList rows = doc.getElementsByTagName("row");
			if (operand.equals("=")) {
				for (int i = 0; i < rows.getLength(); i++) {
					if (rows.item(i).getAttributes().getNamedItem(condition).getNodeValue().equals(value)) {
						doc.getDocumentElement().removeChild(rows.item(i));
						counter++;
					}
				}

			}
			if (operand.equals(">")) {
				for (int i = 0; i < rows.getLength(); i++) {
					if (Integer.parseInt(rows.item(i).getAttributes().getNamedItem(condition).getNodeValue()) > Integer
							.parseInt(value)) {
						doc.getDocumentElement().removeChild(rows.item(i));
						counter++;
					}
				}
			}
			if (operand.equals("<")) {
				for (int i = 0; i < rows.getLength(); i++) {
					if (Integer.parseInt(rows.item(i).getAttributes().getNamedItem(condition).getNodeValue()) < Integer
							.parseInt(value)) {
						doc.getDocumentElement().removeChild(rows.item(i));
						counter++;
					}
				}
			}
		}
		return counter;
	}

	public String[][] getColumns() {
		Document readtable = DOMFactory.getDomObj(dataFile);
		NodeList rows = readtable.getElementsByTagName("row");
		String[][] table = new String[rows.getLength()][rows.item(0).getAttributes().getLength()];
		/*
		 * for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
		 * table[0][j] = rows.item(0).getAttributes().item(j).getNodeName(); }
		 */
		for (int i = 0; i < rows.getLength(); i++) {
			for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
				table[i][j] = rows.item(i).getAttributes().item(j).getNodeValue();
			}
		}
		return table;
	}

	public String[][] getCertainColumns(String[] columnsName) {
		Document readtable = DOMFactory.getDomObj(dataFile);
		NodeList rows = readtable.getElementsByTagName("row");
		String[][] table = new String[rows.getLength()][columnsName.length];
		/*
		 * for (int j = 0; j < columnsName.length; j++) { table[0][j] = columnsName[j];
		 * }
		 */
		for (int i = 0; i < rows.getLength(); i++) {
			int count = 0;
			for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
				if (rows.item(i).getAttributes().item(j).getNodeName().equals(columnsName[count])) {
					table[i][count] = rows.item(i).getAttributes().item(j).getNodeValue();
					count++;
				}
			}
			if (count != columnsName.length) {
				return null;
			}
		}
		return table;
	}

	public String[][] getOneColumn(String columnName) {
		Document readtable = DOMFactory.getDomObj(dataFile);
		NodeList rows = readtable.getElementsByTagName("row");
		String[][] table = new String[rows.getLength()][1];
		// table[0][0] = columnName;
		for (int i = 0; i < rows.getLength(); i++) {
			int count = 0;
			for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
				if (rows.item(i).getAttributes().item(j).getNodeName().equals(columnName)) {
					table[i][count] = rows.item(i).getAttributes().item(j).getNodeValue();
					count++;
				}
			}
			if (count != 1) {
				return null;
			}
		}
		return table;
	}

	public boolean columnExists(String columnName) {
		Document readtable = DOMFactory.getDomObj(dataFile);
		NodeList rows = readtable.getElementsByTagName("row");
		for (int i = 0; i < rows.getLength(); i++) {
			int count = 0;
			for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
				if (rows.item(i).getAttributes().item(j).getNodeName().equals(columnName)) {
					return true;
				}
			}
		}
		return false;
	}

	public Object[][] WherePart(Object[][] cols, String[] splitted, Table table) {
		if (cols == null) {
			throw new RuntimeException("Error invalid columns");
		}
		Object[][] filtered = new Object[cols.length][cols[0].length];
		splitted = deleteQuotes(splitted);
		String columnName = splitted[splitted.length - 3];
		String operator = splitted[splitted.length - 2];
		String comparedValue = splitted[splitted.length - 1];

		if (!table.columnExists(columnName)) {
			throw new RuntimeException("Error in where clause, Column " + columnName + " doesn't exist.");
		}

		int columnIndex = 0;
		if (isInt(comparedValue)) {
			Integer value = Integer.parseInt(comparedValue);
			if (getType(columnName).equals("int")) {
				try {
					int i = 0;
					for (Object[] row : cols) {
						ArrayList<Object> filteredCells = new ArrayList<>();
						i = 0;
						for (Object cell : row) {
							Object x = getCell(i, columnName);
							if (compare(x, value, operator))
								filteredCells.add(cell);
							i++;
						}
						filtered[columnIndex++] = filteredCells.toArray();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (isLetter(comparedValue)) {
			String value = comparedValue.toString();
			if (getType(columnName).equals("varchar")) {
				int i = 0;
				columnIndex = 0;
				for (Object[] row : cols) {
					ArrayList<Object> filteredCells = new ArrayList<>();
					i = 0;
					for (Object cell : row) {
						Object x = getCell(i, columnName);
						if (compare(x, value, operator))
							filteredCells.add(cell);
						i++;
					}
					filtered[columnIndex++] = filteredCells.toArray();
				}
			}
		}
		return filtered;
	}

	private static boolean isInt(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			int i = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private boolean isLetter(String s) {
		if (s == null) {
			return false;
		}
		int len = s.length();
		for (int i = 0; i < len; i++) {
			if ((Character.isLetter(s.charAt(i)) == true)) {
				return true;
			}
		}
		return false;
	}

	private boolean compare(Object val1, Object val2, String operator) {
		if (val1 instanceof Integer && val2 instanceof Integer) {
			Integer value1 = (Integer) val1;
			Integer value2 = (Integer) val2;
			if (operator.equals("=")) {
				return Integer.compare(value1.intValue(), value2.intValue()) == 0 ? true : false;
			} else if (operator.equals(">")) {
				return Integer.compare(value1.intValue(), value2.intValue()) > 0 ? true : false;
			} else if (operator.equals("<")) {
				return Integer.compare(value1.intValue(), value2.intValue()) < 0 ? true : false;
			}
		} else if (val1 instanceof String && val2 instanceof String) {
			String value1 = (String) val1;
			String value2 = (String) val2;
			if (operator.equals("=")) {
				return value1.compareToIgnoreCase(value2) == 0 ? true : false;
			} else if (operator.equals(">")) {
				return value1.compareTo(value2) > 0 ? true : false;
			} else if (operator.equals("<")) {
				return value1.compareTo(value2) < 0 ? true : false;
			}
		}
		return false;
	}

	private String getType(String columnName) {
		Document doc = DOMFactory.getDomObj(schemaFile);
		NodeList elements = doc.getElementsByTagName("element");
		String type = null;
		for (int i = 0; i < elements.getLength(); i++) {
			if (elements.item(i).getAttributes().item(0).getNodeName().equals(columnName)) {
				type = elements.item(i).getAttributes().item(1).getNodeValue();
			}
		}
		return type;
	}

	public Object getCell(int index, String ColumnName) {
		Document readtable = DOMFactory.getDomObj(dataFile);
		NodeList rows = readtable.getElementsByTagName("row");
		Object cell = new Object();
		for (int i = 0; i < rows.getLength(); i++) {
			for (int j = 0; j < rows.item(0).getAttributes().getLength(); j++) {
				if (i == index && rows.item(i).getAttributes().item(j).getNodeName().equals(ColumnName)) {
					cell = rows.item(i).getAttributes().item(j).getNodeValue();
				}
			}
		}
		return cell;
	}

	public static String[] deleteQuotes(String[] splittedQuery) {
		ArrayList<String> filtered = new ArrayList<>();
		for (String x : splittedQuery) {
			if (x.charAt(0) == '\'') {
				filtered.add(x.substring(1, x.length() - 1));
			} else {
				filtered.add(x);
			}
		}
		return filtered.toArray(new String[0]);
	}

}