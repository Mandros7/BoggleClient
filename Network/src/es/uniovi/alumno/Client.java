package es.uniovi.alumno;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import es.uniovi.computadores.Network;
import es.uniovi.computadores.mensajes.*;

class NetInput extends Thread{
	Network red;
	NetInput(Network net){
		this.red = net;
	}
	public void run() {
		
		String textMsg = "";
		while (true){
			try {
				byte [] bytes = red.recv();
				textMsg = new String(bytes);
				JSONObject json = (JSONObject) JSONValue.parse(textMsg);
				Message msg = Message.createFromJSON(json);
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

public class Client {

	public static void main(String[] args) throws InterruptedException {
		Network red = new Network();
		System.out.println("Funcionando");
		red.send(("{\"type\" : \"command\",\"subtype\" : \"start\"}").getBytes());
		//STARTCommandMessage start = new STARTCommandMessage();
		NetInput inputThread = new NetInput(red);
		inputThread.start();
		Thread.sleep(3000);
		red.send(("{\"type\" : \"command\",\"subtype\" : \"start\"}").getBytes());
		Thread.sleep(3000);
		red.send(("{\"type\" : \"command\",\"subtype\" : \"word\", \"params\" : {\"word\" : \"prueba\"}}").getBytes());
		Thread.sleep(3000);
		inputThread.join();
	}

	
}
