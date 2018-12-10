import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class TextFrame extends JFrame{
	
	JTextArea area = new JTextArea(20, 20);
	JScrollPane scroll = new JScrollPane(area);
	
	public TextFrame(String str, String query) throws BadLocationException {
		area.setText(str);
		area.setLineWrap(true);
		area.setEditable(false);
		
		Highlighter highlight = area.getHighlighter();
		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
		
		boolean end = false;
		int passText = 0;
		String nStr = str.toLowerCase();
		query = query.toLowerCase();
		String[] lQuery = query.split(" ");
		
		ArrayList<Object> debug = new ArrayList<Object>();
		int p0 = 0, p1, p2 = -100;
		
		for (String entry : lQuery) {
			passText = 0;
			nStr = str.toLowerCase();
			while(true) {
				p0 = nStr.indexOf(entry);
				if(p0 == -1) break;
				p1 = p0 + entry.length();
				
				debug.add(highlight.addHighlight(p0 + passText, p1 + passText, painter));
				passText += p1;
				nStr = nStr.substring(p1);
			}
		}
		
			
		System.out.println(query);
		
		this.add(scroll);
		
		this.setSize(460, 330);
		this.setTitle("Content");
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
}
