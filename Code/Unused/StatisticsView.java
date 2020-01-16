
import rim.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsView {
	List<Sale> sales = new ArrayList<Sale>();
	public StatisticsView(Product p) {
		this.sales = this.getSales(p);
		this.printStatistics(p);
		
	}
	
	private int getTotalQuantity() {
		int sum = 0;
		for(Sale s : this.sales) {
			sum += s.getQuantity();
		}
		return sum;
	}
	
	private double getTotalSale() {
		int sum = 0;
		for(Sale s : this.sales) {
			sum += s.getTotalSale();
		}
		return sum;		
	}
	
	private void printStatistics(Product p) {
		System.out.print("******************************************************************************\n"
				+ "        Product Statistics: " + p.getName() +"\n"
				+ "******************************************************************************\n");
		
		for(Sale s : sales) {
			System.out.printf("Sale id: %s	Date: %s	Product id: %s	Product Name: %s	Quantity Sold: %d	Total Sale: $%f		Sales Tax: $%f\n",
					s.getSaleId(), s.getDate(), s.getProductId(), s.getProductName(), s.getQuantity(), s.getTotalSale(), s.getSalesTax());
		}
		
		System.out.println();
		System.out.println("Total Items Sold: " + this.getTotalQuantity());
		System.out.println("Total Sale Profits: $" + this.getTotalSale());
	}
	
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
}
