package orderSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import orderSystem.Order.Status;

public class DBConnect {
	String host = "jdbc:mysql://127.0.0.1:3308/nbgardensims";
	String uName = "root";
	String uPass = "netbuilder";
	String jdbc_driver = "com.mysql.jdbc.Driver";

	Connection c = null;

	// singleton
	private static DBConnect dbConnect;

	private ArrayList<Item> inventory;
	private ArrayList<Order> orders;
	private ArrayList<PurchaseOrder> purchaseOrders;

	// singleton instantiation
	public static DBConnect getInstance() {
		if (dbConnect == null) {
			dbConnect = new DBConnect();
		}
		return dbConnect;
	}

	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection(host, uName, uPass);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private DBConnect() {
		connect();
		inventory = new ArrayList<Item>();
		orders = new ArrayList<Order>();
		purchaseOrders = new ArrayList<PurchaseOrder>();
	}

	// add new stock item to database
	public void addStockItem(Item item) {
		Statement stmt = null;
		String itemName = item.getName();
		String itemDescription = item.getDescription();
		int itemPrice = item.getPrice();
		int itemQuantity = item.getQuantity();
		boolean itemPorousWare = item.getPorousWare();
		String itemLocation2 = item.getLocation();
		char location1 = item.getLocation().charAt(0);
		int location2 = Integer.parseInt(itemLocation2);
		try {
			stmt = (Statement) c.createStatement();
			stmt.executeUpdate("INSERT INTO nbgardensims.STOCK_ITEM VALUES (0, '" + itemName + "', '" + itemDescription
					+ "', " + itemPrice + ", " + itemQuantity + ", '" + location1 + "', " + location2 + ", "
					+ itemPorousWare + ")");
		} catch (Exception e) {
			System.out.println("THIS: " + e);
		}
		pullInventory();// refresh inventory
	}

	// delete stock item from database
	public boolean deleteStockItem(Item item) {
		Statement stmt = null;
		try {
			stmt = (Statement) c.createStatement();
			stmt.executeUpdate("DELETE FROM nbgardensims.STOCK_ITEM WHERE STOCK_ITEM_ID = " + item.getID());
			pullInventory();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	// get inventory from database
	public ArrayList<Item> pullInventory() {
		inventory.clear();
		Statement stmt2 = null;
		String query = "SELECT * FROM nbgardensims.stock_item";
		try {
			stmt2 = (Statement) c.createStatement();
			ResultSet rs = stmt2.executeQuery(query);
			while (rs.next()) {
				String location = rs.getString("stock_item_row");
				inventory.add(new Item(rs.getInt("stock_item_id"), rs.getString("stock_item_name"),
						rs.getString("stock_item_description"), rs.getInt("stock_item_price"),
						rs.getInt("stock_item_quantity"), location, rs.getBoolean("stock_item_porousware")));
			}
			return inventory;
		} catch (Exception e) {
			return null;
		}
	}

	// link items to orders given database info
	public ArrayList<Order> associateItems() {

		String query2 = "SELECT * FROM nbgardensims.order_has_stock_item";
		Statement stmt3 = null;
		orders = pullOrders();
		inventory = pullInventory();
		try {
			stmt3 = (Statement) c.createStatement();
			ResultSet rs2 = stmt3.executeQuery(query2);
			while (rs2.next()) {
				int orderId = rs2.getInt("order_order_id");
				for (int i = 0; i < orders.size(); i++) {
					if (orders.get(i).getOrderNumber() == orderId) {
						int itemId = rs2.getInt("stock_item_stock_item_id");
						for (Item item : inventory) {
							if (itemId == item.getID()) {
								Item itemClone = (Item) item.clone(); // clone item for adding to order
								Item item2 = new Item(itemClone.getID(), itemClone.getName(),
										itemClone.getDescription(), itemClone.getPrice(), itemClone.getQuantity(),
										itemClone.getLocation(), itemClone.getPorousWare());
								item2.updateQuantity(rs2.getInt("order_item_quantity"));
								orders.get(i).addItem(item2);
							}
						}
					}
				}
			}
			rs2.close();
			return orders;
		} catch (Exception e) {
			System.out.println("Error in associateItems(): " + e);
			return null;
		}
	}

	// add items to purchase orders given database info
	public ArrayList<PurchaseOrder> associatePurchaseOrders() {
		String query = "SELECT * FROM nbgardensims.purchase_order_has_stock_item";
		purchaseOrders = pullPurchaseOrders();
		inventory = pullInventory();
		Statement stmt = null;
		try {
			stmt = (Statement) c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				for (PurchaseOrder o : purchaseOrders) {
					if (o.getOrderNumber() == rs.getInt("purchase_order_purchase_order_id")) {
						for (Item item : inventory) {
							if (rs.getInt("stock_item_stock_item_id") == item.getID()) {
								Item itemClone = (Item) item.clone();
								Item item2 = new Item(itemClone.getID(), itemClone.getName(),
										itemClone.getDescription(), itemClone.getPrice(), itemClone.getQuantity(),
										itemClone.getLocation(), itemClone.getPorousWare());
								item2.updateQuantity(rs.getInt("purchase_order_item_quantity"));
								o.addItem(item2);
							}
						}
					}
				}
			}
			rs.close();
			return purchaseOrders;
		} catch (Exception e) {
			System.out.println("Exception in AssociatePurchaseOrders(): " + e);
			return null;
		}
	}

	// get orders from database (used in associateItems())
	private ArrayList<Order> pullOrders() {

		orders.clear();
		Statement stmt = null;
		String query = "SELECT * FROM nbgardensims.order";
		try {
			stmt = (Statement) c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				int orderNumber = rs.getInt("order_id");
				String customerName = rs.getString("order_customer_name");
				int customerId = rs.getInt("order_customer_id");
				Order.Status status = null;
				if (rs.getBoolean("order_pending") == true) {
					status = Status.PENDING;
				} else if (rs.getBoolean("order_picking") == true) {
					status = Status.PICKING;
				} else if (rs.getBoolean("order_picked") == true) {
					status = Status.PICKED;
				}
				Order order = new Order(orderNumber, customerId, customerName, status);
				orders.add(order);
			}
			rs.close();
			return orders;
		} catch (Exception e) {
			System.out.println("Exception in pullOrders(): " + e);
			return null;
		}
	}

