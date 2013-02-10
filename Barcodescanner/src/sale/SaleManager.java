package sale;

import java.util.*;
import communicate.MySQL;
import java.sql.SQLException;

public class SaleManager {

	public static void sale(ArrayList<User> users, ArrayList<Product> products) throws SQLException{
		MySQL db = new MySQL();
		ArrayList<Integer> ids = new ArrayList<>();		
		for (User u: users) {
			//! Check if every user is unique in this order
			if (ids.contains(u.getId())) {
				continue;
			} else {
				ids.add(u.getId());
			}
			for (Product p: products) {
				String sql = "INSERT INTO tb_sales(user_id, product_id) VALUES ('" + u.getId() + "','" + p.getId() + "');";	
				db.RunQuery(sql);
			}
		}
		db.Close();
	}
}
