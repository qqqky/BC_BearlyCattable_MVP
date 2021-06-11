package dto;

import app.Dao.TimeStatus;

public class CmdMarkVisitBegan implements Command{

private int specialistNum;
private String visitTime;
private final TimeStatus status = TimeStatus.ONGOING;
	
	public CmdMarkVisitBegan(String visitTime, int specialistNum) {
		this.specialistNum = specialistNum;
		this.visitTime = visitTime;
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
}
	