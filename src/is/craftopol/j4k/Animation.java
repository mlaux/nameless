package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Graphics;

public class Animation {
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
	}
	
	public void setEndPoint(int x, int y) {
		x2 = x;
		y2 = y;
	}

	public void render(Graphics g) {
		g.setColor(Color.green);
		g.drawLine(x1, y1, x2, y2);
	}
}
