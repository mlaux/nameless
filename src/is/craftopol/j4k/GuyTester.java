package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GuyTester extends JComponent implements Runnable {
	private static final int WALK = 1;
	private static final int PUSH = 2;
	private static final int JUMP = 3;
	private static final int STAND = 4;
	
	private int time = 10;
	private int calc = 2;
	private int direction = 1;
	private double bodyAngle;
	private double posX = 600;
	private double posY = 300;
	private int stillCalc;
	private int stillCalcMove;
	
	public GuyTester() {
		setPreferredSize(new Dimension(1600, 1000));
		setDoubleBuffered(true);
	}
	
	public void run() {
		while(true) {
			repaint();
			try { Thread.sleep(15); } catch(Exception e) { }
		}
	}
	
	public void paintComponent(Graphics g) {
		time++;
		
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.fillRect(0, 500, getWidth(), 1000);
		g.fillRect(0, 0, 400, getHeight());
		g.fillRect(1200, 0, 400, getHeight());
		
		
		if (time<250) {
			paintGuy(g, WALK, 0, time);
		} else if (time<500) {
			paintGuy(g, PUSH, 20, time);
		} else {
			paintGuy(g, STAND, 0, time);
		}
		
		//Anti-aliasing is a gift from god.
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public void paintGuy(Graphics _g, int action, int weight, int time) {
		
		Graphics2D g = (Graphics2D) _g;
		
		//possibly draw everything that's repeated (chest head arms) outside of it and change calc if they're pushing/pulling
		
		if (time%20 == 0) {
			direction = -direction;
		}
		calc+=direction*2;
		
		if (action == WALK) {
			stillCalc = calc;
			//walking
			
			//Find a way to make this simpler, I accidently found it, it's a sexytastic bounce effect due to not correctly reversing the sin and cos of the rotation
			//The reason it works is because I cancel out the angle change itself but not what it rotates around
			g.rotate(Math.toRadians(-calc),(int)(posX - 4 + Math.sin(Math.toRadians(-calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-calc))*16));
			g.rotate(Math.toRadians(calc),(int)(posX - 4 + Math.sin(Math.toRadians(calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(calc))*16));
			//Finding a better way...
			//I've tried my hardest... all to vain.
			
			//back arm
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(calc), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(-calc), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			
			//back leg1
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(-calc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(calc),(int)posX, (int)posY - 35);
			
			//back leg2
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(-calc+15),(int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(calc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(calc+15))*16) + 2, 6, 20, 6, 10);
			g.rotate(Math.toRadians(calc-15),(int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2);
			
			//body
			g.setColor(Color.BLACK);
			g.rotate(Math.toRadians(bodyAngle), (int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 8, (int)(posY - 72), 15, 40, 10, 15);
			
			//head
			g.fillRoundRect((int)posX - 12, (int)(posY - 96), 22, 25, 20, 20);
			g.rotate(Math.toRadians(-bodyAngle), (int)posX, (int)posY - 35);
			//I have to draw the head down here because I am getting a nice bounce effect
			
			//front arm
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(-calc), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(calc), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			
			//front leg1
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(calc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(-calc),(int)posX, (int)posY - 35);
			
			//front leg2
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(calc+15),(int)(posX - 4 + Math.sin(Math.toRadians(calc+15))*16 - 2), (int)(posY - 35 + Math.cos(Math.toRadians(calc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2, 6, 20, 6, 10);
		
		} else if (action == PUSH) {
			//be careful about changing the rate at which the body moves because it only accounts for moving by 1 right now
			if (bodyAngle<weight) {
				bodyAngle++;
			} else if (bodyAngle>weight) {
				bodyAngle--;
			}
			
			stillCalc = calc;
			
			//Find a way to make this simpler, I accidently found it, it's a sexytastic bounce effect due to not correctly reversing the sin and cos of the rotation
			//The reason it works is because I cancel out the angle change itself but not what it rotates around
			g.rotate(Math.toRadians(-calc),(int)(posX - 4 + Math.sin(Math.toRadians(-calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-calc))*16));
			g.rotate(Math.toRadians(calc),(int)(posX - 4 + Math.sin(Math.toRadians(calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(calc))*16));
			//Finding a better way...
			//I've tried my hardest... all to vain.
			
			//back arm
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(-100), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(100), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			
			//back leg1
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(-calc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(calc),(int)posX, (int)posY - 35);
			
			//back leg2
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(-calc+15),(int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(calc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(calc+15))*16) + 2, 6, 20, 6, 10);
			g.rotate(Math.toRadians(calc-15),(int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2);
			
			//body
			g.setColor(Color.BLACK);
			g.rotate(Math.toRadians(bodyAngle), (int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 8, (int)(posY - 72), 15, 40, 10, 15);
			
			//head
			g.fillRoundRect((int)posX - 12, (int)(posY - 96), 22, 25, 20, 20);
			g.rotate(Math.toRadians(-bodyAngle), (int)posX, (int)posY - 35);
			
			//front arm
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(-75), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*27));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*27), 6, 30, 6, 10);
			g.rotate(Math.toRadians(75), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*27));
			
			//front leg1
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(calc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(-calc),(int)posX, (int)posY - 35);
			
			//front leg2
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(calc+15),(int)(posX - 4 + Math.sin(Math.toRadians(calc+15))*16 - 2), (int)(posY - 35 + Math.cos(Math.toRadians(calc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2, 6, 20, 6, 10);
		} else if (action == JUMP) {
			//jumping
			//don't know if we need this
		} else if(action == STAND) {
			//standing still
			
			//again, be careful about increments greater than 1 - current SHOULD BE at 0.1
			if (Math.abs(stillCalc)+Math.abs(stillCalcMove)>0) {
				stillCalc-=Math.abs(stillCalc)/(stillCalc*10);
			} else if (Math.abs(stillCalc)+Math.abs(stillCalcMove)<0) {
				stillCalc+=Math.abs(stillCalc)/(stillCalc*10);
			}
			
			//Find a way to make this simpler, I accidently found it, it's a sexytastic bounce effect due to not correctly reversing the sin and cos of the rotation
			//The reason it works is because I cancel out the angle change itself but not what it rotates around
			g.rotate(Math.toRadians(-stillCalc),(int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc))*16));
			g.rotate(Math.toRadians(stillCalc),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc))*16));
			//Finding a better way...
			//I've tried my hardest... all to vain.
			
			//back arm
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(stillCalc), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(-stillCalc), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			
			//back leg1
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(-stillCalc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(stillCalc),(int)posX, (int)posY - 35);
			
			//back leg2
			g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(stillCalc+15),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(stillCalc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc+15))*16) + 2, 6, 20, 6, 10);
			g.rotate(Math.toRadians(-stillCalc-15),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc+15))*16) + 2);
			
			//body
			g.setColor(Color.BLACK);
			g.rotate(Math.toRadians(bodyAngle), (int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 8, (int)(posY - 72), 15, 40, 10, 15);
			
			//head
			g.fillRoundRect((int)posX - 12, (int)(posY - 96), 22, 25, 20, 20);
			g.rotate(Math.toRadians(-bodyAngle), (int)posX, (int)posY - 35);
			//I have to draw the head down here because I am getting a nice bounce effect
			
			//front arm
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(-stillCalc), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(stillCalc), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			
			//front leg1
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(stillCalc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(-stillCalc),(int)posX, (int)posY - 35);
			
			//front leg2
			g.setColor(Color.GRAY);
			g.rotate(Math.toRadians(stillCalc+15),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc+15))*16 - 2), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc+15))*16) + 2, 6, 20, 6, 10);
		}
		//don't draw anything down here, I didn't close the last rotation to save memory
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		GuyTester ch = new GuyTester();
		frame.add(ch);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new Thread(ch).start();
	}
}
