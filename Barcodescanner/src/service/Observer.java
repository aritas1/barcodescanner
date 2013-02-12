package service;

import communicate.InfoBeamer;

public class Observer extends Thread {
	
	InfoBeamer ib = null;
	ServiceManager sm = null;
	
	public Observer(InfoBeamer ib, ServiceManager sm) {
		this.ib = ib;
		this.sm = sm;
	}
	
	public void run() {
		while (true) {
			try {
				if(ib.checkDisplaySwitchBack()) {
					// check if maybe someone have failed to scan ok
					sm.checkSale();
					// set the timer to 0 so it wount check again
					ib.setTimeToChangeDisplayBack(0);
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
