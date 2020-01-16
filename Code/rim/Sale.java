package rim;



import java.sql.Connection;

public class Sale {
	private String saleId;
	private String date;
	private String productId;
	private String productName;
	private double totalSale;
	private double salesTax;
	private int quantity;
	
	public Sale (String saleId, String date, String productId, String productName, double totalSale, double salesTax, int quantity) {
		this.saleId = saleId;
		this.date = date;
		this.productId = productId;
		this.productName = productName;
		this.totalSale = totalSale;
		this.salesTax = salesTax;
		this.quantity = quantity;
	}

	public String getSaleId() {
		return saleId;
	}

	public String getDate() {
		return date;
	}

	public String getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public double getTotalSale() {
		return totalSale;
	}

	public double getSalesTax() {
		return salesTax;
	}

	public int getQuantity() {
		return quantity;
	}

	
	
	
	
	
}

