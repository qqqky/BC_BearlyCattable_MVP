package dto;

public class ResponseMock implements Response{

	private final String response;
	
	public ResponseMock(String response) {
		this.response = response;
	}
	public String getResponse() {
		return response;
	}
}