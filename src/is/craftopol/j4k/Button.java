package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Button extends Item {
	public static final int HEIGHT = 8;
	
	private int xChange;
	private int yChange;
	
	public int width;
	
	public Button() {
		
	}
	
	public Button(int x, int y, int w, Animation a) {
		this.x1 = x;
		this.y1 = y;
		this.width = w;
		animation = a;
	}

	public String serialize() {
		return "" + (char) Item.TYPE_BUTTON + (char) x1 + (char) y1 + (char) width;
	}

	public Dimension getSize() {
		return new Dimension(width, HEIGHT);
	}

	public void render(Graphics g) {
		if(xChange != 0 || yChange != 0)
			g.setColor(Color.gray);
		else 
			g.setColor(Color.black);
		
		int[] xp = { x1, x1 + 8, x1 + 8 };
		int[] yp = { y1, y1, y1 - 4 };
		
		g.fillPolygon(xp, yp, 3);
		
		xp = new int[] { x1 + width + 16, x1 + width + 8, x1 + width + 8 };
		yp = new int[] { y1, y1, y1 - 4 };
		g.fillPolygon(xp, yp, 3);
		g.fillRect(x1 + 8, y1 - 8 , width, HEIGHT);
		
		if(animation != null)
			animation.render(g);
	}

	public void setPosition(int x, int y) {
		this.x1 = x;
		this.y1 = y;
	}

	public void placeItemStart(Cursor cursor) {
		x1 = cursor.getGridX();
		y1 = cursor.getGridY();
	}

	public void placeItemDrag(Cursor cursor) {
		width = cursor.getGridX() - x1 - 16;
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
		return x >= this.x1 && y <= this.y1 && x <= this.x1 + this.width + 32 && y >= this.y1 - HEIGHT;
	}

	public Item clone() {
		return new Button(this.x1, this.y1, this.width, this.animation);
	}

	public void cloneItemDrag(Cursor cursor) {
		setPosition(cursor.getGridX() - xChange, cursor.getGridY() - yChange);
	}

	public void cloneItemStart(Cursor cursor) {
		xChange = cursor.getGridX() - x1;
		yChange = cursor.getGridY() - y1;
	}
}
