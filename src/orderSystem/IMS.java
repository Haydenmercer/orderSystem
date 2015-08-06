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
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
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
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

public class IMS extends JFrame {

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

	private String[] pendingOrdersString;
	private String[] pickingOrdersString;
	private String[] pickedOrdersString;

	private JButton btnNewButton;
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

	public IMS() {
		super("NB Gardens Iventory Management");
		getContentPane().setBackground(new Color(123, 154, 123));
		setForeground(SystemColor.textHighlight);
		getContentPane().setForeground(SystemColor.info);

		orders = new ArrayList();
		purchaseOrders = new ArrayList();
		inventory = Inventory.getInstance();// singleton instance
		addDummyData();

		listModelPending = new DefaultListModel();
		listModelPicking = new DefaultListModel();
		listModelPicked = new DefaultListModel();

		listModelPurchaseOrders = new DefaultListModel();

		fillStrings();
		getContentPane().setLayout(null);

		orderList = new JList(listModelPending);
		orderList.setForeground(SystemColor.inactiveCaptionBorder);
		orderList.setBackground(SystemColor.windowText);
		orderList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		orderList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		orderList.setVisibleRowCount(-1);
		orderList.setLayoutOrientation(JList.VERTICAL);

		JScrollPane listPane = new JScrollPane(orderList);
		listPane.setBounds(73, 79, 188, 395);
		getContentPane().add(listPane);

		btnNewButton = new JButton("Refresh");
		btnNewButton.setBounds(1016, 485, 133, 58);
		getContentPane().add(btnNewButton);

		btnNewButton_1 = new JButton("Check Out");
		btnNewButton_1.setBounds(73, 25, 115, 23);
		getContentPane().add(btnNewButton_1);

		btnNewButton_2 = new JButton("Mark Picked");
		btnNewButton_2.setBounds(318, 25, 115, 23);
		getContentPane().add(btnNewButton_2);

		JLabel lblNewLabel = new JLabel("Pending Orders:");
		lblNewLabel.setBounds(73, 59, 93, 14);
		getContentPane().add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(318, 79, 181, 395);
		getContentPane().add(scrollPane);

		JList list = new JList(listModelPicking);
		list.setForeground(Color.WHITE);
		list.setBackground(Color.BLACK);
		scrollPane.setViewportView(list);
		;

		JLabel lblCurrentlyBeingPicked = new JLabel("Currently Being Picked:");
		lblCurrentlyBeingPicked.setBounds(318, 59, 181, 14);
		getContentPane().add(lblCurrentlyBeingPicked);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(560, 79, 181, 395);
		getContentPane().add(scrollPane_1);

		JList list_1 = new JList(listModelPicked);
		list_1.setForeground(Color.WHITE);
		list_1.setBackground(Color.BLACK);
		scrollPane_1.setViewportView(list_1);

		JLabel lblOrdersPickedAnd = new JLabel("Orders Picked:");
		lblOrdersPickedAnd.setBounds(560, 59, 181, 14);
		getContentPane().add(lblOrdersPickedAnd);

		// should put this in a private method - in fact you must, as it will
		// need recalling probably
		ArrayList<Item> invy = inventory.getInventory();

		inventoryScrollPane = new JScrollPane();
		inventoryScrollPane.setBounds(868, 79, 671, 395);
		getContentPane().add(inventoryScrollPane);

		table = new JTable();
		inventoryScrollPane.setViewportView(table);

		updateInventoryTable();

		String[] columnTitles = { "Product Name", "Description", "Price", "Quantity", "Location" };
		Object[][] rowData = { { "Gnome of destiny", "A deadly beast, not for the faint hearted", "99", "500" },
				{ "21", "22", "23", "24" }, { "31", "32", "33", "34" }, { "41", "42", "44", "44" } };
		// cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JLabel lblInventory = new JLabel("Inventory:");
		lblInventory.setBounds(868, 53, 133, 23);
		lblInventory.setFont(new Font("Calibri Light", Font.BOLD, 22));
		getContentPane().add(lblInventory);

		JButton btnMarkPacked = new JButton("Mark Packed");
		btnMarkPacked.setBounds(560, 25, 109, 23);
		getContentPane().add(btnMarkPacked);

		JButton btnNewButton_3 = new JButton("Delete Item");
		btnNewButton_3.setBounds(868, 485, 133, 58);
		getContentPane().add(btnNewButton_3);

		JPanel panel = new JPanel();
		panel.setForeground(SystemColor.menu);
		panel.setBounds(868, 570, 325, 420);
		panel.setBackground(SystemColor.menu);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);

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

		JButton btnAdd = new JButton("Add!!");

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
		panel_1.setBounds(73, 570, 668, 420);
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

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
		customerNameLabel.setBounds(198, 110, 144, 30);
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
		lblOrderDetails.setBounds(73, 546, 188, 23);
		lblOrderDetails.setFont(new Font("Calibri Light", Font.BOLD, 22));
		getContentPane().add(lblOrderDetails);

