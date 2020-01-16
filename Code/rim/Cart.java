package rim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cart {
	static List<CartItem> cart = new ArrayList<CartItem>();
	static double totalSale = 0;
	static double salesTax;
	
	public static void addToCart(Product p, int quantity) {
		int SellingQuantity = quantity;
		boolean same = false;
		CartItem item = new CartItem(p, SellingQuantity);
		for (CartItem x : cart) {
			if (x.getP().getName().equals(p.getName())) {
				x.setSellingQuantity(x.getSellingQuantity() + quantity);
				same = true;
			}
		}
		if (!same) {
			cart.add(item);
		}
	}

	public static void deleteItem(int index) {
		try {
			cart.remove(index - 1);
		} catch (IndexOutOfBoundsException exception) {
			System.out.println("No such item rank in your cart");
		}

	}

	public static void editItem(int index, int quantity) {
		try {
			cart.get(index - 1).setSellingQuantity(quantity);
		} catch (IndexOutOfBoundsException exception) {
			System.out.println("No such item rank in your cart");
		}
	}

	public static List<CartItem> getCart() {
		return cart;
	}
	public static void removeFromCart(String name) {
		int indexToRemove = -1;
		for (CartItem x : cart) {
			if (x.getP().getName().equals(name)) {
				indexToRemove = cart.indexOf(x);
			}
		}
		cart.remove(indexToRemove);
	}
	public static void clearCart() {
		totalSale = 0;
		salesTax = 0;
		cart.clear();
	}
	public static String formatDetails() {
		String str = "";
		for (CartItem p : cart) {//check items in stock before process to check out
			String price = String.format("%.2f", p.getP().getPrice()*p.getSellingQuantity());
			String tax = "" + p.getP().getPrice()*p.getSellingQuantity() * .07;
			str += String.format("%s: $%s\n", p.getP().getName(), price);
			salesTax += Double.parseDouble(tax);
			totalSale += Double.parseDouble(price);
		}
		str += String.format("%s%s\n", "Tax: ", String.format("$%.2f", salesTax));
		str += String.format("Total: $%.2f", totalSale + salesTax);
		return str;
	}
	public static boolean checkOut() {
		boolean check = true;
		if (cart.isEmpty()) {
			//System.out.println("Your cart is empty");
			return false;
		} else {
			for (CartItem p : cart) {//check items in stock before process to check out
				if (!checkItem(p.getP(), p.getSellingQuantity())) {
					check = false;
					cart.remove(p);
					//System.out.println("Item " + p.getP().getName() + " has been removed from your cart");
					break;
				}
			}
			
			if (check) {
				int saleId=getMaxSaleId()+1;
				for (CartItem p : cart) {
					purchaseProduct(p,Integer.toString(saleId));
				}

//				salesTax = totalSale * .07;
				//System.out.println(saleId);
				//System.out.printf("Tax: %36s%.2f \nTotal: %34s%.2f \n", "$", salesTax, "$", totalSale + salesTax);
				//cart.clear();
				Cart.clearCart();
				return true;
			}
			else{
				return false;
			}
		}
	}

	public static double purchaseProduct(CartItem p, String saleId) {

		String date = Date.getDate();
		String productId = p.getP().getId();
		int quantitySold = p.getSellingQuantity();
		double totalSale = p.getP().getPrice() * quantitySold;
		double salesTax = totalSale * .07;
		String employeeId = Account.getCurrentUser().getId();
		
		insertSaleEntry(saleId,date, productId, quantitySold, totalSale, salesTax, employeeId);
		Product.updateQuantity(productId, -quantitySold);

		//System.out.print(p);
		return totalSale;
	}

	public static boolean checkItem(Product p, int quantityToBuy) {
		int quantity = 0;
		
		Connection conn = ConnectionFactory.makeConnection();
		String query = "SELECT p.quantity AS quantity " + "FROM Products p WHERE id = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setObject(1, p.getId());
			rs = ps.executeQuery();

			if (rs.next()) {
				quantity = Integer.parseInt(rs.getString("quantity"));
			} else {
				quantity = -1;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);

		if (quantity < quantityToBuy && quantity > 0) {
			//System.out.println("Item " + p.getName() + " does not have enough quantity in stock currently");
			return false;
		} else if (quantity < 0) {
			//System.out.println("Item " + p.getName() + " had been removed from stock");
			return false;
		} else {
			return true;
		}
	}
	public static int getMaxSaleId(){
		Connection conn = ConnectionFactory.makeConnection();
		int max=0;
		String query = "SELECT MAX(sale_id)"
				+"FROM Sales";

		PreparedStatement ps = null;
		ResultSet rs = null;

			try {
				ps = conn.prepareStatement(query);
				rs = ps.executeQuery();
				if (rs.next()) {
					max = Integer.parseInt(rs.getString(1));
				}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);
		return max;
	}
	public static void insertSaleEntry(String saleId,String date, String productId, int quantitySold, double totalSale,
			double salesTax, String employeeId) {

		Connection conn = ConnectionFactory.makeConnection();

		String query = "INSERT INTO Sales (sale_id, the_date, product_id, quantity_sold, total_sale, sale_tax, employee_id)"
				+ " 		VALUES ('"  + saleId + "','" + date + "', '" + productId + "','" + quantitySold + "','" + totalSale + "', '"
				+ salesTax + "','" + employeeId + "')";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);
	}
	
}
