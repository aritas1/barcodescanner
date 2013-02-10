package sale;

public class Product {
	
	private int id;
	private String name;
	private String barcode;
	private int price = 0; // in cent
	
	public Product(int id, String name, String barcode)
	{
		this.id = id;
		this.name = name;
		this.barcode = barcode;
	}
	
	static public boolean isValidProduct(String productCode)
	{
		//TODO: implement product validation
		if(productCode.equals("Andi")) {
			return false;
		}
		return true;
	}
	
	static public Product getProductByName(String productCode)
	{
		return new Product(-1, "unbekannt", productCode);
	}

}
