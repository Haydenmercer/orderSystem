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

	public Order(int orderNumber, int customerID, String customerName, Status status) {
		this.orderNumber = orderNumber;
		this.customerID = customerID;
		this.customerName = customerName;
		items = new ArrayList();
		this.status = status;
	}

	// add item to order. If not in stock, will return false
	public boolean addItem(Item item) {
		items.add(item);
		return true;
	}

	// sets status to picking (checked out)
	public void setChecked(boolean checked) {
		if (checked) {
			status = status.PICKING;
		} else {
			status = status.PENDING;
		}

	}

	// set status to picked
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

	// is it picked?
	public boolean getPicked() {
		return (status == status.PICKED);
	}

	// get ArrayList of items in the order
	public ArrayList<Item> getItems() {
		return items;
	}

	// set the status
	public void setStatus(Status s) {
		this.status = s;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public int getCustomerID() {
		return customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public Status getStatus() {
		return status;
	}

	// return status as a string
	public String getStatusString() {
		String statusString = "";
		switch (status) {
		case PENDING:
			statusString = "Pending";
			break;
		case PICKING:
			statusString = "Picking";
			break;
		case PICKED:
			statusString = "Picked";
			break;
		default:
			statusString = "";
			break;
		}
		return statusString;
	}

	// shortest path
	public String[] getRoute() {
		boolean[][] positions = new boolean[7][7];
		int downFromCurrent = 0;
		int rightFromCurrent = 0;

		for (Item item : items) {
			String location = item.getLocation();
			// System.out.println(item.getLocation());
			int x = Character.getNumericValue((location.charAt(0)));
			int y = Character.getNumericValue((location.charAt(1)));
			positions[x][y] = true;
		}

		int shortest = 100;
		String[] path = new String[items.size()];

		int x = 0;
		int y = 0;

		int tempX = 0;
		int tempY = 0;

		for (int k = 0; k < items.size(); k++) {
			for (int i = 0; i < 7; i++) {
				downFromCurrent = i - x;
				for (int j = 0; j < 7; j++) {
					rightFromCurrent = i - y;
					if (positions[i][j] == true) {
						int distanceTemp = downFromCurrent + rightFromCurrent;
						if (distanceTemp < shortest) {
							shortest = distanceTemp;
							tempX = i;
							tempY = j;
							positions[i][j] = false;
						}
					}
				}
			}
			shortest = 100;

			x = tempX;
			y = tempY;
			path[k] = String.valueOf(tempX + " : " + tempY);
		}
		return path;
	}

}
