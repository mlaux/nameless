package is.craftopol.j4k;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Circle implements Item {
	public int centerX;
	public int centerY;
	
	public int radius;
	
	public boolean filled;
	
	public int thickness = 1;
	
	public Circle() {
		
	}
	
	public Circle(int cx, int cy, int r, int t, boolean f) {
		centerX = cx;
		centerY = cy;
		radius = r;
		thickness = t;
		filled = f;
	}

	public String serialize() {
		return "" + (char) (Item.TYPE_CIRCLE | (filled ? Item.FLAG_FILLED : 0))
				+ (char) centerX + (char) centerY + (char) radius + (char) thickness;
	}

	public Dimension getSize() {
		return new Dimension(2 * radius, 2 * radius);
	}

	public void render(Graphics g) {
		int diameter = 2 * radius;
		
		((Graphics2D) g).setStroke(new BasicStroke(thickness));
		
		if(filled)
			g.fillOval(centerX - radius, centerY - radius, diameter, diameter);
		else g.drawOval(centerX - radius, centerY - radius, diameter, diameter);
	}

	public void setPosition(int x, int y) {
		centerX = x;
		centerY = y;
	}

	public void placeItemStart(Cursor cursor) {
		centerX = cursor.getGridX();
		centerY = cursor.getGridY();
	}

	public void placeItemDrag(Cursor cursor) {
		radius = (int) Math.hypot(cursor.getGridX() - centerX, cursor.getGridY() - centerY);
	}

	public void setThickness(int thickness) {
		if(thickness > 0)
			this.thickness = thickness;
	}

	public int getThickness() {
		return thickness;
	}

	public boolean contains(int x, int y) {
		return Math.hypot(x - centerX, y - centerY) <= radius;
	}
}
