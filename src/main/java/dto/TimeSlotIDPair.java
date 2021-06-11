package dto;

public class TimeSlotIDPair {

	private final int id;
	private final String timeSlot;
	private final boolean isEmpty;
	
	
	public TimeSlotIDPair(int id, String timeSlot) {
		this.id = id;
		this.timeSlot = timeSlot;
		this.isEmpty = false;
	}
	private TimeSlotIDPair() {
		this.id = -1;
		this.timeSlot = "";
		this.isEmpty = true;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public int getID() {
		return id;
	}
	public boolean isEmpty() {
		return isEmpty;
	}
	public static TimeSlotIDPair empty() {
		return new TimeSlotIDPair();
	}

}
