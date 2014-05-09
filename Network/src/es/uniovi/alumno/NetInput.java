package es.uniovi.alumno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import es.uniovi.computadores.mensajes.Message;

class NetInput extends Thread {
	// Hilo de entrada de datos desde la red

	Socket socket;
	String StringRed = "";
	ArrayBlockingQueue<Message> InBuf;
	volatile boolean en_ejecucion = true;

	public void close() throws IOException{
		this.interrupt();
	}
	
	NetInput(Socket n, ArrayBlockingQueue<Message> abq){
		this.socket = n;
		this.InBuf = abq;
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
				Message msg = Message.createFromJSON(json);
				try {
					InBuf.put(msg);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
	
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
		}
		try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}