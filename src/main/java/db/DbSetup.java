package db;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.sqlite.SQLiteDataSource;


/*
 * Generator class for a new clean DB.
 * New specialists can only be added though this class directly.
 * 
 * Possible visitation times are set 15mins apart from 9:00 to 16:45.
 * If company policy is different, admins can easily change that.
 * 
 * Database is initialized as blueprint for current day, with all visitation times set to FREE. 
 */
public class DbSetup {
	
	private final SQLiteDataSource ds = new SQLiteDataSource();
	private String[] specNames = {"Amy", "Betty", "Edward", "Emma", "Gary"};//, "John", "Kevin", "Mary", "Richard", "Sarah"};
	private String[] specLast = {"Jones", "Smith", "Miller", "Moore", "Clark"};//, "Carter", "Reed", "Watson", "Myers", "Foster"};
	private String[] times = generateTimes(LocalTime.of(9, 0), LocalTime.of(17, 0), 15);
	public enum TimeStatus  {FREE, OCCUPIED, ONGOING, SERVICED, RESERVED};
	private enum Type {SPECIALIST, ADMIN};
	private static final String mockPasswords = "12345";
	private static final String mockAdmin = "54321";
	//private String currentDir = System.getProperty("catalina.base")+File.separator;

	public static void main(String[] args) throws SQLException{
		
		DbSetup db = new DbSetup("src/main/java/app/reg.db");
		db.initializeNewDb();
		db.populateDayTables(LocalDate.now());
		
		System.out.println("Database has been created");
	}
	private DbSetup(String name) {
		init(name);
	}
	
