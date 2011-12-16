import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class G extends Applet implements Runnable {
	private static final int width = 400;
	private static final int height = 300;
	private static final int scale = 2;
	
	private static final int WALK = 1;
	private static final int PUSH = 2;
	private static final int STAND = 3;
	
	private int time = 10; //use tick??
	private int calc = 2;
	private int direction = 1;
	private double bodyAngle;
	private double posX = 300;
	private double posY = 400;
	private double stillCalc;
	private double armAngle;
	

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		setSize(width * scale, height * scale); // For AppletViewer, remove later.
		
		BufferedImage bkg = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt) bkg.getRaster().getDataBuffer()).getData();
		
		double shade = 0;
		
		for(int y = 0; y < height * scale; y++) {
			for(int x = 0; x < width * scale; x++) {
				int is = (int) shade;
				pixels[y * (width * scale) + x] = is << 16 | is << 8 | is;
			}
			shade += (150.0 / (height * scale));
		}

		// Set up the graphics stuff, double-buffering.
		BufferedImage screen = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
		Graphics g = screen.getGraphics();
		Graphics appletGraphics = getGraphics();

		// Some variables to use for the fps.
		int tick = 0, fps = 0, acc = 0;
		long lastTime = System.nanoTime();

		// Game loop.
		while (true) {
			long now = System.nanoTime();
			acc += now - lastTime;
			tick++;
			if (acc >= 1000000000L) {
				acc -= 1000000000L;
				fps = tick;
				tick = 0;
			}

			// Update
			// TODO add some update logic here.

			lastTime = now;
			
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			
			// render
			g.drawImage(bkg, 0, 0, this);
			
			g.setColor(Color.white);
			g.drawString("FPS " + String.valueOf(fps), 20, 30);
			
			time++;
			drawGuy(g, WALK, 0, time);
			
			// Draw the entire results on the screen.
			appletGraphics.drawImage(screen, 0, 0, this);
			
			try {
				Thread.sleep(10);
			} catch(Exception e) { }

			/*do {
				Thread.yield();
			} while (System.nanoTime() - lastTime < 0); */

			if (!isActive()) {
				return;
			}
		}
	}
	public void drawGuy(Graphics _g, int action, double param, int time) {
		Graphics2D g = (Graphics2D) _g.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);
		
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
			
			//Bounce effect
			g.rotate(Math.toRadians(-calc),(int)(posX - 4 + Math.sin(Math.toRadians(-calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-calc))*16));
			g.rotate(Math.toRadians(calc),(int)(posX - 4 + Math.sin(Math.toRadians(calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(calc))*16));
			
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
			//For a good segue
			time=0;
			
			if (Math.abs(param)>25) {
				param = 25 * Math.signum(param);
			}
			//param is velocity in the Z direction
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
			
			//Bounce effect
			g.rotate(Math.toRadians(-stillCalc),(int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc))*16));
			g.rotate(Math.toRadians(stillCalc),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc))*16));
			
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
		}
		
		g.dispose();
	}

	public boolean handleEvent(Event e) {
		switch (e.id) {
			case Event.KEY_PRESS:
			case Event.KEY_ACTION:
				// key pressed
				break;
			case Event.KEY_RELEASE:
				// key released
				break;
			case Event.MOUSE_DOWN:
				// mouse button pressed
				break;
			case Event.MOUSE_UP:
				// mouse button released
				break;
			case Event.MOUSE_MOVE:
				break;
			case Event.MOUSE_DRAG:
				break;
		}
		return false;
	}
}