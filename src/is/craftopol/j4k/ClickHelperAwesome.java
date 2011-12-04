package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ClickHelperAwesome extends JComponent implements Runnable {
	private int lastX;
	private int lastY;
	
	private int running;
	
	private int initial = 0;
	
	public ClickHelperAwesome() {
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
	
	
	double frequency = Math.random() * 7.5 + 2.5;
	double depth = Math.random() * 5 + 10;
	int height = (int) (Math.random() * 50 + 75);
	
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.black);
		
		initial++;
		for (int i = 0; i < height; i++) {
			int currentSpot = (int) Math.round(Math.sin(Math.toRadians((initial - i) * frequency)) * (depth * ((double)(height - i)/10)) + 400);
			if (i > height / 2) {
				int alpha = (int)(((double)(height/2 - (i - height/2)) / (height / 2)) * 255);
				g.setColor(new Color(0,0,0,alpha));
			} else {
				g.setColor(new Color(0,0,0,255));
			}
			g.fillRect(currentSpot, 600 - i, 1, 1);
		}
	}
	
	private void setupBlobs() {
		
		running = 4000;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		ClickHelperAwesome ch = new ClickHelperAwesome();
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
