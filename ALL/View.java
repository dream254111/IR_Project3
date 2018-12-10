import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class View extends JFrame{
	String[] header = {"Journal Name", "Article Name"};
	
	JPanel input = new JPanel();
	JPanel output = new JPanel();
	
	JTextField inputText = new JTextField(25);
	JButton search = new JButton("SEARCH");
	JButton clear = new JButton("CLEAR");
	
	JTable table = new JTable(new DefaultTableModel(header, 0)) {
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	JScrollPane tableContainer = new JScrollPane(table);
	ListSelectionModel cellSelect = table.getSelectionModel();
	
	public View() {	
		
		tableContainer.setPreferredSize(new Dimension(800, 23));
		table.setCellSelectionEnabled(true);
		cellSelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
//		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		table.setEnabled(false);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setPreferredWidth(500);
		
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
		this.setSize(480, 72);
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
	
	public void addTableListerner(ListSelectionListener action) {
		cellSelect.addListSelectionListener(action);
	}
	
	// Set Input
	public String getTextInput() {
		return inputText.getText();
	}
	
	// Reset Value
	public void reset() {
		this.resetText();
		this.setSize(480, 72);
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
		if(dataset[0][0].length() > 0) {
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			for (int i = 0; i < dataset.length; i++) {
				tableModel.addRow(dataset[i]);
			}
			
			tableContainer.setVisible(true);
			tableContainer.setPreferredSize(new Dimension(800, 183));
			this.setSize(850, 265);
		}
		else {
			this.reset();
			this.resetTable();
		}
	}
	
	public JTable getTable() {
		return this.table;
	}

	// Test
	public String noRow() {
		int num = table.getRowCount();
		return Integer.toString(num);
	}
}
