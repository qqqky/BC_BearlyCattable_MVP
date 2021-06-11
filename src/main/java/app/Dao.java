package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sqlite.SQLiteDataSource;

import db.DbSetup;
import dto.Customer;
import dto.Response;
import dto.ResponseCustList;
import dto.ResponseReset;
import dto.ResponseWaitTime;
import dto.TimeSlotIDPair;

/**
 * 
 * 
 * 
 * Reminders:
 * //Dir for original AWS deploy path (for Amazon Linux AMI):
	//catalina.base/ + "/usr/local/tomcat8/webapps/";
	
   //Dir for deploy path for Amazon Linux 2:
	//catalina.base/ + "/var/app/current/";
	 
	Name/LastName lists:
	{"Amy", "Betty", "Edward", "Emma", "Gary", "John", "Kevin", "Mary", "Richard", "Sarah"};
	{"Jones", "Smith", "Miller", "Moore", "Clark", "Carter", "Reed", "Watson", "Myers", "Foster"};
	
	Names of LOG tables:
	LOG_SERVICED_CUSTOMERS
	LOG_ISSUED_CODES
 
 * @author qqqky
 *
 */

public class Dao {
	
	private final SQLiteDataSource ds = new SQLiteDataSource();
	public enum TimeStatus  {FREE, RESERVED, ONGOING, CANCELLED, SERVICED, NONE};
	public enum ObjectStatus {ADMIN, CUSTOMER, SPECIALIST, NONE};
	private static final int ALLOWED_ERROR_MARGIN = 10;
	private final LocalTime startOfDay = LocalTime.of(9, 0);
	private String[] specNames = {"Amy", "Betty", "Edward", "Emma", "Gary"};// "John", "Kevin", "Mary", "Richard", "Sarah"};
	private String[] specLast = {"Jones", "Smith", "Miller", "Moore", "Clark"};//, "Carter", "Reed", "Watson", "Myers", "Foster"};
	private String[] times = DbSetup.generateTimes(startOfDay, LocalTime.of(17, 0), 15);
	private Map<Integer, List<String>> internalTimeMap = new HashMap<>();
	private SecureRandom rand = new SecureRandom();
	private List<Integer> randomizerHelper;
	private LocalTime current = startOfDay;
	private LocalDate syncDate = LocalDate.now();
	private static boolean canServeCustomers = true; //not fully implemented
	private int specialistCount;
	private static final String currentDir = System.getProperty("catalina.base")+File.separator;
	private final ZoneId zone = ZoneId.of("Europe/Vilnius");
	
	private final ObjectStatus currentStatus;

	public static void main(String[] args) throws IOException{
		
	}
	private Dao() {
		currentStatus = ObjectStatus.NONE;
		//this is for AWS
		ds.setUrl("jdbc:sqlite:"+currentDir+"wtpwebapps/BearlyCattableMVP/WEB-INF/classes/app/reg.db");
	}
	private Dao(LocalDate date) {
		currentStatus = ObjectStatus.ADMIN;	
		
		List<Path> dbPath = PathRetriever.retrievePath(System.getProperty("catalina.base"), "reg.db");
		boolean bound = false;
		
		System.out.println("PathRetriever yielded the following results ["+dbPath.size()+"]: "+System.getProperty("line.separator")+dbPath);
		for(int i=0; i<dbPath.size(); i++) {
			if(bound) break;
			else {
				try{
					ds.setUrl("jdbc:sqlite:"+dbPath.get(i).toString());
					specialistCount = this.getSpecialistCount();
					System.out.println("DB has been bound. Location: "+ds.getUrl());
					bound = true;
				}catch(Exception e) {
					bound = false;
					System.err.println("Db binding unsuccessful for item at location: "+System.getProperty("line.separator")+dbPath.get(i));
				}
			}
		}
		
		//upon creation, check if db is not outdated. If it is - save important info and reset to LocalDate.now()
		if(this.isOutdated()) {
			this.saveDailyCodeTable();
			this.reset();
		}
		this.initializeEmptyMap();
		
		//this is for AWS
		//...+"webapps/ROOT/WEB-INF/classes/app/reg.db" //must be named ROOT
	
	}
	public static Dao create(LocalDate date) {
		return new Dao(date);
	}
	
