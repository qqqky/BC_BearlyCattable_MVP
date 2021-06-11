package dto;

import app.Dao.TimeStatus;

public class CmdMarkVisitCancelled implements Command{
	private final int specialistNum;
	private String visitTime;
	private final TimeStatus status = TimeStatus.CANCELLED;
	private String code;
	private String custLastName;
		
	public CmdMarkVisitCancelled(String visitTime, int specialistNum) {
		this.specialistNum = specialistNum;
		this.visitTime = visitTime;
	}
	//alternative constructor for customer client
	public CmdMarkVisitCancelled(String code, String custLastName) {
		this.code = code;
		this.custLastName = custLastName;
		this.specialistNum = -1;	//for later check
	}
	
		
	public int getSpecialistNum() {
		return specialistNum;
	}
	public String getVisitTime() {
		return visitTime;
	}
	public TimeStatus getStatus() {
		return status;
	}
	public String getCode() {
		return code;
	}
	public String getLastName() {
		return custLastName;
	}
	
}
