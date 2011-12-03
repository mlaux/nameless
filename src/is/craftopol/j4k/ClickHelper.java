package is.craftopol.j4k;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

public class ClickHelper extends Canvas {
	private int lastX;
	private int lastY;
	
	private ArrayList<Integer> xPoints = new ArrayList<Integer>();
	private ArrayList<Integer> yPoints = new ArrayList<Integer>();
	
	public ClickHelper() {
		addMouseListener(new Mouse());
		setPreferredSize(new Dimension(800, 600));
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		g.setColor(Color.black);
		g.drawString("(" + lastX + ", " + lastY + ")", 5, 15);
		
		for(int k = 0; k < xPoints.size(); k++) {
			g.fillRect(xPoints.get(k) - 3, yPoints.get(k) - 3, 6, 6);
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		frame.add(new ClickHelper());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	class Mouse extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			lastX = e.getX();
			lastY = e.getY();
			
			xPoints.add(lastX);
			yPoints.add(lastY);
			
			repaint();
			
			System.out.println("Mouse clicked at (" + lastX + ", " + lastY + ")");
		}
	}
}
