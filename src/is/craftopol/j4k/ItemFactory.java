package is.craftopol.j4k;

public class ItemFactory {
	/**
	 * Returns the appropriate Item instance based on the String type. Case is
	 * ignored.
	 * 
	 * @param type the type of Item to create
	 * @return a new Item with the specified type
	 */
	public static Item newItem(String type) {
		type = type.toLowerCase();

		if(type.equals("spawn point"))
			return new PointItem(Item.TYPE_SPAWNPOINT);
		else if(type.equals("exit point"))
			return new PointItem(Item.TYPE_EXITPOINT);
		else if(type.equals("line"))
			return new Line();
		else if(type.equals("circle"))
			return new Circle();
		else if(type.equals("triangle"))
			return new Triangle();
		else throw new RuntimeException("Invalid item type");
	}
}
