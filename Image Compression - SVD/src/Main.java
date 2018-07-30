import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {

	private static JFrame frame;
	
	public static void main(String s[]) {
		frame = new JFrame("SVD - Image Compression");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new GUI());
		frame.setSize(new Dimension(350, 150));
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	public static void packMe()
	{
		frame.pack();
	}
	
}