	// get all purchase orders from database (used in assocatePurchaseOrders())
	private ArrayList<PurchaseOrder> pullPurchaseOrders() {
		purchaseOrders.clear();
		Statement stmt = null;
		String query = "SELECT * FROM nbgardensims.purchase_order";
		try {
			stmt = (Statement) c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				int id = rs.getInt("purchase_order_id");
				String supplier = rs.getString("purchase_order_supplier");
				purchaseOrders.add(new PurchaseOrder(id, supplier));
			}
			rs.close();
			return purchaseOrders;
		} catch (Exception e) {
			System.out.println("Exception in pullPurchaseOrders: " + e);
			return null;
		}
	}

	// update a stock item using a new item object
	public void updateStockItem(Item item) {
		Statement stmt = null;
		char location = item.getLocation().charAt(0);
		String location2 = item.getLocation();
		String oneLetter = String.valueOf(location);
		
		String query = "UPDATE nbgardensims.stock_item SET stock_item_name = '" + item.getName()
				+ "', stock_item_description = '" + item.getDescription() + "', " + "stock_item_price = "
				+ item.getPrice() + ", stock_item_quantity = " + item.getQuantity() + ", stock_item_aisle = '"
				+ oneLetter + "', stock_item_row =" + location2 + ", " + "stock_item_porousware = " + item.getPorousWare()
				+ " WHERE stock_item_id = " + item.getID();
		try {
			stmt = (Statement) c.createStatement();
			stmt.executeUpdate(query);
			pullInventory();// refresh inventory
		} catch (Exception e) {
			System.out.println("Exception in updateStockItem(): " + e);
		}
	}

	// update a current order with the attributes of a new one
	public void updateOrder(Order order) {
		Statement stmt = null;
		Status status = order.getStatus();
		int pending = 1;
		int picking = 0;
		int picked = 0;
		switch (status) {
		case PENDING:
			pending = 1;
			picking = 0;
			picked = 0;
			break;
		case PICKING:
			picking = 1;
			pending = 0;
			picked = 0;
			break;
		case PICKED:
			picked = 1;
			pending = 0;
			picking = 0;
			break;
		}
		String query = "UPDATE nbgardensims.order SET order_pending = " + pending + ", order_picking = " + picking
				+ ", order_picked = " + picked + " WHERE order_id = " + order.getOrderNumber();
		try {
			stmt = (Statement) c.createStatement();
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Exception in updateOrder(): " + e);
		}
	}

	// delete a purchase order (along with any items associated with it)
	public void deletePurchaseOrder(int id) {
		String query = "DELETE FROM nbgardensims.purchase_order_has_stock_item WHERE purchase_order_purchase_order_id = "
				+ id;
		String query2 = "DELETE FROM nbgardensims.purchase_order WHERE purchase_order_id = " + id;
		Statement stmt = null;
		try {
			stmt = (Statement) c.createStatement();
			stmt.executeUpdate(query);
			stmt.executeUpdate(query2);
		} catch (Exception e) {
			System.out.println("Exception ins deletePurchaseOrder(): " + e);
		}
	}

}
