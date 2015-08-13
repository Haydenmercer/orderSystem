package orderSystem;

import java.util.ArrayList;

public class PurchaseOrder {

	private int orderNumber;
	private ArrayList<Item> items;
	private String supplier;

	public PurchaseOrder(int id, String supplier) {
		orderNumber = id;
		this.supplier = supplier;
		items = new ArrayList();
	}

	// add new item
	public void addItem(Item item) {
		items.add(item);
	}

	// remove item
	public void removeItem(int ID) {
		for (Item item : items) {
			if (item.getID() == ID) {
				items.remove(item);
			}
		}
	}

	// get list of items
	public ArrayList<Item> getItems() {
		return items;
	}

	// return supplier
	public String getSupplier() {
		return supplier;
	}

	// return order number
	public int getOrderNumber() {
		return orderNumber;
	}
}
