package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class PointItem extends Item {
	private static int spawnCounter = 1;
	private static int exitCounter = 1;
	
	public int x;
	public int y;
	
	public int type;
	public int id;
	
	public PointItem(int type) {
		this(type, 0, 0);
	}
	
	public PointItem(int type, int x, int y) {
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
		this.x = x;
		this.y = y;
	}

	public void placeItemStart(Cursor cursor) {
		this.x = cursor.getGridX();
		this.y = cursor.getGridY();
	}

	public void placeItemDrag(Cursor cursor) {
		this.x = cursor.getGridX();
		this.y = cursor.getGridY();
	}

	public void setThickness(int thickness) {
		// do nothing b/c points don't have thickness really
	}
	
	public int getThickness() {
		return 1;
	}

	public boolean contains(int x, int y) {
		return x == this.x && y == this.y;
	}

	@Override
	public Item clone() {
		return new PointItem(type, x, y);
	}

	@Override
	public void cloneItemDrag(Cursor cursor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneItemStart(Cursor cursor) {
		// TODO Auto-generated method stub
		
	}
	
	public Point getPosition() {
		return new Point(x, y);
	}
}
