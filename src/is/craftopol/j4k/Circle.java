package is.craftopol.j4k;

import java.awt.Dimension;
import java.awt.Graphics;

public class Circle implements Item {
	public int centerX;
	public int centerY;
	
	public int radius;
	
	public boolean filled;
	
	public Circle() {
		
	}
	
	public Circle(int cx, int cy, int r, boolean f) {
		centerX = cx;
		centerY = cy;
		radius = r;
		filled = f;
	}

	public String serialize() {
		return "" + (char) (Item.TYPE_CIRCLE | (filled ? Item.FLAG_FILLED : 0))
				+ (char) centerX + (char) centerY + (char) radius;
	}

	public Dimension getSize() {
		return new Dimension(2 * radius, 2 * radius);
	}

	public void render(Graphics g) {
		int diameter = 2 * radius;
		
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
}
