package orderSystem;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

public class OrderDetailsWindow extends JFrame {
	public OrderDetailsWindow() {
		setTitle("Order Details");
		getContentPane().setLayout(null);
		
		JButton btnCheckoutForPicking = new JButton("Check-Out for Picking");
		btnCheckoutForPicking.setBounds(248, 228, 176, 23);
		getContentPane().add(btnCheckoutForPicking);
	}
}
