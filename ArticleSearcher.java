import java.io.File;
import java.util.HashSet;

public class ArticleSearcher {
	//set of document
	static HashSet<Document> docs = new HashSet<>();
	
	public static void main(String[] args) {
		File folder = new File("./DB");
		for(File fileEntry : folder.listFiles()) {
			docs.add(new Document(fileEntry));
		}
		for(Document doc : docs) {
			System.out.println(doc.toString());
			System.out.println("======================");
		}
		System.out.println(docs.size());
	}
}
