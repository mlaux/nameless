package is.craftopol.j4k;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * An Item is an entity that can be placed in a level for the character to
 * interact with.
 */
public interface Item {
	/** Type constant for a {@link java.awt.Point}. */
	public static final byte TYPE_SPAWNPOINT = 0x01;
	
	/** Type constant for an exit of a level. */
	public static final byte TYPE_EXITPOINT = 0x02;
	
	/** Type constant for a {@link Line}. */
	public static final byte TYPE_LINE = 0x03;
	
	/** Type constant for a {@link Rect}. */
	public static final byte TYPE_RECT = 0x04;
	
	/** Type constant for a {@link Circle}. */
	public static final byte TYPE_CIRCLE = 0x05;
	
	/** Flag constant indicating that this Item is filled in. */
	public static final char FLAG_FILLED = 0x100;
	
	/**
	 * Returns the string representation of this Item, readable by the game.
	 * The first char of the string should be one of the TYPE constants, and a
	 * combination of any of the FLAG constants. For example, the first
	 * character of the String representation of a filled {@link Circle} would be:
	 * <br><br>
	 * 
	 * <code>char firstChar = (char) (Item.CIRCLE_TYPE | Item.FILLED_FLAG);</code>
	 * 
	 * <br><br>
	 * The rest of the chars of the string should contain information specific
	 * to this specific Item.
	 * 
	 */
	public abstract String serialize();
	
	/**
	 * @return the size of a rectangle which completely encloses this Item
	 */
	public abstract Dimension getSize();
	
	/**
	 * Draws this Item using the provided Graphics object.
	 * 
	 * @param g
	 */
	public abstract void render(Graphics g);
	
	public abstract void setPosition(int x, int y);
	
	public abstract void placeItemStart(Cursor cursor);
	
	public abstract void placeItemDrag(Cursor cursor);
	
	public abstract void setThickness(int thickness);
	
	public abstract int getThickness();
	
	public abstract boolean contains(int x, int y);
}