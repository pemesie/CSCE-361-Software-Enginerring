import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class App {

	Credentials cred =  new Credentials();
	
	public static void main(String[] args) {
		
		App app = new App();
		
		//We have to discuss about this method.  None of our class has these variable
		//app.addToStock( product_id,  price,  threshold,  quantity,  type,  description,  supplier);
		
		app.getProductCode();
		app.getItemsSold("03/03/2018");
		int id = 1; // to delete;
		app.getThreshold(id);

	}
	
	public void getProductCode(){
		try {
			Class.forName(cred.driver).newInstance();
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Connection con = null;
		
		try {
			con = DriverManager.getConnection(cred.url, cred.userName, cred.password);
		} catch (SQLException e) {
			System.out.println("SQLException: DriverManager.getConnection ... ");
			e.printStackTrace();
		}
		
		String query = "select product_id as ProductCode, description from Products";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			System.out.print("productCode\t\t Description\n");
			while(rs.next()){
				System.out.printf("%s \t\t %s \n", rs.getInt("ProductCode"), rs.getString("description"));
	
			}
		} catch (SQLException e) {
			System.out.println("SQLException:connect preparedStatement ... ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			try{
				if(ps != null && !ps.isClosed())
					ps.close();
				if(con != null && !con.isClosed())
					con.close();
			}catch (SQLException e) {
				System.out.println("SQLException:  cannot close ..." );
				throw new RuntimeException(e);
			}
		}
		
		System.out.println();
	}
	
	/*
	  In our diagram this method has one parameter; I think it should more to fill column in the database. 
	  Please if it makes sense.
	 */
	private void addToStock(int product_id, int price, int threshold, int quantity, String type, String description, String supplier){
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			try {
				Class.forName(cred.driver).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Cannot load Driver ");
		}
		try {
			conn = DriverManager.getConnection(cred.url, cred.userName, cred.password);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		//itemSoldPerDay: int[]
		 String query = " update Products set product_id = ?, price = ?, threshold = ?, quantity = ?, type = ?, description = ?, supplier = ?";
		 
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, product_id);
			ps.setInt(2, price);
			ps.setInt(3, threshold);
			ps.setInt(4, quantity);
			ps.setString(5, type);
			ps.setString(6, description);
			ps.setString(7, supplier);
	
			ps.executeUpdate();
			ps.close();
			rs = ps.executeQuery();
			
			
		} catch (SQLException e) {
			System.out.println("SQLException: ...");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
				
		try {
			if(rs != null && !rs.isClosed()){
				rs.close();
			}
			if(ps != null && !ps.isClosed()){
				ps.close();
			}
			if(conn != null && conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("SQLException: cannot close");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void  getItemsSold(String date){

				
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			try {
				Class.forName(cred.driver).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Cannot load Driver ");
		}
		try {
			conn = DriverManager.getConnection(cred.url, cred.userName, cred.password);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		//itemSoldPerDay
		 String query = " select * from  Sales where date = ?  ";    
		 
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, date);
			rs = ps.executeQuery();
			System.out.print("id\t\t date\t\t sum_total \t quantity\n");
			while(rs.next()){
				System.out.printf("%d \t\t %s \t %d \t\t %d\n", rs.getInt("id"), rs.getString("date"), rs.getInt("sum_total"), rs.getInt("quantity")  );
			}
			System.out.println();
		} catch (SQLException e) {
			System.out.println("SQLException: ...");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
				
		try {
			if(rs != null && !rs.isClosed()){
				rs.close();
			}
			if(ps != null && !ps.isClosed()){
				ps.close();
			}
			if(conn != null && conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("SQLException: cannot close");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//return itemsSold;
	}
	
	
	/*
	  I think this method needs a parameter that refers to a specific product. What do you think? 
	 */
	private int getThreshold(int id){
		int threshold = 0;
		try {
			Class.forName(cred.driver).newInstance();
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Connection con = null;
		
		try {
			con = DriverManager.getConnection(cred.url, cred.userName, cred.password);
		} catch (SQLException e) {
			System.out.println("SQLException: DriverManager.getConnection ... ");
			e.printStackTrace();
		}
		
		String query = "select threshold  from Products where product_id = ?";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next()){
				threshold = rs.getInt("threshold");
			}
			
			System.out.printf(" Threshold of the product (id: %d) is: %d \n", id,  threshold);

		} catch (SQLException e) {
			System.out.println("SQLException:connect preparedStatement ... ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			try{
				if(ps != null && !ps.isClosed())
					ps.close();
				if(con != null && !con.isClosed())
					con.close();
			}catch (SQLException e) {
				System.out.println("SQLException:  cannot close ..." );
				throw new RuntimeException(e);
			}
		}
		
		System.out.println();
		
		
		return threshold;
	}
}
