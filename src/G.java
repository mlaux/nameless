import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class G extends Applet implements Runnable {
	private static final int TYPE_SPAWNPOINT = 0x01;
	private static final int TYPE_EXITPOINT = 0x02;
	private static final int TYPE_LINE = 0x03;
	private static final int TYPE_TRIANGLE = 0x04;
	private static final int TYPE_CIRCLE = 0x05;
	private static final int FLAG_FILLED = 0x100;
	
	private static final int width = 400;
	private static final int height = 300;
	private static final int scale = 2;
	
	private static final int WALK = 1;
	private static final int PUSH = 2;
	private static final int STAND = 3;
	
	private static final String level = "\u0004\u0000\u00e8\u00bd\u0122\u0000\u0122\u0004\u00bd\u0122\u015e\u012f\u00bd\u012f\u0004\u015e\u012f\u01d7\u0131\u015e\u0131\u0004\u01d7\u0131\u024b\u0129\u024b\u0131\u0004\u024b\u0129\u027e\u0113\u027e\u0129\u0004\u027e\u0113\u02d9\u010c\u02d9\u0113\u0004\u02d9\u010c\u0342\u011d\u02d9\u011d\u0004\u0342\u011d\u039b\u0130\u0342\u0130\u0004\u039b\u0130\u03b6\u0132\u039b\u0132\u0004\u03b6\u0132\u03c0\u012f\u03c0\u0132\u0003\u006f\u011a\u0236\u0067\u0017\u0003\u0237\u0076\u0084\u0127\u0010\u0003\u0097\u012e\u0231\u0083\u000c\u0003\u0226\u0091\u0085\u013a\u0008\u0003\u01fa\u00ac\u0067\u0148\n\u0003\u01c9\u00cb\u009d\u013d\u000e\u0003\u016e\u00ff\u00ba\u013f\u0013\u0003\u0134\u012a\u00bb\u0156\u0023\u0005\u0065\u0114\u0023\u0001\u0005\u0082\u010b\u0012\u0001\u0005\u0093\u0107\u000b\u0001\u0005\u0040\u00fe\u0013\u0001\u0005\u0020\u00f8\u0014\u0001\u0005\u000b\u00f1\u000c\u0001\u0005\u0157\u0124\u001b\u0001\u0005\u00d5\u009e\u0019\u0031\u0004\u03c0\u012e\u03c8\u012b\u03c8\u012e\u0004\u03c8\u012b\u03d3\u012a\u03d3\u012b\u0004\u03d3\u012a\u03e3\u012c\u03d3\u012c\u0004\u03e3\u012c\u03f7\u0135\u03e3\u0135\u0004\u03f7\u0135\u041b\u014e\u03f7\u014e\u0004\u041b\u014e\u0454\u0169\u041b\u0169\u0004\u0454\u0169\u0485\u0174\u0454\u0174\u0004\u0485\u0174\u04aa\u0179\u0485\u0179\u0004\u04aa\u0179\u04c3\u017b\u04aa\u017b\u0004\u04c3\u017b\u050b\u017d\u04c3\u017d\u0004\u050b\u017d\u05bf\u0173\u05bf\u017d";
	
	private int time = 10;
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
		
		// make giant array for items since we can't have multiple classes
		// assume 100 max objects in the level for now
		
		// for each item index:
		// [0] = item type, [1] = flags (currently, filled/not filled is only flag)
		
		// [2]-[7] = item-specific data:
		//		for points: [2] = x, [3] = y
		//		for lines: [2] = x1, [3] = y1, [4] = x2, [5] = y2, [6] = thickness
		//		for triangles: [2] = x1, [3] = y1, [4] = x2, [5] = y2, [6] = x3, [7] = y3
		//		for circles: [2] = centerX, [3] = centerY, [4] = radius, [5] = thickness
		
		int[][] allObjects = new int[100][8];
		
		// load da level
			
		int index = 0, numObjects = 0;
		while(index < level.length()) {
			// type is in lower 8 bits, flags are in upper 8 bits
			char ch = level.charAt(index++);
			int type = ch & 0xff, flags = ch & 0xff00;
			
			// store base information that's common to every object in 
			// the first 2 fields
			allObjects[numObjects][0] = type;
			allObjects[numObjects][1] = flags;
			
			switch(type) {
				case TYPE_SPAWNPOINT:
				case TYPE_EXITPOINT:
					allObjects[numObjects][2] = level.charAt(index++); // x
					allObjects[numObjects][3] = level.charAt(index++); // y
					break;
				case TYPE_LINE:
					allObjects[numObjects][2] = level.charAt(index++); // x1
					allObjects[numObjects][3] = level.charAt(index++); // y1
					allObjects[numObjects][4] = level.charAt(index++); // x2
					allObjects[numObjects][5] = level.charAt(index++); // y2
					allObjects[numObjects][6] = level.charAt(index++); // thickness
					break;
				case TYPE_TRIANGLE:
					allObjects[numObjects][2] = level.charAt(index++); // x1
					allObjects[numObjects][3] = level.charAt(index++); // y1
					allObjects[numObjects][4] = level.charAt(index++); // x2
					allObjects[numObjects][5] = level.charAt(index++); // y2
					allObjects[numObjects][6] = level.charAt(index++); // x3
					allObjects[numObjects][7] = level.charAt(index++); // y3
					break;
				case TYPE_CIRCLE:
					allObjects[numObjects][2] = level.charAt(index++); // centerX
					allObjects[numObjects][3] = level.charAt(index++); // centerY
					allObjects[numObjects][4] = level.charAt(index++); // radius
					allObjects[numObjects][5] = level.charAt(index++); // thickness
					break;
			}
			
			numObjects++;
		}
		
		// set up graphics
		BufferedImage screen = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) screen.getGraphics();
		Graphics appletGraphics = getGraphics();

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

			// Update here

			lastTime = now;
			
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			
			// render
			g.drawImage(bkg, 0, 0, this);
			
			// render level, TODO: make this good
			for(int k = 0; k < numObjects; k++) {
				int[] obj = allObjects[k];
				
				switch(obj[0]) {
					case TYPE_LINE:
						((Graphics2D) g).setStroke(new BasicStroke(obj[6]));
						g.drawLine(obj[2], obj[3], obj[4], obj[5]);
						break;
					case TYPE_TRIANGLE:
						int[] xp = { obj[2], obj[4], obj[6] };
						int[] yp = { obj[3], obj[5], obj[7] };
						
						g.fillPolygon(xp, yp, 3);
						
						g.fillRect(Math.min(obj[2], obj[4]), obj[7], Math.abs(obj[4] - obj[2]), 2000);
						break;
					case TYPE_CIRCLE:
						((Graphics2D) g).setStroke(new BasicStroke(obj[5]));
						if((obj[1] & FLAG_FILLED) != 0)
							g.fillOval(obj[2] - obj[4], obj[3] - obj[4], obj[4] * 2, obj[4] * 2);
						else g.drawOval(obj[2] - obj[4], obj[3] - obj[4], obj[4] * 2, obj[4] * 2);
						
						break;
				}
			}
			
			drawGuy(g, WALK, 0, time);
			time++;

			
			g.setColor(Color.white);
			g.drawString("FPS " + String.valueOf(fps), 20, 30);
			
			// render the buffer to the applet
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