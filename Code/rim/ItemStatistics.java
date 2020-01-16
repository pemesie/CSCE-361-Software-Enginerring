package rim;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemStatistics {
	
	private List<Sale> sales = new ArrayList<Sale>();
	private String monthNum = null;
	private double totalSale = 0;
	private double totalQuantity = 0;
	private String productName = null;
	/**
	 * constructor for all sales history statistics
	 * @param p
	 */
	public ItemStatistics(Product p) {
		this.sales = this.getSales(p);
		this.productName = p.getName();
		this.totalSale = this.calculateTotalSale();
		this.totalQuantity = this.calculateTotalQuantity();
	}
	
	/**
	 * constructor for sales statistics pertaining to a specific month
	 * Pass in full name of month
	 */
	public ItemStatistics(Product p, String month) {
		this.monthNum = this.convertMonth(month);
		this.sales = this.getSales(p, monthNum);
		this.productName = p.getName();
		this.totalSale = this.calculateTotalSale();
		this.totalQuantity = this.calculateTotalQuantity();
	}
	
	public int calculateTotalQuantity() {
		int sum = 0;
		for(Sale s : this.sales) {
			sum += s.getQuantity();
		}
		return sum;
	}
	
	public double calculateTotalSale() {
		int sum = 0;
		for(Sale s : this.sales) {
			sum += s.getTotalSale();
		}
		return sum;		
	}
	
	public double getTotalSale() {
		return this.totalSale;
	}

	public double getTotalQuantity() {
		return this.totalQuantity;
	}

	
	public void printStatistics() {
		System.out.print("******************************************************************************\n"
				+ "        Product Statistics: " + this.productName +"\n"
				+ "******************************************************************************\n");
		
		for(Sale s : sales) {
			System.out.printf("Sale id: %s	Date: %s	Quantity Sold: %d	Total Sale: $%f		Sales Tax: $%f\n",
					s.getSaleId(), s.getDate(), s.getQuantity(), s.getTotalSale(), s.getSalesTax());
		}
		
		System.out.println();
		System.out.println("Total Items Sold: " + this.calculateTotalQuantity());
		System.out.println("Total Sale Profits: $" + this.calculateTotalSale());
	}
	
	/**
	 * Get complete sales history of a product
	 */
	
	private List<Sale> getSales (Product product) {
		Connection conn = ConnectionFactory.makeConnection();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Sale> sales = new ArrayList<Sale>();
		
		String query = "SELECT s.id, "
				+ "s.the_date, "
				+ "s.product_id, "
				+ "s.quantity_sold, "
				+ "s.total_sale, "
				+ "s.sale_tax "
				+ "FROM Sales s WHERE s.product_id = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, Integer.parseInt(product.getId()));
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String saleId = rs.getString("s.id");
				String date = rs.getString("s.the_date");
				String productId = rs.getString("s.product_id");
				String name = product.getName();
				double totalSale = rs.getDouble("s.total_sale");
				double tax = rs.getDouble("s.sale_tax");
				int quantity = rs.getInt("s.quantity_sold");
				
				Sale sale = new Sale(saleId, date, productId, name, totalSale, tax, quantity);
				sales.add(sale);
			}
			ConnectionFactory.closeConnection(conn, ps, rs);
			return sales;
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * Get the sale history of an product for a particular month
	 * monthNum is represented as 01-12 corresponding to the month
	 * Operates under the premise that dates are stored as XXXX-XX-XX year-month-day in database 
	 */
	private static List<Sale> getSales (Product product, String monthNum) {
		
		
		Connection conn = ConnectionFactory.makeConnection();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Sale> sales = new ArrayList<Sale>();
		
		String query = "SELECT s.id, "
				+ "s.the_date, "
				+ "s.product_id, "
				+ "s.quantity_sold, "
				+ "s.total_sale, "
				+ "s.sale_tax "
				+ "FROM Sales s WHERE s.product_id = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, Integer.parseInt(product.getId()));
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String saleId = rs.getString("s.id");
				String date = rs.getString("s.the_date");
				String productId = rs.getString("s.product_id");
				String name = product.getName();
				double totalSale = rs.getDouble("s.total_sale");
				double tax = rs.getDouble("s.sale_tax");
				int quantity = rs.getInt("s.quantity_sold");
				
				if(date.contains("-" + monthNum + "-")) {
					Sale sale = new Sale(saleId, date, productId, name, totalSale, tax, quantity);
					sales.add(sale);
				}
			}
			ConnectionFactory.closeConnection(conn, ps, rs);
			return sales;
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
				
	}
	
	public List<Sale> getSales() {
		return sales;
	}

	public String getMonthNum() {
		return monthNum;
	}

	public String getProductName() {
		return productName;
	}

	/**
	 * Coverts name of month to corresponding number from 01 to 12
	 */
	private static String convertMonth(String month) {
		switch (month.toUpperCase()) {
			case "JANUARY" : return "01";
			case "FEBRURARY" : return "02";
			case "MARCH" : return "03";
			case "APRIL" : return "04";
			case "MAY" : return "05";
			case "JUNE" : return "06";
			case "JULY" : return "07";
			case "AUGUST" : return "08";
			case "SEPTEMPER" : return "09";
			case "OCTOBER" : return "10";
			case "NOVEMBER" : return "11";
			case "DECEMBER" : return "12";
		}
		return "00";
	}
}
