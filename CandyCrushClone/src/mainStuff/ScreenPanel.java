package mainStuff;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;


public class ScreenPanel extends JPanel{	

	private static final long serialVersionUID = 967098051215874682L;
	

	private Timer timer;
	private GamePanel gamePanel = new GamePanel();
	
	public ScreenPanel(float FPS) {
		timer = new Timer();
		TimerTask task = new TimerTask() {public void run() {update(); MainClass.time++;} };
		timer.schedule(task, 0, (int) (1000/FPS));
		
		setLayout(null);
		add(gamePanel);
	}	
	
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		setBackground(Color.BLACK);
    }
	
	public void update() {
		repaint();
		gamePanel.setBounds(getWidth()/2 - getHeight()/2, 0, getHeight(), getHeight());
	}
}
