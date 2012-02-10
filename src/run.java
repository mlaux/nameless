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
	private static final int TYPE_SPAWNPOINT = 0x01;
	private static final int TYPE_EXITPOINT = 0x02;
	private static final int TYPE_LINE = 0x03;
	private static final int TYPE_TRIANGLE = 0x04;
	private static final int TYPE_BUTTON = 0x05;
	
	private static final double BTN_ON = 1.0;
	private static final double BTN_OFF = 0.0;
	
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
	
	private static final int SWITCH = 11;
	private static final int CURRENT_POINT = 10;
	private static final int DIRECTION = 12;
	private static final int XDIF = 13;
	private static final int YDIF = 14;
	private static final int ANIM_SPEED = 15;
	
	private static final int SPEED = 2;
	
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
	private static final String level = "\u0004\u0065\u00b0\u0001\u00c0\u0065\u00c0\u0000\u0004\u0065\u00b0\u00e5\u00d3\u0065\u00d3\u0000\u0004\u0151\u00b2\u00e5\u00d3\u0151\u00d3\u0000\u0004\u018c\u00ca\u0152\u00ca\u018c\u00ca\u0002\u0007\u0174\u00ea\u0174\u031b";
	//private static final String level = "\u0004\u0002\u0129\u00d8\u0144\u0002\u0144\u0000\u0004\u00d8\u0144\u0111\u0148\u00d8\u0148\u0000\u0004\u0174\u0141\u0111\u0148\u0174\u0148\u0000\u0004\u0174\u0141\u0174\u0141\u0174\u0141\u0000\u0004\u01ae\u0138\u0174\u0141\u01ae\u0141\u0000\u0004\u0247\u0159\u0287\u015f\u0247\u015f\u0000\u0004\u02d8\u0159\u0286\u015f\u02d8\u015f\u0000\u0004\u030a\u0155\u02d8\u0159\u030a\u0159\u0000\u0004\u032c\u0149\u030a\u0155\u032c\u0155\u0000\u0004\u039d\u0181\u03dd\u0181\u039d\u0181\u0002\u0001\u03c1\u01ff\u04e9\u01ff\u0004\u053d\u0182\u0609\u01a9\u053d\u01a9\u0000\u0004\u0608\u01a9\u0667\u01b7\u0608\u01b7\u0000\u0004\u06b6\u01b1\u0667\u01b7\u06b6\u01b7\u0000\u0004\u06e7\u019f\u06b6\u01b1\u06e7\u01b1\u0000\u0004\u0729\u0150\u06e7\u019f\u0729\u019f\u0000\u0004\u0753\u014a\u0729\u0150\u0753\u0150\u0000\u0004\u0780\u012e\u0753\u014a\u0780\u014a\u0000\u0004\u0765\u0099\u0780\u012e\u0765\u012e\u0000\u0004\u01f9\u013a\u01e6\u0195\u01f9\u0195\u0000\u0004\u01f8\u013d\u020e\u014b\u01f8\u014b\u0000\u0004\u032b\u014a\u0371\u017c\u032b\u017c\u0000\u0004\u0758\u00f4\u0765\u00f4\u0758\u00f4\u0000\u0004\u077e\u012a\u07ae\u01aa\u077e\u01aa\u0000\u0004\u07ae\u01aa\u07eb\u01fd\u07ae\u01fd\u0000\u0004\u07eb\u01fd\u0835\u0220\u07eb\u0220\u0000\u0004\u0835\u0220\u0895\u0232\u0835\u0232\u0000\u0004\u0895\u0232\u08b8\u0232\u0895\u0232\u0000\u0004\u08db\u0233\u090b\u0233\u08db\u0233\u0000\u0004\u093f\u0233\u0961\u0233\u093f\u0233\u0000\u0004\u0994\u0235\u09a8\u0235\u0994\u0235\u0000\u0004\u09d9\u0236\u09e5\u0236\u09d9\u0236\u0000\u0004\u0a28\u0259\u0ae9\u0259\u0a28\u0259\u0000\u0004\u0ae9\u0259\u0ae9\u0259\u0ae9\u0259\u0000\u0004\u0b34\u0255\u0ae9\u0259\u0b34\u0259\u0000\u0004\u0b7c\u0249\u0b34\u0255\u0b7c\u0255\u0000\u0004\u0bc0\u023b\u0b7c\u0249\u0bc0\u0249\u0000\u0004\u0bee\u022b\u0bc0\u023b\u0bee\u023b\u0000\u0004\u0c10\u0226\u0bee\u022b\u0c10\u022b\u0000\u0004\u0c10\u0226\u0c10\u0226\u0c10\u0226\u0000\u0004\u0c3b\u0225\u0c10\u0226\u0c3b\u0226\u0000\u0004\u0c39\u0225\u0c5b\u022a\u0c39\u022a\u0000\u0004\u0c5b\u022a\u0c75\u022c\u0c5b\u022c\u0000\u0004\u0c95\u0229\u0c75\u022c\u0c95\u022c\u0000\u0004\u0cc1\u020f\u0c94\u0229\u0cc1\u0229\u0000\u0004\u0cc1\u020f\u0cc1\u020f\u0cc1\u020f\u0000\u0004\u0cfe\u01de\u0cc1\u020f\u0cfe\u020f\u0000\u0004\u0d3b\u018a\u0cfe\u01de\u0d3b\u01de\u0000\u0004\u0d53\u0180\u0d39\u018c\u0d53\u018c\u0000\u0004\u0d7e\u017d\u0d53\u0180\u0d7e\u0180\u0000\u0004\u0d7e\u017d\u0d9c\u017e\u0d7e\u017e\u0000\u0004\u1160\u056e\u0d68\u059c\u1160\u059c\u0000\u0004\u11ab\u055e\u115e\u056e\u11ab\u056e\u0000\u0004\u11e0\u054c\u11ab\u055e\u11e0\u055e\u0000\u0004\u11e0\u054c\u11e0\u054c\u11e0\u054c\u0000\u0004\u1217\u051c\u11e0\u054c\u1217\u054c\u0000\u0004\u1255\u04ce\u1217\u051c\u1255\u051c\u0000\u0004\u127c\u0465\u1255\u04ce\u127c\u04ce\u0000\u0004\u1297\u03f8\u127b\u0465\u1297\u0465\u0000\u0004\u12ac\u039e\u1295\u03fc\u12ac\u03fc\u0000\u0004\u12ab\u039f\u1452\u039f\u12ab\u039f\u0000\u0004\u0003\u0129\ufef6\u0137\u0003\u0137\u0000\u0004\ufef6\u0137\ufe4a\u0138\ufef6\u0138\u0000\u0004\ufe9a\u0126\ufec9\u0126\ufe9a\u0126\u000c\u0001\ufeb3\u012e\ufe8c\u0125\ufe6f\u0108\ufe69\u00e6\ufe76\u00cc\ufe94\u00b9\ufebc\u00b6\ufeeb\u00c4\ufef5\u00ea\ufee8\u010f\ufed1\u0125\ufeb3\u012e\u0003\u0068\u0111\u00b0\u0119\u000c\u0002\u0001\u0093\u0115\u0171\u007a\u0003\u0344\u005b\u0169\u00ab\u0001\u0000\u0003\u03c3\u0023\u0344\u005a\u0001\u0000\u0003\u04a2\u0027\u04fa\u0056\u0001\u0000\u0003\u0594\u0077\u0505\u0077\u0005\u0002\u0001\u0551\u0077\u04f7\u0184\u0003\u03f6\u0013\u03c4\"\u0001\u0000\u0003\u0425\u000e\u03f7\u0012\u0001\u0000\u0003\u0426\u000e\u0460\u0011\u0001\u0000\u0003\u0461\u0011\u04a1\u0026\u0001\u0000\u0003\u0159\u012b\u00da\u012b\u0001\u0000\u0003\u0195\u011d\u0165\u012a\u0009\u0000";
	
	private int time = 10;
	private int calc = 2;
	private int direction = 1;
	//private double bodyAngle;
	private static final int posX = 400;
	private static final int posY = 300;
	private double stillCalc;
	private double armAngle;
	private double armMoveY;
	private double angle;
	//private byte insideObj;
	
	private double lowestY = 300;
	private double moveOverTimeY;
	
	private boolean stuck = false;
	
	private int diedTimer;
	
	private double[][] allObjects = new double[1000][16];
	private Point[][] animations = new Point[1000][];
	private int numObjects = 0;
	
	private boolean[] keys = new boolean[0xFFFF];

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		setSize(width * SCALE, height * SCALE); // For AppletViewer, remove later.
		
		BufferedImage bkg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt) bkg.getRaster().getDataBuffer()).getData();
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int is = (int) (y * (150.0 / height));
				pixels[y * (width) + x] = is << 16 | is << 8 | is;
			}
		}
		
		resetLevel();
		
		BufferedImage level = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D lg = (Graphics2D) level.getGraphics();
		
		// set up graphics
		BufferedImage screen = new BufferedImage(width * SCALE, height * SCALE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) screen.getGraphics();
		
		Graphics appletGraphics = getGraphics();

		double moveX = 0, moveY = 0;
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
			
			if (moveOverTimeY > lowestY + 100) {
				diedTimer++;
			}
			
			for(int k = 0; k < numObjects; k++) {
				double[] obj = allObjects[k];
				Point[] animation = animations[k];
				
				obj[SWITCH] = BTN_ON; //TODO place this in the button collision code, if there is no button it's on
				
				if (obj[SWITCH] == BTN_ON && animation.length > 1) {
					int curX = animation[(int) obj[CURRENT_POINT]].x;
					int curY = animation[(int) obj[CURRENT_POINT]].y;
					
					int prevY, prevX;
					if (obj[CURRENT_POINT] == 0) {
						prevX = curX;
						prevY = curY;
					} else {
						prevX = animation[(int) (obj[CURRENT_POINT] - obj[DIRECTION])].x;
						prevY = animation[(int) (obj[CURRENT_POINT] - obj[DIRECTION])].y;
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
				
				obj[X1] += obj[MOVEX];
				obj[Y1] += obj[MOVEY];
				
				obj[X2] += obj[MOVEX];
				obj[Y2] += obj[MOVEY];
				
				double x1 = obj[X1];
				double x2 = obj[X2];
				double y1 = obj[Y1];
				double y2 = obj[Y2];
				
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
					obj[X3] += obj[MOVEX];
					obj[Y3] += obj[MOVEY];
				}
				
				if (obj[TYPE] == TYPE_LINE) {
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
							moveObj(0, inside, 0);
							
							//fer bouncing
							if (moveY >= obj[MOVEY]) {
								onGround = true;
							}
							if (onGround) {
								moveY = (Math.abs(obj[MOVEY])>=1 ? Math.round(obj[MOVEY]) : obj[MOVEY]) + 1;
							}
							
							angle = Math.atan2(y2-y1, x2-x1);
							
							if (Math.abs(Math.cos(angle))>=STEEPNESS) {
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
					if (/*obj[TYPE]==TYPE_TRIANGLE*/true) { // ALSO DO THIS IF YOU'RE COLLIDING WITH THE LINE THE WAY I DESCRIBED TO MATT AT THE BEGINNING OF THE YEAR!!!!!!!!!!!
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
						
						int middle = (int) (Math.min(obj[X1], obj[X2]) + width/2);
						if (obj[MOVEX]!=0) {
							if (Math.signum(obj[MOVEX]) == 1 && posX > middle  ||  Math.signum(obj[MOVEX]) == -1 && posX < middle) {
								moveX = obj[MOVEX];
							}
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
				int[] xp, yp;
				
				switch((int) obj[TYPE]) {
					case TYPE_LINE:
						lg.setStroke(new BasicStroke((float) obj[THICKNESS]));
						lg.drawLine((int) obj[X1], (int) obj[Y1], (int) obj[X2], (int) obj[Y2]);
						break;
					case TYPE_TRIANGLE:
						xp = new int[] { (int) obj[X1], (int) obj[X2], (int) obj[X3] };
						yp = new int[] { (int) obj[Y1], (int) obj[Y2], (int) obj[Y3] };
						
						lg.fillPolygon(xp, yp, 3);
						
						lg.fillRect((int) Math.min(obj[X1], obj[X2]), (int) obj[Y3], (int) Math.abs(obj[X2] - obj[X1]), 2000);
						break;
					case TYPE_BUTTON:
						int x1 = (int) obj[X1] * 2;
						int y1 = (int) obj[Y1] * 2;
						
						xp = new int[] { x1, x1 + 16, x1 + 16 };
						yp = new int[] { y1, y1, y1 - 8 };
						
						g.fillPolygon(xp, yp, 3);
						
						xp = new int[] { x1 + width + 32, x1 + width + 16, x1 + width + 16 };
						yp = new int[] { y1, y1, y1 - 8 };
						g.fillPolygon(xp, yp, 3);
						
						g.fillRect((int) obj[X1] + 16, (int) obj[Y1] - 16 , width, HEIGHT);
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
					resetLevel();
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
	
	private int readAnimation(int index) {
		// number of points in the animation
		animations[numObjects] = new Point[level.charAt(index++)];
		if(animations[numObjects].length == 0)
			return index;
		
		allObjects[numObjects][ANIM_SPEED] = level.charAt(index++);
		
		for(int k = 0; k < animations[numObjects].length; k++) {
			int x = level.charAt(index++);
			int y = level.charAt(index++);
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
	
	public void resetLevel() {
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
			char ch = level.charAt(index++);
			int type = ch & 0xff, flags = ch & 0xff00;
			
			// store base information that's common to every object in 
			// the first 2 fields
			allObjects[numObjects][TYPE] = type;
			allObjects[numObjects][FLAGS] = flags;
			
			switch(type) {
				case TYPE_SPAWNPOINT:
				case TYPE_EXITPOINT:
					allObjects[numObjects][X] = (short) level.charAt(index++); // x
					allObjects[numObjects][Y] = (short) level.charAt(index++); // y
					if (allObjects[numObjects][Y]>lowestY)
						lowestY = allObjects[numObjects][Y];
					break;
				case TYPE_LINE:
					allObjects[numObjects][X1] = (short) level.charAt(index++); // x1
					allObjects[numObjects][Y1] = (short) level.charAt(index++); // y1
					if (allObjects[numObjects][Y1]>lowestY)
						lowestY = allObjects[numObjects][Y1];
					allObjects[numObjects][X2] = (short) level.charAt(index++); // x2
					allObjects[numObjects][Y2] = (short) level.charAt(index++); // y2
					if (allObjects[numObjects][Y2]>lowestY)
						lowestY = allObjects[numObjects][Y2];
					allObjects[numObjects][THICKNESS] = level.charAt(index++); // thickness
					index = readAnimation(index);
					break;
				case TYPE_TRIANGLE:
					allObjects[numObjects][X1] = (short) level.charAt(index++); // x1
					allObjects[numObjects][Y1] = (short) level.charAt(index++); // y1
					if (allObjects[numObjects][Y1]>lowestY)
						lowestY = allObjects[numObjects][Y1];
					allObjects[numObjects][X2] = (short) level.charAt(index++); // x2
					allObjects[numObjects][Y2] = (short) level.charAt(index++); // y2
					if (allObjects[numObjects][Y2]>lowestY)
						lowestY = allObjects[numObjects][Y2];
					allObjects[numObjects][X3] = (short) level.charAt(index++); // x3
					allObjects[numObjects][Y3] = (short) level.charAt(index++); // y3
					if (allObjects[numObjects][Y3]>lowestY)
						lowestY = allObjects[numObjects][Y3];
					index = readAnimation(index);
					break;
			}
			
			numObjects++;
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