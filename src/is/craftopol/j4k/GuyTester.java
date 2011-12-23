package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GuyTester extends JComponent implements Runnable {
	private static final int WALK = 1;
	private static final int PUSH = 2;
	private static final int STAND = 3;
	
	private int time = 10;
	private int calc = 2;
	private int direction = 1;
	private double bodyAngle;
	private double posX = 20;
	private double posY = 100;
	private double stillCalc;
	private double armAngle;
	
	private BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
	private Graphics imgg = image.getGraphics();
	
	public GuyTester() {
		setPreferredSize(new Dimension(800, 600));
		setDoubleBuffered(true);
	}
	
	public void run() {
		while(true) {
			repaint();
			try { Thread.sleep(15); } catch(Exception e) { }
		}
	}
	private double test;
	public void paintComponent(Graphics g) {
		time++;
		
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		
		imgg.setColor(Color.white);
		imgg.fillRect(0, 0, image.getWidth(), image.getHeight());
		imgg.setColor(Color.black);
		
		if (time<0) {
			paintGuy(imgg, WALK, 0, time);
		} else if (time<1000) {
			paintGuy(imgg, PUSH, 30, time);
		} else if(time<400) {
			test+=0.02;
			paintGuy(imgg, STAND, test, time);
		} else if (time<500) {
			test=-25;
			paintGuy(imgg, STAND, test, time);
		} else {
			paintGuy(imgg, WALK, 0, time);
		}
		
		//Anti-aliasing is a gift from god.
		g.drawImage(image, 100, 100, 1000, 1000, null);
	}
	
	public void paintGuy(Graphics _g, int action, double param, int time) {
		
		Graphics2D g = (Graphics2D) _g.create();
		
		//possibly draw everything that's repeated (chest head arms) outside of it and change calc if they're pushing/pulling
		
		if (time%20 == 0) {
			direction = -direction;
		}
		calc+=direction*2;
		
		if (action == WALK || action == PUSH) {
			stillCalc = calc;
			armAngle = calc;
			
			if (param>bodyAngle) {
				bodyAngle++;
			} else if (param < bodyAngle) {
				bodyAngle--;
			}
			
			//Find a way to make this simpler, I accidently found it, it's a sexytastic bounce effect due to not correctly reversing the sin and cos of the rotation
			//The reason it works is because I cancel out the angle change itself but not what it rotates around
			g.rotate(Math.toRadians(-calc),(int)(posX - 4 + Math.sin(Math.toRadians(-calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-calc))*16));
			g.rotate(Math.toRadians(calc),(int)(posX - 4 + Math.sin(Math.toRadians(calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(calc))*16));
			//Finding a better way...
			//I've tried my hardest... all to vain.
			
			//back arm
			if (action==WALK) {
				g.rotate(Math.toRadians(calc*1.3), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
				g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
				g.rotate(Math.toRadians(-calc*1.3), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			} else {
				g.rotate(Math.toRadians(-75), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
				g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
				g.rotate(Math.toRadians(75), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			}
			
			//back leg1
			g.rotate(Math.toRadians(-calc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(calc),(int)posX, (int)posY - 35);
			
			//back leg2
			//g.setColor(Color.BLUE);
			g.rotate(Math.toRadians(-calc+15),(int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(calc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(calc+15))*16) + 2, 6, 20, 6, 10);
			g.rotate(Math.toRadians(calc-15),(int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2);
			
			//body
			g.rotate(Math.toRadians(bodyAngle), (int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 8, (int)(posY - 72), 15, 40, 10, 15);
			
			//head
			g.fillRoundRect((int)posX - 12, (int)(posY - 96), 22, 25, 20, 20);
			g.rotate(Math.toRadians(-bodyAngle), (int)posX, (int)posY - 35);
			//I have to draw the head down here because I am getting a nice bounce effect
			
			//front arm
			if (action==WALK) {
				g.rotate(Math.toRadians(-calc*1.3), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
				g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
				g.rotate(Math.toRadians(calc*1.3), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			} else {
				g.rotate(Math.toRadians(-100), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
				g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
				g.rotate(Math.toRadians(100), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			}
			
			//front leg1
			g.rotate(Math.toRadians(calc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(-calc),(int)posX, (int)posY - 35);
			
			//front leg2
			g.rotate(Math.toRadians(calc+15),(int)(posX - 4 + Math.sin(Math.toRadians(calc+15))*16 - 2), (int)(posY - 35 + Math.cos(Math.toRadians(calc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2, 6, 20, 6, 10);
		} else if (action == STAND) {
			time=0;
			
			//standing still
			if (Math.abs(param)>25) {
				param = 25 * Math.signum(param);
			}
			//param is velocity in the Z direction
			//+ param is going down
			if (armAngle==0) {
				armAngle = 1;
			}
			if (Math.round(armAngle)>150) {
				armAngle = 150;
			} else if (Math.round(armAngle)<20) {
				armAngle = 20;
			} else if((Math.round(armAngle)==150.0 && (param<0)) || ((Math.round(armAngle)==20) && (param>0)) || (Math.round(armAngle)!=20 && Math.round(armAngle)!=150)) {
				armAngle+=param;
			}
			
			//again, be careful about increments greater than 1
			if (Math.round(stillCalc)>-10) {
				stillCalc-=0.1;
			} else if (Math.round(stillCalc)<-10) {
				stillCalc+=0.1;
			}
			if (bodyAngle<0) {
				bodyAngle++;
			} else if (bodyAngle>0) {
				bodyAngle--;
			}
			
			//Find a way to make this simpler, I accidently found it, it's a sexytastic bounce effect due to not correctly reversing the sin and cos of the rotation
			//The reason it works is because I cancel out the angle change itself but not what it rotates around
			g.rotate(Math.toRadians(-stillCalc),(int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc))*16));
			g.rotate(Math.toRadians(stillCalc),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc))*16));
			//Finding a better way...
			//I've tried my hardest... all to vain.
			
			//back arm
			g.rotate(Math.toRadians(armAngle), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(-armAngle), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			
			//back leg1
			g.rotate(Math.toRadians(-stillCalc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(stillCalc),(int)posX, (int)posY - 35);
			
			//back leg2
			g.rotate(Math.toRadians(-stillCalc+8),(int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(stillCalc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc+15))*16) + 2, 6, 20, 6, 10);
			g.rotate(Math.toRadians(stillCalc-8),(int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc+15))*16) + 2);
			
			//body
			g.rotate(Math.toRadians(bodyAngle), (int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 8, (int)(posY - 72), 15, 40, 10, 15);
			
			//head
			g.fillRoundRect((int)posX - 12, (int)(posY - 96), 22, 25, 20, 20);
			g.rotate(Math.toRadians(-bodyAngle), (int)posX, (int)posY - 35);
			//I have to draw the head down here because I am getting a nice bounce effect
			
			//front arm
			g.rotate(Math.toRadians(-armAngle), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(armAngle), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			
			//front leg1
			g.rotate(Math.toRadians(stillCalc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(-stillCalc),(int)posX, (int)posY - 35);
			
			//front leg2
			g.rotate(Math.toRadians(stillCalc+15),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc+15))*16 - 2), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc+15))*16) + 2, 6, 20, 6, 10);
			g.rotate(Math.toRadians(-stillCalc-15),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc+15))*16 - 2), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc+15))*16) + 2);
		}
		//don't draw anything down here, I didn't close the last rotation to save memory
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		GuyTester ch = new GuyTester();
		frame.add(ch);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new Thread(ch).start();
	}
}
