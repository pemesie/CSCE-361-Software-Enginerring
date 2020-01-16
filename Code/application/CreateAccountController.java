package application;
import rim.Account;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateAccountController implements Initializable{
	
	
	@FXML private AnchorPane anchorPane;
    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML private TextField userName;
  
    @FXML private TextField password;

    @FXML private TextField passwordConfirmation;
    
    @FXML private TextField managerId;
	
    @FXML Label message;
    
    Stage primaryStage;
    
 // Action on "createAccont" button.
 	public void createAccount(ActionEvent event){
 		
 		if((!firstName.getText().equals(""))  && (!lastName.getText().equals("")) && (!userName.equals("")) &&
 				(password.getText().equals(passwordConfirmation.getText())	  )){
 			Account.addAccount(firstName.getText(), lastName.getText(), userName.getText(), password.getText(), managerId.getText());
 			message.setStyle("-fx-text-fill: green; -fx-font-size: 16;");
 			message.setText(" successfully added to the accounts.");
 		}else if( (password.getText().equals("")) ||    !(password.getText().equals(passwordConfirmation.getText()))   ){
 			message.setStyle("-fx-text-fill: red; -fx-font-size: 16;");
 			message.setText("Enter your password correctly" );
 			password.clear();
 			passwordConfirmation.clear();
 		}else{
 			message.setStyle("-fx-text-fill: red; -fx-font-size: 16;");
 			message.setText("Error try again!" );
 			clearField();
 		}
 	}
    //
//    public void close(){
//    	primaryStage.setOnCloseRequest(e ->{
//    		Boolean answer = ConfirmBox.display("Close", "Sure you want to exit";)
//    		if(answer){
//    			primaryStage.close();;
//    		}
//    	});
//    }
 	// set action on clear button
 	public void clearField(){
 		firstName.clear();
 		lastName.clear();
 		password.clear();
 		passwordConfirmation.clear();
 		managerId.clear();
 		userName.clear();
 		
 	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}