package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InvalidInputPopupController implements Initializable {
	@FXML public Button okButton;
	@FXML public Text text;
	private String message;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setMessage(message);
	}
	
	// return to login page
	@FXML
	public void okButtonPressed(ActionEvent event) {
	    Stage stage = (Stage) okButton.getScene().getWindow();
	    stage.close();
	}
	
	// if message is null, will display default "the value you have entered is not valid"
	@FXML
	public void setMessage(String message) {
		if (message != null) {
			this.message = message;
		}
		
	}
}