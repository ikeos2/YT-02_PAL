package chat_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler extends Thread {

		//system info
		BufferedReader in; //from client
		PrintWriter out; //To client
		String input;
		Socket socket;
		String info;
		Metahandler meta;
		boolean metaListen;
		boolean open;
		//user info
		boolean loggedin;
		String ID;

		public Handler(Socket socket, Metahandler meta) {
			this.socket = socket;
			info = socket.toString();
			this.meta = meta;
			loggedin = false;
			metaListen = false;
			try{
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		//Send data to client
		public void receiveMessage(String message){
			if(loggedin){
				out.println(message);
			}
		}
		
		public void sendMessages(String message){
			if(message.trim().length() < 1) return; //no data to send!
			for(Handler h : meta.connections){
				//if(h == this){ /*Do nothing if we connect to ourself*/}
				//else
					h.receiveMessage("\n " + ID + ": " + message);
			}
			if(metaListen) System.out.println("\n " + ID + ": " + message);
		}

		/**
		 * Toggles server based listening
		 * @return the state of the toggle after the call
		 */
		public boolean toggleListen(){
			metaListen = !metaListen;
			return metaListen;
		}
		
		public void run() {
			try {
				if(!meta.muted) System.out.println("Connection established: " + info);
				//out.println("Hello!");
				
				ID = login();
				
				open = true;
				while (open) {
					input = in.readLine();
					if (input.toLowerCase().equals("quit!") || Thread.interrupted()) {
						open = false;
					}
					if( input.toLowerCase().equals("clear")){
						out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
						input = "";
					}
					
					sendMessages(input);
				}

				out.println("Thank you!");
				shutdown();
				meta.closeConnection(this);
			} catch (Exception e) {
				System.out.println("Client aborted");
				e.printStackTrace();
				shutdown();
			} finally {
				System.out.println("Connection closed: " + info);
			}
		}
		
		/**
		 * Verifies authentication
		 * TODO: Make this hook into database, perhaps just look at token?
		 * this method is different than than API Login.
		 * @return the ID of the user
		 */
		public String login(){
			
			out.println("Please enter your user ID:");
			String ID ="";
			while(ID.length() < 1){
				try {
					ID = in.readLine();
					for(Handler h : meta.connections){
						if(h.ID == ID){
							out.println("This ID has been taken, please choose another");
							ID = "";
						}
					}
					Thread.sleep(100);
				} catch (Exception e) {
					out.println("Please enter a number!");
					//e.printStackTrace();
				}
			}
			loggedin = true;
			sendMessages(ID +" has joined!");
			return ID;
		}
		
		public void shutdown(){
			try {
				open = false;
				loggedin = false;
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}