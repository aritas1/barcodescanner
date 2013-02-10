package service;

import java.sql.SQLException;
import java.util.ArrayList;

import sale.Product;
import sale.SaleManager;

public class ServiceManager {

	public static final String constOkay = "Okay";
	public static final String constCancel = "Cancel";
	public static final String constBack = "Back";
	
	
	public enum globalState {
		sale, admin
	}
	
	public enum saleStates {
		name, product
	}
	
	private globalState state = globalState.sale;
	private saleStates saleState = saleStates.name;
	
	private ArrayList<sale.User> userList = new ArrayList<>();
	private ArrayList<sale.Product> productList = new ArrayList<>();
	
	public void changeState(String s) throws SQLException
	{
		switch (state) {
		case sale:
			changeStateSale(s);
			break;
		case admin:
			
			break;
		default:
			break;
		}
	}
	
	
	public void reset() {
		productList.clear();
		userList.clear();
		saleState = saleStates.name;
		System.out.println("reset to initial state");		
	}
	
	private void changeStateSale(String s) throws SQLException
	{
		switch (saleState) {
		case name:
			if(sale.User.isValidUser(s)) {
				System.out.println(s +" was a valid user");
				userList.add(sale.User.getUserByName(s));
				break;
			} else if(sale.Product.isValidProduct(s) && userList.size() != 0)
			{
				System.out.println("Change state to product");
				saleState = saleStates.product;
			} 
			else if (s.equals(constBack)) {
				if (userList.size() > 0) {
					userList.remove(userList.size() - 1);
				}
				break;
			}
			else if (s.equals(constCancel)) {
				reset();	
				break;
			}
			else {
				System.out.println(s + " is an unknown barcode");
				break;
			}
		case product:
			if(sale.Product.isValidProduct(s)) {
				System.out.println(s +" was a vaild product");
				productList.add(sale.Product.getProductByName(s));
			} 
			else if(sale.User.isValidUser(s)) {
				System.out.println("Try to book a user as a product, not valid: WIXXAAA");
				//TODO: Handle this
			} 
			else if (s.equals(constBack)) {
				if (productList.size() > 0) {
					productList.remove(productList.size() - 1);
				}
				if (productList.size() == 0) {
					saleState = saleStates.name;
					System.out.println("Change state back to name");
				}
			}
			else if (s.equals(constCancel)) {
				reset();
			}
			else if (s.equals(constOkay)) { 
				//TODO: change that to OK or something like that
				System.out.println("SALED ... Now dumping to the database");
				// dump the order into the database
				SaleManager.sale(userList, productList);
				
				productList.clear();
				userList.clear();
				saleState = saleStates.name;				
			} else {
				System.out.println(s + " is an unknown barcode");
			}
			break;
		default:
			break;
		}
	}

}
