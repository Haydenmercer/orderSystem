package orderSystem;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

/**
 * @author hmercer
 *
 */
public class IMS extends JFrame {
	private DBConnect connect;

	private ArrayList<Order> orders;
	private ArrayList<PurchaseOrder> purchaseOrders;
	private Inventory inventory; // singleton

	private ArrayList<Order> pendingOrders = new ArrayList();
	private ArrayList<Order> pickingOrders = new ArrayList();
	private ArrayList<Order> pickedOrders = new ArrayList();

	private JList orderList;
	private JList purchaseOrdersList;

	DefaultListModel<String> listModelPending;
	DefaultListModel<String> listModelPicking;
	DefaultListModel<String> listModelPicked;

	DefaultListModel<String> listModelPurchaseOrders;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JTable table;
	private JTextField quantityField;
	private JTextField priceField;
	private JTextField locationField;
	private JTextField descriptionField;
	private JTextField nameField;

	private JTable orderItemsTable;
	private JTable purchaseItemsTable;

	private JLabel customerIDLabel;
	private JLabel orderNumberLabel;
	private JLabel customerNameLabel;
	private JLabel orderStatusLabel;

	private JLabel supplierNameLabel;
	private JLabel purchaseOrderNumberLabel;

	private JScrollPane inventoryScrollPane;
	private JScrollPane orderTableScrollPane;

	private enum Status {
		PICKING, PACKING, PICKED, DELIVERED, PENDING
	};

