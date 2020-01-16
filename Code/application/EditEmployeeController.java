package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import rim.Account;
import rim.ConnectionFactory;
import rim.Employee;

public class EditEmployeeController implements Initializable {
	@FXML
	private PasswordField passField;
	@FXML
	private TextField newPassField;
	@FXML
	private TextField newNameField;
	@FXML
	private Label credential;

	private String id = "";
	List<Employee> employees = Employee.getEmployees();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		credential.setText("");
	}

	public void updateEmployeePass(ActionEvent event) throws IOException {
		if (checkPassword(Account.getCurrentUser().getUserName(), passField.getText())) {
			if (!newPassField.getText().equals("")) {
				String newPass = newPassField.getText();
				System.out.println(newPass);
				Account.changePassword(id, newPass);
				credential.setText("Password update succeeded!");
			}
			else {
				credential.setText("No password entered.");
			}
		} else {
			credential.setText("Invalid credentials!");
		}
	}

	public void updateEmployeeName(ActionEvent event) throws IOException {
		if (checkPassword(Account.getCurrentUser().getUserName(), passField.getText())) {
			if (!newNameField.getText().equals("")) {
				System.out.println(newNameField.getText());
				String newName = newNameField.getText();
				System.out.println(newName);
				Account.changeUsername(id, newName);
				;
				credential.setText("Username update succeeded!");
			} else {
				credential.setText("No name entered.");
			}
		} else {
			credential.setText("Invalid credentials!");
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	private static boolean checkPassword(String username, String password) {

		String dataPassword = null;
		Connection conn = ConnectionFactory.makeConnection();

		String query = "SELECT e.id AS id, " + " 		e.first_name AS firstName, "
				+ "       e.last_name AS lastName, " + "       e.user_name AS userName, "
				+ "       e.password AS password " + "FROM Employees e WHERE user_name = '" + username + "'";

		// System.out.println(query);

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				dataPassword = rs.getString("password");
				// System.out.println(dataPassword);

			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);
		if (dataPassword == null) {
			return false;
		} else if (dataPassword.equals(password)) {
			return true;
		} else {
			return false;
		}

	}
}
