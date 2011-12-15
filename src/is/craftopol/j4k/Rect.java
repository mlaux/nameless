package is.craftopol.j4k;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Rect implements Item {
	public int x;
	public int y;
	
	public int width;
	public int height;
	
	public int thickness;
	
	public boolean filled;
	
	public Rect() {
		
	}
	
	public Rect(int x, int y, int w, int h, int t, boolean f) {
		this.x = x + thickness/2;
		this.y = y + thickness/2;
		
		width = w;
		height = h;
		
		thickness = t;
		
		filled = f;
	}
	
	public String serialize() {
		return "" + (char) (Item.TYPE_RECT | (filled ? Item.FLAG_FILLED : 0))
				+ (char) x + (char) y + (char) width + (char) height + (char) thickness;
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void render(Graphics g) {
		((Graphics2D) g).setStroke(new BasicStroke(thickness));
		
		if(filled)
			g.fillRect(x, y, width, height);
		else g.drawRect(x, y, width, height);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void placeItemStart(Cursor cursor) {
		setPosition(cursor.getGridX(), cursor.getGridY());
	}

	public void placeItemDrag(Cursor cursor) {
		width  = cursor.getGridX() - x;
		height = cursor.getGridY() - y;
	}

	public void setThickness(int thickness) {
		if(thickness > 0)
			this.thickness = thickness;
	}

	public int getThickness() {
		return thickness;
	}

	public boolean contains(int x, int y) {
		return x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.height;
	}
}
