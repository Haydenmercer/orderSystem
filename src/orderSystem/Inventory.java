package orderSystem;

import java.util.ArrayList;

/*This is a singleton inventory class which holds all the stock and stock levels*/

public class Inventory {

	private ArrayList<Item> inventory;

	// singleton
	private static Inventory invy = new Inventory();

	private Inventory() {
		inventory = new ArrayList();
	}

	// singleton instantiation
	public static Inventory getInstance() {
		return invy;
	}

	// add a new item to the inventory
	public void addItem(Item item) {
		boolean found = false;
		
		for(Item i : inventory){
		if(item.getName().equals(i.getName())){
			found = true;
			break;
		}
		

		}
		inventory.add(item);
	}

	// return inventory ArrayList
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	// remove an item by it's index
	public void removeItem(int index) {
		inventory.remove(index);
	}

	// remove an item by reference to the object
	public void removeByitem(Item item) {
		inventory.remove(item);
	}

	// reduce stock by item ID
	public boolean reduceStock(int ID, int amount) {
		for (Item i : inventory) {
			if (ID == i.getID()) {
				i.addQuantity(amount);
			}
		}
		return true;
	}
	
	public void clear(){
		inventory.clear();
	}

	/*
	 * //update reserved stock public boolean updateReserved(String item, int
	 * amount){ for(Item i : inventory){ if(item.equals(i.getName())){
	 * i.updateReserved(amount); } } return true; }
	 */

}
