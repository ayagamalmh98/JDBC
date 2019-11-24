package eg.edu.alexu.csd.oop.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;

public class Table {
    private File dataFile;
    private File schemaFile;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Table(String name, File databasepath) {
        dataFile = new File(databasepath, name + ".xml");
        schemaFile = new File(databasepath, name + ".xsd");
        try {
            dataFile.createNewFile();
            schemaFile.createNewFile();

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

    String getName(){
        return dataFile.getName().substring(0,dataFile.getName().length()-4);
    }
}
