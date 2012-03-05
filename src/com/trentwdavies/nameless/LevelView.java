package com.trentwdavies.nameless;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

/**
 * This is kindof a mashup between a grid object and a level design. Oh, and
 * it has grid calculations too, for some reason.
 */
public class LevelView extends JComponent {
	/** How many pixels 'snap to grid' snaps to */
	public static final int SNAP_AMOUNT = 4;
	
	private static final Stroke BOLD_STROKE = new BasicStroke(4.0f);
	
	private static final Color X_COLOR = new Color(0, 64, 0);
	private static final Color Y_COLOR = new Color(64, 0, 0);
	
	private int zoom = 2;
	
	private int scrollX;
	private int scrollY;

	private List<Item> items;
	
	private Item newItem;
	private Item animatingItem;
	private Cursor cursor = new Cursor(this);
	
	public LevelView() {
		items = new ArrayList<Item>();
		
		setDoubleBuffered(true);
		setPreferredSize(new Dimension(800, 616));
		
		// set up event listeners
		MouseHandler mh = new MouseHandler();
		addMouseListener(mh);
		addMouseMotionListener(mh);
		addMouseWheelListener(mh);
		
		addKeyListener(new KeyHandler());
	}
	
	public void paintComponent(Graphics _g) {
		Graphics2D g = (Graphics2D) _g;
		
		g.setColor(new Color(210, 210, 210));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setStroke(BOLD_STROKE);
		
		g.setColor(Y_COLOR);
		g.drawLine(0, scrollY, getWidth(), scrollY);
		
		g.setColor(X_COLOR);
		g.drawLine(scrollX, 0, scrollX, getHeight());
		
		int imgW = getWidth() / zoom;
		int imgH = getHeight() / zoom;

		BufferedImage img = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D imgGraphics = img.createGraphics();
		
		imgGraphics.translate(scrollX / zoom, scrollY / zoom);
		
		imgGraphics.setColor(new Color(0, 0, 0, 100));
		imgGraphics.fillRect(187, 100, 25, 50);

		for(Item item : items)
			item.render(imgGraphics);
		
		if(newItem != null)
			newItem.render(imgGraphics);
		
		// holy crap i can't believe this works
		g.drawImage(img, scrollX % zoom, scrollY % zoom, getWidth()
				- (getWidth() % zoom), getHeight() - (getHeight() % zoom), this);
		
		g.setColor(Color.black);
		g.drawString("Zoom: " + zoom + "x", 5, 15);
	}
	
	public void scroll(int dx, int dy) {
		scrollX += dx;
		scrollY += dy;
		repaint();
	}

	public void scroll(Point pt) {
		scroll(pt.x, pt.y);
	}
	
	public void zoomIn() {
		if(zoom == 16)
			return;
		
		zoom++;
		repaint();
	}
	
	public void zoomOut() {
		if(zoom == 1)
			return;
		
		zoom--;
		repaint();
	}
	
	public Point screenToWorld(int x, int y) {
		int gx = (int) Math.round((x - scrollX) / (double) zoom);
		int gy = (int) Math.round((y - scrollY) / (double) zoom);
		return new Point(gx, gy);
	}
	
	public Point screenToWorld(Point pt) {
		return screenToWorld(pt.x, pt.y);
	}
	
	public int screenToWorldX(int x) {
		return (int) Math.round((x - scrollX) / (double) zoom);
	}
	
	public int screenToWorldY(int y) {
		return (int) Math.round((y - scrollY) / (double) zoom);
	}
	
	public Point worldToScreen(int x, int y) {
		int sx = x * zoom + scrollX;
		int sy = y * zoom + scrollY;
		return new Point(sx, sy);
	}
	
	public Point worldToScreen(Point pt) {
		return worldToScreen(pt.x, pt.y);
	}
	
	// The following four methods deal with direction/magnitude only, not position.
	// Good for scaling things like width and height.
	
	public Point worldToScreenVector(int x, int y) {
		return new Point(x * zoom, y * zoom);
	}
	
	public Point worldToScreenVector(Point pt) {
		return worldToScreenVector(pt.x, pt.y);
	}
	
	public Point screenToWorldVector(int x, int y) {
		int gx = (int) (x / (double) zoom);
		int gy = (int) (y / (double) zoom);
		return new Point(gx, gy);
	}
	
	public Point screenToWorldVector(Point pt) {
		return screenToWorldVector(pt.x, pt.y);
	}
	
	// snap-to-grid methods
	
