package is.craftopol.j4k;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Animation {
	private static final Stroke STROKE = new BasicStroke(1.0f);
	public int x1;
	public int y1;
	
	public int x2;
	public int y2;
	
	public Animation() {
		
	}
	
	public Animation(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void setStartPoint(int x, int y) {
		x1 = x;
		y1 = y;
		x2 = x1;
		y2 = y1;
	}
	
	public void setEndPoint(int x, int y) {
		x2 = x;
		y2 = y;
	}

	public void render(Graphics g) {
		//System.out.println("I am " + toString() + " and my points are (" + x1 + ", " + y1 + "), (" + x2 + ", " + y2 + ")");
		((Graphics2D) g).setStroke(STROKE);
		g.setColor(Color.green);
		g.drawLine(x1, y1, x2, y2);
	}
}
