package rim;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;;

public class LoginView {

	public static void login(){
		System.out.print("******************************************************************************\n"
				+ "        Login Screen\n"
				+ "******************************************************************************\n");
		
		System.out.print("Username: ");

		Scanner s = new Scanner(System.in);
		String username = s.nextLine();

		//		Console console = System.console();
		//		String tempPass = new String(console.readPassword("Password: "));
		System.out.print("Password: ");
		String password = s.nextLine();
		while(!checkPassword(username, password))
		{
			System.out.println("\nIncorrect username or password");
			System.out.print("Username: ");
			username = s.nextLine();

			System.out.print("Password: ");
			password = s.nextLine();
		}
		
		Account.setCurrentUser(Account.accountFromUsername(username));
		
		System.out.print("******************************************************************************\n"
				+ "        Welcome to Retail Inventory Management System\n"
				+ "******************************************************************************\n");
//				+ "Commands:\n"
//				+ "quit logout help\n");
		InventoryView.displayProducts(0);
	}

	public static boolean checkPassword(String username, String password) {
		if (username.length() < 1 || password.length() < 1) {
			return false;
		}
		String input = password;
		String dataPassword = null;
		Connection conn = ConnectionFactory.makeConnection();

		String query = "SELECT e.id AS id, " +
				" 		e.first_name AS firstName, " +
				"       e.last_name AS lastName, " + 
				"       e.user_name AS userName, " +
				"       e.password AS password " +
				"FROM Employees e WHERE user_name = '" + username + "'";

		
		//System.out.println(query);


		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				dataPassword       = rs.getString("password");
				//System.out.println(dataPassword);

			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);
		if(dataPassword == null)
		{
			return false;
		}
		else if(dataPassword.equals(password))
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}

