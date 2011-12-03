package is.craftopol.j4k;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Line implements Item {
	public int x1;
	public int y1;
	
	public int x2;
	public int y2;
	
	public int thickness = 1;
	
	public Line() {
		
	}
	
	public Line(int x1, int y1, int x2, int y2, int thick) {
		this.x1 = x1;
		this.y1 = y1;
		
		this.x2 = x2;
		this.y2 = y2;
		
		this.thickness = thick;
	}
	
	public String serialize() {
		return "" + (char) Item.TYPE_LINE + (char) x1 + (char) y1 + (char) x2 + (char) y2 + (char) thickness;
	}

	public Dimension getSize() {
		return new Dimension(Math.abs(x2 - x1), Math.abs(y2 - y1));
	}

	public void render(Graphics g) {
		((Graphics2D) g).setStroke(new BasicStroke(thickness));
		g.drawLine(x1, y1, x2, y2);
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
		if (thickness > 0)
			this.thickness = thickness;
	}

	public int getThickness() {
		return thickness;
	}
	
	public boolean contains(int x, int y) {
		return distanceTo(x, y) < Math.max(5, thickness);
	}
	
	public int distanceTo(int px, int py) {
		int vx = x2 - x1, vy = y2 - y1;
		int wx = px - x1, wy = py - y1;
		
		int c1 = vx * wx + vy * wy;
		if(c1 <= 0) {
			return (int) Math.sqrt(wx * wx + wy * wy);
		}
		
		int c2 = vx * vx + vy * vy;
		if(c2 <= c1) {
			int dx = px - x2, dy = py - y2;
			return (int) Math.sqrt(dx * dx + dy * dy);
		}
		
		double b = (double) c1 / c2;
		int bx = (int) (x1 + (b * vx));
		int by = (int) (y1 + (b * vy));
		
		int dx = px - bx, dy = py - by;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}
}
