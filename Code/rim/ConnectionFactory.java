package rim;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//factory class used to store databaseinfo, establish connections, and close connections
public class ConnectionFactory {

	//database login info
	protected  String driver =  "com.mysql.jdbc.Driver";
	protected static String url = "jdbc:mysql://cse.unl.edu/gnchouwat";
	protected static String userName = "gnchouwat";
	protected static String password = "g8CWga";

	//factory method to establish a connection to the database
	public static Connection makeConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(ConnectionFactory.url, ConnectionFactory.userName, ConnectionFactory.password);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return conn;
	}
	
	//factory method to close a connection with the database
	public static void closeConnection(Connection conn, PreparedStatement ps, ResultSet rs){
		try {
			if(rs != null && !rs.isClosed())
				rs.close();
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
