package orderSystem;

import java.util.ArrayList;

public class DataCenter {

	private static DataCenter dataCenter = new DataCenter();
	private ArrayList<Item> inventory;
	private ArrayList<Order> orders;
	private ArrayList<Item> purchaseOrders;

	// singleton instantiation
	public static DataCenter getInstance() {
		return dataCenter;
	}

	public void updateOrdersTable() {

	}

	public void updateInventoryTable() {

	}

	public void updatePurchaseOrderTable() {

	}

	public ArrayList<Item> pullInventory() {
		return null;
	}

	public ArrayList<Order> pullOrders() {
		return null;
	}

	public ArrayList<Item> pullPurchaseOrders() {
		return null;
	}
}