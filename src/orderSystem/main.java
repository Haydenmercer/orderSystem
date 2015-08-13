package orderSystem;

import java.io.IOException;

import javax.swing.JFrame;

public class main {

	public static void main(String args[]) throws IOException {
		IMS ims = new IMS();
		ims.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ims.setSize(1475, 1040);
		ims.setVisible(true);
	}
}
