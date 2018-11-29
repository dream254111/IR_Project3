import java.io.*;
import java.time.format.TextStyle;

public class Document {
	private String j_id = "";
	private String j_title = "";
	private String a_id = "";
	private String a_title = "";
	private String issue_id = "";
	private String content = "";
	
	public Document(String j_id, String j_title, String a_id, String a_title, String issue_id, String content) {
		this.j_id = j_id;
		this.j_title = j_title;
		this.a_id = a_id;
		this.a_title = a_title;
		this.issue_id = issue_id;
		this.content = content;
	}
	public Document(File file) {
		BufferedReader reader = null;
		String texts = "";
		try {
			reader = new BufferedReader(new FileReader(file));
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
		this.j_id = text[0];
		this.j_title = text[1];
		this.a_id = text[2];
		this.a_title = text[3];
		this.issue_id = text[4];
		this.content = text[5];
	}

	public String getJ_id() {
		return j_id;
	}

	public void setJ_id(String j_id) {
		this.j_id = j_id;
	}

	public String getJ_title() {
		return j_title;
	}

	public void setJ_title(String j_title) {
		this.j_title = j_title;
	}

	public String getA_id() {
		return a_id;
	}

	public void setA_id(String a_id) {
		this.a_id = a_id;
	}

	public String getA_title() {
		return a_title;
	}

	public void setA_title(String a_title) {
		this.a_title = a_title;
	}

	public String getIssue_id() {
		return issue_id;
	}

	public void setIssue_id(String issue_id) {
		this.issue_id = issue_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString()
	{
		return "Journal ID:"+this.j_id+"\nJournal Title: "+this.j_title+
				"\nArticle ID:"+this.a_id+"\nArticle Title:"+this.a_title+
				"\nIssue ID:"+this.issue_id+"\nContent\n"+(this.content.length() > 50? this.content.substring(0, 50)+"...":this.content)+"]";
	}
	
	
}
