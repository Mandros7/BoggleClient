package es.uniovi.alumno;
import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import es.uniovi.computadores.Network;
import es.uniovi.computadores.mensajes.*;

public class Client {
	
	private class UserOutput extends Thread{
        Network red;
        UserOutput(Network net){
            this.red = net;
        }
        public void run() { 
            System.out.println("NetOutput Funcionando");
            boolean started=false;
            while (true){
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                try {
                    String s = bufferRead.readLine();
                    System.out.println(s);
                    String [] datos = s.split(" ");
                    switch (datos[0]) {
                        case ("/START"):
                            STARTCommandMessage start = new STARTCommandMessage();
                            red.send(start.toJSON().toString().getBytes());
                            started = true;
                        case ("/WORD"):
                            if (started){
                                if (datos.length<2){
                                    System.out.println("Introduce una palabra como segundo argumento.");
                                }
                                else{
                                WORDCommandMessage word = new WORDCommandMessage(new WordStats(datos[1]));
                                red.send(word.toJSON().toString().getBytes());
                                }
                            }
                            else {
                                System.out.println("La partida aun no se ha iniciado.");
                                break;
                            }
                        default:
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
} 

	private class UserInput extends Thread{
		Network red;
		UserInput(Network net){
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
	public Client(Network red) throws InterruptedException {
		System.out.println("Funcionando");
		UserInput inputThread = new UserInput(red);
	    UserOutput outputThread = new UserOutput(red);
	    inputThread.start();
	    outputThread.start();
	    inputThread.join();
	    outputThread.join(); 
		red.close();
	}

	
}

