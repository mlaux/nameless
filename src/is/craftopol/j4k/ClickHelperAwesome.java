package is.craftopol.j4k;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ClickHelperAwesome extends JComponent implements Runnable {
	private int lastX;
	private int lastY;
	private int realLastX;
	private int realLastY;
	
	private int finalrunning;
	private int running;
	
	private int initial = 0;
	
	private double frequency;
	private double depth;
	private int finalheight;
	private int height;
	
	private int[] pickSpotX = new int[10];
	private int[] pickSpotY = new int[10];
	
	private BufferedImage perlin;
	
	public ClickHelperAwesome() {
		addMouseListener(new Mouse());
		setPreferredSize(new Dimension(800, 600));
		setDoubleBuffered(true);
		

	}
	
	public void run() {
		while(true) {
			repaint();
			try { Thread.sleep(5); } catch(Exception e) { }
		}
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.black);
		
		if (running>0) {
			if (height < finalheight && running > finalheight) {
				height+=2;
				initial+=2;
			}
			if (running <= finalheight) {
				height-=2;
				lastY-=2;
				initial--;
			}
			initial++;
			for (int i = 0; i < height; i++) {
				int currentSpotX = (int) Math.round(Math.sin(Math.toRadians((initial - i) * frequency)) * (depth * ((double)(height - i)/10)) + lastX);
				int currentSpotY = (height - i) + lastY - height;
				if (i > height / 2) {
					double col = (INoise.noise(currentSpotX * 0.025, currentSpotY * 0.025, 0) + 2d) / 4d;
					int val = (int) (col * 255) & 0xff;
					g.setColor(new Color(0,0,0,val));
				} else {
					g.setColor(new Color(0,0,0,255));
				}
				g.drawImage(perlin,currentSpotX, currentSpotY, this);
				//g.fillRect(currentSpotX, currentSpotY, 1, 1);
				//Math.sin(currentSpotX);
			}
			
			if (running == finalheight + finalheight/2 + 30) {
				for (int i = 0; i<pickSpotX.length; i++) {
					pickSpotY[i] = (int) (realLastY - Math.random()*Math.random()*finalheight);
					pickSpotX[i] = (int) ((Math.pow((finalheight - (realLastY - pickSpotY[i]))/10*Math.random(),2)))*(((int) (Math.random() * 2)) * 2 - 1) + realLastX;
				}
			}
			
			
			
			for (int i = 0; i<pickSpotX.length; i++) {
				g.setColor(Color.BLACK);
				int addX = (int)(Math.cos(Math.atan2(pickSpotY[i] - realLastY, pickSpotX[i] - realLastX)) * (Math.pow((finalrunning), 2) - Math.pow((running), 2) + 1)/400);
				int addY = (int)(Math.sin(Math.atan2(pickSpotY[i] - realLastY, pickSpotX[i] - realLastX)) * (Math.pow((finalrunning), 2) - Math.pow((running), 2) + 1)/400);
			//	System.out.println(Math.atan2(pickSpotY[i], pickSpotX[i] - lastX));
				g.drawImage(perlin,pickSpotX[i]+addX, pickSpotY[i]+addY, this);
				//g.fillRect(pickSpotX[i]+addX, pickSpotY[i]+addY, 5, 5);
			}
			
			running--;
		}
	}
	
	private void setupBlobs() {
		frequency = Math.random() * 7.5 + 2.5;
		depth = Math.random() * 5 + 10;
		finalheight = (int) (Math.random() * 50 + 75);
		height = 0;
		
		finalrunning = finalheight + finalheight/2 + 50;
		running = finalrunning;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		ClickHelperAwesome ch = new ClickHelperAwesome();
		frame.add(ch);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new Thread(ch).start();
	}
	
	class Mouse extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			lastX = e.getX();
			lastY = e.getY();
			realLastX = lastX;
			realLastY = lastY;
			
			perlin = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			int[] pix = ((DataBufferInt) perlin.getRaster().getDataBuffer()).getData();
			
			Graphics2D g2 = perlin.createGraphics();
			g2.setColor(Color.white);
			g2.fillRect(0, 0, 16, 16);
			g2.setPaint(new RadialGradientPaint(new Point(8, 8), 8, new float[] { 0f, 1f}, new Color[] { Color.black, Color.white}));
			g2.fillOval(0, 0, 16, 16);
			g2.dispose();
			
			for(int y = 0; y < 16; y++)
				for(int x = 0; x < 16; x++) {
					double d = (INoise.niceNoise(x, y, 0.2) + 2d) / 4d;
					int val = (int) (d * 255) & 0xff;
					
					int col = pix[y * 16 + x];
					col &= 0x00ffffff;
					col |= (val << 24);
					pix[y * 16 + x] = col;
				}
			
			setupBlobs();
			
			System.out.println("Mouse clicked at (" + lastX + ", " + lastY + ")");
		}
	}
	
	// All code following this line is from http://mrl.nyu.edu/~perlin/noise/
	static class INoise {
		public static double niceNoise(double x, double y, double z) {
			return noise(x, y, z) + 0.5 * noise(x * 2, y * 2, z * 2) + 0.25
					* noise(x * 4, y * 4, z * 4) + 0.125
					* noise(x * 8, y * 8, z * 8) + 0.0625
					* noise(x * 16, y * 16, z * 16);
		}
		
		static public double noise(double x, double y, double z) {
			int X = (int) Math.floor(x) & 255, // FIND UNIT CUBE THAT
			Y = (int) Math.floor(y) & 255, // CONTAINS POINT.
			Z = (int) Math.floor(z) & 255;
			x -= Math.floor(x); // FIND RELATIVE X,Y,Z
			y -= Math.floor(y); // OF POINT IN CUBE.
			z -= Math.floor(z);
			double u = fade(x), // COMPUTE FADE CURVES
			v = fade(y), // FOR EACH OF X,Y,Z.
			w = fade(z);
			int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, // HASH
																// COORDINATES
																// OF
			B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z; // THE 8 CUBE
																// CORNERS,

			return lerp(w,
					lerp(v, lerp(u, grad(p[AA], x, y, z), // AND ADD
							grad(p[BA], x - 1, y, z)), // BLENDED
							lerp(u, grad(p[AB], x, y - 1, z), // RESULTS
									grad(p[BB], x - 1, y - 1, z))),// FROM 8
					lerp(v,
							lerp(u, grad(p[AA + 1], x, y, z - 1), // CORNERS
									grad(p[BA + 1], x - 1, y, z - 1)), // OF
																		// CUBE
							lerp(u, grad(p[AB + 1], x, y - 1, z - 1),
									grad(p[BB + 1], x - 1, y - 1, z - 1))));
		}

		static double fade(double t) {
			return t * t * t * (t * (t * 6 - 15) + 10);
		}

		static double lerp(double t, double a, double b) {
			return a + t * (b - a);
		}

		static double grad(int hash, double x, double y, double z) {
			int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
			double u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
			v = h < 4 ? y : h == 12 || h == 14 ? x : z;
			return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
		}

		static final int p[] = new int[512], permutation[] = { 151, 160, 137,
				91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140,
				36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148,
				247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35,
				11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136,
				171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146,
				158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92,
				41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1,
				216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
				135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3,
				64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126,
				255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189,
				28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70,
				221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98,
				108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97,
				228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241,
				81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181,
				199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4,
				150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243,
				141, 128, 195, 78, 66, 215, 61, 156, 180 };
		static {
			for (int i = 0; i < 256; i++)
				p[256 + i] = p[i] = permutation[i];
		}
	}
}
