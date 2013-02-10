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
		ProcessBuilder pb = new ProcessBuilder("/home/daniel/code/prog3");
		
		//pb.redirectErrorStream(true);
		
		ServiceManager sm = new ServiceManager();	
        try {
            Process p = pb.start();
            String s;
            BufferedReader stdout = new BufferedReader (
                new InputStreamReader(p.getInputStream()));
            
            while ((s = stdout.readLine()) != null) {
                System.out.println(s);
                sm.changeState(s);
            }
            
            System.out.println("Exit value: " + p.waitFor());
            p.getInputStream().close();
            p.getOutputStream().close();
            p.getErrorStream().close();
         } catch (Exception ex) {
            ex.printStackTrace();
        }
	}


}
