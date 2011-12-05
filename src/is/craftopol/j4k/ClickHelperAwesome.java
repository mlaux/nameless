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
	private int realLastX;
	private int realLastY;
	
	private int finalrunning;
	private int running;
	
	private int initial = 0;
	
	private double frequency;
	private double depth;
	private int finalheight;
	private int height;
	
	private int[] pickSpotX = new int[10];
	private int[] pickSpotY = new int[10];
	
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
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.black);
		
		if (running>0) {
			if (height < finalheight && running > finalheight) {
				height+=2;
				initial+=2;
			}
			if (running <= finalheight) {
				height-=2;
				lastY-=2;
				initial--;
			}
			initial++;
			for (int i = 0; i < height; i++) {
				int currentSpotX = (int) Math.round(Math.sin(Math.toRadians((initial - i) * frequency)) * (depth * ((double)(height - i)/10)) + lastX);
				int currentSpotY = (height - i) + lastY - height;
				if (i > height / 2) {
					int alpha = (int)(((double)(height/2 - (i - height/2)) / (height / 2)) * 255);
					g.setColor(new Color(0,0,0,alpha));
				} else {
					g.setColor(new Color(0,0,0,255));
				}
				g.fillRect(currentSpotX, currentSpotY, 1, 1);
				Math.sin(currentSpotX);
			}
			
			if (running == finalheight + finalheight/2 + 30) {
				for (int i = 0; i<pickSpotX.length; i++) {
					pickSpotY[i] = (int) (realLastY - Math.random()*Math.random()*finalheight);
					pickSpotX[i] = (int) ((Math.pow((finalheight - (realLastY - pickSpotY[i]))/10*Math.random(),2)))*(((int) (Math.random() * 2)) * 2 - 1) + realLastX;
				}
			}
			
			
			
			for (int i = 0; i<pickSpotX.length; i++) {
				g.setColor(Color.BLACK);
				int addX = (int)(Math.cos(Math.atan2(pickSpotY[i] - realLastY, pickSpotX[i] - realLastX)) * (Math.pow((finalrunning), 2) - Math.pow((running), 2) + 1)/400);
				int addY = (int)(Math.sin(Math.atan2(pickSpotY[i] - realLastY, pickSpotX[i] - realLastX)) * (Math.pow((finalrunning), 2) - Math.pow((running), 2) + 1)/400);
				System.out.println(Math.atan2(pickSpotY[i], pickSpotX[i] - lastX));
				// * (Math.pow(finalrunning, 2)/Math.pow(finalrunning - running,2)))
				g.fillRect(pickSpotX[i]+addX, pickSpotY[i]+addY, 5, 5);
			}
			
			running--;
		}
	}
	
	private void setupBlobs() {
		frequency = Math.random() * 7.5 + 2.5;
		depth = Math.random() * 5 + 10;
		finalheight = (int) (Math.random() * 50 + 75);
		height = 0;
		
		finalrunning = finalheight + finalheight/2 + 50;
		running = finalrunning;
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
			realLastX = lastX;
			realLastY = lastY;
			
			setupBlobs();
			
			System.out.println("Mouse clicked at (" + lastX + ", " + lastY + ")");
		}
	}
}
