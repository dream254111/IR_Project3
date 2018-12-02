import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

	private View view;
	private Model model;
	
	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		
		view.addSearchListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				view.resetTable();
				model.receiveData(view.getTextInput());
				view.setTable(model.sendData());
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
