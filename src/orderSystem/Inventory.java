package orderSystem;

import java.util.ArrayList;

/*This is a singleton inventory class which holds all the stock and stock levels*/

public class Inventory {

	private ArrayList<Item> inventory;
	private DBConnect connect;

	// singleton
	private static Inventory invy;

	private Inventory() {
		connect = DBConnect.getInstance();
		inventory = connect.pullInventory();
	}

	// singleton instantiation
	public static Inventory getInstance() {
		if (invy == null) {
			invy = new Inventory();
		}
		return invy;
	}

	// add a new item to the inventory
	public void addItem(Item item) {
		boolean found = false;
		for (Item i : inventory) {
			if (item.getName().equals(i.getName())) {
				found = true;
				break;
			}
		}
		inventory.add(item);
		connect.addStockItem(item);
	}

	// refresh inventory from database
	public void rePull() {
		inventory.clear();
		inventory = connect.pullInventory();
	}

	// return inventory ArrayList
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	// remove an item by it's index
	public boolean removeItem(int index) {
		// inventory.remove(index);

		if (connect.deleteStockItem(inventory.get(index))) {
			rePull();
			return true;

		}
		return false;

	}

	// reduce stock by item ID
	public void reduceStock(int ID, int amount) {
		for (int i = 0; i < inventory.size(); i++) {
			Item item = inventory.get(i);
			if (ID == item.getID()) {
				item.addQuantity(amount);
				connect.updateStockItem(item);
			}
		}
		rePull();
	}

	// clear items list
	public void clear() {
		inventory.clear();
	}

	// update item in the database
	private void updateItem(Item item) {
		connect.updateStockItem(item);
	}

}
