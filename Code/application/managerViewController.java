package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import rim.Account;
import rim.Cart;
import rim.Employee;
import rim.Product;
public class managerViewController implements Initializable{
	@FXML private ListView employeeListView;
	@FXML private TextArea accountText;
	@FXML private Label employeeLabel;
	@FXML private TextArea employeeText;

	List<Employee> employees = Employee.getEmployees();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for(Employee e : employees) {
			employeeListView.getItems().add(e.getFirstName()+" "+e.getLastName());
		}
		employeeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		accountText.setText(Account.accountInfo(Account.getCurrentUser()));
	}

	public void inventoryButtonPressed(ActionEvent event) throws IOException{
		Parent inventoryViewParent = FXMLLoader.load(getClass().getResource("InventoryView.fxml"));
		Scene inventoryViewScene = new Scene(inventoryViewParent,1000, 600);
		
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(inventoryViewScene);
		window.show();
	}
	public void logOutPressed(ActionEvent event) throws IOException {
		Parent loginViewParent = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
		Scene loginViewScene = new Scene(loginViewParent, 480, 320);
		
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(loginViewScene);
		window.show();
	}
	public void detailPressed() {
		Employee e = findEmployee((String) employeeListView.getSelectionModel().getSelectedItem());
		this.employeeLabel.setText("Employee: " + e.getFirstName() + " "+e.getLastName());
		this.employeeText.setText(Account.accountInfo(e));
	}
	
	public Employee findEmployee(String username) {
		for(Employee e:employees) {
			if((e.getFirstName()+" "+e.getLastName()).equals(username)) {
				return e;
			}
		}
		return null;
	}

	public void editEmployeePressed() {
		String id = findEmployee((String) employeeListView.getSelectionModel().getSelectedItem()).getId();

		FXMLLoader Loader = new FXMLLoader();
		Loader.setLocation(getClass().getResource("EditEmployeeView.fxml"));
		try {
			Loader.load();
		} catch (IOException ex) {
			Logger.getLogger(managerViewController.class.getName()).log(Level.SEVERE, null, ex);
			
		}

		EditEmployeeController EditEmployeeParent = Loader.getController();
		EditEmployeeParent.setId(id);

		Parent p = Loader.getRoot();
		Scene EditEmployeeViewScene = new Scene(p, 392, 192);
		
		Stage window = new Stage();
		window.setScene(EditEmployeeViewScene);
		window.setResizable(false);
		window.showAndWait();
	}
	public void changePassPressed(ActionEvent event) throws IOException {		
		Parent updatePassParent = FXMLLoader.load(getClass().getResource("updatePass.fxml"));
		Scene updatePassViewScene = new Scene(updatePassParent,390, 122);
		
		Stage window = new Stage();
		
		window.setScene(updatePassViewScene);		
		window.setResizable(false);
		window.showAndWait();

	}
	public void deleteEmployeePressed() {
		Employee e = findEmployee((String) employeeListView.getSelectionModel().getSelectedItem());
		employeeListView.getItems().remove(employeeListView.getSelectionModel().getSelectedIndex());
		Employee.removeEmployee(e);
	}
	public void addEmployeePressed() {
		FXMLLoader Loader = new FXMLLoader();
		Loader.setLocation(getClass().getResource("CreateAccount.fxml"));
		try {
			Loader.load();
		} catch (IOException ex) {
			Logger.getLogger(managerViewController.class.getName()).log(Level.SEVERE, null, ex);
			
		}

		CreateAccountController CreateAccountParent = Loader.getController();

		Parent p = Loader.getRoot();
		Scene CreateAccountViewScene = new Scene(p);
		
		Stage window = new Stage();
		window.setScene(CreateAccountViewScene);
		window.setResizable(false);
		window.showAndWait();
		refreshEmployeeList();
		
	}
	public void refreshEmployeeList() {
		List<Employee> employees = Employee.getEmployees();
		employeeListView.getItems().clear();
		for(Employee e : employees) {
			employeeListView.getItems().add(e.getFirstName()+" "+e.getLastName());
		}
	}
}