	/*
	 * Unused. toString() for table SPECIALISTS
	 */
	@SuppressWarnings("unused")
	private void printSpecialistsTable() {
		String table = "SPECIALISTS";
		
		try(Connection con = ds.getConnection()){
			
			con.setAutoCommit(false);
			
			try(PreparedStatement pstmt = con.prepareStatement(
					"SELECT * FROM "+table)){
			
				
				ResultSet results = pstmt.executeQuery();
			
				while(results.next()) {
					int id = results.getInt("ID");
					String name = results.getString("name");
					String lastName = results.getString("last_name");
					//String pass = results.getString("password");
					String username = results.getString("username");
					
					System.out.println("ID: "+id+"\tname: "+name+"\tlastName: "+lastName+"\tusername: "+username);
					
				}
				con.commit();
				results.close();
			}catch(SQLException e) {
				System.err.println("Could not retrieve full table view");
				e.printStackTrace();
			}
		}catch(SQLException e) {
			System.err.println("Could not retrieve full table view");
			e.printStackTrace();
		}
	}
	/**
	 * Unused. toString() for tables TIMETABLE_X
	 * @param numSpecialist - specialist ID
	 */
	void printTimeTable(int numSpecialist) {
		
		String table = "TIMETABLE_"+this.to2DigitString(numSpecialist);
		
		try(Connection con = ds.getConnection()){
			
			con.setAutoCommit(false);
		
			try(PreparedStatement pstmt = con.prepareStatement(
					"SELECT * FROM "+table)){
			
				ResultSet results = pstmt.executeQuery();
			
				System.out.println(table+"(specialist: "+specNames[numSpecialist-1]+" "+specLast[numSpecialist-1]+" - teller id "+numSpecialist+")");
				
				while(results.next()) {
					LocalTime time = results.getTime("visit_time").toLocalTime();
					String name = results.getString("name");
					String lastName = results.getString("last_name");
					String resCode = results.getString("res_code");
					String status = results.getString("status");
					LocalDate date = results.getDate("registration_date").toLocalDate();
					
					System.out.println("visit_time: "+time+"\tname: "+name+"\tlastName: "+lastName+"\tcode: "+resCode+"\tstatus: "+status+"\tdate:"+date);
					
				}
				con.commit();
				results.close();
			}catch(SQLException e) {
				System.err.println("Error retrieving time table");
				e.printStackTrace();
			}
		}catch(SQLException e) {
			System.err.println("Could not retrieve full time table view");
			e.printStackTrace();
		}
	}
	String to2DigitString(int input) {
		
		if(input>=100 || input < 0) {
			throw new UnsupportedOperationException("This value is not supported");
		}
		String ID = "";
		if(input<10) {
			ID = "0"+input;
		}else {
			ID = ""+input;
		}
		
		return ID;
		
	}
	private String to3DigitString(int input) {
		
		if(input>=1000 || input < 0) {
			throw new UnsupportedOperationException("This value is not supported");
		}
		
		String code = "";
		if(input<10) {
			code = "00"+input;
		}else if(input<100) {
			code = "0"+input;
		}else {
			code = ""+input;
		}
		
		return code;
		
	}
	final String registerCustomer(String name, String lastName) {
		
		String reservationCode = "";
		
		//first, check if office is opened
		if(current.isBefore(startOfDay.minusMinutes(ALLOWED_ERROR_MARGIN))) {
			return "Out of office working hours. Registrations start "+ALLOWED_ERROR_MARGIN+" mins before the opening time.";
		}
		
		//now check if a customer is not already registered (still has a valid appointment time)
		String checkMaybeExists = this.getIfReservedExists(name, lastName);
		System.out.println("registerCustomer() determined if a reservation already exists. Answer: "+checkMaybeExists);
		if(checkMaybeExists != null && !checkMaybeExists.equals("")) {
			String[] temp = checkMaybeExists.split(",");
			String time = temp[0];
			String code = temp[1];
			return "You already have a reservation today. Your code is "+code+", your time is "+time+".";
		}	
		
		/*
		 * Now we determine the closest available time for the customer:
		 * 1. Remove unavailable times and choose closest slot
		 * 2. Make sure status of that time is FREE (otherwise eliminate the time slot and try again)
		 */
		
		
		//1. remove all currently unavailable times from internalTimeMap
		this.updateFilteredTimes();
		TimeSlotIDPair bestChoice = null;
		
		//2. do "choose and check if FREE"
		while(bestChoice == null) {
			bestChoice = this.findNearestTimeSlot();
			if(bestChoice.isEmpty()) {
				reservationCode = "no more free space today";
				Dao.canServeCustomers = false;
			}else{
				TimeStatus status = this.getCurrentCustomerStatus(bestChoice.getID(), LocalTime.parse(bestChoice.getTimeSlot()));
				
				//proceed with registration only if slot is actually FREE
				if(status == TimeStatus.FREE) {
					reservationCode = this.insertCustomerToTimeTable(bestChoice.getID(), bestChoice.getTimeSlot(), name, lastName);	
				}else {
					//remove that time from map, reset and loop again
					internalTimeMap.get(bestChoice.getID()).remove(bestChoice.getTimeSlot());
					bestChoice = null;
				}
			}	
		}
		
		return reservationCode;
	}
	
