import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
					int i = 1;
					
					Element eElement = (Element) nNode;
					
					String j_title = eElement.getElementsByTagName("journal-title").item(0).getTextContent();
					while(eElement.getElementsByTagName("journal-id").item(i).equals(eElement.getElementsByTagName("journal-id").item(i-1))) i++;
					String j_id = eElement.getElementsByTagName("journal-id").item(i).getTextContent();
					String a_title = eElement.getElementsByTagName("article-title").item(0).getTextContent();
					String a_id = eElement.getElementsByTagName("article-id").item(0).getTextContent();
					String i_id = eElement.getElementsByTagName("issue-id").item(0).getTextContent();
					
					if(j_title.equals("") || j_id.equals("") || a_title.equals("") || a_id.equals("") || i_id.equals(""))
						return 0;
					else {
						if(a_id.contains("/"))
							a_id = a_id.substring(8, a_id.length());
						
						j_title = j_title.replaceAll("[^a-zA-Z0-9\\s]", "");
						a_title = a_title.replaceAll("[^a-zA-Z0-9\\s]", "");
						
						String content = readContent(a_id);
						
						if(content == null || content.length() < 10) return 0;
						
						BufferedWriter writer = null;
						File newFile = new File(".\\DB\\" + j_id + "_" + a_id + ".txt");
						
						try {
							writer = new BufferedWriter(new FileWriter(newFile));
							writer.append(j_id + ",");
							writer.append(j_title + ",");
							writer.append(a_id + ",");
							writer.append(a_title + ",");
							writer.append(i_id + ",");
							writer.append(content);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if(writer != null) writer.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
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
			count += Reader(fileEntry);
			if(count > 2500) break;
		}
	}
}
