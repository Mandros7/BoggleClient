package es.uniovi.alumno;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
//import java.util.ArrayList;
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
	final ArrayBlockingQueue<Message> InBuf;
	final ArrayBlockingQueue<Message> OutBuf;
	static MessageBuilder mb = new MessageBuilder();
	
	public Client(ArrayBlockingQueue<Message> InBuf, ArrayBlockingQueue<Message> OutBuf){
		this.InBuf = InBuf;
		this.OutBuf = OutBuf;
	}
	
	public static void main(String[] args) throws InterruptedException, UnknownHostException, IOException {
		if (args.length!=3){
			System.out.println("Client <NICK> <IP> <PORT>");
			return;
		}
		
		String nick = args[0];
		String IP = args[1];
		int port = Integer.parseInt(args[2]);
		Socket socket = new Socket(IP, port); 
		
		//LOGGER.setLevel(Level.WARNING);
		System.out.println("Funcionando");
		ArrayBlockingQueue<Message> OutBuff = new ArrayBlockingQueue<Message>(10);
		ArrayBlockingQueue<Message>	InBuff = new ArrayBlockingQueue<Message>(10);
		
		NetOutput salidaRed = new NetOutput(socket,OutBuff);
	    NetInput entradaRed = new NetInput(socket, InBuff);
	    
	    Client BC = new Client(InBuff, OutBuff);
	    //WindowInterface WI = new WindowInterface(BC);
	    
	    salidaRed.start();
	    entradaRed.start();
	    
	    NICKCommandMessage nick_inicial = new NICKCommandMessage(nick);
	    OutBuff.put(nick_inicial);
	    
	    while(en_ejecucion){
	    
	    }
	    socket.close();
	}
	
	
	public void start() {
		try {
			OutBuf.put(mb.CreateStartCommand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void joinTable(String mesa) {
		try {
			OutBuf.put(mb.CreateJoinCommand(mesa));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void changeNick(String nick) {
		try {
			OutBuf.put(mb.CreateNickCommand(nick));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void leaveTable() {
		try {
			OutBuf.put(mb.CreateLeaveCommand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void listTables() {
		try {
			OutBuf.put(mb.CreateListCommand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void listTablePlayers() {
		try {
			OutBuf.put(mb.CreateWhoCommand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void wordFound(WordStats word) {
		try {
			OutBuf.put(mb.CreateWordCommand(word));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public String eraseBlanks(String texto) {
		texto = texto.replaceAll(" +", " ").trim();
		return texto;
	}

	
public static interface OutputInterface {
		
		public void sendCommand(String [] datos);
		
		public String printASTART(Character[][] matriz);
		
		public String printAEND(ArrayList<PlayerStats> players);
		
		public String printAPLAYED(String nick, int length, boolean discoveredPlayed);
		
		public String printAJOIN(String nick_usuario);
		
		public String printANICK(String new_nick, String old_nick);
		
		public String printALEAVE(String nick_leave);
		
		public String printSWORD(WordStats StatsPalabra);
		
		public String printSJOIN(String table);
		
		public String printSLEAVE();
		
		public String printSNICK(String nick);
		
		public String printSLIST(ArrayList<TableStats> tables);
		
		public String printSSTART();
		
		public String printSWHO(String[] array_jugadores);
		
		public String checkResponse(Message msg);
		
		
	}
	
	
}

