package mainStuff;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Candy {
	
	private short row, col, colTo, rowTo;
	private static final float grv = .4f;
	private float fallSpeed;
	private float xCandy, yCandy;
	private byte type;
	private int offset = 10;
	private boolean grabbed = false;
	private boolean moving = false;
	private boolean falling = false;
	
	private BufferedImage[] images = new BufferedImage[4];
	

	public static final byte NO_COMBO = -1,
			COMBO_THREE = 0,
			COMBO_FOUR	= 1,
			COMBO_FIVE	= 2,
			COMBO_L		= 3,
			COMBO_T		= 4;
	
	private byte comboType = NO_COMBO;
	
	public Candy(byte type, short row, short col) {
		this.type = type;
		this.row = row;
		this.col = col;
		xCandy = col*GamePanel.getGameWidth()/GamePanel.maxCols;
		yCandy = row*GamePanel.getGameHeight()/GamePanel.maxRows;
		
		try {
			images[0]  = ImageIO.read(Candy.class.getResourceAsStream("candy1.png"));
			images[1]  = ImageIO.read(Candy.class.getResourceAsStream("candy2.png"));
			images[2]  = ImageIO.read(Candy.class.getResourceAsStream("candy3.png"));
			images[3]  = ImageIO.read(Candy.class.getResourceAsStream("candy4.png"));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void paintCandy(Graphics g) {
		
		if (grabbed) {
			offset = (int) (.04f * GamePanel.getGameWidth()/GamePanel.maxCols);
		}
		else offset = (int) (.07f * GamePanel.getGameWidth()/GamePanel.maxCols);
		
		Graphics2D g2 = (Graphics2D) g;
		
		int size = (int) ((float) GamePanel.getGameWidth()/GamePanel.maxCols - 2*offset);
		
		try {
			g2.drawImage(resizeImage(images[type], size), (int) xCandy + offset, (int) yCandy + offset, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//visualizeCombo(g);
	}
	
	@SuppressWarnings("unused")
	private void visualizeCombo(Graphics g) {
		
		g.setFont(new Font("Verdana", Font.BOLD, 20));
		g.setColor(Color.BLACK);
		
		if (comboType == COMBO_THREE) {
			g.drawString("3", col*GamePanel.getGameWidth()/GamePanel.maxCols, row*GamePanel.getGameHeight()/GamePanel.maxRows + 20);
		} else if (comboType == COMBO_FOUR) {
			g.drawString("4", col*GamePanel.getGameWidth()/GamePanel.maxCols, row*GamePanel.getGameHeight()/GamePanel.maxRows + 20);
		} else if (comboType == COMBO_FIVE) {
			g.drawString("5", col*GamePanel.getGameWidth()/GamePanel.maxCols, row*GamePanel.getGameHeight()/GamePanel.maxRows + 20);
		}
	}
	
	public byte getType() {
		return type;
	}
	
	public void setCombo(int combo) {
		comboType = (byte) combo;
	}
	
	public byte getCombo() {
		return comboType;
	}
	
	public void setGrabbed(boolean g) {
		grabbed = g;
	}
	
	public void moveCandy(short colTo, short rowTo) {
		moving = true;
		this.colTo = colTo;
		this.rowTo = rowTo;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public boolean isFalling() {
		return falling;
	}
	
	public void setFalling(boolean falling, short fallingTo) {
		this.falling = falling;
		rowTo = fallingTo;
	}
	
	public void update() {
		
		if (!falling) {
			fallSpeed = 0;
		}
		
		if (moving) {			
			short speed = (short) Math.max(1, 0.1*GamePanel.getGameWidth()/GamePanel.maxCols);
			xCandy += (colTo - col)*speed;
			yCandy += (rowTo - row)*speed;
			
			if (Math.abs(xCandy + speed + 1 - colTo*GamePanel.getGameWidth()/GamePanel.maxCols) <= speed ||
					Math.abs(yCandy + speed + 1 - rowTo*GamePanel.getGameHeight()/GamePanel.maxRows) <= speed) {
				col = colTo;
				row = rowTo;
				moving = false;
			}

		} else if (falling) {
			fallSpeed += grv;
			yCandy += fallSpeed;
			if (Math.abs(yCandy + fallSpeed + 1 - rowTo*GamePanel.getGameHeight()/GamePanel.maxRows) <= fallSpeed) {
				row = rowTo;
				falling = false;
			}
		} else {			
			xCandy = col*GamePanel.getGameWidth()/GamePanel.maxCols;
			yCandy = row*GamePanel.getGameHeight()/GamePanel.maxRows;
		}
		
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int size) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(originalImage, 0, 0, size, size, null);
	    graphics2D.dispose();
	    return resizedImage;
	}
}
