package es.uniovi.alumno;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
//import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import es.uniovi.computadores.mensajes.*;

public class Client {
	/*private final static Logger LOGGER = Logger.getLogger(Network.class
			.getName());
	*/
	private static Character[][] MATRIX;
	private static String NICK;
	private static String TABLE;

	public static Character[][] getMATRIX() {
		return MATRIX;
	}

	public static void setMATRIX(Character[][] mATRIX) {
		MATRIX = mATRIX;
	}

	public static String getNICK() {
		return NICK;
	}

	public static void setNICK(String nICK) {
		NICK = nICK;
	}

	public static String getTABLE() {
		return TABLE;
	}

	public static void setTABLE(String tABLE) {
		TABLE = tABLE;
	}

	
	public static void main(String[] args) throws InterruptedException, UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 12345); 
		//LOGGER.setLevel(Level.WARNING);
		System.out.println("Funcionando");
		ArrayBlockingQueue<Message> OutBuff = new ArrayBlockingQueue<Message>(10);
		ArrayBlockingQueue<Message>	InBuff = new ArrayBlockingQueue<Message>(10);
		UserOutput salidaUsuario = new UserOutput(InBuff);
		NetOutput salidaRed = new NetOutput(socket,OutBuff);
	    UserInput entradaUsuario = new UserInput(OutBuff);
	    NetInput entradaRed = new NetInput(socket, InBuff);
	    salidaUsuario.start();
	    entradaUsuario.start();
	    salidaRed.start();
	    entradaRed.start();
	    entradaUsuario.join(); 
	    entradaRed.close();
	    salidaRed.close();
	    salidaUsuario.close();
	    socket.close();
	}

	
}

class NetInput extends Thread {
	// Hilo de entrada de datos desde la red

	Socket socket;
	String StringRed = "";
	ArrayBlockingQueue<Message> InBuf;
	volatile boolean en_ejecucion = true;

	public void close() throws IOException{
		en_ejecucion = false;
		this.socket.close();
		this.interrupt();
	}
	
	NetInput(Socket n, ArrayBlockingQueue<Message> abq){
		this.socket = n;
		this.InBuf = abq;
	}
	
	public void run(){
		
		while (en_ejecucion) {

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
				
				
			//Se crea un objeto Message, que nos servira para identificar el tipo de respuesta que nos ha enviado
			//el servidor
		}
	}
	
}

class NetOutput extends Thread {
	// Hilo de salida de datos hacia la red
	
	Socket socket;
	ArrayBlockingQueue<Message> OutBuf;
	Message msg;
	volatile boolean en_ejecucion = true;
	
	public void close() throws IOException{
		en_ejecucion = false;
		this.socket.close();
		this.interrupt();
	}
	
	NetOutput(Socket n, ArrayBlockingQueue<Message> abq){
		
		this.socket = n;
		this.OutBuf = abq;
	}
	
