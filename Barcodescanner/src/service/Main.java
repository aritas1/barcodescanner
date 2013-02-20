package service;

import java.io.*;

import com.sun.media.sound.FFT;

import sun.misc.IOUtils;

import java.util.*;

import java.sql.SQLException;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, SQLException {
		
		if(args.length != 2) {
			System.out.println("Usage: java service.Main /path/to/cFile cFileName");
			System.exit(0);
		}
	
		// clean up running processes
		ProcessBuilder pb2 = new ProcessBuilder("killall", args[1]);
		pb2.start();
		
		
		ProcessBuilder pb = new ProcessBuilder(args[0]+ args[1]);
		
		//pb.redirectErrorStream(true);
		String s;
		ServiceManager sm = new ServiceManager();
		Process p = pb.start();
		ProcMon pm = ProcMon.create(p);
		BufferedReader stdout;
		while (true) {		
				
	        try {
	        	if (pm.isComplete()) {
		            p = pb.start();
	        	}
	            
	            stdout = new BufferedReader (
	                new InputStreamReader(p.getInputStream()));
	            
	            while ((s = stdout.readLine()) != null) {
	                //System.out.println(s);
	                sm.changeState(s);
	            }
	            
	            //System.out.println("Exit value: " + p.waitFor());
	            p.getInputStream().close();
	            p.getOutputStream().close();
	            p.getErrorStream().close();
	            stdout.close();
	            //p.destroy();
	         } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        //System.out.println("Program ended, try to restart and read values. Is the scanner connected?");
	    	Thread.sleep(2000);
		}
	}


}
