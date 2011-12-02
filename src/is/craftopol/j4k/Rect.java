package is.craftopol.j4k;

import java.awt.Dimension;
import java.awt.Graphics;

public class Rect implements Item {
	public int x;
	public int y;
	
	public int width;
	public int height;
	
	public boolean filled;
	
	public Rect() {
		
	}
	
	public Rect(int x, int y, int w, int h, boolean f) {
		this.x = x;
		this.y = y;
		
		width = w;
		height = h;
		
		filled = f;
	}
	
	public String serialize() {
		return "" + (char) (Item.TYPE_RECT | (filled ? Item.FLAG_FILLED : 0))
				+ (char) x + (char) y + (char) width + (char) height;
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void render(Graphics g) {
		if(filled)
			g.fillRect(x, y, width, height);
		else g.drawRect(x, y, width, height);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void placeItemStart(Cursor cursor) {
		// TODO Auto-generated method stub
		
	}

	public void placeItemDrag(Cursor cursor) {
		// TODO Auto-generated method stub
		
	}
}
