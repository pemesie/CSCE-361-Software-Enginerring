package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import rim.Cart;
import rim.CartItem;
import rim.Product;
import rim
.Cart;public class InventoryViewController implements Initializable {
	
	@FXML private TableView inventoryTable;
	@FXML private TableColumn nameColumn;
	@FXML private TableColumn priceColumn;
	@FXML private TableColumn stockColumn;
	@FXML private TextArea textArea;
	@FXML private Label detailsLabel;
	@FXML private ListView cartList;
	@FXML private Label cartLabel;
	@FXML private TextField searchField;
	
	private double priceSum = 0;
	// list of items that were checked out, this is called in the ReceiptController
	private List<Product> checkoutCart = new ArrayList<Product>();
	
	//private Cart theCart = new Cart();
	public List<Product> getCheckoutCart() {
		return checkoutCart;
	}
	
	ObservableList os = FXCollections.observableArrayList();
	FilteredList filter = new FilteredList(os, e->true);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		priceSum=0;
		List<Product> products = Product.getProducts();
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		for(Product p : products) {
			inventoryTable.getItems().add(p);
			os.add((Product) p);

		}
		cartList.getItems().clear();
		for(CartItem item:Cart.getCart()) {
			cartList.getItems().add(item.getP().getName() +"("+item.getSellingQuantity()+")"+ " - $" + String.format("%.2f", item.getP().getPrice()*item.getSellingQuantity()));
			this.priceSum += item.getP().getPrice()*item.getSellingQuantity();
		}
		this.cartLabel.setText(String.format("Cart Total: $%.2f", priceSum));
		
		inventoryTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		cartList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	
	/**
	 * filters search bar
	 * @param event
	 */
	public void search(KeyEvent event) {
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filter.setPredicate((Predicate<Product>) (Product product)->{
			
			if (newValue.isEmpty() || newValue == null) {
				return true;
			} else if (product.getName().toUpperCase().contains(newValue.toUpperCase())) {
				return true;
			}
			return false;
			
			});
		});
		
		SortedList sort = new SortedList(filter);
		sort.comparatorProperty().bind(inventoryTable.comparatorProperty());
		inventoryTable.setItems(sort);
	}
	
	
	/**
	 * display details in textArea
	 */
	public void detailsButtonPressed() {
		Product p = (Product) inventoryTable.getSelectionModel().getSelectedItem();
		this.detailsLabel.setText("Item: " + p.getName());
		this.textArea.setText(p.formatItemDetails());
	}
	
	
	public void addToCartButtonPressed() {
		this.priceSum=0;
		cartList.getItems().clear();
		Product p = (Product) inventoryTable.getSelectionModel().getSelectedItem();
		Cart.addToCart(p, 1);
		// add to diplay list
		for(CartItem item:Cart.getCart()) {
			cartList.getItems().add(item.getP().getName() +"("+item.getSellingQuantity()+")"+ " - $" + String.format("%.2f", item.getP().getPrice()*item.getSellingQuantity()));
			System.out.println(item);
			this.priceSum += item.getP().getPrice()*item.getSellingQuantity();
		}
		this.cartLabel.setText(String.format("Cart Total: $%.2f", priceSum));
	}
	
	public void removeFromCartButtonPressed() {
		//Product p = findProduct((String) cartList.getSelectionModel().getSelectedItem());
		int productQuantity = Cart.getCart().get(cartList.getSelectionModel().getSelectedIndex()).getSellingQuantity();
		Product p = Cart.getCart().get(cartList.getSelectionModel().getSelectedIndex()).getP();
		
		Cart.removeFromCart(p.getName());
		cartList.getItems().remove(cartList.getSelectionModel().getSelectedIndex());
		this.priceSum -= (p.getPrice() * productQuantity);
		if (this.priceSum < 0.00) {
			this.priceSum = 0.00;
		}
		this.cartLabel.setText(String.format("Cart Total: $%.2f", priceSum));
	}
	
	public void clearCartButtonPressed() {
		cartList.getItems().clear();
		Cart.clearCart();
		this.priceSum = 0;
		this.cartLabel.setText(String.format("Cart Total: $%.2f", priceSum));
	}
	
	public void checkOutButtonPressed(ActionEvent event) throws IOException {
		// add check out items to list
		ObservableList os = cartList.getItems();
		for(int i = 0; i < os.size(); i++) {
			String temp = (String) os.get(i);
			temp = temp.substring(0, temp.indexOf("-") - 1);
//			Cart.addToCart(findProduct(temp), 1);
			checkoutCart.add(findProduct(temp));
		}
		Parent loginViewParent = FXMLLoader.load(getClass().getResource("Receipt.fxml"));
		Scene loginViewScene = new Scene(loginViewParent);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(loginViewScene);
		window.show();
		
	}
	
	public Product findProduct(String productName) {
		List<Product> products = Product.getProducts();
		for (Product p : products) {
			if (p.getName().equals(productName)) {
				return p;
			}
		}
		return null;
	}
	// configure list area
	
	/**
	 * return to login page
	 */
	public void returnToLogin(ActionEvent event) throws IOException{
		Parent loginViewParent = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
		Scene loginViewScene = new Scene(loginViewParent, 480, 320);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(loginViewScene);
		window.show();
		
	}
	
	public void toAccount(ActionEvent event) throws IOException{
		Parent managerViewParent = FXMLLoader.load(getClass().getResource("managerView.fxml"));
		Scene managerScene = new Scene(managerViewParent, 1000, 600);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(managerScene);
		window.setResizable(false);
		window.show();
	}
	public void toStatistic(ActionEvent event) throws IOException{
		Parent statisticsViewParent = FXMLLoader.load(getClass().getResource("StatisticsView.fxml"));
		Scene statisticsViewScene = new Scene(statisticsViewParent, 1000, 600);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(statisticsViewScene);
		window.show();
	}
	
	/**
	 * go to edit view
	 */
	public void editModePressed(ActionEvent event) throws IOException{
		Parent editViewParent = FXMLLoader.load(getClass().getResource("EditView.fxml"));
		Scene editViewScene = new Scene(editViewParent,1000, 600);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(editViewScene);
		window.show();
	}
	
}