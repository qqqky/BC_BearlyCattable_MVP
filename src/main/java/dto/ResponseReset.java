package dto;

public class ResponseReset implements Response{

	private final String response;
	
	public ResponseReset(String response) {
		this.response = response;
	}
	public String getResponse() {
		return response;
	}
}
