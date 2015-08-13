package orderSystem;

/**
 * @author hmercer
 *
 */

public class Item implements Cloneable {

	private int id;
	private String name;
	private String description;
	private int price;
	private int quantity;
	private boolean porousWare;

	private String locationInWarehouse;

	public Item(int id, String name, String description, int price, int quantity, String location, boolean porousWare) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		locationInWarehouse = location;
		this.porousWare = porousWare;
	}

	// change stock/order quantity levels by entering the new levels
	public void updateQuantity(int quantity) {
		this.quantity = quantity;
	}

	// increase or decrease quantity level by number added/removed
	public void addQuantity(int increase) {
		this.quantity += increase;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getLocation() {
		return locationInWarehouse;
	}

	public int getID() {
		return id;
	}

	public boolean getPorousWare() {
		return porousWare;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
