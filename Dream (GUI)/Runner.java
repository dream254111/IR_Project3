import java.io.File;
import java.io.IOException;
import java.util.List;

public class Runner {
	
	public static void main(String[] args) throws IOException {
		
		File file = new File("./DB");
		
		ArticleSearcher test = new ArticleSearcher(file);	
		View view = new View();
		Controller controller = new Controller(view, test);
	}
	
}

