package dto;

public class ResponseBool implements Response{

	private boolean responseValue;
	private int id = -1;
	
	private ResponseBool(boolean value) {
		this.responseValue = value;
	}
	private ResponseBool() {
		
	}
	public static Response getEmpty() {
		return new ResponseBool();
	}
	public static Response negative() {
		return new ResponseBool(false);
	}
	public static Response positive() {
		return new ResponseBool(true);
	}
	public static Response positive(int id) {
		return new ResponseBool(true).setId(id);
	}
	public boolean getValue() {
		return responseValue;
	}
	public int getId() {
		return id;
	}
	private ResponseBool setId(int id) {
		this.id = id;
		return this;
	}
}
