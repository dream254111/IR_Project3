import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class GUI {
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Example");
		
		JPanel panel1 = new JPanel();
		
		String[] headerTable = {"Journal Name", "Article Name"};
		String[][] bodyTable = {{"JournalA", "ArticleA"}, {"JournalB", "ArticleB"}, {"JournalC", "ArticleC"}};
		
		JButton button = new JButton("SEARCH");
		JTextField text = new JTextField(10);
		JTable table = new JTable(bodyTable, headerTable);
		JScrollPane scroll = new JScrollPane(table);
		
		table.setEnabled(false);
		
		panel1.add(text);
		panel1.add(button);
		panel1.add(scroll);
		
		frame.add(panel1);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(300, 500);
	}
}
