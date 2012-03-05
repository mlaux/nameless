package com.trentwdavies.nameless;

public class ItemFactory {
	/**
	 * Returns the appropriate Item instance based on the String type. Case is
	 * ignored.
	 * 
	 * @param type the type of Item to create
	 * @return a new Item with the specified type
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Item> Item newItem(Class<?> type) {
		try {
			return (T) type.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
