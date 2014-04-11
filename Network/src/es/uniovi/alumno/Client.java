package es.uniovi.alumno;
import java.io.*;
//import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import es.uniovi.computadores.Network;
import es.uniovi.computadores.mensajes.*;

public class Client {
	
	private class UserInput extends Thread{
		// Hilo de entrada por teclado del usuario
		ArrayBlockingQueue<Message> OutBuf;
		String entrada;
		UserInput(ArrayBlockingQueue<Message> abq){
			this.OutBuf = abq;
        }
		
        public void run() { 
            boolean started=false;
            while (true){
            	InputStreamReader isr = new InputStreamReader(System.in);
    			BufferedReader br = new BufferedReader(isr);
    				try {
						entrada = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
                    System.out.println(entrada);
                    if (entrada.length()>2){
	                    String [] datos = entrada.split(" ");
	                    if (entrada.charAt(0)=='/'){
		                    switch (datos[0]) {
		                        case ("/START"):
		                            STARTCommandMessage start = new STARTCommandMessage();
		                        	started = true;
								try {
									OutBuf.put(start);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
		                        default:
		                            break;
		                    }
	               
	                    }
	                    else{
	                    	if (started){
	                            WORDCommandMessage word = new WORDCommandMessage(new WordStats(datos[0]));
	                            try {
									OutBuf.put(word);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
	                    	}
	                    	else {
	                            System.out.println("La partida aun no se ha iniciado.");
	                        }
	                    }
                    }
           } 
        }
	}

	private class NetOutput extends Thread {
		// Hilo de salida de datos hacia la red
		Network red;
		ArrayBlockingQueue<Message> OutBuf;
		Message msg;
		
		NetOutput(Network n, ArrayBlockingQueue<Message> abq){
			this.red = n;
			this.OutBuf = abq;
		}
		
		public void run(){
			
			while (true) {
				
				//Se extrae el objeto Comando del buffer circular
				try {
					msg = OutBuf.take();
					red.send(msg.toJSON().toString().getBytes());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class NetInput extends Thread {
		// Hilo de entrada de datos desde la red
		Network red;
		byte[] BytesRed;
		String StringRed;
		ArrayBlockingQueue<Message> InBuf;
		
		NetInput(Network n, ArrayBlockingQueue<Message> abq){
			this.red = n;
			this.InBuf = abq;
		}
		
		public void run(){
			
			while (true) {
				
				try {
					//Se espera a recibir una cadena de byte enviados del servidor
					BytesRed = red.recv();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//Se crea un objeto Message, que nos servirï¿½ para identificar el tipo de respuesta que nos ha enviado
				//el servidor
				StringRed = new String(BytesRed);
				JSONObject json = (JSONObject) JSONValue.parse(StringRed);
				Message msg = Message.createFromJSON(json);
				try {
					InBuf.put(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class UserOutput extends Thread{
		// Hilo de salida de datos por pantalla
		ArrayBlockingQueue<Message> InBuf;
		UserOutput(ArrayBlockingQueue<Message> InBuf){
			this.InBuf = InBuf;
		}
		public void run() {
			while (true){
				try {
					//byte [] bytes = red.recv();
					//textMsg = new String(bytes);
					//JSONObject json = (JSONObject) JSONValue.parse(textMsg);
					//Message msg = Message.createFromJSON(json);
					Message msg = InBuf.take();
					if (msg instanceof NotificationMessage) {
						if (msg instanceof ASTARTNotificationMessage){
							System.out.println("Se ha iniciado la partida");
							System.out.println("------------");
							Character[][] MATRIX = ((ASTARTNotificationMessage) msg).getMatrix();
							for (int i=0; i<MATRIX.length;i++){
								String line = "";
								for (int j = 0; j<MATRIX.length;j++){
									line = line + (MATRIX[i][j]);
									if (j!=MATRIX.length-1){
										line = line + ", ";
									}
								}
								System.out.println(line);
							}
							System.out.println("------------");
						}
						if (msg instanceof APLAYEDNotificationMessage){
							String nick = ((APLAYEDNotificationMessage) msg).getNick();
							int length = ((APLAYEDNotificationMessage) msg).getWordLength();
							boolean discoveredPlayed = ((APLAYEDNotificationMessage) msg).isAlreadyDiscovered();
							if (!discoveredPlayed){
								System.out.println("El jugador "+nick+" ha encontrado una nueva palabra de "+length+" letras.");
							}
							else{
								System.out.println("El jugador "+nick+" ha encontrado una palabra de "+length+" letras que ya habia sido descubierta.");
							}
						}
						if (msg instanceof AENDNotificationMessage) {
							int i = 0;
							while (i<((AENDNotificationMessage)msg).getPlayers().size()) {
								System.out.println("El jugador " +
										((AENDNotificationMessage)msg).getPlayers().get(i).getNick() +
											" ha obtenido una puntuación de " +
											((AENDNotificationMessage)msg).getPlayers().get(i).getScore() + " puntos");
								i++;
							}
						}
					}
					if (msg instanceof ResponseMessage){
						if (((ResponseMessage) msg).isError()){
							String error = ((ResponseMessage) msg).getErrorDescription();
							System.out.println("ERROR: "+error);
						}
						else{
							if (msg instanceof SWORDResponseMessage){
								WordStats StatsPalabra = ((SWORDResponseMessage) msg).getWord();
								String word = StatsPalabra.getWord();
								boolean discoveredResponse = StatsPalabra.isAlreadyDiscovered();
								if (!discoveredResponse){
									System.out.println("Se ha registrado la siguiente palabra nueva: "+word);
								}
								else {
									System.out.println("La palabra "+word+" ya existia.");
								}
							}
						}
					}
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
		}
	}
	public Client(Network red) throws InterruptedException {
		System.out.println("Funcionando");
		ArrayBlockingQueue<Message> OutBuff = new ArrayBlockingQueue<Message>(10);
		ArrayBlockingQueue<Message>	InBuff = new ArrayBlockingQueue<Message>(10);
		UserOutput salidaUsuario = new UserOutput(InBuff);
		NetOutput salidaRed = new NetOutput(red,OutBuff);
	    UserInput entradaUsuario = new UserInput(OutBuff);
	    NetInput entradaRed = new NetInput(red, InBuff);
	    salidaUsuario.start();
	    entradaUsuario.start();
	    salidaRed.start();
	    entradaRed.start();
	    entradaRed.join();
	    salidaRed.join();
	    salidaUsuario.join();
	    entradaUsuario.join(); 
		red.close();
	}
	
	public static void main(String[] args) throws InterruptedException {
		Network red = new Network();
		Client cli = new Client(red);
	}

	
}

