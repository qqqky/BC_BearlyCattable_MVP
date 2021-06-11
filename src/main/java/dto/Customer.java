package dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class Customer {
	
	LocalTime time;
	String name;
	String lastName;
	String code;
	String status;
	LocalDate date;

	public Customer(LocalTime time, String name, String lastName, String code, String status, LocalDate date) {
		
		this.time = time;
		this.name = name;
		this.lastName = lastName;
		this.code = code;
		this.status = status;
		this.date = date;
	}
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Time: ").append(time.toString()).append(" Name: ").append(name)
		.append(" Last_name: ").append(lastName).append(" Code: ").append(code)
		.append(" Status: ").append(status).append(" Date: ").append(date.toString());
		
		return sb.toString();
	}
	public LocalTime getTime() {
		return time;
	}
	public String getTimeString() {
		String s = time.toString();
		s = s.substring(0, 2) + s.substring(3, 5);
		return s;
	}
	public String getCode() {
		return code;
	}
	public String getStatus() {
		return status;
	}
	public String getName() {
		return name;
	}
	public String getLastName() {
		return lastName;
	}
	public LocalDate getDate() {
		return date;
	}
	@Override //auto-generated
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}
	@Override //auto-generated
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}
	
}
