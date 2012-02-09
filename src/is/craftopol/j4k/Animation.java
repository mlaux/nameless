package is.craftopol.j4k;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

public class Animation {
	private static final Stroke STROKE = new BasicStroke(1.0f);
	
	public List<Point> points = new ArrayList<Point>();
	
	public int curX;
	public int curY;
	public int speed = 1;
	
	public Animation() {
		
	}
	
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}
	
	public String serialize() {
		String s = "";
		
		s += (char) points.size();
		s += (char) speed;
		
		for(int k = 0; k < points.size(); k++) {
			s += (char) points.get(k).x;
			s += (char) points.get(k).y;
		}
		
		return s;
	}

	public void render(Graphics g) {
		//System.out.println("I am " + toString() + " and my points are (" + x1 + ", " + y1 + "), (" + x2 + ", " + y2 + ")");
		((Graphics2D) g).setStroke(STROKE);
		g.setColor(Color.green);
		int[] xp = new int[points.size()];
		int[] yp = new int[points.size()];
		
		for(int k = 0; k < points.size(); k++) {
			Point p = points.get(k);
			xp[k] = p.x;
			yp[k] = p.y;
		}
		
		g.drawPolyline(xp, yp, points.size());
		
		g.drawLine(xp[xp.length - 1], yp[yp.length - 1], curX, curY);
	}
}
