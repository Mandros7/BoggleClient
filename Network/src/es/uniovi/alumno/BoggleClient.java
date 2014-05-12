package es.uniovi.alumno;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class BoggleClient {
	public static void main(String[] args) throws UnknownHostException, IOException {
		if (args.length!=3){
			System.out.println("Client <NICK> <IP> <PORT>");
			return;
		}
		
		String nick = args[0];
		String IP = args[1];
		int port = Integer.parseInt(args[2]);
		Socket socket = new Socket(IP, port); 
		
		@SuppressWarnings("unused")
		Client BC = new Client(socket,nick);
	    
		
	}
}