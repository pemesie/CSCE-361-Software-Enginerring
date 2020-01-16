package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rim.Cart;
import rim.Product;

public class EditViewController implements Initializable{

	@FXML private ListView thresholdList;
	@FXML private ListView listView;
	@FXML private TextField productName;
	@FXML private TextField type;
	@FXML private TextField supplier;
	@FXML private TextField price;
	@FXML private TextField quantity;
	@FXML private TextField threshold;
	@FXML private TextArea description;
	@FXML private Label label;
	private Boolean updateMode = false;
	private Boolean addMode = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setInventoryList();
		this.setThresholdList();
	}
	
	/**
	 * return to login page
	 */
	public void returnToLogin(ActionEvent event) throws IOException{
		Parent loginViewParent = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
		Scene loginViewScene = new Scene(loginViewParent);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(loginViewScene);
		window.show();
		
	}
	
	/**
	 * remove item from inventory
	 */
	public void removeButtonPressed() {
		Product p = findProduct((String) listView.getSelectionModel().getSelectedItem());
		listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
		Product.removeProduct(p);
		this.setThresholdList();
		this.clearTextFields();
		label.setText("Update Inventory");
	}

	public void newButtonPressed() {
		this.clearTextFields();
		this.addMode = true;
		this.updateMode = false;
		label.setText("Create New Item");
	}
	
	/**
	 * Save edits made, whether it be adding or updating an item
	 * @throws IOException 
	 */
	public void saveButtonPressed(ActionEvent event) throws IOException {
		String pName = "";
		if(Product.nameExists(productName.getText()) && addMode) {
			triggerPopup(event, "The name you inputted already exists");
			return;
		} else {
			pName = this.productName.getText();
		}
		String pType = type.getText();
		String pSup = supplier.getText();
		double pPrice = 0;
		int pQuan = 0;
		int pThre = 0;
		String pDes = description.getText();
		try {
			pPrice = Double.valueOf(this.price.getText());
			pQuan = Integer.valueOf(this.quantity.getText());
			pThre = Integer.valueOf(this.threshold.getText());
		} catch(NumberFormatException nfe) {
			triggerPopup(event, null);
			return;
		}

		
		if (this.addMode) {
			Product.addProduct(pName,pType,pDes,pQuan,pThre,pPrice,pSup);
			listView.getItems().add(pName);
		} else if (this.updateMode) {
			Product product = findProduct((String) listView.getSelectionModel().getSelectedItem());
			Product.updatePrice(product, pPrice);
			Product.updateQuantity(product.getId(), pQuan);
			Product.updateDescription(product, pDes);
			Product.updateName(product, pName);
			Product.updateSupplier(product, pSup);
			Product.updateThreshold(product, pThre);
			Product.updateType(product, pType);
		}
		// clear text fields
		this.clearTextFields();
		// default label
		label.setText("Update Inventory");
		// refresh threshold list
		this.setThresholdList();
		// refresh list view
		this.setInventoryList();
	}
	
	public void returnToInventory(ActionEvent event) throws IOException {
		Parent inventoryViewParent = FXMLLoader.load(getClass().getResource("InventoryView.fxml"));
		Scene inventoryViewScene = new Scene(inventoryViewParent,1000, 600);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(inventoryViewScene);
		window.show();
		Cart.clearCart();
	}
	
	/**
	 * Update Item Button, this method sets the mode but does not actually update the item
	 */
	public void updateButtonPressed() {
		label.setText("Update Item: " + listView.getSelectionModel().getSelectedItem());
		
		Product p = findProduct((String) listView.getSelectionModel().getSelectedItem());
		productName.setText(p.getName());
		//System.out.println("Name: " +p.getName());
		type.setText(p.getType());
		//System.out.println("Type: " + p.getType());
		supplier.setText(p.getSupplier());
		//System.out.println("Supplier: " + p.getSupplier());
		quantity.setText("" + p.getQuantity());
		//System.out.println("Price: " + p.getPrice());
		price.setText("" + p.getPrice());
		//System.out.println("Description: " + p.getDescription());
		description.setText(p.getDescription());
		//System.out.println("Threshold: " + p.getThreshold());
		threshold.setText("" + p.getThreshold());
		
		this.updateMode = true;
		this.addMode = false;
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
	
	public void clearTextFields() {
		productName.setText("");
		type.setText("");
		supplier.setText("");
		price.setText("");
		quantity.setText("");
		threshold.setText("");
		description.setText("");
	}
	
	private void setThresholdList() {
		this.thresholdList.getItems().clear();
		List<Product> thresholdItems = Product.getThresholdProducts();
		for (Product p : thresholdItems) {
			thresholdList.getItems().add(p.getName());
		}
	}
	
	private void setInventoryList() {
		this.listView.getItems().clear();
		List<Product> products = Product.getProducts();
		for(Product p : products) {
			listView.getItems().add(p.getName());
		}
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	public  void triggerPopup(ActionEvent event, String message) throws IOException {
		
		// set popup message
		FXMLLoader Loader = new FXMLLoader();
		Loader.setLocation(getClass().getResource("InvalidInputPopup.fxml"));
		try {
			Loader.load();
		} catch (IOException ex) {
			Logger.getLogger(managerViewController.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("Nope");
		}

		InvalidInputPopupController popupController = Loader.getController();
		popupController.setMessage(message);
		
		// link to popup screne
		Parent invalidCreds = FXMLLoader.load(getClass().getResource("InvalidInputPopup.fxml"));
		Scene invalidCredScene = new Scene(invalidCreds,400, 200);
		
		Stage window = new Stage();
		
		window.setScene(invalidCredScene);		
		window.setResizable(false);
		window.showAndWait();
	}
}