package is.craftopol.j4k;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GuyTester2 extends JComponent implements Runnable {
	private static final int WALK = 1;
	
	private int lastX;
	private int lastY;
	
	private int frame;
	
	public GuyTester2() {
		addMouseListener(new Mouse());
		setPreferredSize(new Dimension(800, 600));
		setDoubleBuffered(true);
	}
	
	public void run() {
		while(true) {
			repaint();
			frame++;
			try { Thread.sleep(5); } catch(Exception e) { }
		}
	}
	
	public void paintComponent(Graphics _g) {
		Graphics2D g = ((Graphics2D) _g);
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setStroke(new BasicStroke(4.0f));
		g.setColor(Color.white);
		g.drawLine(0, 185, getWidth(), 185);
		drawGuy(g, 50, 50, WALK, frame);
	}
	
	public void drawGuy(Graphics2D g, int x, int y, int action, int frame) {

		int angle = (frame % 360);
		
		int bodyx = x;
		int bodyy = y + (int) (3 * Math.sin(Math.toRadians(6 * angle)));
		
		g.setColor(Color.white);
		g.fillOval(bodyx, bodyy, 30, 30);

		int bx = x + 15;
		int by = y + 50;
		
		int arm = (angle % 60) + 60;
		
		if((angle >= 60 && angle < 120) || (angle >= 180 && angle < 240) || (angle >= 300 && angle < 360)) {
			arm = 180 - arm;
		}
		
		int dx = (int) (bx + (35 * Math.cos(Math.toRadians(arm))));
		int dy = (int) (by + (35 * Math.sin(Math.toRadians(arm))));
		g.drawLine(bx, by, dx, dy);
		

		g.setColor(Color.blue);
		g.fillRoundRect(bodyx + 5, bodyy + 30, 20, 60, 10, 10);
		
		g.setColor(Color.white);
		
		dx = (int) (bx - (45 * Math.cos(Math.toRadians(arm))));
		g.drawLine(bx, by, dx, dy);
		
		by += 40;
		
		dx = (int) (bx + (45 * Math.cos(Math.toRadians(arm))));
		dy = (int) (by + (45 * Math.sin(Math.toRadians(arm))));
		g.drawLine(bx, by, dx, dy);
		dx = (int) (bx - (45 * Math.cos(Math.toRadians(arm))));
		g.drawLine(bx, by, dx, dy);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		GuyTester2 ch = new GuyTester2();
		frame.add(ch);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		
		new Thread(ch).start();
	}
	
	class Mouse extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			lastX = e.getX();
			lastY = e.getY();
			
			System.out.println("Mouse clicked at (" + lastX + ", " + lastY + ")");
		}
	}
}
