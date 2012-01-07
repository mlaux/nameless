package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Button extends Item {
	public static final int HEIGHT = 16;
	
	public int x;
	public int y;
	private int xChange;
	private int yChange;
	
	public int width;
	
	public Button() {
		
	}
	
	public Button(int x, int y, int w, Animation a) {
		this.x = x;
		this.y = y;
		this.width = w;
		animation = a;
	}

	public String serialize() {
		return "" + (char) Item.TYPE_BUTTON + (char) x + (char) y + (char) width;
	}

	public Dimension getSize() {
		return new Dimension(width, HEIGHT);
	}

	public void render(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(xChange != 0 || yChange != 0)
			g.setColor(new Color(255, 255, 255, 100));
		
		int[] xp = { x, x + 16, x + 16 };
		int[] yp = { y, y, y - 8 };
		
		g.fillPolygon(xp, yp, 3);
		
		xp = new int[] { x + width + 32, x + width + 16, x + width + 16 };
		yp = new int[] { y, y, y - 8 };
		g.fillPolygon(xp, yp, 3);
		g.fillRect(x + 16, y - 16 , width, HEIGHT);
		
		if(animation != null)
			animation.render(g);

		g.setColor(Color.white);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void placeItemStart(Cursor cursor) {
		x = cursor.getGridX();
		y = cursor.getGridY();
	}

	public void placeItemDrag(Cursor cursor) {
		width = cursor.getGridX() - x - 32;
		if (width < 0) {
			width = 0;
		}
	}

	public void setThickness(int thickness) {
		
	}

	public int getThickness() {
		return 1;
	}

	public boolean contains(int x, int y) {
		return x >= this.x && y <= this.y && x <= this.x + this.width + 32 && y >= this.y - HEIGHT;
	}

	public Item clone() {
		return new Button(this.x, this.y, this.width, this.animation);
	}

	public void animateItemDrag(Cursor cursor) {
		setPosition(cursor.getGridX() - xChange, cursor.getGridY() - yChange);
		
		if(animation != null)
			animation.setEndPoint(x, y);
	}

	public void animateItemStart(Cursor cursor) {
		xChange = cursor.getGridX() - x;
		yChange = cursor.getGridY() - y;
		
		if(animation != null)
			animation.setStartPoint(x, y);
	}
}
