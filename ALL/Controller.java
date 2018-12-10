import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

public class Controller {

	private View view;
	private ArticleSearcher model;
	List<SearchResult> result = null;
	TextFrame frame = null;
	String query = null;
	
	public Controller(View view, ArticleSearcher model) {
		this.view = view;
		this.model = model;

		view.addTableListerner(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(frame != null)
					frame.dispose();
//				List<SearchResult> result = model.search(view.getTextInput(), 10);
				String selectData = "";
				
				int[] selectRow = view.getTable().getSelectedRows();
				int[] selectCol = view.getTable().getSelectedColumns();
				
				for (int i = 0; i < selectRow.length; i++) {
					for (int j = 0; j < selectCol.length; j++) {
						selectData = (String) view.getTable().getValueAt(selectRow[i], selectCol[j]);
					}
				}
				
				for (SearchResult search : result) {
					if(search.getDocument().getA_title().equals(selectData)) {
						try {
							frame = new TextFrame(search.getDocument().getContent(), query);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
						break;
					}
				}
			}
		});
		
		view.addSearchListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				query = view.getTextInput();
				result = model.search(query, 10);
				boolean check = false;

				String[][] data = new String[10][2];
				for (int i = 0; i < 10; i++) {
					if(Double.isNaN(result.get(i).getScore())) {
						check = true;
						continue;
					}
					if(result.get(i).getScore() == 0.0) {
						continue;
					}
					data[i][0] = result.get(i).getDocument().getJ_title();
					data[i][1] = result.get(i).getDocument().getA_title();
				}
				
				view.resetTable();
				
				if(view.getTextInput().length() == 0) {
					view.reset();
					view.resetTable();
					view.resetText();
				}
				else if (check) {
					view.reset();
					view.resetTable();
					view.resetText();
				}
				else
					view.setTable(data);
				
				view.resetText();
			}
		});
		
		view.addClearListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				view.reset();
				view.resetTable();
			}
		});
	}
	
}
