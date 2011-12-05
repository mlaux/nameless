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
	
	private double currentX[] = new double[500];
	private double currentY[] = new double[500];
	
	private double moveX[] = new double[500];
	private double moveY[] = new double[500];
	
	private double currentAlpha[] = new double[500];
	private double moveAlpha[] =  new double[500];
	
	private int scale = 1;
	
	public ClickHelper() {
		addMouseListener(new Mouse());
		setPreferredSize(new Dimension(800, 600));
		setDoubleBuffered(true);
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
		
		for (int i = 0; i < currentX.length; i++) {
			// when implementing replace currentX.length with the actual
			// count (less data?)
			moveX[i] -= moveX[i] / 150;
			moveY[i] -= moveY[i] / 200 + 0.002;

			currentX[i] += moveX[i];
			currentY[i] -= moveY[i];

			if (currentY[i] >= (600 - 3 * scale)) {
				currentY[i] -= (currentY[i] - (600 - 3 * scale));
				moveY[i] = 0;
			}
			if (currentAlpha[i] > 5) {
				currentAlpha[i] -= moveAlpha[i];
			} else {
				currentAlpha[i] = 0;
			}
			
			
			g.setColor(new Color(0, 0, 0, (int)(currentAlpha[i]-moveAlpha[i])));
			g.fillOval((int) (currentX[i] - 3 * scale),
					(int) (currentY[i] - 3 * scale), 6 * scale, 6 * scale);
		}
	}
	
	private void setupBlobs() {
		for (int i = 0; i < currentX.length; i++) {
			int pos = ((int) (Math.random() * 2)) * 2 - 1;
			currentX[i] = pos * 25 * scale + lastX;
			currentY[i] = lastY;

			moveX[i] = Math.random() * 1.5 * pos * (scale / 1.9);
			moveY[i] = Math.random() * 1 * (scale / 1.9) + 0.5;
			
			currentAlpha[i] = 30;
			moveAlpha[i] = (Math.random()*0.5 + 0.5)/5;
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
