import java.io.*;
import java.time.format.TextStyle;
import java.util.List;

public class Document {
	private String j_id = null;
	private String j_title = null;
	private String a_id = null;
	private String a_title = null;
	private String issue_id = null;
	private String content = null;
	private List<String> contentTokens = null;	//tokens after preprocessing raw text
	private List<String> titleTokens = null;
	
	public Document(String j_id, String j_title, String a_id, String a_title,
			String issue_id, String content, List<String> contentTokens, List<String> titleTokens) {
		this.j_id = j_id;
		this.j_title = j_title;
		this.a_id = a_id;
		this.a_title = a_title;
		this.issue_id = issue_id;
		this.content = content;
		this.contentTokens = contentTokens;
		this.titleTokens = titleTokens;
	}
	
	public Document(String j_title, String a_id, String a_title, String content){
		this.j_title = j_title;
		this.a_id = a_id;
		this.a_title = a_title;
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
//		for(String t : text) {
//			System.out.println(t);
//		}
//
//		System.out.println("++++++++++++++++++++++++++");
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

	public List<String> getContentTokens() {
		return contentTokens;
	}

	public void setContentTokens(List<String> tokens) {
		this.contentTokens = tokens;
	}
	
	public List<String> getTitleTokens() {
		return titleTokens;
	}

	public void setTitleTokens(List<String> tokens) {
		this.titleTokens = tokens;
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
	
	public int compareTo(Object arg0) {
		Document other = (Document)arg0;
		if(this.a_id == other.a_id && this.content != null && other.content != null) return this.content.compareTo(other.content);
		return this.a_id.compareTo(other.a_id);
	}
	
}
