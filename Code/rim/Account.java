package rim;

import application.Check;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public abstract class Account {

	String id;
	String firstName;
	String lastName;
	String userName;
	static String managerId;
	static Account currentUser;

	public Account(String id, String firstName, String lastName, String userName, String managerId) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.managerId = managerId;
	}

	public abstract String getId();

	public abstract String getFirstName();

	public abstract String getLastName();

	public abstract String getUserName();

	public abstract String getManagerId();

	public static Account getCurrentUser()
	{
		return currentUser;
	}

	public static void setCurrentUser(Account a)
	{
		currentUser = a;
	}


	//method to create a person
	public static List<Account> getAccounts(){

		List<Account> accounts = new ArrayList<Account>();

		accounts.addAll(Employee.getEmployees());
		accounts.addAll(Manager.getManagers());

		return accounts;
	}

	public static Account accountFromUsername(String username)
	{
		String loginUsername = username;
		Account found = null;
		List<Account> accounts = getAccounts();
		for(Account a : accounts){
			String accountUsername = a.getUserName();
			//System.out.println(a.getManagerId());
			if(accountUsername.equals(loginUsername))
			{
				//System.out.println(loginUsername + " " + accountUsername);
				found = a;
				break;

			}
		}
		//System.out.println(found.getId());
		return found;

	}

	public static void changePassword(String id, String password) {

		Connection conn = ConnectionFactory.makeConnection();

		String query = "UPDATE Employees SET password = '" + password + "' WHERE id = \"" + id + "\"";


		//System.out.println(query);

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("Password sucsessfully changed.");
		ConnectionFactory.closeConnection(conn, ps, rs);
	}

	public static void changeUsername(String id, String username) {

		Connection conn = ConnectionFactory.makeConnection();

		String query = "UPDATE Employees SET user_name = '" + username + "' WHERE id = \"" + id + "\"";


		//System.out.println(query);

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("Username sucsessfully changed.");
		ConnectionFactory.closeConnection(conn, ps, rs);
	}


	public static void addAccount(String firstName, String lastName, String username, String password,
			Object managerId) {
		if (!Check.checkExistingUser(username)) {
			Connection conn = ConnectionFactory.makeConnection();

			String query = "INSERT INTO Employees (first_name, last_name, user_name, password, manager_id)" +
				" 		VALUES ( '" + firstName + "','" + lastName + "','" + username + "', '" + password + "'," + managerId+ ")";



			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				ps = conn.prepareStatement(query);
				ps.executeUpdate();

			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			System.out.println("'" + firstName + " " + lastName + "' sucsessfully added to the accounts.");
			ConnectionFactory.closeConnection(conn, ps, rs);

		}
	}
	public static String accountInfo(Account a) {
		String str="";
		String role = "Employee";
		str +="******************************************************************************\n"
				+ "        Account Details: " + a.getFirstName() +"\n"
				+ "******************************************************************************\n";
		str += "First Name: " + a.getFirstName() + "   Last Name: " + a.getLastName()+"\n";

		if(!(a.getManagerId() == null)){
			role = "Manager";
			str+="Username: " + a.getUserName() + "   Manager Id: " + a.getManagerId()+"\n";
		}
		else {
			str+="Username: " + a.getUserName() + "   Employee Id: " + a.getId()+"\n";
		}
		str+="Role :" + role;
		return str;
	}
}



