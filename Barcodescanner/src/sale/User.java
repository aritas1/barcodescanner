package sale;

import java.sql.ResultSet;
import java.sql.SQLException;

import communicate.MySQL;

public class User {
	
	private String username;
	
	public String getUsername() {
		return username;
	}

	private int id;
	
	public User(String username, int id)
	{
		this.username = username;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	static public boolean isValidUser(String username) throws SQLException {
		String sql = "SELECT id, username FROM tb_user WHERE tb_user.barcode = '" + username + "'";
		MySQL db = new MySQL();
		ResultSet rs = db.ReturnQuery(sql);
		rs.last();
		int row = rs.getRow();	
		db.Close();
		return row == 1;
	}
	
	static public User getUserByName(String username) throws SQLException {
		String sql = "SELECT id, username FROM tb_user WHERE tb_user.barcode = '" + username + "'";
		MySQL db = new MySQL();
		ResultSet rs = db.ReturnQuery(sql);
		while (rs.next()) {
			int id = rs.getInt(1);
			String user = rs.getString(2);
			db.Close();
			return new User(user, id);
		}
		db.Close();
		return new User("invalid", -1);
	}

}
