package is.craftopol.j4k;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class WallTest extends JComponent implements Runnable {
	
	public WallTest() {
		setPreferredSize(new Dimension(1600, 1000));
		setDoubleBuffered(true);
	}
	
	public void run() {
		while(true) {
			repaint();
			try { Thread.sleep(5); } catch(Exception e) { }
		}
	}
	
	private double curX = 780;
	private double curY = 50;
	
	private double moveX = Math.random()*5 - 2.5;
	private double moveY = Math.random()*5 - 2.5;
	
	private boolean onGround;
	
	ArrayList<Double> hitX = new ArrayList();
	ArrayList<Double> hitY = new ArrayList();
	ArrayList<Integer> counter = new ArrayList();
	
	ArrayList<Double> moveHitX = new ArrayList();
	ArrayList<Double> moveHitY = new ArrayList(); 
	
	ArrayList<Double> posHitX = new ArrayList();
	ArrayList<Double> posHitY = new ArrayList(); 
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.fillRect(0,500,getWidth(), 1000);
		g.fillRect(0,0,400, getHeight());
		g.fillRect(1200,0,400, getHeight());
		
		curX+=moveX;
		curY-=moveY;
		
		moveY-=0.03;
		
		if (curY > 480) {
			if (Math.abs(moveY) < 0.2) {
				onGround = true;
			}
			if (!onGround) {
				System.out.println(moveY);
				moveHitX.add(moveX);
				moveHitY.add(moveY);
				posHitX.add(curX+moveX*5);
				posHitY.add(curY-moveY*5);
			}
			curY -= curY - 480;
			moveY=-moveY;
			moveY-=0.4;
			if (onGround) {
				moveY = 0;
			}
			moveX-=0.002*(moveX/Math.abs(moveX));
		}
		if (curX > 1180) {
			moveHitX.add(moveX);
			moveHitY.add(moveY);
			posHitX.add(curX+moveX*5);
			posHitY.add(curY-moveY*5);
			curX -= curX - 1180;
			moveX = -moveX;
		}
		if (curX < 400) {
			moveHitX.add(moveX);
			moveHitY.add(moveY);
			posHitX.add(curX+moveX*5);
			posHitY.add(curY-moveY*5);
			curX += 400 - curX;
			moveX = -moveX;
		}
		
		g.setColor(Color.black);
		g.fillOval((int) curX,(int) curY, 20, 20);
		
		for (int i = 0; i<moveHitX.size(); i++) {
			moveHitX.set(i,moveHitX.get(i)-moveHitX.get(i)/50);
			moveHitY.set(i,moveHitY.get(i)-moveHitY.get(i)/50);
			
			if (Math.abs(moveHitX.get(i)) + Math.abs(moveHitY.get(i))>=1) {
				posHitX.set(i, posHitX.get(i) + moveHitX.get(i));
				posHitY.set(i, posHitY.get(i) - moveHitY.get(i));
				
				hitX.add(posHitX.get(i));
				hitY.add(posHitY.get(i));
				counter.add(250);
			}
		}
		
		/*
		double speed = Math.sqrt(Math.pow(moveY, 2)+Math.pow(moveX,2));
		if (speed<5) {
			g.setColor(new Color(0,50,50,(int)(speed*51)));
			System.out.println(speed + " - " + ((int)(speed*51)));
		} else {
			g.setColor(new Color(0,50,50));
		}
		*/
		
		//VVVV not necessary VVVV
		g.setColor(new Color(0,200,200,10));
		for (int i = 0; i<hitX.size(); i++) {
			if (counter.get(i)>0) {
				Point2D center = new Point2D.Float((int)(hitX.get(i)+10), (int)(hitY.get(i)+10));
				float radius = 10;
				float[] dist = {0f, 1f};
				Color[] colors = {new Color(0,200,200,30), new Color(0,200,200,0)};
				((Graphics2D) g).setPaint(new RadialGradientPaint(center, radius, dist, colors));
				g.fillOval((int)(hitX.get(i)-0),(int)(hitY.get(i)-0),20,20);
				//Explain why I have to do -0?
				counter.set(i, counter.get(i)-1);
			}
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		WallTest ch = new WallTest();
		frame.add(ch);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new Thread(ch).start();
	}
}
