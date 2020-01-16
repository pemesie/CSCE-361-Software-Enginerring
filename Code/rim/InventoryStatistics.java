package rim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InventoryStatistics {
	
	private List<Product> products = new ArrayList<Product>();
	private String monthNum = null;
	private double totalSales = 0;
	private int totalQuantitySold = 0;
	
	/**
	 * All inventory statistics constructor
	 */
	public InventoryStatistics (List<Product> products) {
		this.products = products;
		this.totalSales = this.calculateTotalSales();
		this.totalQuantitySold = this.calculateQuantitySold();
	}
	
	
	/**
	 * inventory statistics constructor for a specific month
	 */
	public InventoryStatistics (List<Product> products, String month) {
		this.products = products;
		this.monthNum = convertMonth(month);
		this.totalSales = this.calculateTotalSales(month);
		this.totalQuantitySold = this.calculateQuantitySold(month);
	}
	
	
	/**
	 * returns total amount made off sales in using complete history
	 */
	public double calculateTotalSales() {
		int sum = 0;
		for (Product p : this.products) {
			ItemStatistics is = new ItemStatistics(p);
			sum += is.getTotalSale();
		}
		return sum;
	}
	
	/**
	 * returns total amount made off sales in given month
	 */
	public double calculateTotalSales(String month) {
		int sum = 0;
		for (Product p : this.products) {
			ItemStatistics is = new ItemStatistics(p, month);
			sum += is.getTotalSale();
		}
		return sum;
	}
	
	public int calculateQuantitySold() {
		int sum = 0;
		for (Product p : this.products) {
			ItemStatistics is = new ItemStatistics(p);
			sum += is.getTotalQuantity();
		}
		return sum;
	}
	
	public int calculateQuantitySold(String month) {
		int sum = 0;
		for (Product p : this.products) {
			ItemStatistics is = new ItemStatistics(p, month);
			sum += is.getTotalQuantity();
		}
		return sum;
	}
	
	public double getTotalSales() {
		return this.totalSales;
	}
    
	public double getTotalQuantitySold() {
		return totalQuantitySold;
	}


	/**
	 * Coverts name of month to corresponding number from 01 to 12
	 */
	private String convertMonth(String month) {
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
	
	public void printStatistics() {
		System.out.println("Total Products Sold: " + this.totalQuantitySold);
		System.out.printf("Total Sales Made: $%.2f", this.totalSales);
	}
	
	
}
