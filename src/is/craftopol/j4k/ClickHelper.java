package is.craftopol.j4k;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class ClickHelper extends Canvas {
	private int lastX;
	private int lastY;
	private int running;
	private double currentX[] = new double[200];
	private double currentY[] = new double[200];
	private double moveX[] = new double[200];
	private double moveY[] = new double[200];
	private int scale = 4;
	
	public ClickHelper() {
		addMouseListener(new Mouse());
		setPreferredSize(new Dimension(800, 600));
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if (running>0) {
			if (running==4000) {	
				for(int i = 0; i < currentX.length; i++) {
					int pos = ((int)(Math.random()*2))*2-1;
					currentX[i]=pos*25*scale + lastX;
					currentY[i]=lastY;
					
					moveX[i]=Math.random()*0.5*pos*(scale/2);
					moveY[i]=Math.random()*0.5*(scale/2);
					
//					AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC);
					g.fillOval((int)(currentX[i] - 3*scale), (int)(currentY[i] - 3*scale), 6*scale, 6*scale);
					Color color = new Color(0,0,0,0);
					((Graphics2D) g).setPaint(color);
//					((Graphics2D) g).setComposite(alpha);		
				}
			}
			for(int i = 0; i < currentX.length; i++) {
				//when implementing replace currentX.length with the actual count (less data?)
				moveX[i]-=moveX[i]/200;
				moveY[i]-=moveY[i]/200 + 0.005;
				
				currentX[i]+=moveX[i];
				currentY[i]-=moveY[i];
				
				if (currentY[i]>=597) {
					currentY[i]-=(currentY[i]-597);
					moveY[i]=0;
				}
				
				g.fillOval((int)(currentX[i] - 3*scale), (int)(currentY[i] - 3*scale), 6*scale, 6*scale);
			}
			
			running--;
			
			repaint();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Click thing");
		frame.add(new ClickHelper());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	class Mouse extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			lastX = e.getX();
			lastY = e.getY();
			
			running=4000;
			
			
			repaint();
			
			System.out.println("Mouse clicked at (" + lastX + ", " + lastY + ")");
		}
	}
}