		JButton btnViewSelection = new JButton("View Selection");
		btnViewSelection.setBounds(77, 485, 184, 23);
		getContentPane().add(btnViewSelection);

		JButton btnViewSelection_1 = new JButton("View Selection");
		btnViewSelection_1.setBounds(318, 485, 181, 23);
		getContentPane().add(btnViewSelection_1);

		JButton btnViewSelection_2 = new JButton("View Selection");
		btnViewSelection_2.setBounds(560, 485, 181, 23);
		getContentPane().add(btnViewSelection_2);

		// purchase orders scrollpane
		JScrollPane purchaseOrdersScrollPane = new JScrollPane();
		purchaseOrdersScrollPane.setBounds(1246, 570, 292, 189);
		getContentPane().add(purchaseOrdersScrollPane);

		// purchase orders list
		purchaseOrdersList = new JList(listModelPurchaseOrders);
		purchaseOrdersList.setForeground(SystemColor.desktop);
		purchaseOrdersList.setBackground(SystemColor.menu);
		purchaseOrdersScrollPane.setViewportView(purchaseOrdersList);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(1246, 789, 292, 201);
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

		btnConfirmDelivery.setBounds(1247, 536, 140, 23);
		getContentPane().add(btnConfirmDelivery);

		JButton btnViewSelection_3 = new JButton("View Selection");

		btnViewSelection_3.setBounds(1392, 536, 147, 23);
		getContentPane().add(btnViewSelection_3);
		ListSelectionModel cellSelectionModel = table.getSelectionModel();

		// table.getColumn(2).setPreferredWidth(100);

		// check out button
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Order selectedOrder = pendingOrders.get(orderList.getSelectedIndex());
				for (Order o : orders) {
					System.out.println(o.getOrderNumber() == selectedOrder.getOrderNumber());
					if (o.getOrderNumber() == selectedOrder.getOrderNumber()) {
						if (checkAgainstStock(o) == true) {
							o.setChecked(true);
							updateInventoryTable();
							setProductInfo(selectedOrder);
						} else {
							System.out.println("Not enough in Stock ROFLMAO!!");
							// updateInventoryTable();
							JOptionPane.showMessageDialog(null, "NOT ENOUGH IN STOCK M8!!", "Inane warning",
									JOptionPane.WARNING_MESSAGE);
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
						setProductInfo(selectedOrder);
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
				inventory.addItem(new Item(45, nameField.getText(), descriptionField.getText(),
						Integer.parseInt(priceField.getText()), Integer.parseInt(quantityField.getText()),
						locationField.getText()));
				JOptionPane.showMessageDialog(null, "Item Added!", "Message", JOptionPane.WARNING_MESSAGE);

				updateInventoryTable();
				nameField.setText("");
				descriptionField.setText("");
				priceField.setText("");
				quantityField.setText("");
				locationField.setText("");
			}
		});

