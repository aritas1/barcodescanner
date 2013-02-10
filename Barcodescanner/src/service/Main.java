package service;

import java.io.*;

import com.sun.media.sound.FFT;

import sun.misc.IOUtils;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
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
		/** // try to start the c program and access the stdout
		Process p;
		 InputStream inS = null;
		try {
			p = new ProcessBuilder("/home/daniel/code/prog3").redirectOutput(destination)
			
			// p = Runtime.getRuntime().exec("/home/daniel/code/prog3");
			Thread.sleep(1000);
			inS = p.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println("barcode reader started");
		
		System.out.println("inS Available: " + inS.available());
		
		//StringWriter stw = new StringWriter();
		InputStreamReader isr = new InputStreamReader(inS);
		BufferedReader br = new BufferedReader(isr);
		
		String line = "";
		
		while (line == "") {
			try {
				System.out.println("R");
				System.out.println(br.read());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("recived: " + line);
		}
		
		System.out.println("recived line");
		
		System.out.println("recived: " + line);
		//String theString = writer.toString();
	}*/

}
