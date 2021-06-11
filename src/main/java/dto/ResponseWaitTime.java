package dto;

public class ResponseWaitTime implements Response{

	private String waitTimeInMins;
	
	public ResponseWaitTime(String minutes) {
		this.waitTimeInMins = minutes;
	}
	public String getWaitTime() {
		return waitTimeInMins;
	}

}
