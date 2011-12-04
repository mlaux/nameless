package is.craftopol.j4k;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
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
	ArrayList<Integer> counterHit = new ArrayList();
	ArrayList<Integer> alpha = new ArrayList();
	
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
		
		//Anti-aliasing is a gift from god.
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.black);
		g.fillOval((int) curX,(int) curY, 20, 20);
		
		for (int i = 0; i<moveHitX.size(); i++) {
			moveHitX.set(i,moveHitX.get(i)-moveHitX.get(i)/50);
			moveHitY.set(i,moveHitY.get(i)-moveHitY.get(i)/50);
			
			if (Math.abs(moveHitX.get(i)) + Math.abs(moveHitY.get(i))>=1) {
				posHitX.set(i, posHitX.get(i) + moveHitX.get(i));
				posHitY.set(i, posHitY.get(i) - moveHitY.get(i));
				//not gonna try to remove this ^^^^^ after 250 ticks
				
				hitX.add(posHitX.get(i));
				hitY.add(posHitY.get(i));
				double speed = Math.sqrt(Math.pow(moveHitX.get(i), 2)+Math.pow(moveHitY.get(i),2));
				if (((int)(speed*(speed/2.5)*10))<=255) {
					//took me forever, but sexy as hell
					//to change how fast it goes transparent edit the 2.5 (the higher it is the faster)
					alpha.add((int)(speed*(speed/2.5)*10));
				} else {
					alpha.add(255);
				}
				counterHit.add(250);
			}
		}
		
		//I wanted it to kind of start bold and go transparent but that's not easy because it makes more the slower it gets. BOOM: done.
		for (int i = 0; i<hitX.size(); i++) {
			if (counterHit.get(i)>0) {
				Point2D center = new Point2D.Float((int)(hitX.get(i)+10), (int)(hitY.get(i)+10));
				float radius = 10;
				float[] dist = {0f, 1f};
				Color[] colors = {new Color(0,200,200,alpha.get(i)), new Color(0,200,200,0)};
				((Graphics2D) g).setPaint(new RadialGradientPaint(center, radius, dist, colors));
				g.fillOval((int)(hitX.get(i)-0),(int)(hitY.get(i)-0),20,20);
				//Explain why I have to do -0?
				counterHit.set(i, counterHit.get(i)-1);
			} else {
				hitX.remove(i);
				hitY.remove(i);
				counterHit.remove(i);
				alpha.remove(i);
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
