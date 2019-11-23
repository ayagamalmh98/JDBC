package eg.edu.alexu.csd.oop.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;

public class Table {
    private File dataFile;
    private File schemaFile;

    public Table(String name, File databasepath) {
        dataFile = new File(databasepath, name.toLowerCase() + ".xml");
        schemaFile = new File(databasepath, name.toLowerCase() + ".xsd");
        try {
            if (!dataFile.createNewFile() || !schemaFile.createNewFile()) {
                File[] tables = databasepath.listFiles();
                if (tables != null) {
                    for (File table : tables
                    ) {
                        if (table.isFile() && table.getName().substring(0, table.getName().length() - 4).toLowerCase().equals(name)) {
                            if (table.getName().endsWith(".xml")) {
                                dataFile = table;
                            }
                            if (table.getName().endsWith(".xsd")) {
                                schemaFile = table;
                            }

                        }
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Error loading old databases.");
        }

    }

    public int insert(String[] columns , String [] values) {

            Document doc = DOMFactory.getDomObj(dataFile);
            if(doc!=null){
                Element newRow = doc.createElement("row");
                for (int i=0;i<columns.length;i++){
                    newRow.setAttribute(columns[i],values[i]);
                }
                doc.getDocumentElement().appendChild(newRow);
                if(DOMFactory.validateXML(doc,schemaFile)){
                    DOMFactory.writeDOMtoFile(doc,dataFile);
                }else {
                    System.out.println("Not a valid query");
                    return 0;
                }
                return columns.length;
            }
            return 0;
    }
    public int update (String[] columns , String [] values , String column, String value){
        int counter =0;
        Document doc = DOMFactory.getDomObj(dataFile);
        if(doc!=null) {
            NodeList rows = doc.getElementsByTagName("row");
            for(int i=0;i<rows.getLength();i++){
                if(rows.item(i).getAttributes().getNamedItem(column).getNodeValue().equals(value)){
                    for (int  j=0;j<columns.length;j++){
                        rows.item(i).getAttributes().getNamedItem(columns[j]).setNodeValue(values[j]);
                    }
                    counter++;
                }
            }
            if(DOMFactory.validateXML(doc,schemaFile)){
                DOMFactory.writeDOMtoFile(doc,dataFile);
            }else {
                System.out.println("Not a valid query");
                return 0;
            }
        }
        return counter;
        }


}
