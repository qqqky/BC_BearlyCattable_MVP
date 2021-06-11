package dto;

public class CmdRegistrationRequest implements Command {

	private String name;
	private String lastName;
	
	public CmdRegistrationRequest(String name, String lastName) {
		this.name = name;
		this.lastName = lastName;
	}
	
	public String getName() {
		return name;
	}
	public String getLastName() {
		return lastName;
	}

}
