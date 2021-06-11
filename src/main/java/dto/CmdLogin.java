package dto;

public class CmdLogin implements Command{

	String username;
	
	public CmdLogin(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
}
