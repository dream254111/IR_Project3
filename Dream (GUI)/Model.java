
public class Model {
	
	String[][] data;
	
	public void receiveData(String str) {
		data = new String[10][2];
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < 2; j++) {
				data[i][j] = str + Integer.toString(i+1);
			}
		}
	}
	
	public String[][] sendData() {
		return data;
	}
	
}
