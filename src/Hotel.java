import java.sql.*;
import java.util.Scanner;
public class Hotel {
	
	private final static String url="jdbc:mysql://localhost:3306/hotel_db";
	
	private final static String username= "root";
	
	private final static String password= "system@123";

	public static void main(String[] args) {
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
		Connection connection =DriverManager.getConnection(url, username, password);
		
		while(true) {
			System.out.println();
			System.out.println("Hotel Reservation System");
			Scanner sc = new Scanner(System.in);
		    System.out.println("1. Reserve a room");
		    System.out.println("2. View Reservations");
		    System.out.println("3. Get Room No");
		    System.out.println("4. Update Reservations");
		    System.out.println("5. Delete Reservations");
		    System.out.println("6. Exit");
		    System.out.println("Choose an option");
		    int choice = sc.nextInt();
		    
		    switch(choice) {
		    case 1 :
		    	reserveRoom(connection, sc);
		    	break;
		    case 2 :
		    	viewReservation(connection);
		    	break;
		    	
		    case 3 :
		    	getRoomNo(connection, sc);
		    	break;
		    case 4 :
		    	updateReservation(connection, sc);
		    	break;
		    case 5 :
		    	deleteReservation(connection, sc);
		    	break;
		    case 6 :
		    	exit();
		    	sc.close();
		    	return;
		    	
		    default:
		    	System.out.println("Invalid Choice");
		    }
		}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
			catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
	}
	
	private static void reserveRoom(Connection connection, Scanner sc) {
		System.out.print("Enter guest name: ");
		String guestName = sc.nextLine();
		//System.out.println();
		System.out.print("Enter Room No: ");
		int roomNumber = sc.nextInt();
		//System.out.println();
		System.out.print("Enter Contact Number: ");
		String contactNumber = sc.next();
		
		String sql = "INSERT INTO RESERVATIONS (guest_name, room_number, contact_number)" +
		"VALUES (' "+guestName+" ', ' "+roomNumber+" ', ' "+contactNumber+" ');";
		
		try(Statement statement = connection.createStatement()){
			int affectedRows = statement.executeUpdate(sql);
			
			if(affectedRows>0) {
				System.out.println("Reservation Successfull");
			}else {
				System.out.println("Reservation Unsuccessfull");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void viewReservation(Connection connection) throws SQLException{
		String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM RESERVATIONS;";
		
		try(Statement statement = connection.createStatement();
		    ResultSet resultSet = statement.executeQuery(sql)){
			System.out.println("Current Reservations");
			while(resultSet.next()) {
				System.out.println("----------------------------------------");
				int reservationId = resultSet.getInt("reservation_id");
				System.out.println("Reservation Id: "+reservationId);
				String guestName = resultSet.getString("guest_name");
				System.out.println("Guest Name: "+guestName);
				int roomNumber = resultSet.getInt("room_number");
				System.out.println("Room NUmber: "+roomNumber);
				String contactNumber = resultSet.getString("contact_number");
				System.out.println("Contact Number:"+contactNumber);
				String reservationDate = resultSet.getTimestamp("reservation_date").toString();
				System.out.println("Reservation Date: "+reservationDate);
			}
			System.out.println("-------------------------------------");
		}
	}
	
	private static void getRoomNo(Connection connection, Scanner sc) {
		
		System.out.print("Enter Reservation Id: ");
		int reservationId = sc.nextInt();
		System.out.print("Enter Guest Name: ");
		String guestName = sc.nextLine();
		
		String sql = "SELECT room_number FROM reservations"+
				"WHERE reservatio_id = " + reservationId +
				"AND guest_name = '" + guestName +"';";
		
		try(Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql)){
			if(resultSet.next()) {
				int roomNumber = resultSet.getInt("room_number");
				System.out.println("Room no for reservation id"+ reservationId + "and guest"+ guestName+"is: "+roomNumber);
			}else {
				System.out.println("Reservation not found for given reservation id and guest name");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void updateReservation(Connection connection, Scanner sc) {
		
		System.out.println("Enter reservation id to update: ");
		int reservationId = sc.nextInt();
		sc.nextLine();
		
		if(!reservationExists(connection, reservationId)) {
			System.out.println("Reservation not found");
			return;
		}
		
		System.out.print("Enter new guest name: ");
		String newGuestName = sc.nextLine();
		System.out.print("Enter new room no: ");
		int newRoomNumber = sc.nextInt();
		System.out.print("Enter new contact no: ");
		String newContactNumber = sc.next();
		
		String sql = "UPDATE reservations SET guest_name='"+newGuestName+"', "
        +	"room_number = "+ newRoomNumber +"," + "contact_number = '" + newContactNumber + "' "
        + "WHERE reservation_id = " + reservationId;
		
		try(Statement statement = connection.createStatement()){
			int affectedRows = statement.executeUpdate(sql);
			
			if(affectedRows>0) {
				System.out.println("Reservation Updated Successfully");
			}else {
				System.out.println("Reservation update failed");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	

	private static void deleteReservation(Connection connection, Scanner sc) {
		System.out.println("Enter reservation id to delete: ");
		int reservationId = sc.nextInt();
		
		if(!reservationExists(connection, reservationId)) {
			System.out.println("reservation not found");
			return;
		}
		
		String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;
		
		try(Statement statement = connection.createStatement()) {
			int affectedRows = statement.executeUpdate(sql);
			
			if(affectedRows > 0) {
				System.out.println("Reservation deleted successfully!");
			}else {
				System.out.println("Reservation deleted failed.");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	private static boolean reservationExists(Connection connection, int reservationId) {
		// TODO Auto-generated method stub
		String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;
		
		try(Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql)){
			return resultSet.next();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static void exit() throws InterruptedException {
		System.out.println("Exiting system");
		int i = 5;
		while(i!=0) {
			System.out.print(".");
			Thread.sleep(450);
			i--;
		}
	}
	
}
