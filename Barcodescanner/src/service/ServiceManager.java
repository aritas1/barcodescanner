package service;

import java.util.ArrayList;

import sale.Product;

public class ServiceManager {
	
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
	
	public void changeState(String s)
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
	
	private void changeStateSale(String s)
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
				
			} else {
				break;
			}
		case product:
			if(sale.Product.isValidProduct(s)) {
				System.out.println(s +" was a vaild product");
				productList.add(sale.Product.getProductByName(s));
			} else if(sale.User.isValidUser(s)) {
				System.out.println("Try to book a user as a product, not vaild: WIXXAAA");
				//TODO: Handle this
			} 
			else if (s.equals("Andi")) { 
				//TODO: change that to OK or something like that
				System.out.println("saled");
				saleState = saleStates.name;
				
				//TODO: dump the order into the database
				
				productList.clear();
				userList.clear();
			}
		break;

		default:
			break;
		}
	}

}
