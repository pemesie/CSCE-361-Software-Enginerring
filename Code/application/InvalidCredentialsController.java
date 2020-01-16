package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InvalidCredentialsController implements Initializable{

	@FXML public Button okButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("hello world");
		
	}
	
	// return to login page
	@FXML
	public void returnToLogin(ActionEvent event) {
	    Stage stage = (Stage) okButton.getScene().getWindow();
	    stage.close();
	}

}