		// delete button
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				inventory.removeItem(index);
				updateInventoryTable();
			}
		});

		// confirm delivery
		btnConfirmDelivery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PurchaseOrder purchaseOrder = purchaseOrders.get(purchaseOrdersList.getSelectedIndex());

				for (Item stockItem : inventory.getInventory()) {

					for (Item item : purchaseOrder.getItems()) {
						if(item.getID() == stockItem.getID()){
							stockItem.addQuantity(item.getQuantity());;
							purchaseOrders.remove(purchaseOrder);
						}
					}
				}
				updateInventoryTable();
				fillStrings();
			}
		});

	}

	private void updateInventoryTable() {
		System.out.println("INVY QUANT: " + inventory.getInventory().get(0).getQuantity());

		Object[][] rowsData = new Object[inventory.getInventory().size()][6];
		for (int i = 0; i < inventory.getInventory().size(); i++) {
			rowsData[i][0] = inventory.getInventory().get(i).getID();
			rowsData[i][1] = inventory.getInventory().get(i).getName();
			rowsData[i][2] = inventory.getInventory().get(i).getDescription();
			rowsData[i][3] = inventory.getInventory().get(i).getPrice();
			rowsData[i][4] = inventory.getInventory().get(i).getQuantity();
			rowsData[i][5] = inventory.getInventory().get(i).getLocation();
		}

		String[] columnTitles = { "ID", "Product Name", "Description", "Price", "Quantity", "Location" };

		DefaultTableModel model = new DefaultTableModel(rowsData, columnTitles);
		// table = new JTable();
		table.setModel(model);

		table.getColumn("ID").setMaxWidth(50);
		table.getColumn("Description").setMinWidth(200);
		table.getColumn("Price").setMaxWidth(50);
		table.getColumn("Quantity").setMaxWidth(50);
		// table.setCellSelectionEnabled(true);
		// table.repaint();
	}

	private void updateOrderItemsTable(Order order) {
		Object[][] rowsData = new Object[order.getItems().size()][6];
		String[] columnTitles = { "ID", "Product Name", "Description", "Price", "Quantity", "Location" };
		for (int i = 0; i < order.getItems().size(); i++) {
			rowsData[i][0] = order.getItems().get(i).getID();
			rowsData[i][1] = order.getItems().get(i).getName();
			rowsData[i][2] = order.getItems().get(i).getDescription();
			rowsData[i][3] = order.getItems().get(i).getPrice();
			rowsData[i][4] = order.getItems().get(i).getQuantity();
			rowsData[i][5] = order.getItems().get(i).getLocation();
		}
		DefaultTableModel model = new DefaultTableModel(rowsData, columnTitles);
		orderItemsTable.setModel(model);

		orderItemsTable.getColumn("ID").setMaxWidth(50);
		orderItemsTable.getColumn("Description").setMinWidth(200);
		orderItemsTable.getColumn("Price").setMaxWidth(50);
		orderItemsTable.getColumn("Quantity").setMaxWidth(50);
	}

	private void updatePurchaseItemsTable(PurchaseOrder purchaseOrder) {
		Object[][] rowsData = new Object[purchaseOrder.getItems().size()][4];
		String[] columnTitles = { "ID", "Product Name", "Quant", "Location" };
		for (int i = 0; i < purchaseOrder.getItems().size(); i++) {
			rowsData[i][0] = purchaseOrder.getItems().get(i).getID();
			rowsData[i][1] = purchaseOrder.getItems().get(i).getName();
			;
			rowsData[i][2] = purchaseOrder.getItems().get(i).getQuantity();
			rowsData[i][3] = purchaseOrder.getItems().get(i).getLocation();
		}

		DefaultTableModel model = new DefaultTableModel(rowsData, columnTitles);
		purchaseItemsTable.setModel(model);

		purchaseItemsTable.getColumn("ID").setMaxWidth(50);
		purchaseItemsTable.getColumn("Quant").setMaxWidth(50);
	}

	private boolean checkAgainstStock(Order order) {
		boolean[] matched = new boolean[order.getItems().size()];
		int counter = 0;
		for (Item item : order.getItems()) {
			for (Item stockItem : inventory.getInventory()) {
				if (item.getID() == stockItem.getID()) {
					if (stockItem.getQuantity() - item.getQuantity() < 0) {
						System.out.println("Not enough in stock LMFAO!!1111");
						return false;
					} else {
						System.out.println("Success!!!!!");
						inventory.reduceStock(item.getID(), -item.getQuantity());
						System.out.println(stockItem.getQuantity());
						System.out.println("Counter: " + counter);
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

	// display product info in the product details panel
	private void setProductInfo(Order order) {
		customerNameLabel.setText(order.getCustomerName());
		orderNumberLabel.setText(String.valueOf(order.getOrderNumber()));
		customerIDLabel.setText(String.valueOf(order.getCustomerID()));
		orderStatusLabel.setText(order.getStatusString());
		updateOrderItemsTable(order);
	}

	private void setPurchaseOrderInfo(PurchaseOrder purchaseOrder) {
		supplierNameLabel.setText(purchaseOrder.getSupplier());
		purchaseOrderNumberLabel.setText(String.valueOf(purchaseOrder.getOrderNumber()));
		updatePurchaseItemsTable(purchaseOrder);
	}

	private void fillStrings() {
		pendingOrdersString = new String[orders.size()];
		pickingOrdersString = new String[orders.size()];
		pickedOrdersString = new String[orders.size()];

		ArrayList<String> pending = new ArrayList();
		ArrayList<String> picking = new ArrayList();
		ArrayList<String> picked = new ArrayList();

		listModelPending.clear();
		listModelPicking.clear();
		listModelPicked.clear();
		listModelPurchaseOrders.clear();

		System.out.println(purchaseOrders.size());

		pending.clear();
		picking.clear();
		picked.clear();

		pendingOrders.clear();
		pickingOrders.clear();
		pickedOrders.clear();

		System.out.println("ORDERS SIZE " + orders.size());
		System.out.println("INVENTORY SIZE " + inventory.getInventory().size());

		for (int i = 0; i < orders.size(); i++) {

			if (orders.get(i).getStatus().equals(Order.Status.PENDING)) {
				pending.add("Order # " + orders.get(i).getOrderNumber() + " --> \n\n " + orders.get(i).getCustomerName()
						+ "\n");
				pendingOrders.add(orders.get(i));
				listModelPending.addElement("Order # " + orders.get(i).getOrderNumber() + " --> \n\n "
						+ orders.get(i).getCustomerName() + "\n");
				// System.out.println("Pending" + i);

			}
		}
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getStatus().equals(Order.Status.PICKING)) {
				picking.add(
						"Order # " + orders.get(i).getOrderNumber() + " --> " + orders.get(i).getCustomerName() + "\n");
				pickingOrders.add(orders.get(i));
				listModelPicking.addElement("Order # " + orders.get(i).getOrderNumber() + " --> \n\n "
						+ orders.get(i).getCustomerName() + "\n");
				// System.out.println("Picking" + i);
			}
		}
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getStatus().equals(Order.Status.PICKED)) {
				picked.add("Order # " + orders.get(i).getOrderNumber() + " --> " + orders.get(i).getCustomerName()
						+ "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG\n");
				pickedOrders.add(orders.get(i));
				listModelPicked.addElement("Order # " + orders.get(i).getOrderNumber() + " --> \n\n "
						+ orders.get(i).getCustomerName() + "\n");
				// System.out.println("Picked" + i);
			}
		}

		for (int i = 0; i < purchaseOrders.size(); i++) {
			listModelPurchaseOrders.addElement(
					purchaseOrders.get(i).getSupplier() + " - " + "# " + purchaseOrders.get(i).getOrderNumber());

		}

		for (int i = 0; i < pending.size(); i++) {
			pendingOrdersString[i] = pending.get(i);
		}
		for (int i = 0; i < picking.size(); i++) {
			pickingOrdersString[i] = picking.get(i);
		}
		for (int i = 0; i < picked.size(); i++) {
			pickedOrdersString[i] = picked.get(i);
		}

	}

	private void clearPurchaseOrderItemsInfo(){
		purchaseItemsTable.repaint();
	}
	// used to test etc
	public void addDummyData() {


		System.out.println("Calls isnert dummy!!!");
		orders.clear();
		inventory.clear();
		purchaseOrders.clear();

		inventory.addItem(new Item(34, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(67, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));
		inventory.addItem(new Item(35, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(68, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));
		inventory.addItem(new Item(36, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(69, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));
		inventory.addItem(new Item(37, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(70, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));
		inventory.addItem(new Item(38, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(71, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));
		inventory.addItem(new Item(39, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(72, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));
		inventory.addItem(new Item(40, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(73, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));
		inventory.addItem(new Item(41, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(74, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));
		inventory.addItem(new Item(42, "Super Gnome", "A gnome with super pwoers", 35, 30, "Row 7"));
		inventory.addItem(
				new Item(75, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));

		Order orderDummy = new Order(757, 56, "John");
		Order orderDummy1 = new Order(434, 56, "Bob");
		Order orderDummy5 = new Order(438, 55, "Hayden");

		Order orderDummy2 = new Order(457, 56, "Mathew");
		Order orderDummy3 = new Order(345, 56, "Patrick");
		Order orderDummy4 = new Order(345, 56, "Jason");

		orderDummy.addItem(new Item(34, "Super Gnome", "A gnome with super pwoers", 35, 50, "Row 7"));
		orderDummy1.addItem(
				new Item(67, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 15, "Row 8"));
		orderDummy1.addItem(new Item(34, "Super Gnome", "A gnome with super pwoers", 35, 25, "Row 7"));

		orderDummy2.setChecked(true);
		orderDummy3.setChecked(true);
		orderDummy4.setPicked(true);

		orderDummy5.addItem(new Item(34, "Super Gnome", "A gnome with super pwoers", 35, 25, "Row 7"));
		orderDummy5.addItem(
				new Item(75, "Gnome of Death", "We will all meet the gnome of death at some point", 35, 30, "Row 8"));

		Item randomItem = inventory.getInventory().get(1);
		Item randomItem2 = inventory.getInventory().get(0);
		// orderDummy.addItem(randomItem, 5);// add items to this order
		// orderDummy.addItem(randomItem, 10);
		orders.add(orderDummy1);
		orders.add(orderDummy2);
		orders.add(orderDummy3);
		orders.add(orderDummy);
		orders.add(orderDummy4);
		orders.add(orderDummy5);

		PurchaseOrder pOrder1 = new PurchaseOrder("GnomesRus");
		pOrder1.addItem(new Item(75, "Giant Gnome", "A really big gnome", 400, 40, "Row 111"));

		purchaseOrders.add(pOrder1);

		for (Item i : inventory.getInventory()) {
			System.out.println(i.getName());
		}

		System.out.println(inventory.getInventory().toString());
		System.out.println(orders.toString());

	}


	// allocate/check out order
	private void allocateOrder(Order order) {
		order.setChecked(true);
		ArrayList<Item> orders = order.getItems();
		for (Item i : orders) {
			inventory.reduceStock(i.getID(), i.getQuantity());
		}

	}
}
