import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class Controller {

	private View view;
	private ArticleSearcher model;
	
	public Controller(View view, ArticleSearcher model) {
		this.view = view;
		this.model = model;
		
		view.addSearchListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<SearchResult> result = model.search(view.getTextInput(), 10);
				boolean check = false;

				String[][] data = new String[10][2];
				for (int i = 0; i < 10; i++) {
					if(Double.isNaN(result.get(i).getScore()) || result.get(i).getScore() == 0.0) {
						check = true;
						continue;
					}
					data[i][0] = result.get(i).getDocument().getJ_title();
					data[i][1] = result.get(i).getDocument().getA_title();
//					data[i][2] = result.get(i).getDocument().getA_id();
//					data[i][3] = result.get(i).getDocument().getA_title();
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
