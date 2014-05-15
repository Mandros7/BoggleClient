package es.uniovi.alumno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import es.uniovi.computadores.mensajes.Message;
import es.uniovi.computadores.mensajes.NotificationMessage;

class NetInput extends Thread {
	// Hilo de entrada de datos desde la red

	Socket socket;
	String StringRed = "";
	ArrayBlockingQueue<Message> InBuf;
	volatile boolean en_ejecucion = true;
	Client bc;

	public void close() throws IOException{
		this.interrupt();
	}
	
	NetInput(Socket n, ArrayBlockingQueue<Message> abq, Client bc){
		this.socket = n;
		this.InBuf = abq;
		this.bc = bc;
	}
	
	public void run(){
		
		while (Client.en_ejecucion) {

			try {
				StringRed = "";
				int cont = 0;
				BufferedReader in =
				        new BufferedReader(
				            new InputStreamReader(socket.getInputStream()));
				Character c = (char) in.read();
				if (c=='{'){
					StringRed = StringRed+c;
					cont ++;
				do {
					c = (char) in.read();
					switch (c) {
					case ('{'):
						cont ++;
						break;

					case ('}'):
						cont --;
						break;
				}
					StringRed = StringRed+c;
				} while (cont>0);
				}
				
				JSONObject json = (JSONObject) JSONValue.parse(StringRed);
				try{
					Message msg = Message.createFromJSON(json);
					if (msg instanceof NotificationMessage) {
						Client.NoQuitPufBuf();
					}
					InBuf.put(msg);
				} catch (NullPointerException e){
					//System.out.println("ERROR: Se perdio la conexion con el servidor");
					bc.lostConnection();
					break;
				}
				catch (InterruptedException e) {
					//e.printStackTrace();
				}
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
		}
		try {
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}