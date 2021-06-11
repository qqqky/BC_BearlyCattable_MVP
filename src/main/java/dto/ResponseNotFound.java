package dto;

public class ResponseNotFound implements Response{

	public static final ResponseNotFound instance = new ResponseNotFound();
	
	public static ResponseNotFound getInstance() {
		return instance;
	}
}