	private void init(String name) {
		this.getSource().setUrl("jdbc:sqlite:"+name);
		//"wtpwebapps/AssignmentQWeb/WEB-INF/classes/app/"
	}
	private final void initializeNewDb() {
		
		this.createEmptyTables();
		byte[] saltBytes = new byte[8];
		SecureRandom random = new SecureRandom();
		String salt="";
		String hashedPw = "";
		
		for(int i=0; i<specNames.length; i++) {
			random.nextBytes(saltBytes);
			salt = bytesToHexString(saltBytes);
			hashedPw = DigestUtils.sha256Hex(mockPasswords+salt);
			this.addSpecialist("specialists", (i+1),Type.SPECIALIST, specNames[i], specLast[i], specNames[i],  salt, hashedPw);
		}
		/*also add "admin" account */
			random.nextBytes(saltBytes);
			salt = bytesToHexString(saltBytes);
			System.out.println("Admin salt is: "+salt);
			hashedPw = DigestUtils.sha256Hex(mockAdmin+salt);
			System.out.println("So salted pw is: "+(mockAdmin+salt));
			this.addSpecialist("specialists", 99, Type.ADMIN, "", "", "admin", salt, hashedPw);
		
		this.prepareRegNumTable();
		
	}
	private void createEmptyTables() {
		
		try(Connection con = this.getSource().getConnection()){
			
			con.setAutoCommit(false);
		
			try{
				PreparedStatement pstmt = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS SPECIALISTS( "
					+ "ID INTEGER PRIMARY KEY, "
					+ "type TEXT, "
					+ "name TEXT, "
					+ "last_name TEXT, "
					+ "username TEXT, "
					+ "salt TEXT, "
					+ "hashed_password TEXT); ");
					
					pstmt.executeUpdate();
					con.commit();
					pstmt.close();
					
					
			}catch(SQLException e) {
				System.err.println("Could not createTable in db #createEmptyTables()");
				e.printStackTrace();
			}
					
			try{
				PreparedStatement pstmt2 = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS LOG_SERVICED_CUSTOMERS( "
					+ "specialist_id INTEGER, "
					+ "cust_name TEXT, "
					+ "cust_last_name TEXT, "
					+ "visit_date DATE, "
					+ "visit_time TIME); ");
					
					pstmt2.executeUpdate();
					con.commit();
					pstmt2.close();
					
			}catch(SQLException e) {
				System.err.println("Could not create table LOG_SERVICED_CUSTOMERS in db #createEmptyTables()");
				e.printStackTrace();
			}
			
			try{
				PreparedStatement pstmt3 = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS LOG_ISSUED_CODES( "
					+ "date DATE, "
					+ "current_code TEXT, "
					+ "current_spawn_postfix TEXT);");
					
					pstmt3.executeUpdate();
					con.commit();
					pstmt3.close();
					
			}catch(SQLException e) {
				System.err.println("Could not create table LOG_ISSUED_CODES in db #createEmptyTables()");
				e.printStackTrace();
			}
			
			try{
				PreparedStatement pstmt4 = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS REGISTRATION_NUMBERS( "
					+ "date DATE, "
					+ "current_code TEXT, "
					+ "current_spawn_postfix TEXT);");
					
					pstmt4.executeUpdate();
					con.commit();
					pstmt4.close();
					
			}catch(SQLException e) {
				System.err.println("Could not create table REGISTRATION_NUMBERS in db #createEmptyTables()");
				e.printStackTrace();
			}
		
		}catch(SQLException e) {
			System.err.println("Error creating some table");
			e.printStackTrace();
		}			
			
	}
	private void prepareRegNumTable() {
		
		String table = "REGISTRATION_NUMBERS";
		LocalDate date = LocalDate.now();
		
		try(Connection con = ds.getConnection()){
			con.setAutoCommit(false);	
			
			try(PreparedStatement pstmt = con.prepareStatement("INSERT INTO "+ table +
	            " VALUES (?, ?, ?) ")){
				
				
				java.sql.Date sqlDate = java.sql.Date.valueOf(date);
				
				pstmt.setDate(1, sqlDate);
				pstmt.setString(2, "none");
				pstmt.setString(3, "none");
			
				
				pstmt.executeUpdate();
				con.commit();
				pstmt.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}		
			
				
		}catch(SQLException e) {
			e.printStackTrace();
		}	
		
	}
	private SQLiteDataSource getSource() {
		return this.ds;
	}
	private void addSpecialist(String tableName, int ID, Type type, String name, String lastName, String username, String salt, String hashedPw){
	
		PreparedStatement pstmt = null;
		String table = this.getTableName(tableName);
		
		try(Connection con = ds.getConnection()){
				con.setAutoCommit(false);
				
			pstmt = con.prepareStatement("INSERT INTO "+ table +
            " VALUES (?, ?, ?, ?, ?, ?, ?); ");
			pstmt.setInt(1, ID);
			pstmt.setString(2, type.toString());
			pstmt.setString(3, name);
			pstmt.setString(4, lastName);
			pstmt.setString(5, username);
			pstmt.setString(6, salt);
			pstmt.setString(7, hashedPw);
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
		}catch(SQLException e) {
			System.err.println("Could not insert the statement into db in #addSpecialist");
		}
		
		//also create daily customer data for this specialist (except admin)
		if(type != DbSetup.Type.ADMIN) {
			this.createCustomerTableBlueprintForSpecialistID(ID);
		}
		
			
	}
	private String getTableName(String name) {
		return name.toUpperCase();
	}
	private void createCustomerTableBlueprintForSpecialistID(int id) {
		
		String ID = this.to2DigitString(id);
		
		try(Connection con = this.getSource().getConnection()){
			
			con.setAutoCommit(false);
		System.out.println("TIMETABLE_"+ID);
			try{
				PreparedStatement pstmt = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS TIMETABLE_"+ID+"( "
					+ "visit_time TIME PRIMARY KEY,"
					+ "name TEXT, "
					+ "last_name TEXT, "
					+ "res_code TEXT, "
					+ "status TEXT, "
					+ "registration_date DATE"
					+ ");");
					
					pstmt.executeUpdate();
					con.commit();
					pstmt.close();
					
					
			}catch(SQLException e) {
				System.err.println("Could not create visit table for specialistID="+id);
				e.printStackTrace();
			}
		}catch(SQLException e) {
			System.err.println("Could not createTable in db");
			e.printStackTrace();
		}
	}
	private void populateDayTables(LocalDate date) {
		
		int length = specNames.length;
		for(int i=0; i<length; i++) {
			this.insertPlaceholdersToTimeTable(i+1, date);
		}
	}
	private void insertPlaceholdersToTimeTable(int specialistID, LocalDate forDate) {
		
		if(forDate == null) {
			forDate = LocalDate.now();	
		}
		System.out.println(forDate);
		String table = "TIMETABLE_"+this.to2DigitString(specialistID);
		LocalTime time;

		try(Connection con = ds.getConnection()){
			con.setAutoCommit(false);	
			
			for(int i=0; i<times.length; i++) {
				try(PreparedStatement pstmt = con.prepareStatement("INSERT INTO "+ table +
	            " VALUES (?, ?, ?, ?, ?, ?) ")){
				
				time = LocalTime.parse(times[i]);
				java.sql.Date sqlDate = java.sql.Date.valueOf(forDate);
				java.sql.Time sqlTime = java.sql.Time.valueOf(time);
				
				pstmt.setTime(1, sqlTime);
				pstmt.setString(2, "");
				pstmt.setString(3, "");
				pstmt.setString(4, "");
				pstmt.setString(5, TimeStatus.FREE.toString());
				pstmt.setDate(6, sqlDate);
				pstmt.executeUpdate();
				con.commit();
				pstmt.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}		
			}
				
		}catch(SQLException e) {
			e.printStackTrace();
		}	
			
	}
	private String to2DigitString(int input) {
		
		String ID = "";
		if(input<10) {
			ID = "0"+input;
		}else {
			ID = ""+input;
		}	
		return ID;	
	}
	static String bytesToHexString(byte[] hexbytes) {		
		StringBuilder builder = new StringBuilder();
		for(byte a: hexbytes) {
			int i = a&0xFF;		//apply mask so result is always positive
			if(i<16) { 			//not <= !!!
				builder.append("0"); builder.append(Integer.toHexString(i)); //always want 2 symbols
			}else {
				builder.append(Integer.toHexString(i));
			}
		}	
			return builder.toString();
	}
	/*
	 * Generate timetables based on start/end of a shift and appointment's length
	 */
	public static final String[] generateTimes(LocalTime dayStart, LocalTime dayEnd, int spacingInMins) {
		
		String[] times;
		int count;
		if(dayStart == null || dayEnd == null || spacingInMins <1 || dayStart.isAfter(dayEnd)) {
			//default to predetermined values
			dayStart = LocalTime.of(9, 0);
			dayEnd = LocalTime.of(17, 0);
			spacingInMins = 15;
		}
		
		LocalTime temp = dayStart;
		LocalTime target = dayEnd.minusMinutes(spacingInMins);
		count = 0;
		while(!temp.isAfter(target)) {
			temp = temp.plusMinutes(spacingInMins);
			count++;
		}
		temp = dayStart; //reset
		times = new String[count];
		for(int i=0; i<count; i++) {
			times[i] = temp.toString();
			temp = temp.plusMinutes(spacingInMins);
		}
			
		
		//System.out.println("Times generated are [length="+count+"]: "+System.getProperty("line.separator")+Arrays.deepToString(times));
	
		return Arrays.copyOf(times, times.length);
	}

}

