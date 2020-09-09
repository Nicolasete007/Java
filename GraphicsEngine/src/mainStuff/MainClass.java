package mainStuff;

import javax.swing.JFrame;

import mathStuff.MathClass;

public class MainClass {

	public static ScreenPanel screen;
	public static long time = 0;
	public static void main(String[] args) {
		
		JFrame f = new JFrame();
		screen = new ScreenPanel(60);
		
		f.addMouseMotionListener(new MouseController(f, screen));
		f.setBounds(300, 100, MathClass.screenWidth, MathClass.screenHeight); //x, y, w, h
		f.setTitle("3D Graphic Engine");
		f.add(screen);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setVisible(true);

	}
}
