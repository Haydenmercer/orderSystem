package orderSystem;

import javax.swing.JFrame;

public class main {

	public static void main(String args[]) {
		IMS ims = new IMS();
		ims.addDummyData();
		ims.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ims.setSize(1650, 1040);
		//ims.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		ims.setVisible(true);

	}
}
