import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class run extends Applet implements Runnable {
	private static final int TYPE_EXITPOINT = 0x02;
	private static final int TYPE_LINE = 0x03;
	private static final int TYPE_TRIANGLE = 0x04;
	private static final int TYPE_BUTTON = 0x06;
	
	private static final double BTN_ON = 1.0;
	private static final double BTN_OFF = 0.0;
	
	private static final double HALF_PI = Math.PI / 2;
	
	// for ALL the objects
	private static final int TYPE = 0;
	
	private static final int X1 = 2;
	private static final int Y1 = 3;
	private static final int X2 = 4; 
	private static final int Y2 = 5;
	private static final int X3 = 6;
	private static final int Y3 = 7;
	
	private static final int SWITCH = 11;
	private static final int CURRENT_POINT = 10;
	private static final int DIRECTION = 12;
	private static final int XDIF = 13;
	private static final int YDIF = 14;
	private static final int ANIM_SPEED = 15;
	private static final int ATTACHED = 16;
	private static final int PREV_CURRENT_POINT = 17;
	private static final int AT_ZERO = 18;
	
	private static final int MOVEX = 8;
	private static final int MOVEY = 9;
	
	private static final int THICKNESS = 6;
	
	private static final int width = 400;
	private static final int height = 300;
	private static final int SCALE = 2;
	
	private static final int WALK = 1;
	//private static final int PUSH = 2;
	private static final int STAND = 3;
	
	private static final double STEEPNESS = 0.6;
	
	// Removed all prior levels because they are now incompatible with the addition of animation speed
	private static final String[] levels = {
		// TODO: Paste more levels here
		"\u0003\u0014\u001d\n\u0045\u0008\u0000\u0003\u0015\u001d\u001c\u0046\u0008\u0000\u0003\u002a\u0020\u001e\u0043\u0008\u0000\u0003\u0036\u0023\u002d\u0042\u0009\u0000\u0003\u0039\u0026\u0043\u0041\u000b\u0000\u0003\u0037\u003d\u003d\u003d\u0008\u0000\u0003\u0055\"\u004e\u003d\n\u0000\u0003\u0057\u0024\u005e\u0037\u0007\u0000\u0003\u0065\u0023\u0060\u0033\u0007\u0000\u0003\u0066\u0024\u006e\u0040\u0007\u0000\u0003\u0076\u0025\u007a\u003f\u0009\u0000\u0003\u0086\u0024\u007a\u0024\u0007\u0000\u0003\u0089\u003e\u007d\u0041\u0006\u0000\u0003\u0083\u0034\u007d\u0034\u0006\u0000\u0003\u0096\"\u0093\u0040\u0006\u0000\u0003\u00a2\u003f\u0096\u0040\u0007\u0000\u0003\u00af\u0025\u00ac\u0040\u0007\u0000\u0003\u00bf\u0023\u00b3\u0024\u0007\u0000\u0003\u00b1\u0041\u00bd\u0044\u0007\u0000\u0003\u00af\u0032\u00b8\u0034\u0004\u0000\u0003\u00de\u0023\u00cd\u002b\u0009\u0000\u0003\u00d2\u002e\u00de\u003a\u000b\u0000\u0003\u00dc\u003e\u00cc\u0045\u0008\u0000\u0003\u0105\u0023\u00ee\u002b\u0008\u0000\u0003\u00f1\u002e\u0101\u003c\u0008\u0000\u0003\u00fd\u003d\u00ed\u0044\n\u0000\u0004\u0230\u0096\uffb6\u0096\u0230\u0096\u0000\u0004\u0235\uffdf\u011c\uffe9\u0235\uffe9\u0000\u0004\uff3d\uffdf\ufffa\uffe7\uff3d\uffe7\u0000\u0003\u0132\u0048\uffe4\u0051\u0004\u0000\u0003\uffa7\uff8d\u0197\uff8e\u00bf\u0000\u0002\u0008\u008b\u0000",
		"\u0004\ufece\uff0a\u006f\uff0a\ufece\uff0a\u0000\u0006\u023a\u0096\u0200\u0096\u0000\u0004\u02bb\u0095\u0064\u0096\u02bb\u0096\u0002\u0003\u01a2\u00ac\u01a2\u00b8\u0004\u0380\ufff8\u02ae\ufff8\u0380\ufff8\u0000\u0004\u0380\ufff8\u03a4\u0013\u0380\u0013\u0000\u0004\u03a4\u0013\u0412\u0125\u03a4\u0125\u0000\u0004\u0412\u0124\u045b\u0172\u0412\u0172\u0000\u0004\u045b\u0172\u0572\u01ca\u045b\u01ca\u0000\u0004\u07dc\u01ca\u0572\u01ca\u07dc\u01ca\u0000\u0006\u0645\u01ca\u0602\u01ca\u0000\u0004\u0673\u0106\u0668\u0106\u0673\u0106\u0002\u0001\u066e\u010c\u066e\u01c0\u0004\u0916\u001f\u07db\u001f\u0916\u001f\u0000\u0002\u07be\u01b4\u0000",
		"\u0004\u019c\u008b\u0000\u00a0\u019c\u00a0\u0000\u0004\u001c\uffb4\uff2a\uffb4\u001c\uffb4\u0000\u0006\u019c\u008c\u010f\u0093\u0000\u0004\u019c\u008c\u01ed\u008c\u019c\u008c\u0002\u0002\u01c5\u0097\u02ea\u001d\u0004\u0312\u0012\u0439\u0012\u0312\u0012\u0000\u0006\u0439\u0012\u03f0\u0012\u0000\u0004\u0439\u0012\u0488\u0012\u0439\u0012\u0002\u0002\u046d\u0166\u046d\u003a\u0006\u05d1\ufee6\u0588\ufee6\u0000\u0003\u0486\ufd57\u001c\ufe24\u0001\u0002\u0003\u03aa\ufd7e\u03aa\uff0e\u0004\u06ff\ufcc0\u08dc\ufcc0\u06ff\ufcc0\u0000\u0002\uff45\uff9e\u0000\u0004\u0483\ufee6\u070f\ufee6\u0483\ufee6\u0000",
		"\u0004\u00fd\uff7d\u02bd\uff7d\u00fd\uff7d\u0000\u0004\u010d\u0092\ufe76\u00b7\u010d\u00b7\u0000\u0004\ufe12\u00b7\ufdc5\u00b7\ufe12\u00b7\u0000\u0004\ufd61\u00b7\ufd31\u00b7\ufd61\u00b7\u0000\u0004\ufccd\u00b7\ufcb9\u00b7\ufccd\u00b7\u0000\u0004\ufc55\u00b8\ufc29\u00ea\ufc55\u00ea\u0000\u0004\ufc55\u00b9\ufc6c\u0135\ufc55\u0135\u0000\u0004\ufbe4\u0009\ufb34\u0009\ufbe4\u0009\u0000\u0006\ufc29\u0179\ufbe4\u0179\u0000\u0004\ufc2c\u0178\ufbca\u0178\ufc2c\u0178\u0002\u0004\ufc06\u01a7\ufc06\u0119\u0003\ufacc\u0009\ufb35\u0009\u0001\u0002\u0002\ufad1\u0008\uf75b\u0008\u0003\uf690\uffcf\uf61a\uffcf\u0001\u0002\u0002\uf688\uffce\uf995\uffce\u0003\uf9b6\uff84\ufa3b\uff84\u0001\u0002\u0002\uf9d2\uff85\uf767\uff85\u0003\uf4cb\uff3f\uf48c\uff3f\u0001\u0002\u0002\uf4c8\uff3f\uf85a\uff3f\u0004\uf2de\uff40\uf48c\uff40\uf2de\uff40\u0000\u0002\uf2ee\uff2f\u0000\u0004\ueeff\u042a\uf30f\u042a\ueeff\u042a\u0000\u0003\uef30\u0397\uef29\u03e9\u000f\u0000\u0003\uef32\u03ea\uef4d\u03f2\u000f\u0000\u0003\uefab\u0398\uef83\u03bf\n\u0000\u0003\uef84\u03c6\uefa5\u03e3\u000b\u0000\u0003\uefcc\u03c7\uefa9\u03e1\u000c\u0000\u0003\uefb0\u0399\uefcc\u03c1\u0007\u0000\u0003\ueff8\u0391\uefe3\u03ee\n\u0000\u0003\uf01e\u03ec\uefe9\u03ee\u000b\u0000\u0002\uf2c7\u0417\u0000",
		"\u0004\uffb6\u0096\u0230\u0096\uffb6\u0096\u0000\u0004\u0235\uffdf\u011c\uffe9\u0235\uffe9\u0000\u0004\uff3d\uffdf\ufffa\uffe7\uff3d\uffe7\u0000\u0003\u0132\u0048\uffe4\u0051\u0004\u0000\u0003\u0019\u0015\u001b\u0036\u0011\u0000\u0003\u0039\n\u0003\u000b\u0008\u0000\u0003\u0044\u000f\u0041\u0033\u0009\u0000\u0003\u0047\u0027\u0050\u0035\u0007\u0000\u0003\u005b\u000e\u005e\u0034\u0008\u0000\u0003\u005f\u000b\u0071\u000c\u0007\u0000\u0003\u0060\u0034\u007a\u0037\u0007\u0000\u0003\u0061\u0021\u006a\"\u0005\u0000\u0003\u009a\u0009\u009d\u0038\u0009\u0000\u0003\u009d\u0008\u00b8\u0009\u0007\u0000\u0003\u00a1\u0038\u00b7\u003a\u0009\u0000\u0003\u00a0\u0023\u00a8\u0024\u0008\u0000\u0003\u00c5\u0016\u00c3\u0033\u000b\u0000\u0003\u00c9\u0015\u00d4\u0035\u0007\u0000\u0003\u00dd\u0015\u00d6\u0033\u0005\u0000\u0003\u00eb\u0006\u00ea\u0037\u0005\u0000\u0003\u00ee\u0007\u0107\u001e\u0006\u0000\u0003\u0106\u0020\u00eb\u0037\u0005\u0000\u0003\u01c0\uff7c\uffad\uff7d\u00da\u0000\u0003\u0112\u0035\u0112\u0038\u0007\u0000\u0003\u010e\u0000\u0111\"\u000c\u0000"	
	};
	private int levelNum = 0;
	
	private int calc = 2;
	private int direction = 1; //TODO maybe dont need this because the direction changes whenever you press A and D, when you let go it resets it to right. So.....
	//private double bodyAngle;
	private static final int posX = 400;
	private static final int posY = 300;
	private double stillCalc;
	private double armAngle;
	private double angle;
	//private byte insideObj;
	
	private double lowestY = 300;
	private double moveOverTimeY;
	
	private boolean stuck = false;
	
	private int diedTimer;
	
	private double[][] allObjects = new double[1000][19];
	private Point[][] animations = new Point[1000][];
	private int numObjects = 0;
	
	private boolean[] keys = new boolean[0xFFFF];

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		BufferedImage bkg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt) bkg.getRaster().getDataBuffer()).getData();
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int is = (int) (y * (150.0 / height));
				pixels[y * (width) + x] = is << 16 | is << 8 | is;
			}
		}
		
		loadLevel(levelNum);
		
		BufferedImage level = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D lg = (Graphics2D) level.getGraphics();
		
		// set up graphics
		BufferedImage screen = new BufferedImage(width * SCALE, height * SCALE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) screen.getGraphics();
		
		Graphics appletGraphics = getGraphics();

		double moveX = 0, moveY = 0;
		double armMoveY = 0;
		int time = 10;
		boolean onGround = false;
		//int weight = 0;

		// Game loop.
		while (true) {
			long start = System.nanoTime();
			
			// Update here
			moveY += 0.2;
			if (moveY > 7) {
				moveY = 7;
			}
			
			if((keys['a'] || keys['A'])&& !keys['d']/* && insideObj != -1*/) {
				//insideObj = 0;
				if (moveX>-2) {
					moveX += -0.5;
				}/* else {
					moveX = -2;
				}*/
			} else if((keys['d'] || keys['D']) && !keys['a']/* && insideObj != 1*/) {
				//insideObj = 0;
				if (moveX<2) {
					moveX += 0.5;
				}/* else {
					moveX = 2;
				}*/
			} else {
				moveX -= Math.signum(moveX) / 4;
			}
			
			if((keys['w'] || keys['W'] || keys[' ']) && onGround && Math.abs(Math.cos(angle)) >= STEEPNESS && !stuck) {
				moveY += -7;
				angle = 0; //so if you jump it doesn't keep the old angle.
			}
			
			moveObj(moveX, moveY, 0);
			onGround = false;
			System.out.println(moveOverTimeY + " - " + lowestY);
			if (moveOverTimeY > lowestY + 100) {
				diedTimer++;
			}
			
			for(int k = 0; k < numObjects; k++) {
				double[] obj = allObjects[k];
				Point[] animation = animations[k];
				
				if(obj[TYPE] == TYPE_EXITPOINT) {
					double dx = obj[X1] - posX / SCALE;
					double dy = obj[Y1] - posY / SCALE;
					double di = dx * dx + dy * dy;
					
					if(di < 600 && diedTimer == 0) {
						levelNum++;
						diedTimer++;
					}
					continue;
				}
				
				//obj[SWITCH] = BTN_ON; //TODO place this in the button collision code, if there is no button it's on
				boolean atZero = true;
				if (animation.length > 1)
					atZero = ((obj[CURRENT_POINT] - obj[DIRECTION] == 0) && obj[PREV_CURRENT_POINT] == 0);
				obj[PREV_CURRENT_POINT] = obj[CURRENT_POINT];
				
				if (atZero && obj[SWITCH] == BTN_OFF && animation.length > 1) {
					obj[AT_ZERO] = 0;
				}
				
				if (obj[AT_ZERO] == 1 && animation.length > 1) {
					int curX = animation[(int) obj[CURRENT_POINT]].x;
					int curY = animation[(int) obj[CURRENT_POINT]].y;
					
					int prevY, prevX;
					if (obj[CURRENT_POINT] == 0) {
						prevX = curX;
						prevY = curY;
					} else if (obj[CURRENT_POINT] - obj[DIRECTION] < animation.length) {
						prevX = animation[(int) (obj[CURRENT_POINT] - obj[DIRECTION])].x;
						prevY = animation[(int) (obj[CURRENT_POINT] - obj[DIRECTION])].y;
					} else {
						prevX = animation[animation.length - 1].x;
						prevY = animation[animation.length - 1].y;
						curX = animation[animation.length - 2].x;
						curY = animation[animation.length - 2].y;
					}
					
					if (obj[SWITCH] == BTN_OFF && obj[DIRECTION] != -1 && obj[CURRENT_POINT] != animation.length - 1) {
						obj[DIRECTION] = -1;
						prevX ^= curX;
						curX ^= prevX;
						prevX ^= curX;
						
						prevY ^= curY;
						curY ^= prevY;
						prevY ^= curY;
					}
					
					if (Math.abs(curX - obj[X1]-obj[XDIF]) <= Math.abs(obj[MOVEX]/2 * obj[ANIM_SPEED]) && Math.abs(curY - obj[Y1]-obj[YDIF]) <= Math.abs(obj[MOVEY]/2 * obj[ANIM_SPEED])) {
						if (animation[0].equals(animation[animation.length-1]) && obj[CURRENT_POINT]==animation.length-1) {
							obj[CURRENT_POINT] = 0;
							obj[DIRECTION] = -obj[DIRECTION];
						}
						
						if (obj[CURRENT_POINT] == animation.length-1 || obj[CURRENT_POINT] == 0) {
							obj[DIRECTION] = -obj[DIRECTION];
						}
						obj[CURRENT_POINT] += obj[DIRECTION];
						
						curX = animation[(int) obj[CURRENT_POINT]].x;
						curY = animation[(int) obj[CURRENT_POINT]].y;
						prevX = animation[(int) (obj[CURRENT_POINT] - obj[DIRECTION])].x;
						prevY = animation[(int) (obj[CURRENT_POINT] - obj[DIRECTION])].y;
						
						double useX = obj[X1]+obj[XDIF] - prevX;
						double useY = obj[Y1]+obj[YDIF] - prevY;
						
						obj[X1] -= useX; obj[Y1] -= useY;
						obj[X2] -= useX; obj[Y2] -= useY;
						
						if(obj[TYPE] == TYPE_TRIANGLE) {
							obj[X3] -= useX;
							obj[Y3] -= useY;
						}
						
						int length = (int) Math.sqrt(Math.pow(curX - prevX, 2) + Math.pow(curY - prevY, 2));
						obj[MOVEX] = (double)(curX - prevX) / length * obj[ANIM_SPEED];
						obj[MOVEY] = (double)(curY - prevY) / length * obj[ANIM_SPEED];
					}
				}
				
				if (obj[AT_ZERO] == 1) {
					obj[X1] += obj[MOVEX];
					obj[Y1] += obj[MOVEY];
					
					obj[X2] += obj[MOVEX];
					obj[Y2] += obj[MOVEY];
				}
				
				double x1 = obj[X1];
				double x2 = obj[X2];
				double y1 = obj[Y1];
				double y2 = obj[Y2];
				
				if (obj[TYPE] == TYPE_BUTTON) {
					obj[THICKNESS] += 0.05;
					if (obj[THICKNESS] >= 16) {
						obj[THICKNESS] = 16;
					}
					if (obj[THICKNESS] < 16 && obj[THICKNESS] >= 8) {
						allObjects[(int) obj[ATTACHED]][SWITCH] = BTN_ON;
						allObjects[(int) obj[ATTACHED]][AT_ZERO] = 1;
					} else {
						allObjects[(int) obj[ATTACHED]][SWITCH] = BTN_OFF;
					}
				}
				
				if (obj[TYPE] == TYPE_LINE || obj[TYPE] == TYPE_BUTTON) {
					double thicknessUse = obj[THICKNESS]/2;
					
					if (x1 < x2) {
						x1 -= thicknessUse;
						x2 += thicknessUse;
					} else {
						x1 += thicknessUse;
						x2 -= thicknessUse;
					}
					
					y1 -= thicknessUse;
					y2 -= thicknessUse;
				}
				
				double height = Math.abs(y2 - y1);
				double minY = Math.max(y2, y1);
				double width = x2-x1;
				
				double percAcross = 1 - (posX / SCALE - x1) / ((double) width);
				double inside = (minY - (percAcross * height)) - posY / SCALE;
				
				double percA1 = (1 - percAcross) * Math.abs(width);
				double percA2 = percAcross * Math.abs(width);
				double useA = Math.min(percA1, percA2);
				
				boolean collide = false;
				stuck = false;
				
				if(obj[TYPE] == TYPE_TRIANGLE) {
					if (obj[AT_ZERO] == 1) {
						obj[X3] += obj[MOVEX];
						obj[Y3] += obj[MOVEY];
					}
				}
				
				if (obj[TYPE] == TYPE_LINE || obj[TYPE] == TYPE_BUTTON) {
					
					if (percAcross <= 1 && percAcross >=0) {
						if (/*useHeight > 98 && useHeight < 145*/inside >= -50 && inside <= -14) {
							collide = true;
						}
					}
					
					final int PIXEL_AMOUNT = 10;
					if (percAcross <= 1 && percAcross >=0  &&  inside >= -50 && inside <= -40 && percAcross > PIXEL_AMOUNT/Math.abs(width) && percAcross < 1 - (PIXEL_AMOUNT/Math.abs(width))) {
						moveY = 0;
						moveObj(0, -(-50 - inside), 0);
						
						collide = false;
						if (onGround) {
							stuck = true;
							collide = true;
						}
					}
				}
				
				if (posY/SCALE <= minY + (obj[TYPE]==TYPE_TRIANGLE ? 2000 : 14) && inside < 0) {
					if (percAcross <= 1 && percAcross >= 0) {
						if (inside > -14 && inside < 0) {
							if (obj[TYPE] == TYPE_BUTTON && obj[THICKNESS]>=8) {
								obj[THICKNESS] += inside;
								moveObj(0, -(8 - obj[THICKNESS]), 0);
								if (obj[THICKNESS] < 8) {
									obj[THICKNESS] = 8;
								}
							} else {
								moveObj(0, inside, 0);
							}
							
							//fer bouncing
							if (moveY >= obj[MOVEY]) {
								onGround = true;
							}
							if (onGround) {
								if (obj[AT_ZERO] == 1) {
									moveY = (Math.abs(obj[MOVEY])>=1 ? Math.round(obj[MOVEY]) : obj[MOVEY]) + 1;
								} else {
									moveY = 1;
								}
							}
							
							angle = Math.atan2(y2-y1, x2-x1);
							
							if (Math.abs(Math.cos(angle))>=STEEPNESS && obj[AT_ZERO] == 1) {
								moveObj(obj[MOVEX], 0, 0);
							}
							//when it finishes looping this should be whichever object the guy is on.
						} else {
							//if the guy hits a wall on a triangle
							if (obj[TYPE] == TYPE_TRIANGLE) {
								collide = true;
							}
						}
					}
				}
				
				if (collide) {
					//FER BOUNCING
					//moveY = inside/2;
					double insideAmountMove;
					if (posX/2 > Math.abs(x2-x1)/2 + Math.min(x1, x2)) {
						insideAmountMove = useA;
					} else {
						insideAmountMove = -useA;
					}
					
					if (stuck) {
						insideAmountMove = -moveX;
					}
					
					moveObj(insideAmountMove, 0, 0);
					
					int middle = (int) (Math.min(x1, x2) + width/2);
					if (obj[MOVEX]!=0) {
						if (Math.signum(obj[MOVEX]) == 1 && posX > middle  ||  Math.signum(obj[MOVEX]) == -1 && posX < middle) {
							moveX = obj[MOVEX];
						}
					}
					moveObj(moveX, 0, 0);
				}
			}

			if (Math.abs(Math.cos(angle)) < STEEPNESS && onGround) {
				moveObj(Math.cos(angle) * 4 + ((Math.abs(Math.cos(angle))<STEEPNESS/2) ? Math.sin(angle)/2 : 0), Math.sin(angle) * 4, 1);
				//if(true) ? a : b
			}
			
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
						int[] xp = { (int) obj[X1], (int) obj[X2], (int) obj[X3] };
						int[] yp = { (int) obj[Y1], (int) obj[Y2], (int) obj[Y3] };
						
						lg.fillPolygon(xp, yp, 3);
						
						lg.fillRect((int) Math.min(obj[X1], obj[X2]), (int) obj[Y3], (int) Math.abs(obj[X2] - obj[X1]), 2000);
						break;
					case TYPE_BUTTON:
						double angle = Math.atan2(obj[Y2] - obj[Y1], obj[X2] - obj[X1]);
						int distance = (int) Math.sqrt((obj[X2] - obj[X1]) * (obj[X2] - obj[X1]) + (obj[Y2] - obj[Y1]) * (obj[Y2] - obj[Y1]));
						
						int x2 = (int) (obj[X1] + distance);
						
						int[] xp1 = {(int) obj[X1], (int) obj[X1] + 8, (int) obj[X1] + 8};
						int[] yp1 = {(int) obj[Y1], (int) obj[Y1], (int) obj[Y1] + 4};
						
						int[] xp2 = {(int) x2, (int) x2 - 8, (int) x2 - 8};
						int[] yp2 = {(int) obj[Y1], (int) obj[Y1], (int) obj[Y1] + 4};
						
						((Graphics2D) lg).rotate(angle, (int) obj[X1], (int) obj[Y1]);
						lg.fillPolygon(xp1, yp1, 3);
						lg.fillRect((int) obj[X1] + 8, (int) obj[Y1], ((int) x2 - (int) obj[X1]) - 16, (int) obj[THICKNESS]/2);
						lg.fillPolygon(xp2, yp2, 3);
						((Graphics2D) lg).rotate(-angle, (int) obj[X1], (int) obj[Y1]);
						break;
					case TYPE_EXITPOINT:
						for (int x = -8; x <= 8; x++) {
							for (int y = -8; y <= 8; y++) {
								int value = (8 - (Math.abs(x) + Math.abs(y))/2) * 4;
								if (value < 0) {
									value = 0;
								}
								lg.setColor(new Color(value * 2, 0, 0));
								lg.fillRect((int) obj[X1] + x, (int) obj[Y1] + y, 1, 1);
							}
						}
						//lg.fillOval((int) obj[X1], (int) obj[Y1], 10, 10);
						break;
				}
			}
			
			g.drawImage(level, 0, 0, width * SCALE, height * SCALE, this);
			if (diedTimer>0) {
				diedTimer++;
				
				g.setColor(new Color(0, 0, 0, (int)(diedTimer <= 200 ? diedTimer * 1.275 : 255)));
				g.fillRect(0, 0, width * SCALE, height * SCALE);
				if (diedTimer >= 200) {
					moveY = -3;
				}
				if (diedTimer > 300) {
					loadLevel(levelNum);
					moveX = 0;
					moveY = 0;
					diedTimer = 0;
				}
			}
			
			if (!onGround) {
				armMoveY = moveY;
			} else {
				armMoveY -= 5;
			}
			
			//moveX = Math.round(moveX * 4) / 4.0;
			moveX = Math.round(moveX * 1000) / 1000.0;
			
			boolean flip = moveX <= -0.25;
			
			if (!onGround || (int) moveX == 0) {
				drawGuy(g, STAND, armMoveY, time, flip);
			} else if ((int)moveX != 0) {
				drawGuy(g, WALK, 0, time, flip);
			}
			
			time++;
			
			g.setColor(Color.white);
			//g.drawString("FPS " + String.valueOf(fps), 20, 30);
			
			// render the buffer to the applet
			appletGraphics.drawImage(screen, 0, 0, this);
			
			long end = System.nanoTime();
			
			long delta = 20 - ((end - start) / 1000000L);
			if(delta < 0)
				delta = 0;
			
			try {
				Thread.sleep(delta);
			} catch(Exception e) { }
			
			if (!isActive()) {
				return;
			}
		}
	}
	
	private int readAnimation(String level, int index) {
		// number of points in the animation
		animations[numObjects] = new Point[level.charAt(index++)];
		
		if(animations[numObjects].length == 0)
			return index;
		
		allObjects[numObjects][ANIM_SPEED] = level.charAt(index++);
		
		for(int k = 0; k < animations[numObjects].length; k++) {
			int x = (short) level.charAt(index++);
			int y = (short) level.charAt(index++);
			if (y > lowestY)
				lowestY = y;
			
			animations[numObjects][k] = new Point(x, y);
		}
		
		allObjects[numObjects][CURRENT_POINT] = 0.0;
		allObjects[numObjects][DIRECTION] = -1.0;
		
		if(animations[numObjects].length > 0) {
			allObjects[numObjects][XDIF] = animations[numObjects][0].x - allObjects[numObjects][X1];
			allObjects[numObjects][YDIF] = animations[numObjects][0].y - allObjects[numObjects][Y1];
		}
		
		return index;
	}
	
	public void loadLevel(int num) {
		String level = levels[num];
		allObjects = new double[1000][19];
		animations = new Point[1000][];
		numObjects = 0;
		moveOverTimeY = 0;
		
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
			// FLAGS DOES NOT EVEN MATTER ANYMORE
			char ch = level.charAt(index++);
			int type = ch & 0xff;
			
			// store base information that's common to every object in 
			// the first 2 fields
			allObjects[numObjects][TYPE] = type;
			
			switch(type) {
				case TYPE_EXITPOINT:
					allObjects[numObjects][X1] = (short) level.charAt(index++); // x
					allObjects[numObjects][Y1] = (short) level.charAt(index++); // y
					if (allObjects[numObjects][Y1]>lowestY)
						lowestY = allObjects[numObjects][Y1];
					break;
					
				case TYPE_LINE:
				case TYPE_BUTTON:
				case TYPE_TRIANGLE:
					allObjects[numObjects][X1] = (short) level.charAt(index++); // x1
					allObjects[numObjects][Y1] = (short) level.charAt(index++); // y1
					if (allObjects[numObjects][Y1] > lowestY)
						lowestY = allObjects[numObjects][Y1];
	
					allObjects[numObjects][X2] = (short) level.charAt(index++); // x2
					allObjects[numObjects][Y2] = (short) level.charAt(index++); // y2
					if (allObjects[numObjects][Y2] > lowestY)
						lowestY = allObjects[numObjects][Y2];
	
					if (type == TYPE_TRIANGLE) {
						allObjects[numObjects][X3] = (short) level.charAt(index++); // x3
						allObjects[numObjects][Y3] = (short) level.charAt(index++); // y3
						if (allObjects[numObjects][Y3] > lowestY)
							lowestY = allObjects[numObjects][Y3];
					}
	
					if (type == TYPE_LINE)
						allObjects[numObjects][THICKNESS] = level.charAt(index++); // thickness
					else if (type == TYPE_BUTTON)
						allObjects[numObjects][THICKNESS] = 16; // button thickness is always 8
					break;
			}
			index = readAnimation(level, index);
			
			allObjects[numObjects][SWITCH] = BTN_ON;
			allObjects[numObjects][AT_ZERO] = 1.0;
			numObjects++;
		}

		for (int k = 0; k < numObjects; k++) {
			if (allObjects[k][TYPE] == TYPE_BUTTON) {
				for (int j = k; j < numObjects; j++) {
					if (animations[j].length > 0) {
						allObjects[j][AT_ZERO] = 0.0;
						allObjects[k][ATTACHED] = j;
						break;
					}
				}
			}
		}
	}

	public void moveObj(double x, double y, int slope) {
		double useY = 0;
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
				useX = Math.ceil(Math.abs(x)) * Math.signum(x);
				//System.out.println(Math.abs(Math.cos(angle))+ " - " + Math.toDegrees(angle) + " - " + (angle < HALF_PI) + testX + "-" + Math.signum(testX));
			}
			
			useY = Math.ceil(Math.abs(y)) * Math.signum(y);

			Point[] anim = animations[k];
			for(int j = 0; j < anim.length; j++) {
				anim[j].x -= useX;
				anim[j].y -= useY;
			}
			
			obj[X1] -= useX; obj[Y1] -= useY;
			obj[X2] -= useX; obj[Y2] -= useY;
			
			if(obj[TYPE] == TYPE_TRIANGLE) {
				obj[X3] -= useX;
				obj[Y3] -= useY;
			}
			
		}
		moveOverTimeY += useY;
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
		
		if (action == WALK/* || action == PUSH*/) {
			stillCalc = calc;
			armAngle = calc;
			
			//if (param>bodyAngle) {
			//	bodyAngle++;
			//} else if (param < bodyAngle) {
			//	bodyAngle--;
			//}
			
			//Bounce effect
			g.rotate(Math.toRadians(-calc),(int)(posX - 4 + Math.sin(Math.toRadians(-calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-calc))*16));
			g.rotate(Math.toRadians(calc),(int)(posX - 4 + Math.sin(Math.toRadians(calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(calc))*16));
			
			//if (action==WALK) {
				g.rotate(Math.toRadians(calc*1.3), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
				g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32), 6, 30, 6, 10);
				g.rotate(Math.toRadians(-calc*1.3), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
			//} else {
			//	g.rotate(Math.toRadians(-75), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			//	g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			//	g.rotate(Math.toRadians(75), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			//}
			
			//back leg1
			g.rotate(Math.toRadians(-calc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(calc),(int)posX, (int)posY - 35);
			
			//back leg2
			g.rotate(Math.toRadians(-calc+15),(int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(calc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(calc+15))*16) + 2, 6, 20, 6, 10);
			g.rotate(Math.toRadians(calc-15),(int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2);
			
			//body
			//g.rotate(Math.toRadians(bodyAngle), (int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 8, (int)(posY - 72), 15, 40, 10, 15);
			
			//head
			g.fillRoundRect((int)posX - 12, (int)(posY - 96), 22, 25, 20, 20);
			//g.rotate(Math.toRadians(-bodyAngle), (int)posX, (int)posY - 35);
			//I have to draw the head down here because I am getting a nice bounce effect
			
			//front arm
			//if (action==WALK) {
				g.rotate(Math.toRadians(-calc*1.3), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
				g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32), 6, 30, 6, 10);
				g.rotate(Math.toRadians(calc*1.3), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
			//} else {
			//	g.rotate(Math.toRadians(-100), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			//	g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32), 6, 30, 6, 10);
			//	g.rotate(Math.toRadians(100), (int)(posX - Math.cos(Math.toRadians(bodyAngle+90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(bodyAngle+90))*32));
			//}
			
			//front leg1
			g.rotate(Math.toRadians(calc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(-calc),(int)posX, (int)posY - 35);
			
			//front leg2
			g.rotate(Math.toRadians(calc+15),(int)(posX - 4 + Math.sin(Math.toRadians(calc+15))*16 - 2), (int)(posY - 35 + Math.cos(Math.toRadians(calc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(-calc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(-calc+15))*16) + 2, 6, 20, 6, 10);
		} else if (action == STAND) {
			//For a good segue
			time = 0;
			
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
			
			if (stillCalc>-10.5) {
				stillCalc-=0.1;
			} else if (stillCalc<-10.5) {
				stillCalc+=0.1;
			}
			stillCalc = Math.round(stillCalc*10) / 10.0;
			
			//if (bodyAngle<0) {
			//	bodyAngle++;
			//} else if (bodyAngle>0) {
			//	bodyAngle--;
			//}
			
			//Bounce effect
			g.rotate(Math.toRadians(-stillCalc),(int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc))*16));
			g.rotate(Math.toRadians(stillCalc),(int)(posX - 4 + Math.sin(Math.toRadians(stillCalc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc))*16));
			
			//back arm
			g.rotate(Math.toRadians(armAngle), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(-armAngle), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
			
			//back leg1
			g.rotate(Math.toRadians(-stillCalc),(int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 4, (int)posY - 35, 7, 18, 5, 10);
			g.rotate(Math.toRadians(stillCalc),(int)posX, (int)posY - 35);
			
			//back leg2
			g.rotate(Math.toRadians(-stillCalc+8),(int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc+15))*16) + 2);
			g.fillRoundRect((int)(posX - 4 + Math.sin(Math.toRadians(stillCalc+15))*16) - 2, (int)(posY - 35 + Math.cos(Math.toRadians(stillCalc+15))*16) + 2, 6, 20, 6, 10);
			g.rotate(Math.toRadians(stillCalc-8),(int)(posX - 4 + Math.sin(Math.toRadians(-stillCalc+15))*16 + 2), (int)(posY - 35 + Math.cos(Math.toRadians(-stillCalc+15))*16) + 2);
			
			//body
			//g.rotate(Math.toRadians(bodyAngle), (int)posX, (int)posY - 35);
			g.fillRoundRect((int)posX - 8, (int)(posY - 72), 15, 40, 10, 15);
			
			//head
			g.fillRoundRect((int)posX - 12, (int)(posY - 96), 22, 25, 20, 20);
			//g.rotate(Math.toRadians(-bodyAngle), (int)posX, (int)posY - 35);
			//I have to draw the head down here because I am getting a nice bounce effect
			
			//front arm
			g.rotate(Math.toRadians(-armAngle), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
			g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32), 6, 30, 6, 10);
			g.rotate(Math.toRadians(armAngle), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*32) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
			
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