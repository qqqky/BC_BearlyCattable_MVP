package dto;

public class ResponseGUIString implements Response{
	
	private String viewCoords;
	
	public ResponseGUIString(String viewCoords) {
		this.viewCoords = viewCoords;
	}
	public String getCoordString() {
		return viewCoords;
	}
	
}
