package mainStuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import mathStuff.MathStuff;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 2760176606082150286L;
	
	public static final short maxRows = 6;
	public static final short maxCols = 6;

	private short colPressed= -1, rowPressed= -1;
	private short nextCol	= -1, nextRow	= -1;

	private static int width;
	private static int height;
	
	private boolean candySwitching = false;
	private boolean candyFalling = false;

	private static Candy[][] screenCandies = new Candy[maxCols][maxRows];
	private short numCandies = 4; //max 5
	
	private Mouse mouse = new Mouse();
	private int timer = 10;

	public GamePanel() {
		
		setBackground(new Color(255, 192, 203));
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
		
		for (short row = 0; row < maxRows; row++) {
			for (short col = 0; col < maxCols; col++) {
				Candy candy = new Candy((byte) MathStuff.randomRange(0, numCandies), row, col);
				screenCandies[col][row] = candy;
			}
		}
	}
	
	protected void paintComponent(Graphics g) {
		update();
		
		super.paintComponent(g);
		drawGrid(g);
		
		for (short row = 0; row < maxRows; row++) {
			for (short col = 0; col < maxCols; col++) {
				screenCandies[col][row].paintCandy(g);	
			}
		}
	}
	
	private void update() {

		candySwitching = false;
		for (short row = 0; row < maxRows; row++) {
			for (short col = 0; col < maxCols; col++) {
				if (screenCandies[col][row].isMoving()) {
					candySwitching = true;
					break;
				}
			}
		}
		
		candyFalling = false;
		for (short row = 0; row < maxRows; row++) {
			for (short col = 0; col < maxCols; col++) {
				if (screenCandies[col][row].isFalling()) {
					candyFalling = true;
					break;
				}
			}
		}
		
		for (short row = 0; row < maxRows; row++) {
			for (short col = 0; col < maxCols; col++) {
				screenCandies[col][row].update();
			}
		}
		
		if (timer >= 0) timer--;
		else if (!candySwitching && !candyFalling) checkCombos();

		if (!candySwitching) {
			short[] voids = markNull();
	
			for (short col = 0; col < maxCols; col++) {
				for (short row = maxRows - 1; row >= 0; row--) {
					
					if (screenCandies[col][row] == null) {
						short fallTo = row;
						short firstToFall = (short) Math.max(row - 1, 0);
						while (screenCandies[col][firstToFall] == null && firstToFall > 0) firstToFall--;
						switchCandies(col, fallTo, col, firstToFall);
						if (screenCandies[col][fallTo] != null) screenCandies[col][fallTo].setFalling(true, fallTo);
					}
	
				}
			}
			
			if (voids != new short[maxCols]) {
				for (short col = 0; col < maxCols; col++) {
					for (short row = 0; row < voids[col]; row++) {
						dropCandy(col, (short) (row - voids[col]), row);
					}
				}
			}
		}

	}

	private short[] markNull() {
		
		short voids[] = new short[maxCols];
		for (short row = 0; row < maxRows; row++) {
			for (short col = 0; col < maxCols; col++) {
				try {
					if (screenCandies[col][row].getCombo() != Candy.NO_COMBO) {
						screenCandies[col][row] = null;
						voids[col]++;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
		return voids;
	}

	private void dropCandy(short col, short row, short rowTo) {
		screenCandies[col][rowTo] = new Candy((byte) MathStuff.randomRange(0, numCandies), row, col);
		screenCandies[col][rowTo].setFalling(true, rowTo);
	}
	
	@SuppressWarnings("unused")
	private void printList(short[] voids) {
		System.out.println();
		for (short v: voids) System.out.print(v);
		System.out.println();
	}
	
	@SuppressWarnings("unused")
	private void printCandies() {
		System.out.println();
		for (short row = 0; row < maxRows; row++) {
			for (short col = 0; col < maxCols; col++) {
				try {
					System.out.print("  ");
					System.out.print(screenCandies[col][row].getType());
					System.out.print("  ");
				} catch (Exception e) {
					System.out.print("null");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private void drawGrid(Graphics g) {

		width = getWidth();
		height = getHeight();
		
		g.setColor(new Color(234, 137, 154));
		
		//for (short row = 1; row < maxRows; row++) g.drawLine(0, row * height/maxRows, width, row * height/maxRows);
		//for (short col = 1; col < maxCols; col++) g.drawLine(col * width/maxCols, 0, col * width/maxCols, height);
	}

	private void checkCombos() {
		for (short row = 0; row < maxRows; row++) {
			for (short col = 0; col < maxCols; col++) {
				checkVerticalCombos(col, row);
				checkHorizontalCombos(col, row);
			}
		}
	}

	private void checkVerticalCombos(short col, short row) {
		
		if (screenCandies[col][row].getCombo() != Candy.NO_COMBO) return;
		
		if (row+1 < maxRows && screenCandies[col][row].getType() == screenCandies[col][row + 1].getType()) {
			
			if (row+2 < maxRows && screenCandies[col][row].getType() == screenCandies[col][row + 2].getType()) {
				
				if (row+3 < maxRows && screenCandies[col][row].getType() == screenCandies[col][row + 3].getType()) {
					
					if (row+4 < maxRows && screenCandies[col][row].getType() == screenCandies[col][row + 4].getType()) {
						
						screenCandies[col][row].setCombo(Candy.COMBO_FIVE);
						screenCandies[col][row+1].setCombo(Candy.COMBO_FIVE);
						screenCandies[col][row+2].setCombo(Candy.COMBO_FIVE);
						screenCandies[col][row+3].setCombo(Candy.COMBO_FIVE);
						screenCandies[col][row+4].setCombo(Candy.COMBO_FIVE);

					} else {
					
						screenCandies[col][row].setCombo(Candy.COMBO_FOUR);
						screenCandies[col][row+1].setCombo(Candy.COMBO_FOUR);
						screenCandies[col][row+2].setCombo(Candy.COMBO_FOUR);
						screenCandies[col][row+3].setCombo(Candy.COMBO_FOUR);
					}
				
				} else {

					screenCandies[col][row].setCombo(Candy.COMBO_THREE);
					screenCandies[col][row+1].setCombo(Candy.COMBO_THREE);
					screenCandies[col][row+2].setCombo(Candy.COMBO_THREE);

				}
			}
		}

	}

	private void checkHorizontalCombos(short col, short row) {
		
		if (screenCandies[col][row].getCombo() != Candy.NO_COMBO) return;
		
		if (col+1 < maxCols && screenCandies[col][row].getType() == screenCandies[col + 1][row].getType()) {
			
			if (col+2 < maxCols && screenCandies[col][row].getType() == screenCandies[col + 2][row].getType()) {
				
				if (col+3 < maxCols && screenCandies[col][row].getType() == screenCandies[col + 3][row].getType()) {
					
					if (col+4 < maxCols && screenCandies[col][row].getType() == screenCandies[col + 4][row].getType()) {
						
						screenCandies[col][row].setCombo(Candy.COMBO_FIVE);
						screenCandies[col+1][row].setCombo(Candy.COMBO_FIVE);
						screenCandies[col+2][row].setCombo(Candy.COMBO_FIVE);
						screenCandies[col+3][row].setCombo(Candy.COMBO_FIVE);
						screenCandies[col+4][row].setCombo(Candy.COMBO_FIVE);

					} else {
					
						screenCandies[col][row].setCombo(Candy.COMBO_FOUR);
						screenCandies[col+1][row].setCombo(Candy.COMBO_FOUR);
						screenCandies[col+2][row].setCombo(Candy.COMBO_FOUR);
						screenCandies[col+3][row].setCombo(Candy.COMBO_FOUR);
					}
				
				} else {

					screenCandies[col][row].setCombo(Candy.COMBO_THREE);
					screenCandies[col+1][row].setCombo(Candy.COMBO_THREE);
					screenCandies[col+2][row].setCombo(Candy.COMBO_THREE);

				}
			}
		}

	}

	private void switchCandies(short col1, short row1, short col2, short row2) {
		Candy intermediate = screenCandies[col1][row1];
		screenCandies[col1][row1] = screenCandies[col2][row2];
		screenCandies[col2][row2] = intermediate;
	}
	
	private void moveCandies(short col1, short row1, short col2, short row2) {
		screenCandies[col1][row1].moveCandy(col1, row1);
		screenCandies[col2][row2].moveCandy(col2, row2);
	}
	
	public static int getGameWidth() {
		return width;
	}

	public static int getGameHeight() {
		return height;
	}

	private class Mouse implements MouseListener, MouseMotionListener{
		
		public void mouseClicked(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			if (candySwitching) return;

			if (colPressed == -1 && rowPressed == -1) {
				colPressed = (short) (e.getX()*maxCols/width);
				rowPressed = (short) (e.getY()*maxRows/height);
			} 
			//Coger caramelo
			screenCandies[colPressed][rowPressed].setGrabbed(true);
		}
		public void mouseReleased(MouseEvent e) {
			if (candySwitching) return;
			
			if (colPressed != -1 && rowPressed != -1 && nextCol != -1 && nextRow != -1) {
				int tileDistance =
						Math.abs(Math.abs(colPressed)-Math.abs(nextCol)) +
						Math.abs(Math.abs(rowPressed)-Math.abs(nextRow));
				
				//Tile dist checks if the tile you are trying to change is one of the four allowed
				if (tileDistance == 1) {
					//Then it switches the candies to check if the move is succesfull, if not it won't move the candies
					switchCandies(colPressed, rowPressed, nextCol, nextRow);
					checkCombos();
					if (screenCandies[colPressed][rowPressed].getCombo() != Candy.NO_COMBO || screenCandies[nextCol][nextRow].getCombo() != Candy.NO_COMBO) {
						moveCandies(colPressed, rowPressed, nextCol, nextRow);
					} else switchCandies(colPressed, rowPressed, nextCol, nextRow);
				}
			}
			
			for (short row = 0; row < maxRows; row++) {
				for (short col = 0; col < maxCols; col++) {
					screenCandies[col][row].setGrabbed(false);
					screenCandies[col][row].setCombo(Candy.NO_COMBO);
				}
			}
			
			colPressed = -1;
			rowPressed = -1;
			nextCol = -1;
			nextRow = -1;
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		public void mouseDragged(MouseEvent e) {
			if (colPressed != -1 && rowPressed != -1) {
				nextCol = (short) (e.getX()*maxCols/width);
				nextRow = (short) (e.getY()*maxRows/height);
			}
		}
		public void mouseMoved(MouseEvent e) {}
		
	}
	
}
