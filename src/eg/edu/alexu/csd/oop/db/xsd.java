package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.XMLConstants;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xsd {

	private File schemaFile ;
	public  xsd(File schemaFile) {
		this.schemaFile =schemaFile;
		
	}
	
public File createSchema () {
		 
	
		 final String NS_PREFIX = "xs:";
	 try {
         DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

         Document doc = docBuilder.newDocument();

         Element schemaRoot = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, NS_PREFIX+"schema");
         doc.appendChild(schemaRoot);

         NameTypeElementMaker elMaker = new NameTypeElementMaker(NS_PREFIX, doc);

      

         Element itemType = elMaker.createElement("complexType", "row");
         schemaRoot.appendChild(itemType);
         Element sequence = elMaker.createElement("sequence");
         itemType.appendChild(sequence);
         Element ageElement = elMaker.createElement("element", "age", "int");
         sequence.appendChild(ageElement);
         Element dateElement = elMaker.createElement("element", "date", "int");
         sequence.appendChild(dateElement);         



         TransformerFactory tFactory = TransformerFactory.newInstance();
         Transformer transformer = tFactory.newTransformer();
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         DOMSource domSource = new DOMSource(doc);
         transformer.transform(domSource, new StreamResult(schemaFile));
        return schemaFile;
       
         
	 }  
     
     catch (FactoryConfigurationError | ParserConfigurationException | TransformerException e) {
         //handle exception
         e.printStackTrace();
     }
	return schemaFile;
	 
 
		 }
public void UpdatewhenDelete () throws IOException, ParserConfigurationException, SAXException {
	
	 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
     DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
     Document doc = dBuilder.parse(schemaFile);
	
     NodeList element =  doc.getChildNodes();
System.out.println(element);
    // element.getParentNode().removeChild(element);
   
     //DOMFactory.writeDOMtoFile( doc, schemaFile);
	
}

 
 private static class NameTypeElementMaker {
     private String nsPrefix;
     private Document doc;

     public NameTypeElementMaker(String nsPrefix, Document doc) {
         this.nsPrefix = nsPrefix;
         this.doc = doc;
     }

     public Element createElement(String elementName, String nameAttrVal, String typeAttrVal) {
         Element element = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, nsPrefix+elementName);
         if(nameAttrVal != null)
             element.setAttribute("name", nameAttrVal);
         if(typeAttrVal != null)
             element.setAttribute("type", typeAttrVal);
         return element;
     }

     public Element createElement(String elementName, String nameAttrVal) {
         return createElement(elementName, nameAttrVal, null);
     }

     public Element createElement(String elementName) {
         return createElement(elementName, null, null);
     }
	


}
}
