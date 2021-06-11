package dto;

public class ResponseToRegistration implements Response{

	private final String responseCode;
	
	public ResponseToRegistration(String code) {
		this.responseCode = code;
	}
	public String getCode() {
		return responseCode;
	}
	public boolean isACode() {
		
		if(responseCode.length()!=3) {
			return false;
		}else if(Integer.parseInt(responseCode) >=0 && Integer.parseInt(responseCode) <=999) {
			return true;
		}else {
			return false;
		}
	}
}