	public void run(){
		
		while (en_ejecucion) {
			
			//Se extrae el objeto Comando del buffer circular
			try {
				msg = OutBuf.take();
				try {
					socket.getOutputStream().write(msg.toJSON().toString().getBytes(), 0, msg.toJSON().toString().getBytes().length);
				} catch (IOException e) {
					//e.printStackTrace();
				}
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}
}

class UserInput extends Thread{
	// Hilo de entrada por teclado del usuario
	ArrayBlockingQueue<Message> OutBuf;
	String entrada;
	UserInput(ArrayBlockingQueue<Message> abq){
		this.OutBuf = abq;
    }
	
    public void run() { 
        boolean funcionando=true;
    	InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
        while (funcionando){
			try {
				entrada = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
            System.out.println(entrada);
            entrada = entrada.toUpperCase();
            if (entrada.length()>2){
                String [] datos = entrada.split(" ");
                if (entrada.charAt(0)=='/'){
                    switch (datos[0]) {
                        case ("/START"):		                   
                            STARTCommandMessage start = new STARTCommandMessage();
							try {
								OutBuf.put(start);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							break;
							
                        case ("/QUIT"):
                        	funcionando=false;
                        	break;

                        case ("/WORD"):
                        	if (datos.length>1){
	                        	if (datos.length>2){
	                        		System.out.println("Solo se reconoce "+datos[1]+" como palabra");
	                        	}
	                        	
		                            WORDCommandMessage word = new WORDCommandMessage(new WordStats(datos[1]));
		                            try {
										OutBuf.put(word);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}	
                        	}
                        	break;
                        default:
                            break;
                    }
           
                }
                else{
                	if (funcionando){
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
      try {
		isr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

class UserOutput extends Thread{
	// Hilo de salida de datos por pantalla
	ArrayBlockingQueue<Message> InBuf;
	volatile boolean en_ejecucion = true;

	public void close(){
		en_ejecucion = false;
		this.interrupt();
	}
	UserOutput(ArrayBlockingQueue<Message> InBuf){
		this.InBuf = InBuf;
	}
	public void run() {
		while (en_ejecucion){
			try {
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
										" ha obtenido una puntuaciï¿½ï¿½ï¿½n de " +
										((AENDNotificationMessage)msg).getPlayers().get(i).getScore() + " puntos");
							i++;
						}
					}
					if (msg instanceof AJOINNotificationMessage) {
						String nick_usuario = ((AJOINNotificationMessage)msg).getNick();
						System.out.println("El usuario " +
								nick_usuario +
									" se ha unido a la mesa");
					}
					if (msg instanceof ANICKNotificationMessage) {
						String new_nick = ((ANICKNotificationMessage)msg).getNewNick();
						String old_nick = ((ANICKNotificationMessage)msg).getOldNick();
						System.out.println("Nick anterior: " + old_nick +
								"\n" +
									"Nick nuevo: " + new_nick);
						
					}
					if (msg instanceof ALEAVENotificationMessage) {
						String nick_leave = ((ALEAVENotificationMessage)msg).getNick();
						System.out.println("El usuario " +
								nick_leave +
									" ha abandonado la mesa.");
						
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
						if (msg instanceof SJOINResponseMessage) {
							String mesa = ((SJOINResponseMessage)msg).getTable();
							System.out.println("Te has unido a la mesa " + mesa);
						}
						if (msg instanceof SLEAVEResponseMessage) {
							System.out.println("Has abadonado la mesa.");
						}
						if (msg instanceof SNICKResponseMessage) {
							String nick = ((SNICKResponseMessage)msg).getNick();
							System.out.println("Tu nuevo nick es: " + nick);
							//VARIABLE NICK
						}
						if (msg instanceof SLISTResponseMessage) {
							String añadido;
							int tam = ((SLISTResponseMessage)msg).getTables().size();
							
							for (int i=0; i<tam; i++) {
								String info_tables = "En la mesa " +
										((SLISTResponseMessage)msg).getTables().get(i).getName() +
											" hay " +
											((SLISTResponseMessage)msg).getTables().get(i).getPlayerCount() +
												" jugadores ";
								if (((SLISTResponseMessage)msg).getTables().get(i).getAlreadyPlaying()) {
									añadido = " y la partida ya se ha iniciado.";
								}
								else {
									añadido = " y la partida aún no se ha iniciado";
								}
								System.out.println(info_tables + añadido);
							}
							
						}
						if (msg instanceof SWHOResponseMessage) {
							int tam = ((SWHOResponseMessage)msg).getNicks().length;
							if (tam==1) {
								System.out.println("El jugador de esta mesa es: " + ((SWHOResponseMessage)msg).getNicks()[0]);
							}
							else {
								String jugadores = "Los jugadores de esta mesa son: ";
								for (int i=0; i<tam; i++) {
									if (i==0) {
										jugadores = jugadores + ((SWHOResponseMessage)msg).getNicks()[i];
									}
									if (i==tam-1) {
										jugadores = jugadores + " y " + ((SWHOResponseMessage)msg).getNicks()[i];									
									}
									jugadores = jugadores + ", " + ((SWHOResponseMessage)msg).getNicks()[i];
								}
								
							}
							
						}
					}
				}
				
				
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}}
	}
}

