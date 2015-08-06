package orderSystem;

import java.util.ArrayList;

public class Order {
	
	private int orderNumber;
	private int customerID;
	private String customerName;
	private ArrayList<Item> items;

	enum Status {
		PICKING, PACKING, PICKED, DELIVERED, PENDING
	};

	private Status status;

	public Order(int orderNumber, int customerID, String customerName) {
		this.orderNumber = orderNumber;
		this.customerID = customerID;
		this.customerName=customerName;
		items = new ArrayList();
		status = Status.PENDING;
	}

	// add item to order. If not in stock, will return false
	public boolean addItem(Item item) {
		//item.updateQuantity(quantity);

		// insert code to check quantity against stock

		items.add(item);
		return true;
	}

	
	public boolean updateQuantityOfItem(String name, int quantity) {
		return true;
	}

	//sets status to picking (checked out)
	public void setChecked(boolean checked) {
		if (checked) {
			status = status.PICKING;
		} else {
			status = status.PENDING;
		}

	}

	public void setPicked(boolean picked) {
		if (picked) {
			status = status.PICKED;
		} else {
			status = status.PICKING;
		}
	}

	// is the order currently being worked on?
	public boolean getChecked() {
		return (status == status.PICKING);
	}

	public boolean getPicked() {
		return (status == status.PICKED);
	}

	// get ArrayList of items in the order
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setStatus(Status s) {
		this.status = s;
	}
	
	public int getOrderNumber(){
		return orderNumber;
	}
	
	public int getCustomerID(){
		return customerID;
	}
	
	public String getCustomerName(){
		return customerName;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public String getStatusString(){
		String statusString = "";
		
		switch(status){
		case PENDING:	statusString = "Pending"; break;
		case PICKING:	statusString = "Picking"; break;
		case PICKED: 	statusString = "Picked"; break;	
		default: statusString = "";break;
		}
		return statusString;
	}
}
