package is.craftopol.j4k;
import java.awt.Point;

public class Cursor {
	private LevelView grid;
	
	private int downX, downY;
	private int lastX, lastY;
	private int x, y;
	
	private boolean buttons[] = new boolean[4];
	
	public Cursor(LevelView g) {
		grid = g;
	}
	
	public void clicked(int x, int y, int button) {
		downX = x;
		downY = y;
		buttons[button] = true;
		
		updatePosition(x, y);
	}
	
	public void released(int x, int y, int button) {
		buttons[button] = false;
	}
	
	public boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public void updatePosition(int x, int y) {
		lastX = this.x;
		lastY = this.y;
		
		this.x = x;
		this.y = y;
	}
	
	public Point getScreenPos() {
		return new Point(x, y);
	}
	
	public int getScreenX() {
		return x;
	}
	
	public int getScreenY() {
		return y;
	}
	
	public Point getGridPos() {
		return grid.screenToWorld(x, y);
	}
	
	public int getGridX() {
		return grid.screenToWorldX(x);
	}
	
	public int getGridY() {
		return grid.screenToWorldY(y);
	}
	
	public Point getSnappedPos() {
		return grid.snap(getGridPos());
	}
	
	public Point getDeltaScreenPos() {
		return new Point(x - lastX, y - lastY);
	}
	
	public Point getDeltaGridPos() {
		Point cur = getGridPos();
		Point old = grid.screenToWorld(lastX, lastY);
		return new Point(cur.x - old.x, cur.y - old.y);
	}
	
	private int changeX, changeY;
	public Point getDeltaSnappedPos() {
		int rx = 0, ry = 0;
		
		// first case: cursor has not yet moved a full square
		// increment counters by change in grid distance
		Point pt = getDeltaGridPos();
		changeX += pt.x;
		changeY += pt.y;
		
		if(Math.abs(changeX) >= LevelView.SNAP_AMOUNT) {
			rx = grid.snap(changeX);
			changeX = 0;
		}
		
		if(Math.abs(changeY) >= LevelView.SNAP_AMOUNT) {
			ry = grid.snap(changeY);
			changeY = 0;
		}
		
		return new Point(rx, ry);
	}
	
	public Point getDraggedPos() {
		return new Point(x - downX, y - downY);
	}
	
	public Point getDraggedGridPos() {
		return grid.screenToWorld(x - downX, y - downY);
	}
}