	public Point snap(int x, int y) {
		int sx = x / SNAP_AMOUNT * SNAP_AMOUNT;
		int sy = y / SNAP_AMOUNT * SNAP_AMOUNT;
		return new Point(sx, sy);
	}
	
	public int snap(int x) {
		return x / SNAP_AMOUNT * SNAP_AMOUNT;
	}
	
	public Point snap(Point pt) {
		return snap(pt.x, pt.y);
	}
	
	public String encode() {
		StringBuilder builder = new StringBuilder();
		
		for(Item item : items) {
			builder.append(item.serialize());
			if(item.animation != null)
				builder.append(item.animation.serialize());
			else builder.append((char) 0);
		}
		
		return builder.toString();
	}
	
	public String encodeJava() {
		String result = "";
		String actual = encode();
		for(char ch : actual.toCharArray()) {
			if(ch == 13)
				result += "\\r";
			else if(ch == 10)
				result += "\\n";
			else if(ch == 92)
				result += "\\\\";
			else if(ch == 34)
				result += "\\\"";
			else result += String.format("\\u%04x", (int) ch);
		}
		return result;
	}
	
	// this whole decode method is pretty bad but whatever
	public void decode(String codestr) {
		items.clear();
		
		// convert unicode, \r, \n, \" to the real character values
		
		String str = "";
		for(int k = 0; k < codestr.length(); k++) {
			char ch = codestr.charAt(k);
			if(ch == '\\') {
				if(codestr.charAt(k + 1) == '\\') {
					str += "\\";
					k++;
				} else continue;
			}
			
			if(ch == 'u') {
				str += (char) Integer.parseInt(codestr.substring(k + 1, k + 5), 16);
				k += 3;
			}
			
			if(ch == 'r') {
				str += "\r";
			}
			
			if(ch == 'n') {
				str += "\n";
			}
			
			if(ch == '"') {
				str += "\"";
			}
		}
		
		int index = 0;
		while(index < str.length()) {
			// type is in lower 8 bits, flags are in upper 8 bits
			char ch = str.charAt(index++);
			int type = ch & 0xff;
			
			switch(type) {
				case Item.TYPE_EXITPOINT:
					ExitPoint pt = new ExitPoint();
					pt.x1 = (short) str.charAt(index++);
					pt.y1 = (short) str.charAt(index++);
					// Exit points don't have animation but w/e
					index = readAnimation(pt, str, index);
					items.add(pt);
					break;
				case Item.TYPE_LINE:
					Line line = new Line();
					line.x1 = (short) str.charAt(index++);
					line.y1 = (short) str.charAt(index++);
					line.x2 = (short) str.charAt(index++);
					line.y2 = (short) str.charAt(index++);
					line.thickness = (short) str.charAt(index++);
					index = readAnimation(line, str, index);
					items.add(line);
					break;
				case Item.TYPE_TRIANGLE:
					Triangle tri = new Triangle();
					tri.x1 = (short) str.charAt(index++);
					tri.y1 = (short) str.charAt(index++);
					tri.x2 = (short) str.charAt(index++);
					tri.y2 = (short) str.charAt(index++);
					str.charAt(index++); str.charAt(index++); // x3 and y3 unneeded
					index = readAnimation(tri, str, index);
					items.add(tri);
					break;
				case Item.TYPE_BUTTON:
					Button b = new Button();
					b.x1 = (short) str.charAt(index++);
					b.y1 = (short) str.charAt(index++);
					b.x2 = (short) str.charAt(index++);
					b.y2 = (short) str.charAt(index++);
					index = readAnimation(b, str, index);
					items.add(b);
					break;
			}
		}
		
		repaint();
	}
	
	private int readAnimation(Item item, String str, int index) {
		int nPoints = str.charAt(index++);
		
		if(nPoints == 0)
			return index;
		
		item.animation = new Animation();
		item.animation.speed = str.charAt(index++);
		System.out.println("speed="+ item.animation.speed);
			
		int x = 0, y = 0;
		for(int k = 0; k < nPoints; k++) {
			x = (short) str.charAt(index++);
			y = (short) str.charAt(index++);
			
			item.animation.addPoint(x, y);
		}
		
		item.animation.curX = x;
		item.animation.curY = y;
		
		return index;
	}

	public void placeItemStart(Item item) {
		newItem = item;
		newItem.placeItemStart(cursor);
		repaint();
	}

	public void placeItemDrag() {
		if(newItem != null) {
			System.out.println(newItem instanceof Line);
			newItem.placeItemDrag(cursor);
		}
		repaint();
	}
	