	public IMS() throws IOException {
		super("NB Gardens Inventory Management");
		getContentPane().setBackground(SystemColor.windowBorder);

		connect = DBConnect.getInstance();
		connect.connect();

		// getContentPane().setBackground(new Color(123, 154, 123));
		// getContentPane().setBackground(new ImageIcon(ImageIO.read(new
		// File("C:/Users/hmercer/Documents/gnomes.jpg"))));
		// JLabel label = new JLabel(new ImageIcon(ImageIO.read(new
		// File("C:/Users/hmercer/Documents/gnomes3.jpg"))));
		// setContentPane(label);
		// setForeground(SystemColor.textHighlight);
		getContentPane().setForeground(SystemColor.info);

		// initialise orders, purchase orders and inventory from database
		orders = connect.associateItems();
		purchaseOrders = connect.associatePurchaseOrders();
		inventory = Inventory.getInstance();// singleton instance

		// list models
		listModelPending = new DefaultListModel();
		listModelPicking = new DefaultListModel();
		listModelPicked = new DefaultListModel();
		listModelPurchaseOrders = new DefaultListModel();

		fillStrings();// fills list models

		getContentPane().setLayout(null);

		// pending orders list
		orderList = new JList(listModelPending);
		orderList.setForeground(SystemColor.inactiveCaptionBorder);
		orderList.setBackground(SystemColor.windowText);
		orderList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		orderList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		orderList.setVisibleRowCount(-1);
		orderList.setLayoutOrientation(JList.VERTICAL);
		orderList.setSelectedIndex(0);

		JScrollPane listPane = new JScrollPane(orderList);
		listPane.setBounds(37, 79, 188, 395);
		getContentPane().add(listPane);

		// check out button
		btnNewButton_1 = new JButton("Check Out");
		btnNewButton_1.setBounds(37, 25, 115, 23);
		getContentPane().add(btnNewButton_1);

		// mark picked button
		btnNewButton_2 = new JButton("Mark Picked");
		btnNewButton_2.setBounds(285, 25, 115, 23);
		getContentPane().add(btnNewButton_2);

		JLabel lblNewLabel = new JLabel("Pending Orders:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(37, 59, 93, 14);
		getContentPane().add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(285, 79, 181, 395);
		getContentPane().add(scrollPane);

		// picking orders list
		JList list = new JList(listModelPicking);
		list.setForeground(Color.WHITE);
		list.setBackground(Color.BLACK);
		scrollPane.setViewportView(list);

		JLabel lblCurrentlyBeingPicked = new JLabel("Currently Being Picked:");
		lblCurrentlyBeingPicked.setForeground(Color.WHITE);
		lblCurrentlyBeingPicked.setBounds(285, 59, 181, 14);
		getContentPane().add(lblCurrentlyBeingPicked);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(524, 79, 181, 395);
		getContentPane().add(scrollPane_1);

		// picked orders list
		JList list_1 = new JList(listModelPicked);
		list_1.setForeground(Color.WHITE);
		list_1.setBackground(Color.BLACK);
		scrollPane_1.setViewportView(list_1);

		JLabel lblOrdersPickedAnd = new JLabel("Orders Picked:");
		lblOrdersPickedAnd.setForeground(Color.WHITE);
		lblOrdersPickedAnd.setBounds(524, 59, 181, 14);
		getContentPane().add(lblOrdersPickedAnd);

		inventoryScrollPane = new JScrollPane();
		inventoryScrollPane.setBounds(744, 79, 671, 395);
		getContentPane().add(inventoryScrollPane);

		table = new JTable();
		table.setBackground(SystemColor.info);
		inventoryScrollPane.setViewportView(table);

		updateInventoryTable();// update the inventory table with recordz

		JLabel lblInventory = new JLabel("Inventory:");
		lblInventory.setForeground(Color.WHITE);
		lblInventory.setBounds(744, 54, 133, 23);
		lblInventory.setFont(new Font("Calibri Light", Font.BOLD, 22));
		getContentPane().add(lblInventory);

		JButton btnNewButton_3 = new JButton("Delete Item");
		btnNewButton_3.setBounds(744, 486, 133, 58);
		getContentPane().add(btnNewButton_3);

		JPanel panel = new JPanel();
		panel.setForeground(SystemColor.menu);
		panel.setBounds(744, 570, 325, 420);
		panel.setBackground(SystemColor.info);
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);

		// add new product fields

		quantityField = new JTextField();
		quantityField.setBounds(126, 248, 86, 20);
		panel.add(quantityField);
		quantityField.setColumns(10);

		priceField = new JTextField();
		priceField.setBounds(126, 195, 86, 20);
		panel.add(priceField);
		priceField.setColumns(10);

		locationField = new JTextField();
		locationField.setBounds(126, 300, 86, 20);
		panel.add(locationField);
		locationField.setColumns(10);

		descriptionField = new JTextField();
		descriptionField.setBounds(126, 99, 132, 61);
		panel.add(descriptionField);
		descriptionField.setColumns(10);

		nameField = new JTextField();
		nameField.setBounds(126, 49, 86, 20);
		panel.add(nameField);
		nameField.setColumns(10);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(60, 52, 86, 14);
		panel.add(lblName);

		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(37, 99, 86, 14);
		panel.add(lblDescription);

		JButton btnAdd = new JButton("Add");

		btnAdd.setBounds(126, 386, 86, 23);
		panel.add(btnAdd);

		JLabel lblAddNewProduct = new JLabel("Add New Product:");
		lblAddNewProduct.setBounds(10, 11, 174, 27);
		panel.add(lblAddNewProduct);
		lblAddNewProduct.setFont(new Font("Calibri Light", Font.BOLD, 22));

		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(60, 198, 56, 14);
		panel.add(lblPrice);

		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setBounds(39, 251, 77, 14);
		panel.add(lblQuantity);

		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(39, 303, 77, 14);
		panel.add(lblLocation);

		JCheckBox chckbxPorousWare = new JCheckBox("Porous Ware");
		chckbxPorousWare.setBounds(115, 343, 112, 23);
		panel.add(chckbxPorousWare);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.info);
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBounds(37, 570, 668, 420);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		// end

		JLabel lblCustomerId = new JLabel("Customer ID:");
		lblCustomerId.setFont(new Font("Calibri Light", Font.BOLD, 22));
		lblCustomerId.setBounds(62, 69, 144, 30);
		panel_1.add(lblCustomerId);

		JLabel lblCustomerName = new JLabel("Customer Name:");
		lblCustomerName.setFont(new Font("Calibri Light", Font.BOLD, 22));
		lblCustomerName.setBounds(28, 110, 178, 30);
		panel_1.add(lblCustomerName);

		JLabel lblOrderNumber = new JLabel("Order Number:");
		lblOrderNumber.setFont(new Font("Calibri Light", Font.BOLD, 22));
		lblOrderNumber.setBounds(44, 28, 162, 30);
		panel_1.add(lblOrderNumber);

