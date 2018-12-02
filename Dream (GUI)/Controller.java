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

				String[][] data = new String[10][3];
				for (int i = 0; i < result.size(); i++) {
					data[i][0] = result.get(i).getDocument().getJ_id();
					data[i][1] = result.get(i).getDocument().getA_id();
					data[i][2] = Double.toString(result.get(i).getScore());
				}
				
				view.resetTable();
				
				if(view.getTextInput().length() == 0) {
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
