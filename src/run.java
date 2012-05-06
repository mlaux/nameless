import java.applet.Applet;
import java.awt.AlphaComposite;
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
	
	private static final int TYPE = 0;
	
	private static final int X1 = 1;
	private static final int Y1 = 2;
	private static final int X2 = 3; 
	private static final int Y2 = 4;
	private static final int X3 = 5;
	private static final int Y3 = 6;
	
	private static final int MOVEX = 7;
	private static final int MOVEY = 8;

	private static final int CURRENT_POINT = 9;
	private static final int SWITCH = 10;
	private static final int DIRECTION = 11;
	private static final int XDIF = 12;
	private static final int YDIF = 13;
	private static final int ANIM_SPEED = 14;
	private static final int ATTACHED = 15;
	private static final int PREV_CURRENT_POINT = 16;
	private static final int AT_ZERO = 17;
	private static final int THICKNESS = 18;
	
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
		"\u0004\u004b\u0092\u018d\u009c\u004b\u009c\u0000\u0004\u018d\u009c\u01f1\u00a2\u018d\u00a2\u0000\u0004\u01f1\u00a2\u0216\u00a8\u01f1\u00a8\u0000\u0004\u0216\u00a8\u022d\u00b6\u0216\u00b6\u0000\u0004\u022d\u00b6\u0241\u00c8\u022d\u00c8\u0000\u0004\u0241\u00c8\u0248\u00d6\u0241\u00d6\u0000\u0004\u0248\u00d6\u0253\u00ef\u0248\u00ef\u0000\u0004\u0253\u00ef\u0260\u00fe\u0253\u00fe\u0000\u0004\u0260\u00fe\u0273\u010b\u0260\u010b\u0000\u0004\u0273\u010b\u028b\u0118\u0273\u0118\u0000\u0004\u028b\u0118\u0299\u011c\u028b\u011c\u0000\u0004\u0299\u011c\u02b4\u011e\u0299\u011e\u0000\u0004\u02e7\u011c\u02b4\u011e\u02e7\u011e\u0000\u0004\u0303\u0117\u02e7\u011c\u0303\u011c\u0000\u0004\u0342\u0101\u0302\u0117\u0342\u0117\u0000\u0004\u0389\u00de\u0342\u0101\u0389\u0101\u0000\u0004\u03b6\u00c3\u0389\u00de\u03b6\u00de\u0000\u0004\u03e4\u009d\u03b6\u00c3\u03e4\u00c3\u0000\u0004\u0401\u0084\u03e3\u009e\u0401\u009e\u0000\u0004\u041a\u0076\u0401\u0084\u041a\u0084\u0000\u0004\u043c\u006a\u041a\u0076\u043c\u0076\u0000\u0004\u0461\u0065\u043c\u006a\u0461\u006a\u0000\u0004\u0461\u0065\u0494\u0066\u0461\u0066\u0000\u0004\u0494\u0066\u04c1\u006c\u0494\u006c\u0000\u0004\u0078\uff78\uffaa\uff78\u0078\uff78\u0000\u0004\u04c1\u006c\u0506\u0072\u04c1\u0072\u0000\u0004\u0536\u0071\u0506\u0072\u0536\u0072\u0000\u0004\u057d\u0071\u0536\u0071\u057d\u0071\u0000\u0004\u05b5\u006c\u057d\u0071\u05b5\u0071\u0000\u0004\u05f4\u005e\u05b5\u006c\u05f4\u006c\u0000\u0004\u0631\u0044\u05f4\u005e\u0631\u005e\u0000\u0004\u0692\u000c\u0620\u000c\u0692\u000c\u0000\u0004\u06ea\uffc8\u0681\uffc8\u06ea\uffc8\u0000\u0004\u07c4\uffc8\u0755\uffc8\u07c4\uffc8\u0000\u0004\u07c4\uffc8\u07dc\uffca\u07c4\uffca\u0000\u0004\u07dc\uffca\u07f0\uffce\u07dc\uffce\u0000\u0004\u07f0\uffce\u080a\uffd7\u07f0\uffd7\u0000\u0004\u080a\uffd7\u0817\uffe1\u080a\uffe1\u0000\u0004\u0817\uffe1\u0827\ufff1\u0817\ufff1\u0000\u0004\u0827\ufff1\u0834\u0003\u0827\u0003\u0000\u0004\u0834\u0003\u084a\u0043\u0834\u0043\u0000\u0004\u084a\u0043\u0869\u00cb\u084a\u00cb\u0000\u0004\u0868\u00c9\u0884\u01c6\u0868\u01c6\u0000\u0004\u0884\u01c0\u08ab\u0334\u0884\u0334\u0000\u0004\u08ab\u032f\u08cd\u0447\u08ab\u0447\u0000\u0004\u08cd\u0443\u08dd\u04ae\u08cd\u04ae\u0000\u0004\u08dd\u04ae\u08f8\u0518\u08dd\u0518\u0000\u0004\u08f8\u0518\u090e\u056a\u08f8\u056a\u0000\u0004\u090e\u056a\u0922\u058d\u090e\u058d\u0000\u0004\u0921\u058d\u095b\u05da\u0921\u05da\u0000\u0004\u095b\u05da\u097b\u05f2\u095b\u05f2\u0000\u0004\u097b\u05f2\u09b4\u060e\u097b\u060e\u0000\u0004\u09b4\u060e\u09eb\u061d\u09b4\u061d\u0000\u0004\u09eb\u061d\u0a25\u0626\u09eb\u0626\u0000\u0004\u0a5f\u0625\u0a24\u0626\u0a5f\u0626\u0000\u0004\u0aa1\u0623\u0a5f\u0625\u0aa1\u0625\u0000\u0004\u0aeb\u061c\u0aa1\u0622\u0aeb\u0622\u0000\u0004\u0b56\u0611\u0aeb\u061c\u0b56\u061c\u0000\u0004\u0c5a\u0611\u0b55\u0611\u0c5a\u0611\u0000\u0004\u0ce8\u0611\u0c5a\u0611\u0ce8\u0611\u0000\u0004\u0d40\u05db\u0cda\u05db\u0d40\u05db\u0000\u0004\u0ce2\u05dc\u0cd4\u05dd\u0ce2\u05dd\u0000\u0004\u0cdb\u05df\u0ccf\u05df\u0cdb\u05df\u0000\u0004\u0cda\u05e2\u0cca\u05e2\u0cda\u05e2\u0000\u0004\u0cd1\u05e5\u0cc5\u05e5\u0cd1\u05e5\u0000\u0004\u0ccc\u05e9\u0cbe\u05e9\u0ccc\u05e9\u0000\u0004\u0cc6\u05ec\u0cbb\u05ec\u0cc6\u05ec\u0000\u0004\u0cc0\u05f0\u0cb4\u05f0\u0cc0\u05f0\u0000\u0004\u0cbc\u05f4\u0cb1\u05f4\u0cbc\u05f4\u0000\u0004\u0cb9\u05f8\u0cac\u05f8\u0cb9\u05f8\u0000\u0004\u0cb4\u05fc\u0ca7\u05fc\u0cb4\u05fc\u0000\u0004\u0caf\u05ff\u0ca2\u0600\u0caf\u0600\u0000\u0004\u0ca8\u0603\u0c9b\u0604\u0ca8\u0604\u0000\u0004\u0c9f\u0607\u0c96\u0607\u0c9f\u0607\u0000\u0004\u0c9a\u0609\u0c92\u060a\u0c9a\u060a\u0000\u0004\u0c96\u060b\u0c8e\u060c\u0c96\u060c\u0000\u0004\u0c95\u060d\u0c8b\u060e\u0c95\u060e\u0000\u0004\u0cc3\u05ed\u0cb8\u05ee\u0cc3\u05ee\u0000\u0004\u0cca\u05e7\u0cc2\u05e7\u0cca\u05e7\u0000\u0004\u0cc8\u05e3\u0cd0\u05e4\u0cc8\u05e4\u0000\u0004\u0cd4\u05e0\u0ccd\u05e0\u0cd4\u05e0\u0000\u0004\u0cab\u0600\u0ca0\u0602\u0cab\u0602\u0000\u0002\u0d0e\u05db\u0000\u0004\u0e33\u051e\u0d34\u051e\u0e33\u051e\u0000\u0004\ufebe\uff78\uffc0\uff78\ufebe\uff78\u0000\u0006\u0b9f\u0611\u0b57\u0611\u0000\u0004\u0be5\u055d\u0bc1\u055d\u0be5\u055d\u0002\u0001\u0bd2\u0562\u0bd2\u0616",
		//"\u0004\u006f\uff0a\ufece\uff0a\u006f\uff0a\u0000\u0004\u02ae\ufff8\u0380\ufff8\u02ae\ufff8\u0000\u0004\u0380\ufff8\u03a4\u0013\u0380\u0013\u0000\u0004\u045b\u0172\u0572\u01ca\u045b\u01ca\u0000\u0004\u0572\u01ca\u07dc\u01ca\u0572\u01ca\u0000\u0006\u0645\u01ca\u0602\u01ca\u0000\u0004\u0668\u0106\u0673\u0106\u0668\u0106\u0002\u0001\u066e\u010c\u066e\u01c0\u0004\u07db\u001f\u0916\u001f\u07db\u001f\u0000\u0006\u026a\u0096\u0216\u0096\u0000\u0004\u02d3\u0096\u0065\u0096\u02d3\u0096\u0002\u0004\u01c5\u00dc\u01c5\u00c8\u0004\u03a4\u0013\u0464\u017d\u03a4\u017d\u0000\u0002\u07c5\u01ca\u0000",
		"\u0004\u019c\u008b\u0000\u00a0\u019c\u00a0\u0000\u0004\uff2a\uffb4\u001c\uffb4\uff2a\uffb4\u0000\u0006\u019c\u008c\u010f\u0093\u0000\u0004\u01ed\u008c\u019c\u008c\u01ed\u008c\u0002\u0002\u01c5\u0097\u02ea\u001d\u0004\u0439\u0012\u0312\u0012\u0439\u0012\u0000\u0006\u0439\u0012\u03f0\u0012\u0000\u0004\u0488\u0012\u0439\u0012\u0488\u0012\u0002\u0002\u046d\u0166\u046d\u003a\u0006\u05d1\ufee6\u0588\ufee6\u0000\u0003\u0486\ufd57\u001c\ufe24\u0001\u0002\u0003\u03aa\ufd7e\u03aa\uff0e\u0004\u08dc\ufcc0\u06ff\ufcc0\u08dc\ufcc0\u0000\u0004\u070f\ufee6\u0483\ufee6\u070f\ufee6\u0000\u0002\uff48\uffb4\u0000",
		"\u0004\u00fd\uff7d\u02bd\uff7d\u00fd\uff7d\u0000\u0004\u010d\u0092\ufe76\u00b7\u010d\u00b7\u0000\u0004\ufe12\u00b7\ufdc5\u00b7\ufe12\u00b7\u0000\u0004\ufd61\u00b7\ufd31\u00b7\ufd61\u00b7\u0000\u0004\ufccd\u00b7\ufcb9\u00b7\ufccd\u00b7\u0000\u0004\ufc55\u00b8\ufc29\u00ea\ufc55\u00ea\u0000\u0004\ufc55\u00b9\ufc6c\u0135\ufc55\u0135\u0000\u0004\ufbe4\u0009\ufb34\u0009\ufbe4\u0009\u0000\u0006\ufc29\u0179\ufbe4\u0179\u0000\u0004\ufc2c\u0178\ufbca\u0178\ufc2c\u0178\u0002\u0004\ufc06\u01a7\ufc06\u0119\u0003\ufacc\u0009\ufb35\u0009\u0001\u0002\u0002\ufad1\u0008\uf75b\u0008\u0003\uf690\uffcf\uf61a\uffcf\u0001\u0002\u0002\uf688\uffce\uf995\uffce\u0003\uf9b6\uff84\ufa3b\uff84\u0001\u0002\u0002\uf9d2\uff85\uf767\uff85\u0003\uf4cb\uff3f\uf48c\uff3f\u0001\u0002\u0002\uf4c8\uff3f\uf85a\uff3f\u0004\uf2de\uff40\uf48c\uff40\uf2de\uff40\u0000\u0002\uf2f1\uff40\u0000",
		"\u0004\uffb6\u0096\u0230\u0096\uffb6\u0096\u0000\u0004\u0235\uffdf\u011c\uffe9\u0235\uffe9\u0000\u0004\uff3d\uffdf\ufffa\uffe7\uff3d\uffe7\u0000\u0003\u0132\u0048\uffe4\u0051\u0004\u0000\u0003\u0019\u0015\u001b\u0036\u0011\u0000\u0003\u0039\n\u0003\u000b\u0008\u0000\u0003\u0044\u000f\u0041\u0033\u0009\u0000\u0003\u0047\u0027\u0050\u0035\u0007\u0000\u0003\u005b\u000e\u005e\u0034\u0008\u0000\u0003\u005f\u000b\u0071\u000c\u0007\u0000\u0003\u0060\u0034\u007a\u0037\u0007\u0000\u0003\u0061\u0021\u006a\"\u0005\u0000\u0003\u009a\u0009\u009d\u0038\u0009\u0000\u0003\u009d\u0008\u00b8\u0009\u0007\u0000\u0003\u00a1\u0038\u00b7\u003a\u0009\u0000\u0003\u00a0\u0023\u00a8\u0024\u0008\u0000\u0003\u00c5\u0016\u00c3\u0033\u000b\u0000\u0003\u00c9\u0015\u00d4\u0035\u0007\u0000\u0003\u00dd\u0015\u00d6\u0033\u0005\u0000\u0003\u00eb\u0006\u00ea\u0037\u0005\u0000\u0003\u00ee\u0007\u0107\u001e\u0006\u0000\u0003\u0106\u0020\u00eb\u0037\u0005\u0000\u0003\u01c0\uff7c\uffad\uff7d\u00da\u0000\u0003\u0112\u0035\u0112\u0038\u0007\u0000\u0003\u010e\u0000\u0111\"\u000c\u0000"	
	};
	
	private int calc = 2;
	private int direction = 1; //TODO maybe dont need this because the direction changes whenever you press A and D, when you let go it resets it to right. So.....
	//private double bodyAngle;
	private static final int posX = 400;
	private static final int posY = 300;
	//private double stillCalc;
	private double armAngle;
	private double angle;
	//private byte insideObj;
	
	private double lowestY = 300;
	private double moveOverTimeY;
	
	private double[][] allObjects = new double[400][19];
	private Point[][] animations = new Point[400][];
	private int numObjects = 0;
	private double[][][] clouds = new double[100][999][5];
	
	private boolean[] keys = new boolean[400];
	
	public void start() {
		new Thread(this).start();
	}

	public void run() {
		double moveX = 0, moveY = 0;
		double armMoveY = 0;
		int time = 10;
		boolean stuck = false;
		boolean onGround = false;
		int levelNum = 0;
		int diedCount = 0;
		boolean died = false;
		int diedTimer = 0;
		
		BufferedImage bkg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt) bkg.getRaster().getDataBuffer()).getData();
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int is = (int) (y * (150.0 / height));
				pixels[y * (width) + x] = is << 16 | is << 8 | is;
			}
		}
		
		BufferedImage level = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		BufferedImage screen = new BufferedImage(width * SCALE, height * SCALE, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D lg = (Graphics2D) level.getGraphics();
		Graphics2D g = (Graphics2D) screen.getGraphics();
		Graphics appletGraphics = getGraphics();
		
		loadLevel(levels[levelNum]);
		
		// Game loop.
		while (true) {
			long start = System.nanoTime();
			
			// Update here
			moveY += 0.2;
			if (moveY > 7) {
				moveY = 7;
			}
			
			if(keys['a'] || keys['A']/* && insideObj != -1*/) {
				//insideObj = 0;
				if (moveX>-2) {
					moveX += -0.5;
				}/* else {
					moveX = -2;
				}*/
			} else if(keys['d'] || keys['D']/* && insideObj != 1*/) {
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
				died = true;
			}
			
			for(int k = 0; k < numObjects; k++) {
				double[] obj = allObjects[k];
				Point[] animation = animations[k];
				
				if(obj[TYPE] == TYPE_EXITPOINT) {
					double dx = obj[X1] - posX / SCALE;
					double dy = obj[Y1] - posY / SCALE;
					if(dx * dx + dy * dy < 600 && diedTimer == 0) {
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
						
						obj[X3] -= useX;
						obj[Y3] -= useY;
						
						int length = (int) Math.sqrt(Math.pow(curX - prevX, 2) + Math.pow(curY - prevY, 2));
						obj[MOVEX] = (double)(curX - prevX) / length * obj[ANIM_SPEED];
						obj[MOVEY] = (double)(curY - prevY) / length * obj[ANIM_SPEED];
					}
					
					obj[X1] += obj[MOVEX];
					obj[Y1] += obj[MOVEY];
					
					obj[X2] += obj[MOVEX];
					obj[Y2] += obj[MOVEY];
					
					obj[X3] += obj[MOVEX];
					obj[Y3] += obj[MOVEY];
				}
				
				for (int i = 1; i <= clouds[0][0][0]; i++) {
					cloudPosX[i] += clouds[i][900][0];
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
					double thicknessUse = obj[THICKNESS]/2.0;
					
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
				
				double useA = Math.min((1 - percAcross) * Math.abs(width), percAcross * Math.abs(width));
				
				boolean collide = false;
				stuck = false;

				
				if (obj[TYPE] == TYPE_LINE || obj[TYPE] == TYPE_BUTTON) {
					if (percAcross <= 1 && percAcross >=0  && inside >= -50 && inside <= -40 && percAcross > 10.0/Math.abs(width) && percAcross < 1 - (10.0/Math.abs(width))) {
						moveY = 0;
						moveObj(0, -(-50 - inside), 0);
						
						// simplified..
						// seems to work exactly the same as was previously
						collide = stuck = onGround;
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
			
			drawClouds(lg, moveX, moveY);

			lg.setColor(Color.black);
			
			for(int k = 0; k < numObjects; k++) {
				double[] obj = allObjects[k];
				
				switch((int) obj[TYPE]) {
					case TYPE_LINE:
					case TYPE_BUTTON:
						lg.setStroke(new BasicStroke((float) obj[THICKNESS]));
						lg.drawLine((int) obj[X1], (int) obj[Y1], (int) obj[X2], (int) obj[Y2]);
						break;
					case TYPE_TRIANGLE:
						int[] xp = { (int) obj[X1], (int) obj[X2], (int) obj[X3] };
						int[] yp = { (int) obj[Y1], (int) obj[Y2], (int) obj[Y3] };
						
						lg.fillPolygon(xp, yp, 3);
						
						lg.fillRect((int) Math.min(obj[X1], obj[X2]), (int) obj[Y3], (int) Math.abs(obj[X2] - obj[X1]), 2000);
						break;
				/*	case TYPE_BUTTON:
						double angle = Math.atan2(obj[Y2] - obj[Y1], obj[X2] - obj[X1]);
						int distance = (int) Math.sqrt((obj[X2] - obj[X1]) * (obj[X2] - obj[X1]) + (obj[Y2] - obj[Y1]) * (obj[Y2] - obj[Y1]));
						
						int x2 = (int) (obj[X1] + distance);
						
						int[] xp1 = {(int) obj[X1], (int) obj[X1] + 8, (int) obj[X1] + 8};
						int[] yp1 = {(int) obj[Y1], (int) obj[Y1], (int) obj[Y1] + 4};
						
						int[] xp2 = {(int) x2, (int) x2 - 8, (int) x2 - 8};
						int[] yp2 = {(int) obj[Y1], (int) obj[Y1], (int) obj[Y1] + 4};
						
						lg.rotate(angle, (int) obj[X1], (int) obj[Y1]);
						lg.fillPolygon(xp1, yp1, 3);
						lg.fillRect((int) obj[X1] + 8, (int) obj[Y1], ((int) x2 - (int) obj[X1]) - 16, (int) obj[THICKNESS]/2);
						lg.fillPolygon(xp2, yp2, 3);
						lg.rotate(-angle, (int) obj[X1], (int) obj[Y1]);
						break;*/
					case TYPE_EXITPOINT:
						lg.setColor(new Color(100, 0, 0));
						lg.fillRect((int) obj[X1] - 8, (int) obj[Y1] - 50, 16, 50);
						lg.setColor(Color.black);
						break;
				}
			}
			
			g.drawImage(level, 0, 0, width * SCALE, height * SCALE, this);
			if (diedTimer>0) {
				diedTimer++;
				int useAlpha = diedTimer;
				if (useAlpha > 255) {
					useAlpha = 255;
				}
				g.setColor(new Color(0, 0, 0, useAlpha));
				g.fillRect(0, 0, width * SCALE, height * SCALE);
				
				if (diedTimer >= 255) {
					loadLevel(levels[levelNum]);
					moveY = -3;
					moveX = 0;
					moveY = 0;
					diedTimer = 0;
					if (died)
						diedCount++;
					died = false;
				}
			}
			
			if (!onGround) {
				armMoveY = moveY;
			} else {
				armMoveY -= 5;
			}
			
			//moveX = Math.round(moveX * 4) / 4.0;
			moveX = Math.round(moveX * 1000) / 1000.0;
			
			if (!onGround || (int) moveX == 0) {
				drawGuy(g, STAND, armMoveY, time, moveX <= -0.25);
			} else if ((int)moveX != 0) {
				drawGuy(g, WALK, 0, time, moveX <= -0.25);
			}
			
			time++;
			
			// Removing this method call ADDED bytes
			g.setColor(Color.gray);
			g.drawString("Deaths: " + diedCount, 730, 15);
			
			appletGraphics.drawImage(screen, 0, 0, this);
			
			try {
				Thread.sleep(Math.max(0, 20 - ((System.nanoTime() - start) / 1000000L)));
			} catch(Exception e) { }
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
	
	public void loadLevel(String level) {
		allObjects = new double[1000][19];
		animations = new Point[1000][];
		numObjects = 0;
		moveOverTimeY = 0;
		clouds = new double[100][999][5];
		
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
			int type = level.charAt(index++);
			
			// store base information that's common to every object in 
			// the first 2 fields
			allObjects[numObjects][TYPE] = type;
			
			allObjects[numObjects][X1] = (short) level.charAt(index++); // x
			allObjects[numObjects][Y1] = (short) level.charAt(index++); // y
			if (allObjects[numObjects][Y1]>lowestY)
				lowestY = allObjects[numObjects][Y1];
			
			if(type != TYPE_EXITPOINT) {
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
		double useX = 0;
		double testX = 0;
		
		for(int k = 0; k < numObjects; k++) {
			double[] obj = allObjects[k];
			
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

			for(int j = 0; j < animations[k].length; j++) {
				animations[k][j].x -= useX;
				animations[k][j].y -= useY;
			}
			
			obj[X1] -= useX; obj[Y1] -= useY;
			obj[X2] -= useX; obj[Y2] -= useY;
			
			if(obj[TYPE] == TYPE_TRIANGLE) {
				obj[X3] -= useX;
				obj[Y3] -= useY;
			}
			
		}
		/*for (int i = 1; i < clouds[0][0][0] + 1; i++) {
			for (int a = 0; a < clouds[i][0][0]; a++) {
				clouds[i][a][1] -= x * 1.2;
				if (clouds[i][a][1] < -50) {
					clouds[i][a][1] += 500;
				}
				if (clouds[i][a][1] > 450) {
					clouds[i][a][1] -= 500;
				}
			}
		}*/
		
		for (int i = 0; i < clouds[0][0][0]; i++) {
			cloudPosX[i] -= x * 0.5;
			if (cloudPosX[i] < -150) {
				cloudPosX[i] += 600;
			}
			if (cloudPosX[i] > 450) {
				cloudPosX[i] -= 600;
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
		
		if (calc%20 == 0 && action == WALK) {
			direction = -direction;
		}
		if (action == WALK) 
			calc+=direction*2;
		
		//if (action == WALK || action == STAND) {
			//stillCalc = calc;
			//armAngle = calc;
			
			//if (param>bodyAngle) {
			//	bodyAngle++;
			//} else if (param < bodyAngle) {
			//	bodyAngle--;
			//}
			
			if (action == STAND) {
				calc = 12;
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
			} else {
				armAngle = calc;
			}
			
			//Bounce effect
			g.rotate(Math.toRadians(-calc),(int)(posX - 4 + Math.sin(Math.toRadians(-calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(-calc))*16));
			g.rotate(Math.toRadians(calc),(int)(posX - 4 + Math.sin(Math.toRadians(calc))*-16), (int)(posY - 35 + Math.cos(Math.toRadians(calc))*16));
			
			//back arm
			//if (action==WALK) {
				g.rotate(Math.toRadians(armAngle*1.3), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
				g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32), 6, 30, 6, 10);
				g.rotate(Math.toRadians(-armAngle*1.3), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
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
				g.rotate(Math.toRadians(-armAngle*1.3), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
				g.fillRoundRect((int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35) - 3, (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32), 6, 30, 6, 10);
				g.rotate(Math.toRadians(armAngle*1.3), (int)(posX - Math.cos(Math.toRadians(/*bodyAngle+*/90))*35), (int)(posY - 35 - Math.sin(Math.toRadians(/*bodyAngle+*/90))*32));
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
		//}
		
		g.dispose();
	}
	
	private BufferedImage[] cloudImages = new BufferedImage[10];
	private double[] cloudPosX = new double[10];
	private double[] cloudPosY = new double[10];
	
	public void drawClouds(Graphics _g, double x, double y) {
		Graphics2D g = (Graphics2D) _g.create();
		if (clouds[0][0][0] == 0) {
			int amtClouds = 5 + (int) (Math.random()*2);
			clouds[0][0][0] = amtClouds;
			
			for (int i = 1; i <= amtClouds; i++) {
				int numCircles = (int) (Math.random() * 150) + 50;
				clouds[i][0][0] = numCircles;
				double dir = (Math.floor(Math.random() * 2) - 1) * 2 + 1;
				clouds[i][900][0] = (dir/100) + (Math.random() * 0.01 * dir);
				
				double variableCenterCluster = 0.005; //goes from 0 to 1, the higher it goes more cluster will appear close to the center
				
				int ranAcrossX = (int) (Math.random()*400) - 100;
				int ranAcrossY = (int) (Math.random()*100) - 100;
				
				cloudPosX[i] = ranAcrossX;
				cloudPosY[i] = ranAcrossY;
				
				cloudImages[i - 1] = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
				
				for (int a = 0; a < numCircles; a++) {
					double randomAngle = Math.toRadians(Math.random()*360);
					double ranDistance = Math.random() * (Math.random() * variableCenterCluster + (1 - variableCenterCluster));
					int ranCloudSize = (int) (25 * ranDistance);
					int yDist = (int) (Math.sin(randomAngle) * ranCloudSize + 100);
					int xDist = (int) (Math.cos(randomAngle) * ranCloudSize + 100);
					int ranSize = (int) (Math.random()*20 + 5); //It's so random it hurts!
					clouds[i][a][1] = xDist;
					clouds[i][a][2] = yDist;
					clouds[i][a][3] = ranSize;
					
					Graphics2D cg = (Graphics2D) cloudImages[i - 1].getGraphics();
					cg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					cg.setColor(new Color(0, 0, 0));
					cg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
					cg.fillOval((int) clouds[i][a][1], (int) clouds[i][a][2], (int) clouds[i][a][3], (int) clouds[i][a][3]);
				}
			}
		}
		
		for (int i = 1; i <= clouds[0][0][0]; i++) {
			g.drawImage(cloudImages[i], (int) cloudPosX[i], (int) cloudPosY[i], null);
		}
	}

	public boolean handleEvent(Event e) {
		if(e.id == Event.KEY_PRESS)
			keys[e.key] = true;
		else if(e.id == Event.KEY_RELEASE)
			keys[e.key] = false;
		return false;
	}
}