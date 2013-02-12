package communicate;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class InfoBeamer {

	// echo "barcodebeamer/scanner/userAdd:Daniel" | nc -u localhost 4444

	
	private String host = "192.168.1.111";
    private int port = 4444;
	private String nodeCode = "kitchen_cycler/barcodebeamer/scanner/";
	
	long timeChangeDisplayLast = 0; //System.currentTimeMillis();
	long timeToChangeDisplayBack = 0; //System.currentTimeMillis();
	
	public long getTimeToChangeDisplayBack() {
		return timeToChangeDisplayBack;
	}


	public void setTimeToChangeDisplayBack(long timeToChangeDisplayBack) {
		this.timeToChangeDisplayBack = timeToChangeDisplayBack;
	}


	public boolean checkDisplaySwitchBack() {
		if(timeToChangeDisplayBack != 0 && timeToChangeDisplayBack <= System.currentTimeMillis()) {
			displayNormal();
			return true;
		}
		return false;
	}
	
	
	public InfoBeamer()
	{
		resetAll();
	}
	
	public void displayScanner()
	{
		//kitchen_cycler/interrupt/tonode:barcodebeamer
		sendMessage("kitchen_cycler/interrupt/tonode:barcodebeamer");
	}
	
	public void displayNormal()
	{
		//kitchen_cycler/interrupt/tonode:barcodebeamer
		sendMessage("kitchen_cycler/interrupt/clear:");
	}
	
	public void addUser(String name)
	{
		sendMessage(nodeCode +"userAdd:"+name);
	}
	
	public void addOrder(String name)
	{
		sendMessage(nodeCode +"orderAdd:"+name);
	}
	
	public void setOrderPrice(String price)
	{
		sendMessage(nodeCode +"setPrice:"+price);
	}
	
	public void setOrderPrice(int price)
	{
		int euro = (price / 100);
		int cent = price % 100;
		sendMessage(nodeCode +"setPrice:"+euro+"."+cent);
	}
	
	public void userBack() {
		sendMessage(nodeCode +"userUndo:");
	}
	
	public void orderBack() {
		sendMessage(nodeCode +"orderUndo:");
	}
	
	public void resetUser() {
		sendMessage(nodeCode +"userClear:");
	}
	
	public void resetOrder() {
		sendMessage(nodeCode +"orderClear:");
	}
	
	public void resetAll() {
		resetOrder();
		resetUser();
		setOrderPrice("0.0");
	}
	
	
	private void sendMessage(String messageIn) {
		try {
		      byte[] message = messageIn.getBytes();

		      // Get the internet address of the specified host
		      InetAddress address = InetAddress.getByName(host);

		      // Initialize a datagram packet with data and address
		      DatagramPacket packet = new DatagramPacket(message, message.length,
		          address, port);

		      // Create a datagram socket, send the packet through it, close it.
		      DatagramSocket dsocket = new DatagramSocket();
		      
		      System.out.println("Send message: " + messageIn);
		      
		      dsocket.send(packet);
		      dsocket.close();
		    } catch (Exception e) {
		      System.err.println(e);
		    }
	}

}
