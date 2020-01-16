package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import rim.Cart;
import rim.CartItem;
import rim.Product;
import application.InventoryViewController;

public class ReceiptController implements Initializable{

	@FXML TextArea textArea;
	public void initData(List<Product> items) {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String str = "";
		str += Cart.formatDetails();
		if(!Cart.checkOut()) {			
			str = "check out fail.";
		}
		textArea.setText(str);
	}

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
		Cart.clearCart();
		
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
	
	public void exit() {
		System.exit(0);
	}
}
