package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Triangle extends Item {
	public int x2;
	public int y2;
	
	private int dragStartX;
	private int dragStartY;
	private int changeX;
	private int changeY;
	
	public Triangle() {
		
	}
	
	public Triangle(int x1, int y1, int x2, int y2, Animation anim) {
		this.x1 = x1;
		this.y1 = y1;
		
		this.x2 = x2;
		this.y2 = y2;
		
		this.animation = anim;
	}
	
	public String serialize() {
		if (y2>y1) {
			return "" + (char) Item.TYPE_TRIANGLE + (char) x1 + (char) y1 + (char) x2 + (char) y2 
				+ (char) (y1 < y2 ? x1 : x2) // this is the x3
				+ (char) Math.max(y1, y2); // y3
		} else {
			return "" + (char) Item.TYPE_TRIANGLE + (char) x2 + (char) y2 + (char) x1 + (char) y1 
			+ (char) (y1 < y2 ? x1 : x2) // this is the x3
			+ (char) Math.max(y1, y2); // y3
		}
	}

	public Dimension getSize() {
		return new Dimension(Math.abs(x2 - x1), Math.abs(y2 - y1));
	}

	public void render(Graphics g) {
		int x3 = (y1 < y2 ? x1 : x2);
		int[] xp = { x1 + changeX, x2 + changeX, x3 + changeX};
		int[] yp = { y1 + changeY, y2 + changeY, Math.max(y1, y2) + changeY};
		
		if(changeX != 0 || changeY != 0)
			g.setColor(Color.gray);
		else 
			g.setColor(Color.black);
		
		g.fillPolygon(xp, yp, 3);
		
		g.fillRect(Math.min(x1, x2) + changeX, Math.max(y1, y2) + changeY, Math.abs(x2 - x1), 2000);
		
		if (animation!=null) {
			animation.render(g);
		}
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
		
	}

	public int getThickness() {
		return 1;
	}
	
	public boolean contains(int x, int y) {
		int height = Math.abs(y2-y1);
		int minY = Math.max(y2, y1);
		int width = x2-x1;
		
		double percAcross = (x - x1) / ((double) width);
		if (y <= minY + 2000 && y >= (minY - ((double) height) * ((double) percAcross))) {
			if (percAcross <= 1 && percAcross >=0) {
				//did it on a different line so you'll get it. because of how I did percAcross it's always in between 0 and 1 if your mouse is inside the triangle.
				return true;
			}
		}
		return false;
	}

	public Item clone() {
		return new Triangle(x1, y1, x2, y2, animation);
	}

	@Override
	public void cloneItemDrag(Cursor cursor) {
		changeX = cursor.getGridX() - dragStartX;
		changeY = cursor.getGridY() - dragStartY;
	}

	@Override
	public void cloneItemStart(Cursor cursor) {
		dragStartX = cursor.getGridX();
		dragStartY = cursor.getGridY();
	}
}
