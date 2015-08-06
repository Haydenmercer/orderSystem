package orderSystem;

import java.util.ArrayList;

public class PurchaseOrder {

	private int orderNumber;
	private ArrayList<Item> items;
	private String supplier;
	
	
	public PurchaseOrder(String supplier){
		orderNumber = 53;
		this.supplier=supplier;
		items = new ArrayList();
	}
	
	public void addItem(Item item){
		items.add(item);
	}
	
	public void removeItem(int ID){
		for(Item item : items){
			if (item.getID() == ID){
				items.remove(item);
			}
		}
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	
	public String getSupplier(){
		return supplier;
	}
	
	public int getOrderNumber(){
		return orderNumber;
	}
	
	
	
}