	final boolean changeCustomerStatus(String visitTime, TimeStatus newStatus, int specialistID) {
			
		String[] IDtimes = this.getSupportedTimesByID(specialistID);
		System.out.println(Arrays.deepToString(IDtimes));

		boolean check = Arrays.stream(IDtimes).anyMatch(time -> time.equals(visitTime));
		LocalTime time = null;
		
		System.out.println("Change customer status executed with the following arguments: "+visitTime+ " "+newStatus+" "+specialistID);
			
		if(!check) {
			System.err.println("Time "+visitTime+" not found");
			return false;
		}
		else {
			time = LocalTime.parse(visitTime);
		}
		boolean result = false;
			
		//check current status before changing
		TimeStatus status = newStatus;
		TimeStatus currentStatus = this.getCurrentCustomerStatus(specialistID, time);
		
		/*
		 * Do not allow changing the NONE status and make sure this method will never try
		 * to change status to RESERVED (must use the registration method instead)
		 */
		if(currentStatus == TimeStatus.NONE || newStatus == TimeStatus.RESERVED) { //don't deal with status RESERVED
			return false;
		}
		if(status == currentStatus) {
			return true;
		}
		
		if(status == TimeStatus.FREE) {
			/*
			 * Can only change to FREE status if that time hasnt't passed yet.
			 * Cannot change status to free if status is SERVICED or ONGOING (because we assume a customer
			 * IS BEING or HAS BEEN serviced - so it is likely a misclick).
			 * We can, however, change any status to CANCELLED (no restrictions).
			 */
			if(time.isBefore(current.minusMinutes(ALLOWED_ERROR_MARGIN))) {
				System.err.println("Cannot change status to FREE. That time has already passed");
				return false;
			}
			if(currentStatus == TimeStatus.SERVICED || currentStatus == TimeStatus.ONGOING || currentStatus == TimeStatus.RESERVED) {
				System.err.println("Cannot change "+currentStatus.toString()+" status to FREE. Not allowed");
				return false;
			}
		}
			
		/*
		 * If specialist wants to mark the visit as ongoing - allow some range (+-10min)
		 * (maybe the customer got there early or was too late)
		 */
//!!! testing checks
		System.out.println("Status booleans will be checked, current server time is: "+current);
		System.out.println("is timestatus really ongoing?: "+ (status==TimeStatus.ONGOING));
		System.out.println("Time is parsed correctly? - it is now: "+time);
		System.out.println("Is time NOT before current.minus10minutes? "+ (!time.isBefore(current.minusMinutes(ALLOWED_ERROR_MARGIN))));
		System.out.println("Is time NOT after current.plus10minutes? "+ (!time.isAfter(current.plusMinutes(ALLOWED_ERROR_MARGIN))));

		//strict rules for changing status to ONGOING
		if(status == TimeStatus.ONGOING) {
			/*
			 * Firstly, only RESERVED or FREE times can be marked as ONGOING (FREE is allowed here to make
			 * the rules more lax - eg. a customer comes unregistered, sees free specialists and asks
			 * for appointment.
			 * Note that specialists themselves cannot mark any visit as RESERVED - not allowed
			 */
			if(!(currentStatus == TimeStatus.FREE || currentStatus == TimeStatus.RESERVED)) {
				System.out.println("Status is not FREE or RESERVED. Cannot make it ONGOING");
				return false;
			}
			/*
			 * A specialist can only have 1 ongoing visit at once.
			 * Regardless, to mark it as ONGOING, time cannot be too early or too late (+-10mins).
			 */
			if(time.isBefore(current.minusMinutes(ALLOWED_ERROR_MARGIN)) || time.isAfter(current.plusMinutes(ALLOWED_ERROR_MARGIN))
					|| this.ongoingVisitExists(specialistID)){
				return false;
			}
		}
		/*
		 * Can only changed to SERVICED if status was ONGOING
		 * (this one doesn't need any time restriction)
		 */
		if(status == TimeStatus.SERVICED) {
			if(currentStatus != TimeStatus.ONGOING) {
				return false;
			}
		}
		
		/*
		 * If all checks passed - change the status. 
		 * We must still give special treatment for one special case - if RESERVED status was CANCELLED - the associated
		 * reservation code must be removed
		 */
		
		String query = "";
		if(currentStatus == TimeStatus.RESERVED && newStatus == TimeStatus.CANCELLED) { //if 
			query = "UPDATE TIMETABLE_"+this.to2DigitString(specialistID)
			+ " SET status = ?, res_code = ? "
			+ " WHERE visit_time = ?";
			
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(query)){
						
				con.setAutoCommit(false);
						
				pstmt.setString(1, newStatus.toString());
				pstmt.setString(2, "");
				pstmt.setTime(3, java.sql.Time.valueOf(time));
					
				pstmt.executeUpdate();
				con.commit();
				result = true;
						
			}catch(SQLException e) {
				System.err.println("Error changing customer status to CANCELLED for specialist [ID="+specialistID+"]");
				e.printStackTrace();
				result = false;
			}
		}else {
			query = "UPDATE TIMETABLE_"+this.to2DigitString(specialistID)
		+ " SET status = ? "
		+ " WHERE visit_time = ?";
			
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(query)){
						
				con.setAutoCommit(false);
						
				pstmt.setString(1, newStatus.toString());
				pstmt.setTime(2, java.sql.Time.valueOf(time));
					
				pstmt.executeUpdate();
				con.commit();
				result = true;
						
			}catch(SQLException e) {
				System.err.println("Error changing customer status for specialist [ID="+specialistID+"]");
				e.printStackTrace();
				result = false;
			}			
		}
			
		
		/*
		 * Post-processing.
		 * If status was changed to FREE (but not to CANCELLED) successfully - allocate space in internalTimeMap 
		 * so new customers can be registered.
		 * If status was changed to SERVICED successfully - add customer data to CUSTOMERS table.
		 */
		if(result && status == TimeStatus.FREE) {	
			Dao.canServeCustomers = true;
			this.internalTimeMap.get(specialistID).add(time.toString());
			System.out.println("Time slot freed up at time "+time+" [specialist ID="+specialistID+"]");
		}
		
		if(result && status == TimeStatus.SERVICED) {		
			this.addCustomerToLogTable(visitTime, specialistID);
		}
		
		return result;
	}
	/*
	 * Adds a customer to table LOG_SERVICED_CUSTOMERS (not the daily visit table) if they
	 * have been serviced successfully
	 * Eg. as a future reference (log) for company customer data/ recurring customers
	 */
	private final void addCustomerToLogTable(String visitTime, int specialistID) {
			
			
		LocalTime time = LocalTime.parse(visitTime);
		LocalTime vTime = null;
		String name = null;
		String lastName = null;
		LocalDate vDate = null;
			
			
		String table = "TIMETABLE_"+this.to2DigitString(specialistID);
			
		//1. fetch visitation info based on visitation time
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
			"SELECT visit_time, name, last_name, registration_date FROM "+table
			+" WHERE visit_time = ?")){
				
			con.setAutoCommit(false);
			pstmt.setTime(1, java.sql.Time.valueOf(time));
			ResultSet results = pstmt.executeQuery();
				
			//should only match a single line
			if(results.next()) {
				vTime = results.getTime(1).toLocalTime();
				name = results.getString(2);
				lastName = results.getString(3);
				vDate = results.getDate(4).toLocalDate();
			}
			con.commit();
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
			
		table = "LOG_SERVICED_CUSTOMERS";
		//2. Add customer to customer table
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
			"INSERT INTO "+table +" VALUES (?, ?, ?, ?, ?);")){
			
			con.setAutoCommit(false);
			pstmt.setInt(1, specialistID);
			pstmt.setString(2, name);
			pstmt.setString(3, lastName);
			pstmt.setDate(4, java.sql.Date.valueOf(vDate));
			pstmt.setTime(5, java.sql.Time.valueOf(vTime));
				
			pstmt.executeUpdate();
			con.commit();
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
			
	}
	private final void sortTimes() {
		int length = internalTimeMap.size();
		
		for(int i=0; i<length; i++) {
			Collections.sort(internalTimeMap.get(i+1));
		}
	}
	
	private final void initializeEmptyMap() {
		
		if(internalTimeMap != null) internalTimeMap.clear();
		
		for(String id: this.getAllSpecialistIDs().split(",")) {
			String[] listBasedOnCurrentTime = this.filterAvailableTimes(Integer.parseInt(id)).toArray(new String[0]);
			internalTimeMap.put(Integer.parseInt(id), new ArrayList<String>());
			for(int j=0; j<listBasedOnCurrentTime.length;j++) {
				internalTimeMap.get(Integer.parseInt(id)).add(listBasedOnCurrentTime[j]);
			}
		}
		
	}
	private final TimeSlotIDPair findNearestTimeSlot() {
		
		this.sortTimes();
		String bestTime = "zzzz";
		int bestID = -1;
		String currentTime = "";
		
		/*
		 * First, find lowest time slot
		 */
		
		for(int item: internalTimeMap.keySet()) {
			if(!internalTimeMap.get(item).isEmpty()) {	//do not engage if empty already
				currentTime = internalTimeMap.get(item).get(0);	//but if not, then only look at first index
					if(currentTime!=null) {
						if(currentTime.compareTo(bestTime)<=0) {
							bestTime = currentTime;	//only interested in this for now
							bestID = item; //
						}						
					}
				}
			}	
		
		/*
		 * We found nearest empty time slot, but multiple specialists can have that,
		 * so to make it fair, need to randomize which specialist is chosen (eg.: as all of them are free at first)
		 */
		String item = "";
		if(bestID == -1) { //bestID == -1 only if all lists are empty (no more free space)
			return TimeSlotIDPair.empty();
		}
		else {
			if(randomizerHelper == null) {
				randomizerHelper = new ArrayList<>();
			}
			//now filter in all specialists that have same best times
			for(int number : internalTimeMap.keySet()) {
				if(!internalTimeMap.get(number).isEmpty()) {	//avoid ones that are empty
					item = internalTimeMap.get(number).get(0);
					if(item !=null && item.equals(bestTime)) {
						randomizerHelper.add(number);	//will represent all specialists (IDs) that have closest time slot available
					}
				}
			}
		
		/*
		 * Once we have the list - choose the specialist randomly
		 * (if reached here, randomizerHelper should never be empty)
		 */
			int bound = randomizerHelper.size();
		
			int rng = rand.nextInt(bound);
			int chosenSpecialist = randomizerHelper.get(rng); //id
			randomizerHelper.clear(); //cleanup helper
		
			internalTimeMap.get(chosenSpecialist).remove(bestTime); //remove from available times
			
			
			return new TimeSlotIDPair(chosenSpecialist, bestTime);
			
		}		
	}
	/*
	 * Only allow reservation codes (0)0-99
	 */
	private final synchronized String getNextReservationCodeString() {
		
		
		String table = "REGISTRATION_NUMBERS";
		LocalDate date = LocalDate.now();
		String currentCode = "";
		String newCode = "";
				
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT current_code FROM "+table
					+ " WHERE date = ?")){
			
			con.setAutoCommit(false);
			
			pstmt.setDate(1, java.sql.Date.valueOf(date));
			
			ResultSet results = pstmt.executeQuery();
			if(results.next()) {
				currentCode = results.getString("current_code");
			}	
			con.commit();
			results.close();
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		if(currentCode.equals("none") || currentCode.equals("999")) {
			newCode = this.to3DigitString(0);
		}else {
			int current = Integer.parseInt(currentCode);
				current++;
			newCode = this.to3DigitString(current);
		}
		
		/* now update 'last issued code' in db */
		
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("UPDATE "+table
						+ " SET current_code = ?"
						+ " WHERE date = ?")){
				con.setAutoCommit(false);
				
				pstmt.setString(1, newCode);
				pstmt.setDate(2, java.sql.Date.valueOf(date));
				
				
				pstmt.executeUpdate();
				con.commit();
			
			}catch(SQLException e) {
				e.printStackTrace();
			}
		
		System.out.println("ReservationCode issued was: "+newCode+". It should reflect in DB");
		
		return newCode;
	}
	private String insertCustomerToTimeTable(int specialistID, String timeSlot, String name, String lastName) {
		
		System.out.println("insertCustomerToTimeTable() is being executed for specialist ID="+specialistID);
		LocalTime time = LocalTime.parse(timeSlot);
			if(time.isBefore(current.minusMinutes(ALLOWED_ERROR_MARGIN))) {
			return "Cannot add customer. The time has passed already";
		}
		
		String table = "TIMETABLE_"+this.to2DigitString(specialistID);
		String reservationCode = this.getNextReservationCodeString();
		
		System.out.println("Will now add customer with status RESERVED at time "+timeSlot+" (specialist ID="+specialistID+")");
				
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement("UPDATE "+table
					+ " SET name = ? , last_name = ? , res_code = ? , status = ?"
					+ " WHERE visit_time = ?")){
			con.setAutoCommit(false);
			
			pstmt.setString(1, name);
			pstmt.setString(2, lastName);
			pstmt.setString(3, reservationCode);
			pstmt.setString(4, TimeStatus.RESERVED.toString());
			pstmt.setTime(5, java.sql.Time.valueOf(time));
			
			pstmt.executeUpdate();
			con.commit();
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return reservationCode;
	}
	/**
	 * Make sure only future times are offered
	 * @return List<String> - only times that are past or equal to the current time
	 */
	private final List<String> filterAvailableTimes(int specialistID) {
		
		LocalTime currentTime = LocalTime.now(zone).plusMinutes(ALLOWED_ERROR_MARGIN);
		String[] times = this.getSupportedTimesByID(specialistID);		
		List<String> filteredTimes = Arrays.stream(times).filter(str -> str.compareTo(currentTime.toString())>=0).collect(Collectors.toList());
		
		//System.out.println("FilteredTimes [ID="+specialistID+"]: \r\n"+filteredTimes);
		
		return new ArrayList<>(filteredTimes);
	}
	/**
	 * Helper method, opposite of the one above
	 * @return List<String> - only times that are before current time
	 */
	private final List<String> filterUnavailableTimes(int specialistID) {
		
		LocalTime currentTime = LocalTime.now(zone).minusMinutes(ALLOWED_ERROR_MARGIN);
		String[] times = this.getSupportedTimesByID(specialistID);	
		List<String> filteredTimes = Arrays.stream(times).filter(str -> str.compareTo(currentTime.toString())<0).collect(Collectors.toList());
		
		return new ArrayList<>(filteredTimes);
	}
	
	/*
	 * Do not offer unavailable times (eg. after a customer cancelled)
	 */
	public void updateFilteredTimes() {
		
		String[] ids = this.getAllSpecialistIDs().split(",");
		List<String> unavailableTimes = new ArrayList<>();
		
		for(String id: ids) {
			unavailableTimes = this.filterUnavailableTimes(Integer.parseInt(id));
			for(String unavailable: unavailableTimes) {
				for(int i=0; i<internalTimeMap.keySet().size(); i++) {
					if(internalTimeMap.get(Integer.parseInt(id)).contains(unavailable)) {
						internalTimeMap.get(Integer.parseInt(id)).remove(unavailable);
					}
				}
			}
		}
		 
		
	}
	
	final boolean getCanServe() {
		return canServeCustomers;
	}
	Response timeLeftUntilVisit(String code, String lastName) {
		
		LocalTime time = current;
		LocalTime visitTime = null;
		String answer = "";
		
		String[] ids = this.getAllSpecialistIDs().split(",");
		
		//no info about specialist ID is retained, so we must search all tables
		for(String spec: ids) {
			String table = "TIMETABLE_"+spec;
		
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
				"SELECT visit_time FROM "+table+
				" WHERE last_name = ? AND res_code = ? AND status = ?")){
			
				con.setAutoCommit(false);
				pstmt.setString(1, lastName);
				pstmt.setString(2, code);
				pstmt.setString(3, TimeStatus.RESERVED.toString());
				
				ResultSet results = pstmt.executeQuery();
			
				if(results.next()) {
					visitTime = results.getTime("visit_time").toLocalTime();
				}	
				con.commit();
				results.close();
			}catch(SQLException e) {
				System.err.println("Error finding visit time in #timeLeftUntilVisit()");
				e.printStackTrace();
			}
		}
		
		if(visitTime == null) {	//if time not found
			return new ResponseWaitTime("No such entry found");
		}
		
		int result = visitTime.toSecondOfDay() - time.toSecondOfDay();
			
	/*
	 * Evaluate the result, give some margin for customers
	 */
		if(result <-300) {
			answer = "Sorry, your visit has already passed";
		}else if(result <0) {
			answer = "Your visit is ongoing. Hurry up";	
		}else {
			int minutes = result/60;
			answer = "Waiting time is "+minutes+" minutes";
		}
		
		return new ResponseWaitTime(answer);
	}
	
	/*
	 * Eliminates all RESERVED items that are in the past (changes status to NONE)
	 */
	private final synchronized void discardIllegalReservedValues(int specialistID) {
		
		List<String> unavailableList = this.filterUnavailableTimes(specialistID);
		for(String item: unavailableList) {
			if(this.getCurrentCustomerStatus(specialistID, current) == TimeStatus.RESERVED) {
				this.changeCustomerStatus(item, TimeStatus.NONE, specialistID);
			}
		}
	}
	/**
	 * Retrieves today's full schedule (customer list) for the specified specialist ID
	 * @return CustListResponse - custom DTO for convenient toString()
	 */
	ResponseCustList retrieveCustomerList(int numSpecialist) {

		if(numSpecialist <0) return null;
		
		//first, we get rid of all RESERVED values that are in the past and change them to NONE.
		this.discardIllegalReservedValues(numSpecialist);
		
		String table = "TIMETABLE_"+this.to2DigitString(numSpecialist);
		
		//for more convenient response format
		ResponseCustList response = new ResponseCustList();
		Customer[] partialData = new Customer[this.getSupportedTimesByID(numSpecialist).length];
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT * FROM "+table)){
			
			
				con.setAutoCommit(false);
			
			ResultSet results = pstmt.executeQuery();
			
			int count = 0;
		while(results.next()) {
			LocalTime time = results.getTime("visit_time").toLocalTime();
			//System.out.println("Retrieved time: "+time+"["+(++count)+"]");
			String name = results.getString("name");
			String lastName = results.getString("last_name");
			String resCode = results.getString("res_code");
			String status = results.getString("status");
			//System.out.println("Retrieved status: "+status+"["+(count)+"]");
			LocalDate date = results.getDate("registration_date").toLocalDate();
			
			partialData[count] = new Customer(time, name, lastName, resCode, status, date);
			count++;
		}		
			con.commit();
			results.close();
		
			//response.add();
						
		}catch(SQLException e) {
			System.err.println(e.getMessage());
				System.err.println("Error retrieving time table");
				e.printStackTrace();
		}
		
		/*
		 * Before returning, remove all invalid customers (and set their status to NONE):
		 * 1. Invalid time slots are ones in the past and having STATUS of either RESERVED or FREE.
		 * 		(ONGOING status is a special case and it will be available to be marked
		 * 		SERVICED without any constraints.
		 */
		
		for(int i=0; i<partialData.length; i++) {
			LocalTime time = partialData[i].getTime();
			Customer potentialReplacement = null;
			TimeStatus status = TimeStatus.valueOf(partialData[i].getStatus());
			
			if(time.isBefore(current.minusMinutes(ALLOWED_ERROR_MARGIN))
					&& (status == TimeStatus.FREE || status == TimeStatus.RESERVED)) {
				potentialReplacement = this.forceChange(partialData[i], TimeStatus.NONE, numSpecialist);
				partialData[i] = potentialReplacement;
			}
		}
		Arrays.stream(partialData).sequential().forEachOrdered(response::add);
		
		return response;
	}
	
	/*
	 * Method to check if customer table is outdated (not LocalDate.now())
	 */
	final boolean isOutdated() {
		return !this.getLastSyncDate().isEqual(LocalDate.now());
	}
	/*
	 * Internal helper method to retrieve the date of last synchronization
	 */
	private final synchronized LocalDate getLastSyncDate() {
		
		String[] ids = this.getAllSpecialistIDs().split(",");
		String table = "";
		LocalDate current = LocalDate.now();
		LocalDate old = null;
		boolean isSync = true;
		
		MAIN: for(String item: ids) {
			
			//do not loop if already found a single older date
			if(old != null && !old.isEqual(current)) {
				break MAIN;
			}
			table = "TIMETABLE_"+item;
			try(Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement(
							"SELECT registration_date FROM "+table+";")){
				con.setAutoCommit(false);
				
				ResultSet result = pstmt.executeQuery();
				while(result.next()) {
					old = result.getDate("registration_date").toLocalDate();
					if(!old.isEqual(current)) {
						isSync = false;
						break;
					}
				}
				result.close();
				con.commit();
			}catch(SQLException e) {
				System.err.println(e.getMessage());
				System.err.println("Error retrieving dates");
				e.printStackTrace();
			}
		
		}
		return isSync? LocalDate.now() : old;	
		
	}
	final String retrieveSpecialistNameByID(int id) {
		
		System.out.println("retrieveSpecialistNameByID called. ID="+id);
		String table = "SPECIALISTS";
		String answer = "";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT name FROM "+table
					+" WHERE ID = ?")){
			
				con.setAutoCommit(false);
				pstmt.setInt(1, id);
				ResultSet result = pstmt.executeQuery();
				
				if(result.next()) {
					answer = result.getString("name");
				}
				result.close();
				con.commit();
		}catch(SQLException e) {
			System.err.println(e.getMessage());
				System.err.println("Error retrieving specialist name");
				e.printStackTrace();
		}
		return answer;
		
	}
	/**
	 * Visit time can be either marked CANCELLED or FREE, based on current time
	 * @param code
	 * @param lastName
	 */
	boolean findAndCancelVisitByCustomerName(String code, String lastName) {
		
		LocalTime time = current;
		LocalTime visitTime = null;
		int tableNum = -1;
		boolean result = false;

		//no info about specialist ID is retained, so we must search all tables
		for(int i=0; i<this.getSpecialistCount(); i++) {
			tableNum = i+1;
			String table = "TIMETABLE_"+this.to2DigitString(i+1);
		
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
				"SELECT visit_time FROM "+table+
				" WHERE last_name = ? AND res_code = ? AND status = ?")){
			
				con.setAutoCommit(false);
				pstmt.setString(1, lastName);
				pstmt.setString(2, code);
				pstmt.setString(3, TimeStatus.RESERVED.toString());
				
				ResultSet results = pstmt.executeQuery();
			
				if(results.next()) {
					visitTime = results.getTime("visit_time").toLocalTime();
					break;
				}	
				con.commit();
				results.close();
			}catch(SQLException e) {
				System.err.println("Error finding visit time and customer in #findAndCancelVisitByCustomerName()");
				e.printStackTrace();
			}
		}	
		//once we have specialistID (tableNum) and visit time, can cancel the appointment (or set to FREE)
		
		if(visitTime == null) {	//if not found
			return false;
		}
		int resultMins = (visitTime.toSecondOfDay() - time.toSecondOfDay())/60;
			
			String table = "TIMETABLE_"+this.to2DigitString(tableNum);
			
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
				"UPDATE "+table+
				" SET status = ?, res_code = ? WHERE visit_time = ? AND last_name = ?")){
			
				con.setAutoCommit(false);
				if(resultMins<=0) {			//won't allow any margin of error when customer cancels 'just in time'
					pstmt.setString(1, TimeStatus.CANCELLED.toString());
					pstmt.setString(2, "");
				}else {
					pstmt.setString(1, TimeStatus.FREE.toString());
					pstmt.setString(2, "");
				}
				pstmt.setTime(3, java.sql.Time.valueOf(visitTime));
				pstmt.setString(4, lastName);
				
				pstmt.executeUpdate();
				con.commit();
				result = true;
				
			}catch(SQLException e) {
				System.err.println("Error resetting status in #findAndCancelVisitByCustomerName()");
				e.printStackTrace();
			}
			
		
		return result;
			
	}
	private boolean ongoingVisitExists(int specialistID) {
		
		System.out.println("Ongoing visit exists being executed");
		
		String table = "TIMETABLE_"+this.to2DigitString(specialistID);
		boolean result = false;
			
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
				"SELECT visit_time FROM "+table+" WHERE status = ?")){
			
			
				con.setAutoCommit(false);
				pstmt.setString(1, TimeStatus.ONGOING.toString());
		
				ResultSet results = pstmt.executeQuery();
		
		//if at least 1 result found - ongoing visit exists
		if(results.next()) {
		result = true;	
		}
						
		con.commit();
		results.close();
				
		}catch(SQLException e) {
				System.err.println("Error in ongoingVisitExists()");
				e.printStackTrace();
		}
		return result;
	}
