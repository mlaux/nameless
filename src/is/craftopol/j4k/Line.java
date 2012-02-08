package is.craftopol.j4k;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Line extends Item {
	private int originalX1;
	private int originalY1;
	private int fakeX2;
	private int fakeY2;
	
	public int x2;
	public int y2;
	
	private int dragStartX;
	private int dragStartY;
	
	private int changeX;
	private int changeY;
	
	public int thickness = 1;
	
	public Line() {
		
	}
	
	public Line(int x1, int y1, int x2, int y2, int ox1, int oy1, int fx2, int fy2, int thick, Animation anim) {
		this.thickness = thick;
		
		this.x1 = x1;
		this.y1 = y1;
		
		this.x2 = x2;
		this.y2 = y2;
		
		originalX1 = ox1;
		originalY1 = oy1;
		fakeX2 = fx2;
		fakeY2 = fy2;
		
		this.animation = anim;
		
//		this.clone = clone;
	}
	
	public String serialize() {
		if (y2>y1) {
			return "" + (char) Item.TYPE_LINE + (char) x1 + (char) y1 + (char) x2 + (char) y2 + (char) thickness;
		} else {
			return "" + (char) Item.TYPE_LINE + (char) x2 + (char) y2 + (char) x1 + (char) y1 + (char) thickness;
		}
	}
	
	public Dimension getSize() {
		return new Dimension((int)Math.abs(x2 - x1), (int)Math.abs(y2 - y1));
	}
	
	public void render(Graphics g) {
		((Graphics2D) g).setStroke(new BasicStroke(thickness));
		if(changeX != 0 || changeY != 0)
			g.setColor(Color.gray);
		else 
			g.setColor(Color.black);
		g.drawLine((int)x1 + changeX, (int)y1 + changeY, (int)x2 + changeX, (int)y2 + changeY);
		
		if(animation != null)
			animation.render(g);
	}
	
	public void setPosition(int x, int y) {
		this.x2 = (this.x2 - this.x1) + x;
		this.y2 = (this.y2 - this.y1) + y;
	}
	
	public void placeItemStart(Cursor cursor) {
		x1 = x2 = cursor.getGridX();
		y1 = y2 = cursor.getGridY();
		originalX1 = cursor.getGridX();
		originalY1 = cursor.getGridY();
		//I didn't wanna cast it.
	}
	
	public void placeItemDrag(Cursor cursor) {
//		x2 = cursor.getGridX();
//		y2 = cursor.getGridY();
//		if (!clone) {
			double angle = Math.atan2(y2-originalY1, x2-originalX1);
			
			x1 = (int) (originalX1 + Math.cos(angle)*thickness/2);
			y1 = (int) (originalY1 + Math.sin(angle)*thickness/2);
			
			fakeX2 = cursor.getGridX();
			fakeY2 = cursor.getGridY();
			
			x2 = (int) (fakeX2 - Math.cos(angle)*thickness/2);
			y2 = (int) (fakeY2 - Math.sin(angle)*thickness/2);
//		}
	}
	
	public void setThickness(int thickness) {
//		if (!clone) {
			if (thickness > 0) {
				double angle = Math.atan2(y2-originalY1, x2-originalX1);
				
				x1 = (int) (originalX1 + Math.cos(angle)*thickness/2);
				y1 = (int) (originalY1 + Math.sin(angle)*thickness/2);
				
				x2 = (int) (fakeX2 - Math.cos(angle)*thickness/2);
				y2 = (int) (fakeY2 - Math.sin(angle)*thickness/2);
				
				this.thickness = thickness;
			}
//		}
	}

	public int getThickness() {
		return thickness;
	}
	
	public boolean contains(int x, int y) {
		return distanceTo(x, y) < Math.max(5, thickness);
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
		return new Line(x1, y1, x2, y2, originalX1, originalY1, fakeX2, fakeY2, thickness, animation);
	}

	public void cloneItemDrag(Cursor cursor) {
		changeX = cursor.getGridX() - dragStartX;
		changeY = cursor.getGridY() - dragStartY;
	}

	public void cloneItemStart(Cursor cursor) {
		dragStartX = cursor.getGridX();
		dragStartY = cursor.getGridY();
	}
}
