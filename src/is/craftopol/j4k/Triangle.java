package is.craftopol.j4k;

import java.awt.Dimension;
import java.awt.Graphics;

public class Triangle implements Item {
	public int x1;
	public int y1;
	
	public int x2;
	public int y2;
	
	public Triangle() {
		
	}
	
	public Triangle(int x1, int y1, int x2, int y2, int thick) {
		this.x1 = x1;
		this.y1 = y1;
		
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public String serialize() {
		return "" + (char) Item.TYPE_LINE + (char) x1 + (char) y1 + (char) x2 + (char) y2;
	}

	public Dimension getSize() {
		return new Dimension(Math.abs(x2 - x1), Math.abs(y2 - y1));
	}

	public void render(Graphics g) {
		int x3 = y1 < y2 ? x1 : x2;
		int[] xp = { x1, x2, x3 };
		int[] yp = { y1, y2, Math.max(y1, y2) };
		
		g.fillPolygon(xp, yp, 3);
	}

	public void setPosition(int x, int y) {
		this.x2 = (this.x2 - this.x1) + x;
		this.y2 = (this.y2 - this.y1) + y;
		
		this.x1 = x;
		this.y1 = y;
	}
	
	public void placeItemStart(Cursor cursor) {
		x1 = x2 = cursor.getGridX();
		y1 = y2 = cursor.getGridY();
	}

	public void placeItemDrag(Cursor cursor) {
		x2 = cursor.getGridX();
		y2 = cursor.getGridY();
	}

	public void setThickness(int thickness) {
		
	}

	public int getThickness() {
		return 1;
	}
	
	public boolean contains(int x, int y) {
		return false; // TODO
	}
}
