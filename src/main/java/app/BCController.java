package app;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import app.Dao.TimeStatus;
import dto.CmdGetWaitingTime;
import dto.CmdLogin;
import dto.CmdMarkVisitBegan;
import dto.CmdMarkVisitCancelled;
import dto.CmdMarkVisitEnded;
import dto.CmdMarkVisitFree;
import dto.CmdRegistrationRequest;
import dto.CmdReset;
import dto.CmdRetrieveCustomerList;
import dto.CmdRetrieveGUIString;
import dto.Command;
import dto.Response;
import dto.ResponseBool;
import dto.ResponseCustList;
import dto.ResponseGUIString;
import dto.ResponseNotFound;
import dto.ResponseToRegistration;
import dto.SpawnCredentials;

/**
 * This class is the main controller class.
 * It is also the entry point to the program
 * 
 * Make sure DB is initialized beforehand and is reachable for name "reg.db".
 * Empty blueprint database can be easily initialized by launching the main method of db/DbSetup.java class.
 * 
 * Class explanation:
 * 	app/DbModel is the main logic class, which is controlled by this class (BCController).
 * 	CustomerClient, as the name says is the logic representation of CustomerGUIController
 * 	SpecialistClient is the same, but for SpecialistGUIController
 * 
 * 	Only BCController has direct access to DbModel.
 * 
 * Input parameters in most cases aren't strictly checked (just enough to verify the GUI is working as intended).
 * There is also no bogus input control and any gibberish will be accepted as name/last name for customer registration,
 * 		as long as both of the fields are not completely empty.
 * To cancel a visit, customer must supply the code and his last name.
 * 
 * Additional info:
 * 		For simplicity reasons all passwords for the 5 registered specialists are: 12345
 * 		All usernames are their own names (see DbSetup class)
 * 		Example: login credentials of 1st specialist are: Amy 12345
 * 		Passwords are not hashed
 * 
 * 		GUI has "Spawn" button that spawns a random customer (name is Bobby+integer, last name is Bobson+integer - eg.:
 * 		Bobby0 Bobson0 will always be the first one spawned. His code is shown in the queue simulation.
 * 		If program is restarted, codes are again started from value 000 (even though DB can already contain them).
 * 		Similarly, the random customer will also start from Bobby0 Bobson0 - the database is not checked for existing
 * 		entries for this (only allowed time is being checked).
 * 		+1/-1 buttons are used to increment/decrement the timer (eg. customer cannot cancel a visit if the visit is already
 * 		over (some margin of 'being late' is allowed).
 * 
 * 		Program will not register customers to time slots that are already (after some allowed margin) in the past.
 * 
 * 		The easiest way to view the important contents of the DB is to login any specialist and click "Refresh List".
 * 
 * Not implemented:
 * 		Time management was not fully implemented due to time limitations on the project (the clock always starts from
 *  	8:55 - the beginning of the (nine-to-five) work day).
 *  	This time is updated every second (not shown) and is synchronized with the DbModel's class current time, 
 *  	at least until a specialist logs in.
 *  	State of the whole CustomerClient view is not retained and is not loaded from DB upon each launch,
 *  		it always starts from the blank slate.
 *  		That means program currently assumes that, upon initializing the blueprint DB, the main client will be started
 *  		and will stay functioning for the whole work day 
 * 	
 * Therefore, given more time, the focus would be to implement full state recovery and complete communication with DB
 * on every aspect (including the clock).
 * 
 * @author qqqky
 */

public class BCController {
	
	private static final ZoneId zone = ZoneId.of("Europe/Vilnius");
	private static final BCController instance = new BCController(LocalDate.now(zone));
	public static final LocalTime base = LocalTime.of(8, 55); //not implemented
	private final Dao db;
	private LocalTime current;
	private Command com;
	private final String spawnNameBase = "Bobby";
	private final String spawnLastNameBase = "Bobson";
	private LocalDate syncDate = LocalDate.now(zone);
	

	public static void main(String[] args) {
		
	}

	private BCController(LocalDate date) {
		db = Dao.create(date);
		
	}
	public static BCController getInstance() {
       return instance;
	}
	
	private Response login(String username) {
			
		int result = Integer.parseInt(db.getIDByUsername(username));
		return result == -1? ResponseBool.negative() : ResponseBool.positive(result);
			
	}
	private ResponseToRegistration registerCustomer(String name, String lastName) {
		if(!db.getCanServe()) return new ResponseToRegistration("No more free space today or out of office working hours");
		else {
			return new ResponseToRegistration(db.registerCustomer(name, lastName));
		}
	}
	