/*
 * Internal helper method.
 * If reserved visit is found, returns String in format: "XX:XX,YYY" (X - time, Y - code)
 * If not, returns "";
 */
private final String getIfReservedExists(String name, String lastName) {
		
		System.out.println("reservedVisitExists() being executed");
		String table = "";
		String answer = "";
		boolean result = false;
		
		String[] ids = this.getAllSpecialistIDs().split(",");
		
		for(String item: ids) {
			table = "TIMETABLE_"+item;
			if(result) break; //do not search anymore if we have already found an item
			
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
				"SELECT visit_time, res_code FROM "+table+" WHERE name = ? AND last_name = ? AND status = ?")){
			
				con.setAutoCommit(false);
				
				pstmt.setString(1, name);
				pstmt.setString(2, lastName);
				pstmt.setString(3, TimeStatus.RESERVED.toString());
		
				ResultSet results = pstmt.executeQuery();
		
				//if at least 1 result found - reserved visit exists
				if(results.next()) {
					answer = results.getTime("visit_time").toLocalTime().toString()+","+results.getString("res_code");
					System.out.println("Reserved visit was found: "+answer);
					result = true;	
				}
						
				con.commit();
				results.close();
				
			}catch(SQLException e) {
				System.err.println("Error in ongoingVisitExists()");
				e.printStackTrace();
			}
		
	}
		return answer;
	}
	
