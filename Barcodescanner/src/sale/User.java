package sale;

public class User {
	
	private String username;
	private int id;
	
	public User(String username, int id)
	{
		this.username = username;
		this.id = id;
	}
	
	static public boolean isValidUser(String username) {
		//TODO: implement user validation
		
		switch (username) {
		case "Daniel":
			return true;
		case "William":
			return true;
		case "Jonas":
			return true;

		default:
			return false;

		}
	}
	
	static public User getUserByName(String username) {
		return new User(username, -1);
	}

}
