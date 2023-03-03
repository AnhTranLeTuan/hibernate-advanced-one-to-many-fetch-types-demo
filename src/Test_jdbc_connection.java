import java.sql.DriverManager; 

public class Test_jdbc_connection {
	
	public static void main(String[] args) {
		String url_jdbc = "jdbc:mysql://localhost:3306/one-to-many?useSSL=false&serverTimezone=UTC";
		// Set user and password the same is not the good practice in reality, so I only do this for testing purposes
		String user = "student";
		String password = "student";
		
		try {
			// If Java successfully connect to MySQL, it will be able to print out the message
			// If not, the exception will be thrown, and its stack trace will be printed out instead
			DriverManager.getConnection(url_jdbc, user, password);
			System.out.println("Connected Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
