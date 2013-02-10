package sale;

import java.sql.ResultSet;
import java.sql.SQLException;

import communicate.MySQL;

public class Product {
	
	private int id;
	private String name;
	private String barcode;
	private int price = 0; // in cent
	
	public Product(int id, String name, String barcode, int price)
	{
		this.id = id;
		this.name = name;
		this.barcode = barcode;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}
	
	
	static public boolean isValidProduct(String productCode) throws SQLException {
		String sql = "SELECT id, name, price FROM tb_product WHERE tb_product.barcode = '" + productCode + "'";
		MySQL db = new MySQL();
		ResultSet rs = db.ReturnQuery(sql);
		rs.last();
		int row = rs.getRow();	
		db.Close();
		return row == 1;
	}
	
	static public Product getProductByName(String productCode) throws SQLException	{
		String sql = "SELECT id, name, price FROM tb_product WHERE tb_product.barcode = '" + productCode + "'";
		MySQL db = new MySQL();
		ResultSet rs = db.ReturnQuery(sql);
		while (rs.next()) {
			int id = rs.getInt(1);
			String proname = rs.getString(2);
			int cent = rs.getInt(3);
			db.Close();
			return new Product(id, proname, productCode, cent);
		}
		db.Close();
		return new Product(-1, "unbekannt", productCode, -1);
	}

}
