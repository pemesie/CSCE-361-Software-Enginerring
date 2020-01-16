package rim;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Product {

	String id;
	String name;
	String type;
	String description;
	int quantity;
	int threshold;
	double price;
	String supplier;

	public Product(String id, String name, String type, String description , int quantity, int threshold, double price, String supplier) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.quantity = quantity;
		this.threshold = threshold;
		this.price = price;
		this.supplier = supplier;

	}

	public Product(Product p) {
		this.id = p.getId();
		this.name = p.getName();
		this.type = p.getType();
		this.description = p.getDescription();
		this.quantity = p.getQuantity();
		this.threshold = p.getThreshold();
		this.price = p.getPrice();
		this.supplier = p.getSupplier();

	}
	
	/**
	 * intended for GUI
	 */
	public String formatItemDetails() {
		return String.format("Type: %s\nSystem Identifier: %s\nPrice: $%.2f\nQuantity: %s\nQuantity Threshold: %s\n"
				+ "Description: %s\nSupplier: %s", this.getType(), this.getId(), this.getPrice(), this.getQuantity(), this.getThreshold(),
				this.getDescription(), this.getSupplier());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<Product> getProducts(){


		Connection conn = ConnectionFactory.makeConnection();

		String query = "SELECT p.id AS id, " +
				" 		p.name AS name, " +
				" 		p.type AS type, " +
				"       p.description AS description, " + 
				"       p.quantity AS quantity, " +
				"       p.threshold AS threshold, " +
				"       p.price AS price, " +
				"       p.supplier AS supplier " +
				"FROM Products p";

		//System.out.println(query);


		List<Product> products = new ArrayList<Product>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String id       = rs.getString("id");
				String name       = rs.getString("name");
				String type       = rs.getString("type");
				String description   = rs.getString("description");
				int quantity = Integer.parseInt(rs.getString("quantity"));
				int threshold = Integer.parseInt(rs.getString("threshold"));
				double price = Double.parseDouble(rs.getString("price"));
				String supplier = rs.getString("supplier");

				Product p = new Product(id, name, type, description, quantity, threshold, price, supplier);
				products.add(p);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);
		return products;
	}
	public static List<Product> getThresholdProducts(){


		Connection conn = ConnectionFactory.makeConnection();

		String query = "SELECT p.id AS id, " +
				" 		p.name AS name, " +
				" 		p.type AS type, " +
				"       p.description AS description, " + 
				"       p.quantity AS quantity, " +
				"       p.threshold AS threshold, " +
				"       p.price AS price, " +
				"       p.supplier AS supplier " +
				"FROM Products p";

		//System.out.println(query);


		List<Product> products = new ArrayList<Product>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String id       = rs.getString("id");
				String name       = rs.getString("name");
				String type       = rs.getString("type");
				String description   = rs.getString("description");
				int quantity = Integer.parseInt(rs.getString("quantity"));
				int threshold = Integer.parseInt(rs.getString("threshold"));
				double price = Double.parseDouble(rs.getString("price"));
				String supplier = rs.getString("supplier");

				if(quantity < threshold)
				{
				Product p = new Product(id, name, type, description, quantity, threshold, price, supplier);
				products.add(p);
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);
		return products;
	}
	// TODO: Should change quantity instead of sum the quantity with the input
	//number should be pass as positive or negative as need be
	public static void updateQuantity(String id, int number){
		//System.out.println(number);

		Connection conn = ConnectionFactory.makeConnection();

		String query = "UPDATE Products SET quantity = (" + number + ") WHERE id = \"" + id + "\"";


		//System.out.println(query);

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			if(number > 0)
			{
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);
	}
	
	public static void updateDescription(Product p, String str) {

			Connection conn = ConnectionFactory.makeConnection();

			String query = "UPDATE Products SET description = ? WHERE id = ?";


			//System.out.println(query);

			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, str);
				ps.setInt(2, Integer.parseInt(p.getId()));
				ps.executeUpdate();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			ConnectionFactory.closeConnection(conn, ps, rs);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int theshold) {
		this.threshold = theshold;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public static void addProduct(String name, String type, String description , int quantity, int threshold, double price, String supplier) {
		Connection conn = ConnectionFactory.makeConnection();
		name = name.replace("'", "\'");
		type = type.replace("'", "\'");
		description = description.replace("'", "\'");
		supplier = supplier.replace("'", "\'");
		String query = "INSERT INTO Products (name, type, description, quantity, threshold, price,  supplier)" +
				" 		VALUES ( '" + name + "','" + type + "','" + description + "', '" + quantity + "','" + threshold + "'" + ",'" + price + "'" + ",'" + supplier + "')";

		//System.out.println(query);


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
		//System.out.println("'" + name + "' sucsessfully added to the inventory.");
		ConnectionFactory.closeConnection(conn, ps, rs);


	}

	public static void removeProduct(Product p) {
		Connection conn = ConnectionFactory.makeConnection();

		String query = "DELETE FROM Products WHERE id = " + p.getId();

		//System.out.println(query);


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
		//System.out.println("'" + p.getName() + "' successfully removed from the inventory.");
		ConnectionFactory.closeConnection(conn, ps, rs);
		
	}

	public static List<Product> sortName(List<Product> products) {
		for(int i = 0; i < products.size(); i++){
			int index = i;
			for(int j = i +1; j < products.size(); j++){
				String productName1 = products.get(index).getName();
				String productName2 = products.get(j).getName();

				if(productName1.compareToIgnoreCase(productName2) > 0){
					index = j;
				}

				Collections.swap(products, index, i);

			}
		}
		return products;
		
	}
	
	public static List<Product> sortType(List<Product> products) {
		for(int i = 0; i < products.size(); i++){
			int index = i;
			for(int j = i +1; j < products.size(); j++){
				String productType1 = products.get(index).getType();
				String productType2 = products.get(j).getType();

				if(productType1.compareToIgnoreCase(productType2) > 0){
					index = j;
				}

				Collections.swap(products, index, i);

			}
		}
		return products;
		
	}
	
	public static List<Product> sortSupplier(List<Product> products) {
		for(int i = 0; i < products.size(); i++){
			int index = i;
			for(int j = i +1; j < products.size(); j++){
				String productSupplier1 = products.get(index).getSupplier();
				String productSupplier2 = products.get(j).getSupplier();

				if(productSupplier1.compareToIgnoreCase(productSupplier2) > 0){
					index = j;
				}

				Collections.swap(products, index, i);

			}
		}
		return products;
		
	}
	
	public static List<Product> sortPrice(List<Product> products) {
		for(int i = 0; i < products.size(); i++){
			int index = i;
			for(int j = i +1; j < products.size(); j++){
				double productPrice1 = products.get(index).getPrice();
				double productPrice2 = products.get(j).getPrice();

				if(productPrice1 > (productPrice2)){
					index = j;
				}

				Collections.swap(products, index, i);

			}
		}
		return products;
		
	}
	
	public static List<Product> sortQuantity(List<Product> products) {
		for(int i = 0; i < products.size(); i++){
			int index = i;
			for(int j = i +1; j < products.size(); j++){
				double productQuantity1 = products.get(index).getQuantity();
				double productQuantity2 = products.get(j).getQuantity();

				if(productQuantity1 > (productQuantity2)){
					index = j;
				}

				Collections.swap(products, index, i);

			}
		}
		return products;
		
	}
	
	public static Product findProduct(String productName) {
		List<Product> products = Product.getProducts();
		for (Product p : products) {
			if (p.getName().equals(productName)) {
				return p;
			}
		}
		return null;
	}

	public static void updatePrice(Product p, double newPrice) {
		Connection conn = ConnectionFactory.makeConnection();

		String query = "UPDATE Products SET price = " + newPrice + " WHERE id = \"" + p.getId() + "\"";


		//System.out.println(query);

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
		System.out.println("'" + p.getName() + "' sucsessfully repriced in the inventory.");
		ConnectionFactory.closeConnection(conn, ps, rs);
		
	}
	
	public static void updateType(Product p, String newType) {
		Connection conn = ConnectionFactory.makeConnection();

		String query = "UPDATE Products SET type = \"" + newType + "\" WHERE id = \"" + p.getId() + "\"";

		//System.out.println(query);

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
		System.out.println("'" + p.getName() + "' successfully retyped in the inventory.");
		ConnectionFactory.closeConnection(conn, ps, rs);
	}
	
	public static void updateName(Product p, String newName) {
		Connection conn = ConnectionFactory.makeConnection();

		String query = "UPDATE Products SET name = \"" + newName + "\" WHERE id = \"" + p.getId() + "\"";

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
		System.out.println("'" + p.getName() + "' successfully renamed in the inventory.");
		ConnectionFactory.closeConnection(conn, ps, rs);
	}
	
	public static void updateSupplier(Product p, String newSupplier) {
		Connection conn = ConnectionFactory.makeConnection();

		String query = "UPDATE Products SET supplier = \"" + newSupplier + "\" WHERE id = \"" + p.getId() + "\"";

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
		System.out.println("'" + p.getName() + "' successfully updated supplier in the inventory.");
		ConnectionFactory.closeConnection(conn, ps, rs);
	}
	
	public static void updateThreshold(Product p, int newThreshold) {
		Connection conn = ConnectionFactory.makeConnection();

		String query = "UPDATE Products SET threshold = \"" + newThreshold + "\" WHERE id = \"" + p.getId() + "\"";

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
		System.out.println("'" + p.getName() + "' successfully updated threshold in the inventory.");
		ConnectionFactory.closeConnection(conn, ps, rs);
	}
	public static boolean nameExists(String name) {
		for (Product p : Product.getProducts()) {
			if(name.equalsIgnoreCase(p.getName())) {
				return true;
			}
		}
		return false;
	}
}