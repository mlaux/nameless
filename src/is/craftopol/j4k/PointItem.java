package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class PointItem extends java.awt.Point implements Item {
	private static int spawnCounter = 1;
	private static int exitCounter = 1;
	
	public int type;
	public int id;
	
	public PointItem(int type) {
		this(type, 0, 0);
	}
	
	public PointItem(int type, int x, int y) {
		super(x, y);
		
		this.type = type;
		
		if(type == Item.TYPE_SPAWNPOINT)
			id = spawnCounter++;
		else id = exitCounter++;
	}
	
	public String serialize() {
		return "" + (char) (type | (id << 8)) + (char) x + (char) y;
	}

	public Dimension getSize() {
		return new Dimension(1, 1);
	}

	public void render(Graphics g) {
		g.setColor(type == Item.TYPE_SPAWNPOINT ? Color.green : Color.red);
		g.drawRect(x, y, 1, 1);
	}

	public void setPosition(int x, int y) {
		setLocation(x, y);
	}

	public void placeItemStart(Cursor cursor) {
		setLocation(cursor.getGridPos());
	}

	public void placeItemDrag(Cursor cursor) {
		setLocation(cursor.getGridPos());
	}

	public void setThickness(int thickness) {
		// do nothing b/c points don't have thickness really
	}
	
	public int getThickness() {
		return 1;
	}
}
