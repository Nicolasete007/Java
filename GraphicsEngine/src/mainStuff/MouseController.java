package mainStuff;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class MouseController extends MouseAdapter{

	private static Robot robot = null;
	private JFrame frame;
	private ScreenPanel screen;
	private Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
		    new Point(0, 0), "blank cursor");
	
	public MouseController(JFrame frame, ScreenPanel screen) {
		this.frame  = frame;
		this.screen = screen;
		
		try {
			robot = new Robot();			
		} catch (AWTException e) {e.printStackTrace();}
		
	}

	public void mouseMoved(MouseEvent e) {

		if (!IsKeyPressed.isUpPressed()) {

			float sensibility = 0.004f;
			
			float dyaw	= (float) (frame.getX() + frame.getWidth() /2 - e.getXOnScreen());
			float dpitch= (float) (frame.getY() + frame.getHeight()/2 - e.getYOnScreen());
			
			screen.yaw += dyaw * sensibility;
			screen.pitch += dpitch * sensibility;
			
			screen.pitch = (float) Math.max(screen.pitch, -Math.PI/2 + 0.001);
			screen.pitch = (float) Math.min(screen.pitch, Math.PI/2 - 0.001);
			
			robot.mouseMove(
					frame.getX() + frame.getWidth() /2,
					frame.getY() + frame.getHeight()/2);
			screen.setCursor(blankCursor);
		} else {screen.setCursor(Cursor.getDefaultCursor());}
		
		
		
	}	
}
