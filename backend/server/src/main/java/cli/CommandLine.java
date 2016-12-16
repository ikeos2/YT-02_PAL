package cli;

import java.util.Scanner;

public class CommandLine extends Thread{
	
	public void run(){
		Scanner in = new Scanner(System.in);
		while(true){
			String input = in.nextLine();
			if(input.equalsIgnoreCase("exit")){
				in.close();
				System.exit(1);
			}
		}
		
	}
	/**
	 * This class will be used to help control the server on server
	 * Commands:
	 * Status - shows number of DB connections, average API calls, other info we log
	 * Stop - shuts off the server
	 * Restart - restarts the server
	 */
}