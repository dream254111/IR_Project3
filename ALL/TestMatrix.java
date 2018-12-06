import Jama.Matrix;

public class TestMatrix {
	static double[][] array = {{1.,2.,3.},{4.,5.,6.},{7.,8.,10.}};
	
	public static void main(String[] args) {
		Matrix a = new Matrix(array);
		double det = a.det();
		System.out.println(det);
	}
}
