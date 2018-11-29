import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class XMLReader {
	
	public static String readContent (String a_id) {
		String content = "";
		File file = new File(".\\1\\ocr\\journal-article-10.2307_" + a_id + ".txt");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
					
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("page");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				content += nNode.getTextContent();
			}
			
			content = content.replaceAll("[^a-zA-Z0-9\\s]", "");
			
			return content;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int Reader(File file) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
					
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("article");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element eElement = (Element) nNode;
					
					String j_title = eElement.getElementsByTagName("journal-title").item(0).getTextContent();
					String j_id = eElement.getElementsByTagName("journal-id").item(1).getTextContent();
					String a_title = eElement.getElementsByTagName("article-title").item(0).getTextContent();
					String a_id = eElement.getElementsByTagName("article-id").item(0).getTextContent();
					String i_id = eElement.getElementsByTagName("issue-id").item(0).getTextContent();
					
					if(j_title.equals("") || j_id.equals("") || a_title.equals("") || a_id.equals("") || i_id.equals("")) {
						System.out.println("ERROR");
						return 0;
					}
					else {
						if(a_id.contains("/"))
							a_id = a_id.substring(8, a_id.length());
						
						System.out.println("Journal Title: " + j_title);
						System.out.println("   Journal ID: " + j_id);
						System.out.println("Article Title: " + a_title);
						System.out.println("   Article ID: " + a_id);
						System.out.println("     Issue ID: " + i_id);
						
						String content = readContent(a_id);
						
						System.out.println("      Content: " + content);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public static void main(String argv[]) {
		File folder = new File(".\\1\\metadata");
		int count = 1;
		for(File fileEntry : folder.listFiles()) {
			System.out.println("==================");
			System.out.println("File " + count);
			count += Reader(fileEntry);
			if(count > 1) break;
		}
	}
}
