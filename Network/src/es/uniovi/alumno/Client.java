package es.uniovi.alumno;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

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

	static boolean en_ejecucion = true;
	static boolean astart_recibido = false;
	static ArrayBlockingQueue<String> QuitBuf = new ArrayBlockingQueue<String>(10);
	static MessageBuilder mb = new MessageBuilder();
	private WindowInterface mWindowInterface;
	private NetOutput netOutput;
	private NetInput netInput;
	private ArrayBlockingQueue<Message> OutBuff;
	private ArrayBlockingQueue<Message> InBuff;
	private Socket sock;
	
	public Client(Socket socket, String nick) throws IOException{
		
		this.sock = socket;
		OutBuff = new ArrayBlockingQueue<Message>(10);
		InBuff = new ArrayBlockingQueue<Message>(10);

	    @SuppressWarnings("unused")
		WindowInterface WI = new WindowInterface(this);
		netOutput = new NetOutput(socket,OutBuff);
	    netInput = new NetInput(socket, InBuff);
	    
	    netOutput.start();
	    netInput.start();
	    
	    changeNick(nick);
	    
	    /* Con takeMessages se entra en un bucle del que se saldra al enviar un mensaje Quit o cerrar la ventana 
	    * de la interfaz. Mas adelante se cerraran los hilos de la conexion y la ventana en caso de que se mandase un mensaje QUIT
	    */
		takeMessages();
		
		netOutput.close();
		netInput.close();
		sock.close();
		
		mWindowInterface.frame.dispose();
		
	}
	

	// Funcion que coge mensajes del buffer de entrada
	public void takeMessages(){
		while (Client.en_ejecucion){
					
					if (CheckIfQuit()) {
						Client.en_ejecucion = false;
					}
					else {
						try {
							Message msg = InBuff.take();
							checkResponse(msg);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
	}
	
	/* Metodo implementado para reconocer si se ha enviado un mensaje QUIT mediante el uso de un buffer bloqueante
	 * auxiliar que se comprueba cada vez que se ejecuta "CheckIfQuit"
	 */
	
	public void quitGame() {
		QuitPutBuf();
	}
	
	public void QuitPutBuf() {
		try {
			QuitBuf.put("quit");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void NoQuitPufBuf() {
		try {
			QuitBuf.put("NoQuit");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean CheckIfQuit() {
		String end = null;
		try {
			end = QuitBuf.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (end.equals("quit")) {
			return true;
		}
		return false;
	}
	
	/*
	 * Diferentes funciones para generar comandos de las que dispone el cliente. La interfaz podra acceder a ellos 
	 * al instanciarse un objeto Client dentro de ésta.
	 */
	
	protected void start() {
		try {
			NoQuitPufBuf();
			OutBuff.put(mb.CreateStartCommand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void joinTable(String mesa) {
		try {
			NoQuitPufBuf();
			OutBuff.put(mb.CreateJoinCommand(mesa));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void changeNick(String nick) {
		try {
			NoQuitPufBuf();
			OutBuff.put(mb.CreateNickCommand(nick));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void leaveTable() {
		try {
			NoQuitPufBuf();
			OutBuff.put(mb.CreateLeaveCommand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void listTables() {
		try {
			NoQuitPufBuf();
			OutBuff.put(mb.CreateListCommand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void listTablePlayers() {
		try {
			NoQuitPufBuf();
			OutBuff.put(mb.CreateWhoCommand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void wordFound(WordStats word) {
		try {
			NoQuitPufBuf();
			OutBuff.put(mb.CreateWordCommand(word));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public String eraseBlanks(String texto) {
		texto = texto.replaceAll(" +", " ").trim();
		return texto;
	}
	
	/*
	 * Funcion para reconocer un comando a partir de una linea introducida
	 */
	
	public void sendCommand(String[] datos) {
		String[] comandos = {"start","join","nick","leave","word","list","who","quit"};
		boolean com_incom = false;
		if (datos!=null){
			if (datos[0].charAt(0)=='/') {
	        	datos[0] = datos[0].toUpperCase();
	        	
	        	switch (datos[0]) {
	        	case ("/START"):
					start();
					break;
	        	case ("/NICK"):
	         		if (datos.length!=2) {
	         			mWindowInterface.printError("Numero de arumentos incorrecto.\n" +
	         					"Formato: /nick <nick>"+"\n");
	         		}
	         		else {
	         			changeNick(datos[1]);
	         		}
	          		break;
	         	case ("/JOIN"):
	         		if (datos.length!=2) {
	         			mWindowInterface.printError("Numero de arumentos incorrecto.\n" +
	         					"Formato: /join <mesa>"+"\n");
	         		}
	         		else {
	            		joinTable(datos[1]);
	         		}
	          		break;
	         	case ("/LEAVE"):
	 				leaveTable();
	          		break;
	         	case ("/LIST"):
	      			listTables();
	      			break;
	         	case ("/WHO"):
	          		listTablePlayers();
	              	break;
	         	case ("/QUIT"):
	         		quitGame();
	          		break;
	         	case ("/WORD"):
	         		if (datos.length>1){
	         			if (datos.length>2){
	         				mWindowInterface.printError("Solo se reconoce "+datos[1]+" como palabra"+"\n");
	         			}
	         			if (datos[1].length()<3) {
	         				mWindowInterface.printError("Longitud minima: 3 caracteres."+"\n");
	         			}
	         			else {
	         				wordFound(new WordStats(datos[1]));
	         			}
	         		}
	         		else {
	         			mWindowInterface.printError("Debes escribir una palabra.\n" +
	         					"Formato: /word <palabra>"+"\n");
	         		}
	          		break;
	         	default:
	         		mWindowInterface.printError("Has introducido un comando incorrecto."+"\n");
	         		break;
	        	}
	        }
			 //El usuario escribe una palabra
	        else {
	        	//Para comprobar si el usuario escribio el comando sin /
	        	for (int i=0; i<comandos.length; i++) {
					if (datos[0].equals(comandos[i])) {
						com_incom = true;
					}
				}
				if (com_incom) {
					mWindowInterface.printError("Has escrito el comando sin \"/\"."+"\n");
					com_incom = false;
				}
				else {
					if (datos.length>1){
						mWindowInterface.printError("Solo se reconoce \""+datos[0]+"\" como palabra"+"\n");
	     			}
	     			if (datos[0].length()<3) {
	     				mWindowInterface.printError("Longitud minima: 3 caracteres."+"\n");
	     			}
	     			else {
	     				wordFound(new WordStats(datos[0]));
	     			}
				}
	        }
		}
	}
	
	
	/*
	 * Funcion para clasificar un mensaje extraido mediante la funcion takeMessages
	 * Una vez decidido se solicita una accion correspondiente a la interfaz (que se asigno mediante la funcion
	 * addListener)
	 */
private void checkResponse(Message msg) {
			String mesa = Client.getTABLE();
			if (mWindowInterface!=null){
				if (msg instanceof NotificationMessage) {
					if (msg instanceof ASTARTNotificationMessage){
						mWindowInterface.printASTART(((ASTARTNotificationMessage)msg).getMatrix(),((ASTARTNotificationMessage) msg).getDurationSecs());
					}
					if (msg instanceof APLAYEDNotificationMessage){
						String nick = ((APLAYEDNotificationMessage) msg).getNick();
						int length = ((APLAYEDNotificationMessage) msg).getWordLength();
						boolean discoveredPlayed = ((APLAYEDNotificationMessage) msg).isAlreadyDiscovered();
						mWindowInterface.printAPLAYED(nick,length,discoveredPlayed);;
						
					}
					if (msg instanceof AENDNotificationMessage) {
						mWindowInterface.printAEND(((AENDNotificationMessage)msg).getPlayers());
					}
					if (msg instanceof AJOINNotificationMessage) {
						String nick_usuario = ((AJOINNotificationMessage)msg).getNick();
			
						mWindowInterface.printAJOIN(nick_usuario);
					}
					if (msg instanceof ANICKNotificationMessage) {
						String new_nick = ((ANICKNotificationMessage)msg).getNewNick();
						String old_nick = ((ANICKNotificationMessage)msg).getOldNick();
						
						mWindowInterface.printANICK(new_nick,old_nick);
					}
					if (msg instanceof ALEAVENotificationMessage) {
						String nick_leave = ((ALEAVENotificationMessage)msg).getNick();
			
						mWindowInterface.printALEAVE(nick_leave);
					}
					
				}
				if (msg instanceof ResponseMessage){
					if (((ResponseMessage) msg).isError()){
						String error = "ERROR:" + ((ResponseMessage) msg).getErrorDescription();
						mWindowInterface.printError(error);
					}
					else{
						if (msg instanceof SWORDResponseMessage){
							WordStats StatsPalabra = ((SWORDResponseMessage) msg).getWord();
							
							mWindowInterface.printSWORD(StatsPalabra);
						}
						if (msg instanceof SJOINResponseMessage) {
							mesa = ((SJOINResponseMessage)msg).getTable();
			
							mWindowInterface.printSJOIN(mesa);
						}
						if (msg instanceof SLEAVEResponseMessage) {
							mWindowInterface.printSLEAVE();
						}
						if (msg instanceof SNICKResponseMessage) {
							String nick = ((SNICKResponseMessage)msg).getNick();
			
							mWindowInterface.printSNICK(nick);
						}
						if (msg instanceof SLISTResponseMessage) {
							
							mWindowInterface.printSLIST(((SLISTResponseMessage)msg).getTables());
							
						}
						if (msg instanceof SSTARTResponseMessage){
			
							mWindowInterface.printSSTART();
						}
						if (msg instanceof SWHOResponseMessage) {
							
							mWindowInterface.printSWHO(((SWHOResponseMessage)msg).getNicks());
						}
					}
				}		
		}
		
	}

	/*
	 * Se definen los metodos de los que constara una interfaz de salida dada. Las interfaces implementaran 
	 * este codigo.
	 */

public static interface OutputInterface {
		
		public void printASTART(Character[][] matriz,int duration);
		
		public void printAEND(ArrayList<PlayerStats> players);
		
		public void printAPLAYED(String nick, int length, boolean discoveredPlayed);
		
		public void printAJOIN(String nick_usuario);
		
		public void printANICK(String new_nick, String old_nick);
		
		public void printALEAVE(String nick_leave);
		
		public void printSWORD(WordStats StatsPalabra);
		
		public void printSJOIN(String table);
		
		public void printSLEAVE();
		
		public void printSNICK(String nick);
		
		public void printSLIST(ArrayList<TableStats> tables);
		
		public void printSSTART();
		
		public void printSWHO(String[] array_jugadores);
		
		public void printError(String error);
		
	}


/* 
 * Funcion para asignar una interfaz al cliente y que este pueda llamar a metodos de la interfaz 
 * para imprimir resultados y respuestas
 */

public void addListener(WindowInterface WI){
	mWindowInterface = WI;
}
		
	
}

