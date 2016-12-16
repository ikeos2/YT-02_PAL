package api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

	public static void main(){
		log(new Exception("Test error!"));
	}
	
	public static void log(Exception x){
		try{
			Calendar calobj = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("log", true));
			bw.write( df.format(calobj.getTime())+" | "+x.toString() + "\n");
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