	private final Response changeCustomerStatus(String visitTime, TimeStatus newStatus, int specialistID) {
		boolean result = db.changeCustomerStatus(visitTime, newStatus, specialistID);
		
		return result? ResponseBool.positive(specialistID) : ResponseBool.negative();
	}
	private final Response cancelVisit(String code, String lastName) {
		boolean result = db.findAndCancelVisitByCustomerName(code, lastName);
		
		return result? ResponseBool.positive(): ResponseBool.negative();
	}
	private final Response timeUntilVisit(String code, String lastName) {
		return db.timeLeftUntilVisit(code, lastName);
	}
	private final ResponseCustList retrieveTodaysClients (int specialistNum) {
		return db.retrieveCustomerList(specialistNum);
	}
	private final ResponseGUIString retrieveGUIString() {
		return new ResponseGUIString(db.retrieveGUIString());
	}
	private final Response reset() {
		return db.reset();
	}
	private final void checkDateSync() {
		
		LocalDate today = LocalDate.now(zone);
		
		if(!syncDate.equals(today) && db.isOutdated()) {	//isOutdated is 'just in case' check
			db.saveDailyCodeTable();
			db.reset();
			syncDate = LocalDate.now(zone);
		}
	}
	
	public Response setAndExecuteRequest(Command command) {
		
		//server local time
		synchronize(LocalTime.now(zone));
		checkDateSync();
		
		this.com = command;
		return this.execute();
	}
	/**
	 * Global execution method based on passed Command
	 */
	private Response execute() {
		
		Command cmd = this.com;
		
		if(cmd instanceof CmdReset) {
			return reset();
		}else if(cmd instanceof CmdRetrieveCustomerList) {
			return retrieveTodaysClients(((CmdRetrieveCustomerList) cmd).getSpecialistNum());
			
		}else if(cmd instanceof CmdRetrieveGUIString) {
			return retrieveGUIString();
			
		}else if(cmd instanceof CmdMarkVisitBegan) {
			return changeCustomerStatus(((CmdMarkVisitBegan) cmd).getVisitTime(),
				((CmdMarkVisitBegan) cmd).getStatus(), ((CmdMarkVisitBegan) cmd).getSpecialistNum());
			
		}else if(cmd instanceof CmdMarkVisitEnded) {
			return changeCustomerStatus(((CmdMarkVisitEnded) cmd).getVisitTime(),
					((CmdMarkVisitEnded) cmd).getStatus(), ((CmdMarkVisitEnded) cmd).getSpecialistNum());
		}else if(cmd instanceof CmdMarkVisitFree) {
			return changeCustomerStatus(((CmdMarkVisitFree) cmd).getVisitTime(),
					((CmdMarkVisitFree) cmd).getStatus(), ((CmdMarkVisitFree) cmd).getSpecialistNum());		
		}else if(cmd instanceof CmdMarkVisitCancelled) {
			//if specialist ID=-1, request came from customer client and has different parameters
			if(((CmdMarkVisitCancelled) cmd).getSpecialistNum() == -1) {
				return cancelVisit(((CmdMarkVisitCancelled) cmd).getCode(), ((CmdMarkVisitCancelled) cmd).getLastName());
			}else {
				return changeCustomerStatus(((CmdMarkVisitCancelled) cmd).getVisitTime(),
						((CmdMarkVisitCancelled) cmd).getStatus(), ((CmdMarkVisitCancelled) cmd).getSpecialistNum());
			}
			
		}else if(cmd instanceof CmdRegistrationRequest) {
			return registerCustomer(((CmdRegistrationRequest) cmd).getName(), ((CmdRegistrationRequest) cmd).getLastName());
		
		}else if(cmd instanceof CmdGetWaitingTime) {
			return timeUntilVisit(((CmdGetWaitingTime) cmd).getCode(), ((CmdGetWaitingTime) cmd).getName());
		}else if(cmd instanceof CmdLogin) {
			return login(((CmdLogin) cmd).getUsername());
		}else return ResponseNotFound.getInstance();

	}
	
	private void synchronize(LocalTime time) {
		current = time;
		db.synchronize(time);
	}
	public String askTime() {
		
		LocalTime time = current;
		String mins;
		String hours;
		mins = time.getMinute()<10? "0"+time.getMinute() : ""+time.getMinute();
		hours = time.getHour()<10? "0"+time.getHour() : ""+time.getHour();
		
		return hours+":"+mins;
	}
	public SpawnCredentials getSpawnCredentials() {
		
		checkDateSync();
		
		String postfix = db.getNextPostfixString();
		
		SpawnCredentials creds = new SpawnCredentials(
				spawnNameBase+postfix, 
				spawnLastNameBase+postfix);
		
		System.out.println("spawnPostfix was: "+postfix);
		
		return creds;
		
	}
	public String getSalt(String uname) {
		return db.getSalt(uname);
	}
	public String getHash(String uname) {
		return db.getHash(uname);
	}

}