/*
 * 
 */
	final int getSpecialistCount() {
		return (int)Stream.of(getAllSpecialistIDs().split(",")).count();
	}
	void synchronize(LocalTime time) {
		current = time;
		System.out.println("Dao synchronized to time: "+current);
	}
	
	/*
	 * Unused. Old (cleartext) login method
	 */
/*	int checkCredentials(String username, String password) {
		
		String table = "SPECIALISTS";
		
		int answer = -1;
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
			"SELECT * FROM "+table+
			" WHERE username = ? AND password = ?")){
			
			con.setAutoCommit(false);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			ResultSet results = pstmt.executeQuery();
			
			if(results.next()) {
				answer = results.getInt("ID");
			}else {
				answer = -1;
			}
			con.commit();
			results.close();
			
		}catch(SQLException e) {
			System.err.println("Error in checkCredentials()");
			e.printStackTrace();
		}
			
		return answer;
	}
*/
	String retrieveGUIString() {
		String result = "";
		
		String item = ""; //items from a single ID
		
		String[] ids = this.getAllSpecialistIDs().split(",");
		int size = ids.length;
		
		for(int i=0; i<size; i++) {
			item = getReservedValuesForID(ids[i]);
			if(i<size-1) item = item+".";
			result +=item;	//concat ok, not too many items
		}
		System.out.println("GUI retrieved in DAO: "+result);
		return result;
	}
	private String getReservedValuesForID(String id) {
		
		String table = "TIMETABLE_"+id;
		//for more convenient response format
		String response = "";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT visit_time, res_code FROM "+table+" WHERE status = ?;")){
			
				pstmt.setString(1, TimeStatus.RESERVED.toString());
				con.setAutoCommit(false);
			
			ResultSet results = pstmt.executeQuery();
		
			List<String> list = new ArrayList<>();
			
			/*
			 * Only retrieve RESERVED values that are not in the past (some error margin is allowed)
			 */
			while(results.next()) {
				if(results.getTime("visit_time").toLocalTime().isAfter(current.minusMinutes(ALLOWED_ERROR_MARGIN))) {
					list.add(results.getString("res_code"));
				}
				
			}
		
			int itemsFound = list.size();
		
		//we are only interested in first 5 items (customer web GUI limitation)
			if(itemsFound >=5) {
				response = list.stream().sequential().limit(5).collect(Collectors.joining(","));
			}else {
				response = list.stream().sequential().collect(Collectors.joining(","));
			}
		
			con.commit();
			results.close();
				
		}catch(SQLException e) {
			System.err.println(e.getMessage());
				System.err.println("Error retrieving time table");
				e.printStackTrace();
		}
		return response;
	}
	private final ObjectStatus getAccountType(String username) {
		
		ObjectStatus result = ObjectStatus.NONE;
		
		String table = "SPECIALISTS";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
				"SELECT type FROM "+table+" WHERE username = ?;")){
			
			pstmt.setString(1, username);
			con.setAutoCommit(false);
			
			ResultSet results = pstmt.executeQuery();
		
			if(results.next()) {
				result = ObjectStatus.valueOf(results.getString("type").toUpperCase());
			}
			con.commit();
			results.close();
				
		}catch(SQLException e) {
			System.err.println(e.getMessage());
				System.err.println("Error checking #isAdmin()");
				e.printStackTrace();
		}
		return result;
		
	}
	private final String getAllSpecialistIDs() {
		
		//System.out.println("getAllSpecialistIDs called");
		String table = "SPECIALISTS";
		String result = "";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
				"SELECT ID FROM "+table+" WHERE type = ?;")){
			
			pstmt.setString(1, "SPECIALIST");
			con.setAutoCommit(false);
			
			ResultSet results = pstmt.executeQuery();
			
			List<String> list = new ArrayList<>();
			while(results.next()) {
				list.add(this.to2DigitString(Integer.parseInt(results.getString("ID"))));
			}
			result = list.stream().sequential().collect(Collectors.joining(","));
			con.commit();
			results.close();
				
		}catch(SQLException e) {
			System.err.println(e.getMessage());
				System.err.println("Error in #getAllSpecialistIDs()");
				e.printStackTrace();
		}
		return result;
	}