		orderTableScrollPane = new JScrollPane();
		orderTableScrollPane.setBounds(28, 227, 614, 167);
		panel_1.add(orderTableScrollPane);

		orderItemsTable = new JTable();
		orderTableScrollPane.setViewportView(orderItemsTable);

		JLabel lblItems = new JLabel("Items:");
		lblItems.setHorizontalAlignment(SwingConstants.CENTER);
		lblItems.setFont(new Font("Calibri Light", Font.PLAIN, 22));
		lblItems.setBounds(300, 187, 73, 30);
		panel_1.add(lblItems);

		orderNumberLabel = new JLabel("");
		orderNumberLabel.setFont(new Font("Calibri Light", Font.PLAIN, 22));
		orderNumberLabel.setBounds(198, 28, 144, 30);
		panel_1.add(orderNumberLabel);

		customerIDLabel = new JLabel("");
		customerIDLabel.setFont(new Font("Calibri Light", Font.PLAIN, 22));
		customerIDLabel.setBounds(198, 69, 144, 30);
		panel_1.add(customerIDLabel);

		customerNameLabel = new JLabel("");
		customerNameLabel.setFont(new Font("Calibri Light", Font.PLAIN, 22));
		customerNameLabel.setBounds(198, 111, 144, 30);
		panel_1.add(customerNameLabel);

		JLabel lblOrderStatus = new JLabel("Order Status:");
		lblOrderStatus.setFont(new Font("Calibri Light", Font.BOLD, 22));
		lblOrderStatus.setBounds(58, 151, 162, 30);
		panel_1.add(lblOrderStatus);

		orderStatusLabel = new JLabel("");
		orderStatusLabel.setFont(new Font("Calibri Light", Font.PLAIN, 22));
		orderStatusLabel.setBounds(198, 151, 144, 30);
		panel_1.add(orderStatusLabel);

		JLabel lblOrderDetails = new JLabel("Order Details:");
		lblOrderDetails.setForeground(Color.WHITE);
		lblOrderDetails.setBounds(37, 534, 188, 23);
		lblOrderDetails.setFont(new Font("Calibri Light", Font.BOLD, 22));
		getContentPane().add(lblOrderDetails);

		JButton btnViewSelection = new JButton("View Selection");
		btnViewSelection.setBounds(41, 485, 184, 23);
		getContentPane().add(btnViewSelection);

		JButton btnViewSelection_1 = new JButton("View Selection");
		btnViewSelection_1.setBounds(285, 485, 181, 23);
		getContentPane().add(btnViewSelection_1);

		JButton btnViewSelection_2 = new JButton("View Selection");
		btnViewSelection_2.setBounds(524, 485, 181, 23);
		getContentPane().add(btnViewSelection_2);

		// purchase orders scrollpane
		JScrollPane purchaseOrdersScrollPane = new JScrollPane();
		purchaseOrdersScrollPane.setBounds(1110, 570, 305, 182);
		getContentPane().add(purchaseOrdersScrollPane);

