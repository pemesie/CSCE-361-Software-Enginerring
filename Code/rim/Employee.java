package rim;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Employee extends Account{

	public Employee(String id, String firstName, String lastName, String userName, String managerId) {
		super(id, firstName, lastName, userName, managerId);
		
	}

	public Employee(Employee e) {
		super(e.getId(), e.getFirstName(), e.getLastName(), e.getUserName(), e.getManagerId());
	}

	//method to create a person
	public static List<Employee> getEmployees(){

		Connection conn = ConnectionFactory.makeConnection();

		String query = "SELECT e.id AS id, " +
				" 		e.first_name AS firstName, " +
				"       e.last_name AS lastName, " + 
				"       e.user_name AS userName " +
				"FROM Employees e WHERE manager_id is NULL";
		
		//System.out.println(query);


		List<Employee> employees = new ArrayList<Employee>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String id       = rs.getString("id");
				String firstName       = rs.getString("firstName");
				String lastName   = rs.getString("lastName");
				String userName = rs.getString("userName");

				Employee em = new Employee(id, firstName, lastName, userName, managerId);
				employees.add(em);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.closeConnection(conn, ps, rs);
		return employees;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getManagerId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void removeEmployee(Employee employee) {
		Connection conn = ConnectionFactory.makeConnection();

		String query = "DELETE FROM Employees WHERE id = " + employee.getId();

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
		//System.out.println("'" + p.getName() + "' successfully removed from the inventory.");
		ConnectionFactory.closeConnection(conn, ps, rs);
	}


}