	public void cloneItemStart() {
		if(newItem != null)
			newItem.cloneItemStart(cursor);
		if(animatingItem != null && animatingItem.animation != null) {
			animatingItem.animation.curX = cursor.getGridX();
			animatingItem.animation.curY = cursor.getGridY();
		}
		repaint();
	}
	
	public void cloneItemDrag() {
		if(newItem != null)
			newItem.cloneItemDrag(cursor);
		
		if(animatingItem != null && animatingItem.animation != null) {
			animatingItem.animation.curX = cursor.getGridX();
			animatingItem.animation.curY = cursor.getGridY();
		}
		repaint();
	}
	
	public void placeItemEnd() {
		if(newItem == null)
			return;
		
		if(!LevelEditor.getInstance().getSelectedMode().equals("animate")) {
			items.add(newItem);
		} else {
			newItem.animation.addPoint(cursor.getGridX(), cursor.getGridY());
			try {
				newItem.animation.speed = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter animation speed in pixels per tick"));
			} catch(Exception e) {
				newItem.animation.speed = 1;
			}
		}
		
		animatingItem = null;
		newItem = null;
		
		repaint();
	}
	
	public void removeItemUnderCursor() {
		for(int k = items.size() - 1; k >= 0; k--) 
			if(items.get(k).contains(cursor.getGridX(), cursor.getGridY()))
				items.remove(k);
	}
	
	public Item getItemUnderCursor() {
		for(int k = items.size() - 1; k >= 0; k--) 
			if(items.get(k).contains(cursor.getGridX(), cursor.getGridY()))
				return items.get(k);
		return null;
	}
	
	class KeyHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			
		}

		public void keyReleased(KeyEvent e) {
			
		}
	}

	class MouseHandler extends MouseInputAdapter {
		public void mousePressed(MouseEvent e) {
			cursor.clicked(e.getX(), e.getY(), e.getButton());
			

			String mode = LevelEditor.getInstance().getSelectedMode();
			
			if(mode.equals("addremove")) {
				if(cursor.isButtonDown(MouseEvent.BUTTON1)) {
					placeItemStart(ItemFactory.newItem(LevelEditor.getInstance().getSelectedTool()));
				} else if(cursor.isButtonDown(MouseEvent.BUTTON3)) {
					removeItemUnderCursor();
					repaint();
				}
			} else if(mode.equals("clone") || mode.equals("animate")) {
				if(cursor.isButtonDown(MouseEvent.BUTTON1)) {
					Item it = getItemUnderCursor();
					
					if(it == null)
						return;
					
					if(mode.equals("animate") && newItem == null) {
						it.animation = new Animation();
						it.animation.addPoint(cursor.getGridX(), cursor.getGridY());
					}
					
					animatingItem = it;
					newItem = it.clone();
					cloneItemStart();
				}
			}
		}

		public void mouseMoved(MouseEvent e) {
			cursor.updatePosition(e.getX(), e.getY());
			
			LevelEditor.getInstance().setStatusText(cursor.getGridPos().toString());
		}
		
		public void mouseReleased(MouseEvent e) {
			cursor.released(e.getX(), e.getY(), e.getButton());
			if (e.getButton() == MouseEvent.BUTTON1) {
				placeItemEnd();
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				if(LevelEditor.getInstance().getSelectedMode().equals("animate")) {
					if(animatingItem != null) {
						animatingItem.animation.addPoint(cursor.getGridX(), cursor.getGridY());
						repaint();
					}
				}
			}
		}

		public void mouseDragged(MouseEvent e) {
			cursor.updatePosition(e.getX(), e.getY());
			LevelEditor.getInstance().setDragText(cursor.getGridPos().toString());
			
			String mode = LevelEditor.getInstance().getSelectedMode();
			
			if(cursor.isButtonDown(MouseEvent.BUTTON1)) {
				if(mode.equals("addremove"))
					placeItemDrag();
				else if(mode.equals("clone") || mode.equals("animate")) {
					cloneItemDrag();
				}
			} else if(cursor.isButtonDown(MouseEvent.BUTTON3))
				scroll(cursor.getDeltaScreenPos());
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			if(newItem != null) {
				// currently placing an item
				if(e.getWheelRotation() < 0)
					newItem.setThickness(newItem.getThickness() + 1);
				else newItem.setThickness(newItem.getThickness() - 1);
				
				repaint();
			} else {
				if(e.getWheelRotation() < 0)
					zoomIn();
				else zoomOut();
			}
		}
	}
}