		// purchase orders list
		purchaseOrdersList = new JList(listModelPurchaseOrders);
		purchaseOrdersList.setForeground(SystemColor.desktop);
		purchaseOrdersList.setBackground(SystemColor.info);
		purchaseOrdersScrollPane.setViewportView(purchaseOrdersList);
		purchaseOrdersList.setSelectedIndex(0);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.info);
		panel_2.setBounds(1110, 782, 305, 208);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);

		JLabel lblSupplier = new JLabel("Supplier:");
		lblSupplier.setBounds(10, 42, 61, 14);
		panel_2.add(lblSupplier);

		JLabel lblOrderNumber_1 = new JLabel("Order Number:");
		lblOrderNumber_1.setBounds(10, 11, 96, 14);
		panel_2.add(lblOrderNumber_1);

		JLabel lblItems_1 = new JLabel("Items:");
		lblItems_1.setBounds(119, 73, 61, 14);
		panel_2.add(lblItems_1);

		JScrollPane purchaseItemsScrollPane = new JScrollPane();
		purchaseItemsScrollPane.setBounds(10, 98, 272, 92);
		panel_2.add(purchaseItemsScrollPane);

		// purchase items table
		purchaseItemsTable = new JTable();
		purchaseItemsScrollPane.setViewportView(purchaseItemsTable);

		purchaseOrderNumberLabel = new JLabel("");
		purchaseOrderNumberLabel.setBounds(116, 11, 111, 14);
		panel_2.add(purchaseOrderNumberLabel);

		supplierNameLabel = new JLabel("");
		supplierNameLabel.setBounds(116, 42, 111, 14);
		panel_2.add(supplierNameLabel);

		JButton btnConfirmDelivery = new JButton("Confirm Delivery");

		btnConfirmDelivery.setBounds(1110, 535, 140, 23);
		getContentPane().add(btnConfirmDelivery);

		JButton btnViewSelection_3 = new JButton("View Selection");

		btnViewSelection_3.setBounds(1268, 535, 147, 23);
		getContentPane().add(btnViewSelection_3);

		JLabel lblPurchaseOrders = new JLabel("Purchase Orders:");
		lblPurchaseOrders.setForeground(Color.WHITE);
		lblPurchaseOrders.setFont(new Font("Calibri Light", Font.BOLD, 22));
		lblPurchaseOrders.setBounds(1110, 503, 188, 23);
		getContentPane().add(lblPurchaseOrders);
		ListSelectionModel cellSelectionModel = table.getSelectionModel();

		// immediately view product info of first order and purchase order
		Order selectedOrder = pendingOrders.get(orderList.getSelectedIndex());
		setProductInfo(selectedOrder);

		PurchaseOrder selectedOrder2 = purchaseOrders.get(purchaseOrdersList.getSelectedIndex());
		setPurchaseOrderInfo(selectedOrder2);

		// check out button
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Order selectedOrder = pendingOrders.get(orderList.getSelectedIndex());
				for (Order o : orders) {
					if (o.getOrderNumber() == selectedOrder.getOrderNumber()) {
						try {
							if (checkAgainstStock(o) == true) {
								o.setChecked(true);
								updateInventoryTable();
								setProductInfo(o);
								connect.updateOrder(o);
							} else {
								JOptionPane.showMessageDialog(null, "Insufficient stock!", "Check Out Failed",
										JOptionPane.WARNING_MESSAGE);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				fillStrings();
			}
		});

		// set picked button
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Order selectedOrder = pickingOrders.get(list.getSelectedIndex());
				for (Order o : orders) {
					System.out.println(o.getOrderNumber() == selectedOrder.getOrderNumber());
					if (o.getOrderNumber() == selectedOrder.getOrderNumber()) {
						o.setPicked(true);
						setProductInfo(o);
						connect.updateOrder(o);
					}
				}
				fillStrings();
			}
		});

		// view
		btnViewSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Order selectedOrder = pendingOrders.get(orderList.getSelectedIndex());
				setProductInfo(selectedOrder);
			}
		});

		// view
		btnViewSelection_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Order selectedOrder = pickingOrders.get(list.getSelectedIndex());
				setProductInfo(selectedOrder);
			}
		});

		// view
		btnViewSelection_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Order selectedOrder = pickedOrders.get(list_1.getSelectedIndex());
				setProductInfo(selectedOrder);
			}
		});

		// view purchase order selection
		btnViewSelection_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PurchaseOrder selectedOrder = purchaseOrders.get(purchaseOrdersList.getSelectedIndex());
				setPurchaseOrderInfo(selectedOrder);
			}
		});

		// add button
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean validInput = true;
				int price = 0;
				int quantity = 0;

				try {
					price = Integer.parseInt(priceField.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid Price! Please enter a valid number", "Invalid Input!",
							JOptionPane.WARNING_MESSAGE);
					validInput = false;
				}

				try {
					quantity = Integer.parseInt(quantityField.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid Quantity! Please enter a valid number",
							"Invalid Input!", JOptionPane.WARNING_MESSAGE);
					validInput = false;
				}

				if (validInput) {
					inventory.addItem(new Item(45, nameField.getText(), descriptionField.getText(), price, quantity,
							locationField.getText(), chckbxPorousWare.isSelected()));
					JOptionPane.showMessageDialog(null, "Item Added!", "Message", JOptionPane.WARNING_MESSAGE);
					updateInventoryTable();
					nameField.setText("");
					descriptionField.setText("");
					priceField.setText("");
					quantityField.setText("");
					locationField.setText("");
					chckbxPorousWare.setSelected(false);
				}
			}
		});

		// delete button
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				if (!inventory.removeItem(index)) {
					JOptionPane.showMessageDialog(null,
							"Orders Depend on this Item! Either clear these orders or reduce stock", "Delete Failed",
							JOptionPane.WARNING_MESSAGE);
				}
				updateInventoryTable();
			}
		});

		// confirm delivery
		btnConfirmDelivery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PurchaseOrder purchaseOrder = purchaseOrders.get(purchaseOrdersList.getSelectedIndex());
				for (int i = 0; i < inventory.getInventory().size(); i++) {
					for (Item item : purchaseOrder.getItems()) {
						if (item.getID() == inventory.getInventory().get(i).getID()) {
							inventory.getInventory().get(i).addQuantity(item.getQuantity());
							;
							connect.updateStockItem(inventory.getInventory().get(i));
							purchaseOrders.remove(purchaseOrder);
							//connect.deletePurchaseOrder(purchaseOrder.getOrderNumber()); //COMMENT THIS OUT IF YOU DONT WANT TO DELETE PURCHASE ORDERS FROM DATABASE (So don't have to keep re-adding)
						}
					}
				}
				updateInventoryTable();
				fillStrings();
			}
		});
	}

	// update the inventory table view (top right)
	private void updateInventoryTable() {
		Object[][] rowsData = new Object[inventory.getInventory().size()][7];
		for (int i = 0; i < inventory.getInventory().size(); i++) {
			rowsData[i][0] = inventory.getInventory().get(i).getID();
			rowsData[i][1] = inventory.getInventory().get(i).getName();
			rowsData[i][2] = inventory.getInventory().get(i).getDescription();
			rowsData[i][3] = "£" + inventory.getInventory().get(i).getPrice();
			rowsData[i][4] = inventory.getInventory().get(i).getQuantity();
			rowsData[i][5] = inventory.getInventory().get(i).getLocation();
			rowsData[i][6] = inventory.getInventory().get(i).getPorousWare();
		}
		String[] columnTitles = { "ID", "Product Name", "Description", "Price", "Quantity", "Location", "Porous Ware" };
		DefaultTableModel model = new DefaultTableModel(rowsData, columnTitles);
		table.setModel(model);
		table.getColumn("ID").setMaxWidth(50);
		table.getColumn("Description").setMinWidth(200);
		table.getColumn("Price").setMaxWidth(50);
		table.getColumn("Quantity").setMaxWidth(50);
		table.getColumn("Location").setMaxWidth(50);
		table.getColumn("Porous Ware").setMaxWidth(70);
	}

	// update the order items table view (bottom left)
	private void updateOrderItemsTable(Order order) {
		Object[][] rowsData = new Object[order.getItems().size()][7];
		String[] columnTitles = { "ID", "Product Name", "Description", "Price", "Quantity", "Location" };
		for (int i = 0; i < order.getItems().size(); i++) {
			rowsData[i][0] = order.getItems().get(i).getID();
			rowsData[i][1] = order.getItems().get(i).getName();
			rowsData[i][2] = order.getItems().get(i).getDescription();
			rowsData[i][3] = "£" + order.getItems().get(i).getPrice();
			rowsData[i][4] = order.getItems().get(i).getQuantity();
			rowsData[i][5] = order.getItems().get(i).getLocation();
		}
		DefaultTableModel model = new DefaultTableModel(rowsData, columnTitles);
		orderItemsTable.setModel(model);
		orderItemsTable.getColumn("ID").setMaxWidth(50);
		orderItemsTable.getColumn("Description").setMinWidth(200);
		orderItemsTable.getColumn("Price").setMaxWidth(50);
		orderItemsTable.getColumn("Quantity").setMaxWidth(50);
		orderItemsTable.getColumn("Location").setMaxWidth(70);
	}

	// update the purchase order items table view (bottom right)
	private void updatePurchaseItemsTable(PurchaseOrder purchaseOrder) {
		Object[][] rowsData = new Object[purchaseOrder.getItems().size()][4];
		String[] columnTitles = { "ID", "Product Name", "Quant", "Location" };
		for (int i = 0; i < purchaseOrder.getItems().size(); i++) {
			rowsData[i][0] = purchaseOrder.getItems().get(i).getID();
			rowsData[i][1] = purchaseOrder.getItems().get(i).getName();
			rowsData[i][2] = purchaseOrder.getItems().get(i).getQuantity();
			rowsData[i][3] = purchaseOrder.getItems().get(i).getLocation();
		}
		DefaultTableModel model = new DefaultTableModel(rowsData, columnTitles);
		purchaseItemsTable.setModel(model);
		purchaseItemsTable.getColumn("ID").setMaxWidth(50);
		purchaseItemsTable.getColumn("Quant").setMaxWidth(50);
	}

	// ensure that there is enough stock to check out an order, if so, reduce it
	// from the inventory
	private boolean checkAgainstStock(Order order) {
		boolean[] matched = new boolean[order.getItems().size()];
		int counter = 0;
		@SuppressWarnings("unchecked") // ?
		ArrayList<Item> temp = (ArrayList<Item>) inventory.getInventory().clone();
		for (Item item : order.getItems()) {
			for (Item stockItem : temp) {
				if (item.getID() == stockItem.getID()) {
					if (stockItem.getQuantity() - item.getQuantity() < 0) {
						;
						return false;
					} else {
						inventory.reduceStock(item.getID(), -item.getQuantity());
						matched[counter] = true;
					}
				} else {
					System.out.println("Did not match");
				}
			}
			counter++;
		}
		// check all have been successful
		for (boolean v : matched) {
			if (!v) {
				return false;
			}
		}
		return true;

	}

	// display product info in the product details panel and display shortest
	// path in the console
	private void setProductInfo(Order order) {
		customerNameLabel.setText(order.getCustomerName());
		orderNumberLabel.setText(String.valueOf(order.getOrderNumber()));
		customerIDLabel.setText(String.valueOf(order.getCustomerID()));
		orderStatusLabel.setText(order.getStatusString());
		updateOrderItemsTable(order);

		// shortest path
		for (int i = 0; i < order.getRoute().length; i++) {
			System.out.println(order.getRoute()[i]);
		}
		System.out.println("-----------------------");
	}

	private void setPurchaseOrderInfo(PurchaseOrder purchaseOrder) {
		supplierNameLabel.setText(purchaseOrder.getSupplier());
		purchaseOrderNumberLabel.setText(String.valueOf(purchaseOrder.getOrderNumber()));
		updatePurchaseItemsTable(purchaseOrder);
	}

	// splits the orders by status and puts them into the appropriate list
	// models and array lists
	private void fillStrings() {
		listModelPending.clear();
		listModelPicking.clear();
		listModelPicked.clear();
		listModelPurchaseOrders.clear();

		pendingOrders.clear();
		pickingOrders.clear();
		pickedOrders.clear();

		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getStatus().equals(Order.Status.PENDING)) {
				pendingOrders.add(orders.get(i));
				listModelPending.addElement("Order # " + orders.get(i).getOrderNumber() + " --> \n\n "
						+ orders.get(i).getCustomerName() + "\n");
			}
		}
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getStatus().equals(Order.Status.PICKING)) {
				;
				pickingOrders.add(orders.get(i));
				listModelPicking.addElement("Order # " + orders.get(i).getOrderNumber() + " --> \n\n "
						+ orders.get(i).getCustomerName() + "\n");
			}
		}
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getStatus().equals(Order.Status.PICKED)) {
				pickedOrders.add(orders.get(i));
				listModelPicked.addElement("Order # " + orders.get(i).getOrderNumber() + " --> \n\n "
						+ orders.get(i).getCustomerName() + "\n");
			}
		}

		for (int i = 0; i < purchaseOrders.size(); i++) {
			listModelPurchaseOrders.addElement(
					purchaseOrders.get(i).getSupplier() + " - " + "# " + purchaseOrders.get(i).getOrderNumber());
		}
	}
}
