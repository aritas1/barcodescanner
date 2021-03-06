package service;

import java.security.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.SealedObject;

import communicate.InfoBeamer;

import sale.Product;
import sale.SaleManager;

public class ServiceManager {

	public static final String constOkay = "Okaz";
	public static final String constCancel = "Canc";
	public static final String constBack = "Back";
	
	
	public enum globalState {
		sale, admin
	}
	
	public enum saleStates {
		name, product
	}
	
	private globalState state = globalState.sale;
	private saleStates saleState = saleStates.name;
	
	long timeChangeDisplayLast = 0; //System.currentTimeMillis();
	
	private ArrayList<sale.User> userList = new ArrayList<>();
	private ArrayList<sale.Product> productList = new ArrayList<>();
	
	InfoBeamer ib = new InfoBeamer();
	
	int price = 0;
	
	public ServiceManager() {
		Observer o = new Observer(ib, this);
		o.start();
		ib.displayNormal();
	}
	
	public void checkSale() {
		System.out.println("Checking sale");
		if(saleState == saleStates.name) {
			reset();
		} else if (saleState == saleState.product) {
			System.out.println("Someone has failed to scan ok, book his items");
			try {
				SaleManager.sale(userList, productList);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// wait 3 sec befor change back
			ib.setTimeToChangeDisplayBack(System.currentTimeMillis() + 3000);
			reset();
		}
	}
	
	public void changeState(String s) throws SQLException
	{
		ib.displayScanner();
		//timeChangeDisplayLast = System.currentTimeMillis();
		ib.setTimeToChangeDisplayBack(System.currentTimeMillis() + (1000*20));
		
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
		price = 0;
		
		ib.resetAll();
		
		System.out.println("reset to initial state");		
	}
	
	private void changeStateSale(String s) throws SQLException
	{
		switch (saleState) {
		case name:
			if(sale.User.isValidUser(s)) {
				System.out.println(s +" was a valid user");
				sale.User u = sale.User.getUserByName(s);
				userList.add(u);
				ib.addUser(u.getUsername());
				break;
			} else if(sale.Product.isValidProduct(s) && userList.size() != 0)
			{
				System.out.println("Change state to product");
				saleState = saleStates.product;
			} 
			else if (s.equals(constBack)) {
				if (userList.size() > 0) {
					userList.remove(userList.size() - 1);
					ib.userBack();
				}
				break;
			}
			else if (s.equals(constCancel)) {
				reset();
				// change display back after 3 sec
				ib.setTimeToChangeDisplayBack(System.currentTimeMillis() + 3000);
				break;
			}
			else {
				System.out.println(s + " is an unknown barcode");
				break;
			}
		case product:
			if(sale.Product.isValidProduct(s)) {
				System.out.println(s +" was a vaild product");
				sale.Product p = sale.Product.getProductByName(s);
				productList.add(p);
				price += p.getPrice();
				ib.addOrder(p.getName());
				ib.setOrderPrice(price);
			} 
			else if(sale.User.isValidUser(s)) {
				System.out.println("Try to book a user as a product, not valid: WIXXAAA");
				//TODO: Handle this
			} 
			else if (s.equals(constBack)) {
				if (productList.size() > 0) {
					Product p = productList.get(productList.size()-1);
					price -= p.getPrice();
					productList.remove(productList.size() - 1);
					ib.orderBack();
					ib.setOrderPrice(price);
					}
				if (productList.size() == 0) {
					saleState = saleStates.name;
					System.out.println("Change state back to name");
				}
			}
			else if (s.equals(constCancel)) {
				reset();
				// set the display back after 3 sec
				ib.setTimeToChangeDisplayBack(System.currentTimeMillis() + 3000);
			}
			else if (s.equals(constOkay)) { 
				//TODO: change that to OK or something like that
				System.out.println("SALED ... Now dumping to the database");
				// dump the order into the database
				SaleManager.sale(userList, productList);
				
				ib.setTimeToChangeDisplayBack(System.currentTimeMillis() + 3000);
				reset();
			} else {
				System.out.println(s + " is an unknown barcode");
			}
			break;
		default:
			break;
		}
	}

}
