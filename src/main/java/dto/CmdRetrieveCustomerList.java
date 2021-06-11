package dto;

public class CmdRetrieveCustomerList implements Command {

	private int specialistNum;
	
	public CmdRetrieveCustomerList(int specialistNum) {
		this.specialistNum = specialistNum;
	}
	
	public int getSpecialistNum() {
		return specialistNum;
	}
}
