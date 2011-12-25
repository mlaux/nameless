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
	
	private static final double HALF_PI = Math.PI / 2;
	
	// for ALL the objects
	private static final int TYPE = 0;
	private static final int FLAGS = 1;
	
	// for points
	private static final int X = 2;
	private static final int Y = 3;
	
	// for lines
	private static final int X1 = 2;
	private static final int Y1 = 3;
	private static final int X2 = 4; 
	private static final int Y2 = 5;
	private static final int X3 = 6;
	private static final int Y3 = 7;
	
	private static final int RADIUS = 4;
	private static final int THICKNESS = 6;
	
	private static final int width = 400;
	private static final int height = 300;
	private static final int scale = 2;
	
	private static final int WALK = 1;
	private static final int PUSH = 2;
	private static final int STAND = 3;
	
	private static final double STEEPNESS = 0.6;
	
	private static final String level = "\u0004\u0000\u0089\u007c\u00c1\u0000\u00c1\u0004\u007c\u00c1\u00a8\u00c5\u007c\u00c5\u0004\u00c8\u00c5\u00a8\u00c5\u00c8\u00c5\u0004\u00d2\u00c5\u00c8\u00c5\u00d2\u00c5\u0004\u00d2\u00c5\u00d9\u00c7\u00d2\u00c7\u0004\u00d9\u00c7\u00dd\u00c9\u00d9\u00c9\u0004\u00dd\u00c9\u00e1\u00ce\u00dd\u00ce\u0004\u00e1\u00ce\u00e7\u00d8\u00e1\u00d8\u0004\u00e7\u00d8\u00ed\u00e0\u00e7\u00e0\u0004\u00ed\u00e0\u00f3\u00e3\u00ed\u00e3\u0004\u00f3\u00e3\u00fa\u00e4\u00f3\u00e4\u0004\u0109\u00e4\u00fa\u00e4\u0109\u00e4\u0004\u0115\u00e1\u0109\u00e4\u0115\u00e4\u0004\u0118\u00df\u0115\u00e1\u0118\u00e1\u0004\u011c\u00d8\u0118\u00df\u011c\u00df\u0004\u0121\u00ce\u011c\u00d8\u0121\u00d8\u0004\u0129\u00b8\u0121\u00cd\u0129\u00cd\u0004\u0132\u00a5\u0129\u00b8\u0132\u00b8\u0004\u013d\u009c\u0132\u00a5\u013d\u00a5\u0004\u014a\u0097\u013d\u009c\u014a\u009c\u0004\u0165\u0097\u014a\u0097\u0165\u0097\u0004\u0165\u0097\u017d\u009c\u0165\u009c\u0004\u017d\u009c\u019a\u00a7\u017d\u00a7\u0004\u019a\u00a7\u01d0\u00cc\u019a\u00cc\u0004\u01d0\u00cc\u0207\u0108\u01d0\u0108\u0004\u0207\u0108\u023b\u015f\u0207\u015f\u0004\u023b\u015f\u027d\u01bc\u023b\u01bc\u0004\u027d\u01bc\u0297\u01d1\u027d\u01d1\u0004\u0297\u01d1\u02a9\u01db\u0297\u01db\u0004\u02a9\u01db\u02b5\u01df\u02a9\u01df\u0004\u02b5\u01df\u02c5\u01e2\u02b5\u01e2\u0004\u02d7\u01e2\u02c5\u01e2\u02d7\u01e2\u0004\u02eb\u01df\u02d7\u01e2\u02eb\u01e2\u0004\u030b\u01d5\u02eb\u01df\u030b\u01df\u0004\u033a\u01ad\u030b\u01d5\u033a\u01d5\u0004\u0371\u0172\u033a\u01ad\u0371\u01ad\u0004\u03af\u0116\u0371\u0172\u03af\u0172\u0004\u03b7\u00f3\u03af\u0116\u03b7\u0116\u0004\u03ba\u00ed\u03b7\u00f3\u03ba\u00f3\u0004\u03c0\u00e9\u03ba\u00ed\u03c0\u00ed\u0004\u03f1\u00e1\u03c0\u00ea\u03f1\u00ea\u0004\u041a\u00e1\u03f1\u00e1\u041a\u00e1\u0004\u041a\u00e1\u0463\u00eb\u041a\u00eb\u0004\u0463\u00eb\u0493\u00ee\u0463\u00ee\u0004\u04be\u00e3\u0493\u00ee\u04be\u00ee\u0004\u04cc\u00d6\u04be\u00e3\u04cc\u00e3\u0004\u04d5\u00c1\u04cc\u00d6\u04d5\u00d6\u0004\u04e0\u009a\u04d5\u00c1\u04e0\u00c1\u0004\u04ed\u007a\u04e0\u009a\u04ed\u009a\u0004\u0501\u0067\u04ed\u007a\u0501\u007a\u0004\u0529\u005f\u0501\u0067\u0529\u0067\u0004\u0529\u005f\u0547\u0060\u0529\u0060\u0004\u04df\u019f\u04df\u019f\u04df\u019f\u0004\u057e\u01cf\u057e\u01cf\u057e\u01cf\u0003\u04ab\u0081\u0259\u00a6\u0015\u0105\u025c\u0086\u002e";
	//private static final String level = "\u0004\u0064\u00e8\u0000\u0105\u0064\u0105\u0004\u0091\u00d4\u0064\u00e8\u0091\u00e8\u0004\u00c0\u00d3\u0091\u00d4\u00c0\u00d4\u0004\u00c0\u00d3\u00d8\u00dd\u00c0\u00dd\u0004\u00d8\u00dd\u00e4\u00ed\u00d8\u00ed\u0004\u00e4\u00ed\u00e7\u00fe\u00e4\u00fe\u0004\u00e7\u00fe\u00ef\u0116\u00e7\u0116\u0004\u00ef\u0116\u00fe\u012b\u00ef\u012b\u0004\u00fe\u012b\u0111\u0135\u00fe\u0135\u0004\u0111\u0135\u0127\u013b\u0111\u013b\u0004\u0140\u0139\u0127\u013b\u0140\u013b\u0004\u015f\u0124\u0140\u0139\u015f\u0139\u0004\u0176\u0108\u015f\u0124\u0176\u0124\u0004\u0186\u00e5\u0176\u0108\u0186\u0108\u0004\u018f\u0017\u0186\u00e5\u018f\u00e5\u0004\u018f\u0017\u018f\u0017\u018f\u0017\u0004\u0197\"\u018f\u0026\u0197\u0026\u0004\u01a0\u0011\u0197\"\u01a0\"\u0004\u01b6\u000e\u01a0\u0011\u01b6\u0011\u0004\u01b6\u000e\u01c2\u0010\u01b6\u0010\u0004\u01c2\u0010\u01d7\u0020\u01c2\u0020\u0004\u01d7\u0020\u01e2\u0046\u01d7\u0046\u0004\u01e2\u0046\u01f4\u0115\u01e2\u0115\u0004\u01f4\u0115\u0207\u013d\u01f4\u013d\u0004\u0207\u013d\u0217\u014e\u0207\u014e\u0004\u0217\u014e\u022d\u015a\u0217\u015a\u0004\u022d\u015a\u0246\u015f\u022d\u015f\u0004\u025a\u015e\u0246\u015f\u025a\u015f\u0004\u0280\u0153\u025a\u015e\u0280\u015e\u0004\u02b6\u0140\u0280\u0153\u02b6\u0153\u0004\u0310\u012b\u02b6\u0140\u0310\u0140\u0004\u0367\u0124\u0310\u012a\u0367\u012a\u0004\u0367\u0124\u039c\u0129\u0367\u0129\u0004\u03af\u0120\u039c\u0129\u03af\u0129\u0004\u03c0\u00fc\u03af\u0120\u03c0\u0120";
	
	private int time = 10;
	private int calc = 2;
	private int direction = 1;
	private double bodyAngle;
	private static final int posX = 400;
	private static final int posY = 300;
	private double stillCalc;
	private double armAngle;
	private double armMoveY;
	private double angle;
	
	private double[][] allObjects = new double[100][8];
	private int numObjects = 0;
	
	private boolean[] keys = new boolean[0xFFFF];

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		setSize(width * scale, height * scale); // For AppletViewer, remove later.
		
		BufferedImage bkg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt) bkg.getRaster().getDataBuffer()).getData();
		
		double shade = 0;
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int is = (int) shade;
				pixels[y * (width) + x] = is << 16 | is << 8 | is;
			}
			shade += (150.0 / (height));
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
		
		// load da level
			
		int index = 0;
		while(index < level.length()) {
			// type is in lower 8 bits, flags are in upper 8 bits
			char ch = level.charAt(index++);
			int type = ch & 0xff, flags = ch & 0xff00;
			
			// store base information that's common to every object in 
			// the first 2 fields
			allObjects[numObjects][TYPE] = type;
			allObjects[numObjects][FLAGS] = flags;
			
			switch(type) {
				case TYPE_SPAWNPOINT:
				case TYPE_EXITPOINT:
					allObjects[numObjects][X] = level.charAt(index++); // x
					allObjects[numObjects][Y] = level.charAt(index++); // y
					break;
				case TYPE_LINE:
					allObjects[numObjects][X1] = level.charAt(index++); // x1
					allObjects[numObjects][Y1] = level.charAt(index++); // y1
					allObjects[numObjects][X2] = level.charAt(index++); // x2
					allObjects[numObjects][Y2] = level.charAt(index++); // y2
					allObjects[numObjects][THICKNESS] = level.charAt(index++); // thickness
					break;
				case TYPE_TRIANGLE:
					allObjects[numObjects][X1] = level.charAt(index++); // x1
					allObjects[numObjects][Y1] = level.charAt(index++); // y1
					allObjects[numObjects][X2] = level.charAt(index++); // x2
					allObjects[numObjects][Y2] = level.charAt(index++); // y2
					allObjects[numObjects][X3] = level.charAt(index++); // x3
					allObjects[numObjects][Y3] = level.charAt(index++); // y3
					break;
				case TYPE_CIRCLE:
					allObjects[numObjects][X] = level.charAt(index++); // centerX
					allObjects[numObjects][Y] = level.charAt(index++); // centerY
					allObjects[numObjects][RADIUS] = level.charAt(index++); // radius
					break;
			}
			
			numObjects++;
		}
		
		BufferedImage level = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D lg = (Graphics2D) level.getGraphics();
		
		// set up graphics
		BufferedImage screen = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) screen.getGraphics();
		
		Graphics appletGraphics = getGraphics();

		int tick = 0, fps = 0, acc = 0;
		long lastTime = System.nanoTime();

		// Game loop.
		double moveX = 0, moveY = 0;
		boolean grabbing = false;
		boolean onGround = false;
		int weight = 0;
		
		while (true) {
			long now = System.nanoTime();
			acc += now - lastTime;
			tick++;
			if (acc >= 1000000000L) {
				acc -= 1000000000L;
				fps = tick;
				tick = 0;
			}
			
			moveY += 0.2;

			// Update here
			if(keys['a'] && !keys['d']) {
				if (moveX>-2) {
					moveX += -0.5;
				} else {
					moveX = -2;
				}
			} else if(keys['d'] && !keys['a']) {
				if (moveX<2) {
					moveX += 0.5;
				} else {
					moveX = 2;
				}
			} else {
				moveX -= Math.signum(moveX) / 2;
			}
			
			if(keys['w'] && onGround && Math.abs(Math.cos(angle)) >= STEEPNESS) {
				moveY += -7;
				angle = 0; //so if you jump it doesn't keep the old angle.
			}
			grabbing = keys['e'];
			
			moveObj(moveX, moveY, 0);
			
			onGround = false;
			for(int k = 0; k < numObjects; k++) {
				double[] obj = allObjects[k];
				switch((int) obj[TYPE]) {
					case TYPE_TRIANGLE:
						// do things pertaining to triangles
						
						double x1 = obj[X1];
						double x2 = obj[X2];
						double y1 = obj[Y1];
						double y2 = obj[Y2];
						
						double height = Math.abs(y2-y1);
						double minY = Math.max(y2, y1);
						double width = x2-x1;
						
						double percAcross = 1 - (posX/scale - x1) / ((double) width);
						double inside = (minY - (percAcross * height)) - posY/scale;
						if (posY/scale <= minY + 2000 && inside < 0) {
							if (percAcross <= 1 && percAcross >=0) {
								
								if (Math.abs(inside) <= 10) {
									moveObj(0, inside, 0);

									//fer bouncing
									moveY = 1;
									onGround = true;
									
									angle = Math.atan2(y2-y1, x2-x1);
									//when it finishes looping this should be whichever object the guy is on.
								} else {
									if (Math.abs(inside) >= 10) {
										moveObj(-moveX, 0, 0);
									}
									moveX = 0;
								}
							}
						}
						
						break;
					case TYPE_LINE:
						// do things pertaining to lines
						
						break;
					case TYPE_CIRCLE:
						// do things pertaining to circles
						
						break;
				}
			}
			
			if (Math.abs(Math.cos(angle)) < STEEPNESS) {
				moveObj(Math.cos(angle) * 4, Math.sin(angle) * 4, 1);
			}

			lastTime = now;
			
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			
			// render
			
			lg.setColor(new Color(0, 0, 0, 0));
			lg.drawImage(bkg, 0, 0, width, height, this);
			
			lg.setColor(Color.black);
			
			// render level, TODO: make this good
			for(int k = 0; k < numObjects; k++) {
				double[] obj = allObjects[k];
				
				switch((int) obj[TYPE]) {
					case TYPE_LINE:
						lg.setStroke(new BasicStroke((float) obj[THICKNESS]));
						lg.drawLine((int) obj[X1], (int) obj[Y1], (int) obj[X2], (int) obj[Y2]);
						break;
					case TYPE_TRIANGLE:
						int[] xp = { (int) obj[2], (int) obj[4], (int) obj[6] };
						int[] yp = { (int) obj[3], (int) obj[5], (int) obj[7] };
						
						lg.fillPolygon(xp, yp, 3);
						
						lg.fillRect((int) Math.min(obj[2], obj[4]), (int) obj[7], (int) Math.abs(obj[4] - obj[2]), 2000);
						break;
					case TYPE_CIRCLE:
						if(((int) obj[FLAGS] & FLAG_FILLED) != 0)
							lg.fillOval((int) (obj[X] - obj[RADIUS]), (int) (obj[Y] - obj[RADIUS]), (int) obj[RADIUS] * 2, (int) obj[RADIUS] * 2);
						else lg.drawOval((int) (obj[X] - obj[RADIUS]), (int) (obj[Y] - obj[RADIUS]), (int) obj[RADIUS] * 2, (int) obj[RADIUS] * 2);
						
						break;
				}
			}
			
			g.drawImage(level, 0, 0, width * scale, height * scale, this);
			
			if (!onGround) {
				armMoveY = moveY;
			} else {
				armMoveY -= 5;
			}
			
			boolean flip = moveX < 0;
			
			if (grabbing) {
				drawGuy(g, PUSH, weight, time, flip);
			} else if (!onGround || (int) moveX == 0) {
				drawGuy(g, STAND, armMoveY, time, flip);
			}else if ((int)moveX != 0) {
				drawGuy(g, WALK, 0, time, flip);
			}
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
	
	public void moveObj(double x, double y, int slope) {
		for(int k = 0; k < numObjects; k++) {
			double[] obj = allObjects[k];
			double useX = 0;
			double testX = 0;
			
			if (angle < HALF_PI) {
				testX = x;
			} else {
				testX = -x;
			}
			
			if (Math.abs(Math.cos(angle)) >= STEEPNESS || slope == 1 || Math.signum(testX) == 1) {
				useX = (int) x;
			}
			
			switch((int) obj[TYPE]) {
				case TYPE_TRIANGLE:
					obj[X1] -= useX; obj[Y1] -= (int)y;
					obj[X2] -= useX; obj[Y2] -= (int)y;
					obj[X3] -= useX; obj[Y3] -= (int)y;
					break;
				case TYPE_LINE:
					obj[X1] -= useX; obj[Y1] -= (int)y;
					obj[X2] -= useX; obj[Y2] -= (int)y;
					break;
				case TYPE_CIRCLE:
					obj[X] -= useX; obj[Y] -= (int)y;
					break;
			}
		}
	}
	
	public void drawGuy(Graphics _g, int action, double param, int time, boolean flip) {
		Graphics2D g = (Graphics2D) _g.create();
		if(flip) {
			g.translate(posX * 2, 0);
			g.scale(-1.0, 1.0);
		}
		
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
				keys[e.key] = true;
				break;
			case Event.KEY_RELEASE:
				keys[e.key] = false;
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