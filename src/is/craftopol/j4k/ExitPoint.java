package is.craftopol.j4k;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class ExitPoint extends Item {
	public ExitPoint() {
		
	}
	
	public ExitPoint(int x, int y) {
		x1 = x;
		y1 = y;
	}
	
	public String serialize() {
		return "" + (char) Item.TYPE_EXITPOINT + (char) x1 + (char) y1;
	}

	public Dimension getSize() {
		return new Dimension(1, 1);
	}

	public void render(Graphics g) {
		((Graphics2D) g).setStroke(new BasicStroke(1));
		g.setColor(Color.red);
		g.fillRect(x1 - 2, y1 - 2, 4, 4);
	}

	public void setPosition(int x, int y) {
		this.x1 = x;
		this.y1 = y;
	}

	public void placeItemStart(Cursor cursor) {
		this.x1 = cursor.getGridX();
		this.y1 = cursor.getGridY();
	}

	public void placeItemDrag(Cursor cursor) {
		this.x1 = cursor.getGridX();
		this.y1 = cursor.getGridY();
	}

	public void setThickness(int thickness) {
		// do nothing b/c points don't have thickness really
	}
	
	public int getThickness() {
		return 1;
	}

	public boolean contains(int x, int y) {
		return Math.abs(x - x1) <= 5 && Math.abs(y - y1) <= 5;
	}

	@Override
	public Item clone() {
		return new ExitPoint(x1, y1);
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
		return new Point(x1, y1);
	}
}
