import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class View extends JFrame{
	String[] header = {"Journal Name", "Article Name"};
	
	JPanel input = new JPanel();
	JPanel output = new JPanel();
	
	JTextField inputText = new JTextField(10);
	JButton search = new JButton("SEARCH");
	JButton clear = new JButton("CLEAR");
	
	JTable table = new JTable(new DefaultTableModel(header, 0));
	JScrollPane tableContainer = new JScrollPane(table);

	public View() {	
		
		tableContainer.setPreferredSize(new Dimension(290, 23));
		table.setEnabled(false);
		
		input.add(inputText);
		input.add(search);
		input.add(clear);
		
		output.add(tableContainer);
		
		this.add(input, BorderLayout.NORTH);
		this.add(output, BorderLayout.CENTER);
		
		this.setTitle("Arcademic Journal Search Engine");
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 72);
		this.setResizable(false);
	}
	
	// Add Listener
	public void addSearchListerner(ActionListener action) {
		search.addActionListener(action);
		inputText.addActionListener(action);
	}
	
	public void addClearListener(ActionListener action) {
		clear.addActionListener(action);
	}
	
	// Set Input
	public String getTextInput() {
		return inputText.getText();
	}
	
	// Reset Value
	public void reset() {
		this.resetText();
		this.setSize(400, 72);
		tableContainer.setVisible(false);
	}
	
	public void resetTable() {
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		int num = table.getRowCount();
		for (int i = 0; i < num; i++) {
			tableModel.removeRow(0);
		}
	}
	
	public void resetText() {
		inputText.setText("");
	}
	
	// Set Output
	public void setTable(String[][] dataset) {		
		if(dataset[0][0].length() > 1) {
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			for (int i = 0; i < dataset.length; i++) {
				tableModel.addRow(dataset[i]);
			}
			
			tableContainer.setVisible(true);
			tableContainer.setPreferredSize(new Dimension(375, 183));
			this.setSize(400, 265);
		}
		else {
			this.reset();
			this.resetTable();
		}
	}

	// Test
	public String noRow() {
		int num = table.getRowCount();
		return Integer.toString(num);
	}
}
