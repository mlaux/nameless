package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Button extends Item {
	public static final int THICKNESS = 8;
	
	private int xChange;
	private int yChange;
	
	public int x2;
	public int y2;
	
	public Button() {
		
	}
	
	public Button(int x1, int y1, int x2, int y2, Animation a) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		animation = a;
	}

	public String serialize() {
		if (y2 > y1) {
			return "" + (char) Item.TYPE_BUTTON + (char) x1 + (char) y1 + (char) x2 + (char) y2;
		} else {
			return "" + (char) Item.TYPE_BUTTON + (char) x2 + (char) y2 + (char) x1 + (char) y1;
		}
	}

	public Dimension getSize() {
		return new Dimension(Math.abs(x2 - x1), Math.abs(y2 - y1));
	}

	public void render(Graphics g) {
		if(xChange != 0 || yChange != 0)
			return;
		else 
			g.setColor(Color.black);
		
		if(x2 < x1) {
			// swap
			
			int t = x1;
			x1 = x2;
			x2 = t;
			t = y1;
			y1 = y2;
			y2 = t;
		}
		
		double angle = Math.atan2(y2 - y1, x2 - x1);
		int distance = (int) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		
		int newx2 = x1 + distance;
		
		//if (x2 - 16 - MAX_BUTTON_SIZE < x1) {
		///	x2 = x1 + 16 + MAX_BUTTON_SIZE;
		//}
		
		int[] xp1 = {x1, x1 + 8, x1 + 8};
		int[] yp1 = {y1, y1, y1 - 4};
		
		int[] xp2 = {newx2, newx2 - 8, newx2 - 8};
		int[] yp2 = {y1, y1, y1 - 4};
		
		((Graphics2D) g).rotate(angle, x1, y1);
		g.fillPolygon(xp1, yp1, 3);
		g.fillRect(x1 + 8, y1 - 8, (newx2 - x1) - 16, 8);
		g.fillPolygon(xp2, yp2, 3);
		((Graphics2D) g).rotate(-angle, x1, y1);
		
		if(animation != null)
			animation.render(g);
	}

	public void setPosition(int x, int y) {
		this.x2 = (this.x2 - this.x1) + x;
		this.y2 = (this.y2 - this.y1) + y;
	}

	public void placeItemStart(Cursor cursor) {
		x1 = cursor.getGridX();
		y1 = cursor.getGridY();
		x2 = x1;
		y2 = y1;
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
		return distanceTo(x, y) < Math.max(5, THICKNESS);
	}

	public int distanceTo(int px, int py) {
		int vx = (int) (x2 - x1), vy = (int) (y2 - y1);
		int wx = (int) (px - x1), wy = (int) (py - y1);
		
		int c1 = vx * wx + vy * wy;
		if(c1 <= 0) {
			// to the left
			return (int) Math.sqrt(wx * wx + wy * wy);
		}
		
		int c2 = vx * vx + vy * vy;
		if(c2 <= c1) {
			// to the right
			int dx = (int) (px - x2), dy = (int) (py - y2);
			return (int) Math.sqrt(dx * dx + dy * dy);
		}
		
		double b = (double) c1 / c2;
		int bx = (int) (x1 + (b * vx));
		int by = (int) (y1 + (b * vy));
		
		int dx = px - bx, dy = py - by;
		// on top or below
		return (int) Math.sqrt(dx * dx + dy * dy);
	}
	
	public Item clone() {
		return new Button(this.x1, this.y1, this.x2, this.y2, this.animation);
	}

	public void cloneItemDrag(Cursor cursor) {
		setPosition(cursor.getGridX() - xChange, cursor.getGridY() - yChange);
	}

	public void cloneItemStart(Cursor cursor) {
		xChange = cursor.getGridX() - x1;
		yChange = cursor.getGridY() - y1;
	}
}