/*
 * Resets all TIMETABLE_X and REGISTRATION_NUMBERS tables in db to default values.
 * SPECIALISTS, LOG_SERVICED_CUSTOMERS and LOG_ISSUED_CODES tables are left intact.
 */
	final synchronized Response reset() {
		
		boolean result = true;
		LocalTime start = startOfDay;
		LocalTime end = startOfDay.plusHours(8);
		
		String[] allIDs = this.getAllSpecialistIDs().split(",");
		String[] allTimes = DbSetup.generateTimes(start, end, 15);
		String table = "TIMETABLE_";
		
		//1. reset all TIMETABLE_X
		for(String id : allIDs) {
			
			//first delete all
			System.out.println("Initiating delete all from table: "+(table+id));
			try(Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement(
							"DELETE FROM "+(table+id)+";")){
					
				con.setAutoCommit(false);	
				int results = pstmt.executeUpdate();
				con.commit();
				pstmt.close();
						
				}catch(SQLException e) {
					result = false;
					System.err.println("DELETE error in #reset()");
					e.printStackTrace();		
				}
			
			//according to SQLite documentation, it is advisable to use VACUUM after every delete operation
			try(Connection con = ds.getConnection()){
				Statement stmt = con.createStatement();
			    stmt.executeUpdate("VACUUM;");
			    System.out.println("VACUUMING...");
			    stmt.close();
			}catch(SQLException e) {
				result = false;
				System.err.println("VACUUM (first) error in #reset()");
				e.printStackTrace();
			}
			
			//then insert predetermined values
			try(Connection con = ds.getConnection()){
				con.setAutoCommit(false);	
				
				LocalTime time;
				LocalDate date = LocalDate.now();
				
				for(int i=0; i<allTimes.length; i++) {
					try(PreparedStatement pstmt = con.prepareStatement("INSERT INTO "+ (table+id) +
		            " VALUES (?, ?, ?, ?, ?, ?) ")){
					
					time = LocalTime.parse(allTimes[i]);
					java.sql.Date sqlDate = java.sql.Date.valueOf(date);
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
						result = false;
						System.out.println("INSERT error in #reset()");
						e.printStackTrace();
					}		
				}
					
			}catch(SQLException e) {
				result = false;
				e.printStackTrace();
			}	
		}
	//2. if everything is OK so far, renew the code counter table
		if(result) {
			result = this.resetCodeTable();
		}
		
	//3. re-initialize the time map and set indicator flag to true
		this.initializeEmptyMap();
		this.canServeCustomers = true;
		
		return result? new ResponseReset("OK, database has been reset to blank slate") : new ResponseReset("Error resetting the database");
	}
	final boolean resetCodeTable() {
		
		boolean result = true;
		String regTable = "REGISTRATION_NUMBERS";
		LocalDate date = LocalDate.now();
		
		//1. delete
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("DELETE FROM " + regTable+";")){
				
				con.setAutoCommit(false);
				int results = pstmt.executeUpdate();
				con.commit();
				pstmt.close();
						
		}catch(SQLException e) {
			result = false;
			System.err.println("DELETE error in #reset()");
			e.printStackTrace();		
		}
		//2. vacuum	
		try(Connection con = ds.getConnection()){
			Statement stmt = con.createStatement();
			   stmt.executeUpdate("VACUUM;");
			   stmt.close();
		}catch(SQLException e) {
				result = false;
				System.err.println("VACUUM (second) error in #reset()");
				e.printStackTrace();
		}
		//3. insert defaults
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO "+ regTable +
	            " VALUES (?, ?, ?) ")){
				
			con.setAutoCommit(false);
				
			java.sql.Date sqlDate = java.sql.Date.valueOf(date);
				
			pstmt.setDate(1, sqlDate);
			pstmt.setString(2, "none");
			pstmt.setString(3, "none");
			
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
		}catch(SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	final String getSalt(String username) {
		
		String table = "SPECIALISTS";
		String result = "";
		ObjectStatus accountType = this.getAccountType(username);
		
		if(accountType == ObjectStatus.NONE) {
			return "no-salt";
		}else {
			try(Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement(
					"SELECT salt FROM "+table+" WHERE username = ? AND type = ?;")){
			
				pstmt.setString(1, username);
				pstmt.setString(2, accountType.toString());
				con.setAutoCommit(false);
			
				ResultSet results = pstmt.executeQuery();
		
				if(results.next()) {
					result = results.getString("salt");
				}
		
				con.commit();
				results.close();
				
			}catch(SQLException e) {
				System.err.println(e.getMessage());
				System.err.println("Error in #getAdminSalt()");
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	final String getHash(String username) {
		
		String table = "SPECIALISTS";
		String result = "";
		ObjectStatus accountType = this.getAccountType(username);
		
		if(accountType == ObjectStatus.NONE) {
			return "no-hash";
		}else {
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
				"SELECT hashed_password FROM "+table+" WHERE username = ? AND type = ?;")){
			
				pstmt.setString(1, username);
				pstmt.setString(2, accountType.toString());
				con.setAutoCommit(false);
			
				ResultSet results = pstmt.executeQuery();
		
				if(results.next()) {
					result = results.getString("hashed_password");
				}
		
				con.commit();
				results.close();
				
			}catch(SQLException e) {
				System.err.println(e.getMessage());
				System.err.println("Error in #getHash()");
				e.printStackTrace();
			}
		}
		return result;
	}
	final String getIDByUsername(String username) {
		
		String table = "SPECIALISTS";
		String answer = "-1";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
			"SELECT ID FROM "+table+" WHERE username = ?")){
			
				con.setAutoCommit(false);
				pstmt.setString(1, username);
				ResultSet result = pstmt.executeQuery();
				
				if(result.next()) {
					answer = String.valueOf(result.getInt("ID"));
				}
				
				result.close();
				con.commit();
		}catch(SQLException e) {
			System.err.println(e.getMessage());
				System.err.println("Error retrieving specialist ID");
				e.printStackTrace();
		}
		return answer;
	}
	private final String[] getSupportedTimesByID(int id) {
		
		String table = "TIMETABLE_"+this.to2DigitString(id);
		List<String> list = new ArrayList<>();
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
			"SELECT visit_time FROM "+table+" WHERE 1=1;")){
				
				con.setAutoCommit(false);
				ResultSet results = pstmt.executeQuery();
			
				while(results.next()) {
					list.add(results.getTime("visit_time").toLocalTime().toString());
				}
			
				//System.out.println("Times retrieved in getTimesByID: "+list);
				con.commit();
				results.close();
					
		}catch(SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Error in #getSupportedTimesByID()");
					e.printStackTrace();
				}
		return list.toArray(new String[0]);
	}
	final synchronized String getNextPostfixString() {
		
		
		String table = "REGISTRATION_NUMBERS";
		LocalDate date = LocalDate.now();
		String currentPostfix = "";
		String newPostfix = "";
				
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT current_spawn_postfix FROM "+table
					+ " WHERE date = ?")){
			
			con.setAutoCommit(false);
			
			pstmt.setDate(1, java.sql.Date.valueOf(date));
			
			ResultSet results = pstmt.executeQuery();
			if(results.next()) {
				currentPostfix = results.getString("current_spawn_postfix");
			}	
			con.commit();
			results.close();
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		/*
		 * if date requested is not in table (means we are operating on old data) - create additional row
		 * with date = LocalDate.now()
		 */
		if(currentPostfix.equals("")) { //should never be true now - remove later
			currentPostfix = "none";
		}
		
		/*
		 * Now, determine the new postfix
		 */
		if(currentPostfix.equals("none") || currentPostfix.equals("99")) {
			newPostfix = this.to2DigitString(0);
		}else {
			try {
				int current = Integer.parseInt(currentPostfix);
				current++;
				newPostfix = this.to2DigitString(current);
			}catch(NumberFormatException e) {
				System.err.println("#getNextPostfixString() received illegal argument - empty string (failed to parse)");
			}
			
			
		}
		
		/* now update 'last issued postfix' in db */
		
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("UPDATE "+table
						+ " SET current_spawn_postfix = ?"
						+ " WHERE date = ?")){
				con.setAutoCommit(false);
				
				pstmt.setString(1, newPostfix);
				pstmt.setDate(2, java.sql.Date.valueOf(date));
				
				
				pstmt.executeUpdate();
				con.commit();
			
			}catch(SQLException e) {
				e.printStackTrace();
			}
		
		System.out.println("Spawn postfix issued was: "+newPostfix+". It should reflect in DB");
		
		return newPostfix;
	}
	final boolean saveDailyCodeTable() {
		
		String regTable = "REGISTRATION_NUMBERS";
		LocalDate oldDate = null;
		String oldCode = null;
		String oldSpawnPostfix = null;
		boolean result = true;
		
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "+ regTable +";")){
			
			con.setAutoCommit(false);
			ResultSet results = pstmt.executeQuery();
			
			//should only match a single line
			if(results.next()) {
				oldDate = results.getDate("date").toLocalDate();
				oldCode = results.getString("current_code");
				oldSpawnPostfix = results.getString("current_spawn_postfix");
			}
			
			con.commit();
			results.close();
			
		}catch(SQLException e) {
			result = false;
			e.printStackTrace();
		}
		
		//if all is OK so far - save spawn/code date to the log table
		
		if(oldDate == null || oldCode == null || oldSpawnPostfix == null) {
			return false;
		}else {
			String log_table = "LOG_ISSUED_CODES";
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO "+ log_table +
	            " VALUES (?, ?, ?) ")){
				
				con.setAutoCommit(false);
				
				java.sql.Date sqlDate = java.sql.Date.valueOf(oldDate);
				
				pstmt.setDate(1, sqlDate);
				pstmt.setString(2, oldCode);
				pstmt.setString(3, oldSpawnPostfix);
			
				pstmt.executeUpdate();
				con.commit();
				pstmt.close();
			}catch(SQLException e) {
				result = false;
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	private final TimeStatus getCurrentCustomerStatus(int specialistID, LocalTime time) {
		
		String table = "TIMETABLE_"+this.to2DigitString(specialistID);
		TimeStatus currentStatus = TimeStatus.NONE;
		
		//1. fetch visitation info based on visitation time
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT status FROM "+table
					+" WHERE visit_time = ?;")){
			
			con.setAutoCommit(false);
			pstmt.setTime(1, java.sql.Time.valueOf(time));
			
			ResultSet results = pstmt.executeQuery();
			
			//should only match a single line
			if(results.next()) {
				currentStatus = TimeStatus.valueOf(results.getString("status"));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return currentStatus;
		
	}
	
	private final Customer forceChange(Customer custToChange, TimeStatus newStatus, int specID) {

			Customer changed = custToChange;
			boolean result = false;
			
			String query = "UPDATE TIMETABLE_"+this.to2DigitString(specID)
			+ " SET status = ?, res_code = ? "
			+ " WHERE visit_time = ?";
			
			try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(query)){
						
				con.setAutoCommit(false);
						
				pstmt.setString(1, newStatus.toString());
				pstmt.setString(2, "");
				pstmt.setTime(3, java.sql.Time.valueOf(changed.getTime()));
					
				pstmt.executeUpdate();
				con.commit();
				result = true;
						
			}catch(SQLException e) {
				System.err.println("Error changing(FORCED) customer status to "+newStatus+" for specialist [ID="+specID+"]");
				e.printStackTrace();
				result = false;
			}
			
			return result? new Customer(
					changed.getTime(), 
					changed.getName(), 
					changed.getLastName(), 
					"", 
					newStatus.toString(), 
					changed.getDate()) : custToChange;
		}
	
}
