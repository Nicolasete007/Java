package mainStuff;

import javax.swing.JFrame;

public class MainClass {

	public static ScreenPanel screen;
	public static long time = 0;
	
	public static void main(String[] args) {
		
		JFrame f = new JFrame();
		screen = new ScreenPanel(60);
		
		f.setTitle("Candy Crush Clone");
		f.setBounds(10, 10, 800, 600);
		f.add(screen);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setVisible(true);

	}
}
