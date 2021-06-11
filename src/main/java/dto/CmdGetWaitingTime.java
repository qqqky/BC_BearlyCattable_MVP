package dto;

public class CmdGetWaitingTime implements Command{

	private String code;
	private String lastName;
	
	public CmdGetWaitingTime(String code, String lastName) {
		this.code = code;
		this.lastName = lastName;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return lastName;
	}

}
