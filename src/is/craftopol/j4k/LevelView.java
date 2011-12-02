package is.craftopol.j4k;

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
import javax.swing.event.MouseInputAdapter;

/**
 * This is kindof a mashup between a grid object and a level design. Oh, and
 * it has grid calculations too, for some reason.
 */
public class LevelView extends JComponent {
	/** How many pixels 'snap to grid' snaps to */
	public static final int SNAP_AMOUNT = 4;
	
	private static final Stroke BOLD_STROKE = new BasicStroke(4.0f);
	private static final Stroke THIN_STROKE = new BasicStroke(1.0f);
	
	private static final Color X_COLOR = new Color(0, 64, 0);
	private static final Color Y_COLOR = new Color(64, 0, 0);
	
	private static final Color REG_COLOR = new Color(24, 24, 24);
	
	private boolean showGrid = true;
	
	private int zoom = 16;
	
	private int scrollX;
	private int scrollY;

	private List<Item> items;
	
	private Item newItem;
	
	private Cursor cursor = new Cursor(this);
	
	public LevelView() {
		items = new ArrayList<Item>();
		
		// for test purposes until we can create stuff with the mouse
		items.add(new Circle(50, 50, 50, false));
		items.add(new Rect(20, 20, 10, 10, true));
		System.out.println(encodeJava());
		
		setDoubleBuffered(true);
		setPreferredSize(new Dimension(640, 480));
		
		// set up event listeners
		MouseHandler mh = new MouseHandler();
		addMouseListener(mh);
		addMouseMotionListener(mh);
		addMouseWheelListener(mh);
		
		addKeyListener(new KeyHandler());
	}
	
	public void paintComponent(Graphics _g) {
		Graphics2D g = (Graphics2D) _g;
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(showGrid)
			drawGrid(g);

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

		for(Item item : items)
			item.render(imgGraphics);
		
		if(newItem != null)
			newItem.render(imgGraphics);
		
		// holy crap i can't believe this works
		g.drawImage(img, scrollX % zoom, scrollY % zoom, getWidth()
				- (getWidth() % zoom), getHeight() - (getHeight() % zoom), this);
		
		g.setColor(Color.white);
		g.drawString("Zoom: " + zoom + "x", 5, 15);
	}
	
	private void drawGrid(Graphics2D g) {
		int startX = scrollX % zoom;
		int startY = scrollY % zoom;
		
		g.setStroke(THIN_STROKE);
		g.setColor(REG_COLOR);
		
		for(int y = startY; y < getHeight(); y += zoom) {
			g.drawLine(0, y, getWidth(), y);
			for(int x = startX; x < getWidth(); x += zoom) {
				g.drawLine(x, 0, x, getHeight());
			}
		}
	}
	
	public void scroll(int dx, int dy) {
		scrollX += dx;
		scrollY += dy;
		repaint();
	}

	public void scroll(Point pt) {
		scroll(pt.x, pt.y);
	}
	
	public void toggleShowGrid() {
		showGrid = !showGrid;
		repaint();
	}
	
	public void zoomIn() {
		if(zoom == 16)
			return;
		
		zoom++;
		if(zoom >= 4)
			showGrid = true;
		
		repaint();
	}
	
	public void zoomOut() {
		if(zoom == 1)
			return;
		
		zoom--;
		if(zoom < 4)
			showGrid = false;
		
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
		
		for(Item item : items)
			builder.append(item.serialize());
		
		return builder.toString();
	}
	
	public String encodeJava() {
		String result = "String level = \"";
		String actual = encode();
		for(char ch : actual.toCharArray())
			result += String.format("\\u%04x", (int) ch);
		result += "\";";
		
		return result;
	}
	
	public void decode(String str) {
		items.clear();
		
		int index = 0;
		while(index < str.length()) {
			// type is in lower 8 bits, flags are in upper 8 bits
			char ch = str.charAt(index++);
			int type = ch & 0xff, flags = ch & 0xff00;
			System.out.println(flags);
			
			switch(type) {
				case Item.TYPE_SPAWNPOINT:
				case Item.TYPE_EXITPOINT:
					PointItem pt = new PointItem(flags);
					pt.x = str.charAt(index++);
					pt.y = str.charAt(index++);
					items.add(pt);
					break;
				case Item.TYPE_LINE:
					Line line = new Line();
					line.x1 = str.charAt(index++);
					line.y1 = str.charAt(index++);
					line.x2 = str.charAt(index++);
					line.y2 = str.charAt(index++);
					items.add(line);
					break;
				case Item.TYPE_RECT:
					Rect rect = new Rect();
					rect.filled = (flags & Item.FLAG_FILLED) != 0;
					rect.x = str.charAt(index++);
					rect.y = str.charAt(index++);
					rect.width = str.charAt(index++);
					rect.height = str.charAt(index++);
					items.add(rect);
					break;
				case Item.TYPE_CIRCLE:
					Circle circle = new Circle();
					circle.filled = (flags & Item.FLAG_FILLED) != 0;
					circle.centerX = str.charAt(index++);
					circle.centerY = str.charAt(index++);
					circle.radius = str.charAt(index++);
					items.add(circle);
					break;
			}
		}
	}

	public void placeItemStart(Item item) {
		newItem = item;
		newItem.placeItemStart(cursor);
		repaint();
	}

	public void placeItemDrag() {
		newItem.placeItemDrag(cursor);
		repaint();
	}
	
	public void placeItemEnd() {
		if(newItem == null)
			return;
		
		items.add(newItem);
		newItem = null;
		
		repaint();
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
			
			if(cursor.isButtonDown(MouseEvent.BUTTON1))
				placeItemStart(ItemFactory.newItem(LevelEditor.getInstance().getSelectedTool()));
		}

		public void mouseMoved(MouseEvent e) {
			cursor.updatePosition(e.getX(), e.getY());
			
			LevelEditor.getInstance().setStatusText(cursor.getGridPos().toString());
		}
		
		public void mouseReleased(MouseEvent e) {
			cursor.released(e.getX(), e.getY(), e.getButton());
			placeItemEnd();
		}

		public void mouseDragged(MouseEvent e) {
			cursor.updatePosition(e.getX(), e.getY());
			if(cursor.isButtonDown(MouseEvent.BUTTON1))
				placeItemDrag();
			else if(cursor.isButtonDown(MouseEvent.BUTTON3))
				scroll(cursor.getDeltaScreenPos());
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			if(e.getWheelRotation() < 0)
				zoomIn();
			else zoomOut();
		}
	}
}
