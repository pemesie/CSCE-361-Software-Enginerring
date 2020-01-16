package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rim.Account;
import rim.ConnectionFactory;
import rim.LoginView;




public class LoginController implements Initializable {
	
	@FXML private Label credentials;
	@FXML private TextField username;
	@FXML private PasswordField password;
	
	public void loginButtonPushed(ActionEvent event) throws IOException {
		// if valid credentials, go to InventoryView page
		if (LoginView.checkPassword(username.getText(), password.getText())) {
			
			//if user is a manager:
			Account thisAccount = Account.accountFromUsername(username.getText());
			if (thisAccount.getManagerId() != null) {
				Account.setCurrentUser(Account.accountFromUsername(username.getText()));
				Parent inventoryViewParent = FXMLLoader.load(getClass().getResource("InventoryView.fxml"));
				Scene inventoryViewScene = new Scene(inventoryViewParent,1000, 600);
				
				// get the stage information
				Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
				window.setScene(inventoryViewScene);
				window.show();
			} else {
				Account.setCurrentUser(Account.accountFromUsername(username.getText()));
				Parent inventoryViewParent = FXMLLoader.load(getClass().getResource("NonManagerInventoryView.fxml"));
				Scene inventoryViewScene = new Scene(inventoryViewParent,1000, 600);
				
				// get the stage information
				Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
				window.setScene(inventoryViewScene);
				window.show();
			}
		} else {
			triggerPopup(event);
		}
	}
		
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		credentials.setText("");
		
		
	}
	
	
	public void triggerPopup(ActionEvent event) throws IOException {
		Parent invalidCreds = FXMLLoader.load(getClass().getResource("InvalidCredentials.fxml"));
		Scene invalidCredScene = new Scene(invalidCreds,400, 200);
		
		Stage window = new Stage();
		
		window.setScene(invalidCredScene);		
		window.setResizable(false);
		window.showAndWait();
	}
	
	
}