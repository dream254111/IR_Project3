import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLReader {
	
	public static void Reader(File file) {
		try {

			File fXmlFile = file;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
					
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
			
			NodeList nList = doc.getElementsByTagName("article");
					
			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out.println("Journal id 1: " + eElement.getElementsByTagName("journal-id").item(0).getTextContent());
					System.out.println("Journal id 2: " + eElement.getElementsByTagName("journal-id").item(1).getTextContent());
					System.out.println("Journal title : " + eElement.getElementsByTagName("journal-title").item(0).getTextContent());
					System.out.println("Article id : " + eElement.getElementsByTagName("article-id").item(0).getTextContent());
					System.out.println("Article name : " + eElement.getElementsByTagName("article-title").item(0).getTextContent());
					System.out.println("Publish date : ");
					System.out.println("Issue id : " + eElement.getElementsByTagName("issue-id").item(0).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String argv[]) {
		File folder = new File("./1/metadata");
		int count = 0;
		for(File fileEntry : folder.listFiles()) {
			Reader(fileEntry);
			count++;
			if(count > 20) break;
		}
	}
}
