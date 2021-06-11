package dto;

public class SpawnCredentials {
	private final String name;
	private final String lastName;
	private final boolean isEmpty;
	
	
	public SpawnCredentials(String name, String lastName) {
		this.name = name;
		this.lastName = lastName;
		this.isEmpty = false;
	}
	private SpawnCredentials() {
		this.name = "";
		this.lastName = "";
		this.isEmpty = true;
	}
	
	public String getName() {
		return name;
	}
	public String getLastName() {
		return lastName;
	}
	public boolean isEmpty() {
		return isEmpty;
	}
	public static SpawnCredentials empty() {
		return new SpawnCredentials();
	}
	@Override
	public String toString() {
		return "SpawnCredentials [name=" + name + ", lastName=" + lastName + ", isEmpty=" + isEmpty + "]";
	}

}
