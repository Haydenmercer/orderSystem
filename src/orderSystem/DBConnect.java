package orderSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class DBConnect {
	String host = "jdbc:mysql://127.0.0.1:3308/nbgardensims";
	String uName = "root";
	String uPass = "netbuilder";
	String jdbc_driver = "com.mysql.jdbc.Driver";
	
	

	public void connect() {
		
		Connection c = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection(host, uName, uPass);
			System.out.println("VERKED");
		
		
		Statement stmnt = null;
		stmnt = (Statement) c.createStatement();
		String query = "SELECT * FROM nbgardensims.order";
		ResultSet rs = stmnt.executeQuery(query);
		
		while(rs.next()){
			System.out.println(rs.getInt("order_customer_id"));
		}
		
		}
		
		catch (Exception e) {
			System.out.println(e);
		}
	}

}
