import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Controller {

	private View view;
	private ArticleSearcher model;
	
	public Controller(View view, ArticleSearcher model) {
		this.view = view;
		this.model = model;
		
//		view.addSelectCellListener(new ListSelectionListener() {
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				String select = null;
//				
//				int[] selectRow = view.getTable().getSelectedRows();
//				int[] selectColumn = view.getTable().getSelectedColumns();
//				
//				select = (String) view.getTable().getValueAt(selectRow[0], selectColumn[0]);
//				
//				view.setContentText(select);
//				view.setContent();
//			}
//		});
		
		view.addSearchListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<SearchResult> result = model.search(view.getTextInput(), 10);
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
