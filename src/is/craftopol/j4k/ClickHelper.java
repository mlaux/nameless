package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ClickHelper extends JComponent implements Runnable {
	private int lastX;
	private int lastY;
	
	private int running;
	
	private double currentX[] = new double[200];
	private double currentY[] = new double[200];
	
	private double moveX[] = new double[200];
	private double moveY[] = new double[200];
	
	private int scale = 4;
	
	public ClickHelper() {
		addMouseListener(new Mouse());
		setPreferredSize(new Dimension(800, 600));
	}
	
	public void run() {
		while(true) {
			repaint();
			try { Thread.sleep(5); } catch(Exception e) { }
		}
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.black);
		
		if (running>0) {
			for (int i = 0; i < currentX.length; i++) {
				// when implementing replace currentX.length with the actual
				// count (less data?)
				moveX[i] -= moveX[i] / 200;
				moveY[i] -= moveY[i] / 200 + 0.005;

				currentX[i] += moveX[i];
				currentY[i] -= moveY[i];

				if (currentY[i] >= 597) {
					currentY[i] -= (currentY[i] - 597);
					moveY[i] = 0;
				}

				g.fillOval((int) (currentX[i] - 3 * scale),
						(int) (currentY[i] - 3 * scale), 6 * scale, 6 * scale);
			}

			running--;
		}
	}
	
	private void setupBlobs() {
		for (int i = 0; i < currentX.length; i++) {
			int pos = ((int) (Math.random() * 2)) * 2 - 1;
			currentX[i] = pos * 25 * scale + lastX;
			currentY[i] = lastY;

			moveX[i] = Math.random() * 0.5 * pos * (scale / 2);
			moveY[i] = Math.random() * 0.5 * (scale / 2);
		}
		
		running = 4000;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		ClickHelper ch = new ClickHelper();
		frame.add(ch);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new Thread(ch).start();
	}
	
	class Mouse extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			lastX = e.getX();
			lastY = e.getY();
			
			setupBlobs();
			
			System.out.println("Mouse clicked at (" + lastX + ", " + lastY + ")");
		}
	}
}
