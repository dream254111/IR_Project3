import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class createDataset {
	public static void main(String[] args) {
		File file = new File("./DB");
		File file2 = new File("./DataSet");
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		for (File entry : file.listFiles()) {
			
			String texts = "";
			try {
				reader = new BufferedReader(new FileReader(entry));
				String line;
				while((line = reader.readLine()) != null) {
					texts += line;
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch(Exception e) {
					e.printStackTrace();
				} 
			}
			String[] text = texts.split(",");
			String name = text[0] + "_" + text[2];
			
			try {
				writer = new BufferedWriter(new FileWriter("./DataSet/" + name + ".txt"));
				writer.append("Journal ID: " + text[0]);
				writer.newLine();
				writer.append("Journal Name: " + text[1]);
				writer.newLine();
				writer.append("Article ID: " + text[2]);
				writer.newLine();
				writer.append("Article Name: " + text[3]);
				writer.newLine();
				writer.append("Issue ID: " + text[4]);
				writer.newLine();
				writer.append("Content: " + text[5]);
				writer.newLine();
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
