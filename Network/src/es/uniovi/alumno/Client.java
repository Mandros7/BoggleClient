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

	
}

class NetInput extends Thread {
	// Hilo de entrada de datos desde la red

	Socket socket;
	String stringNet = "";
	ArrayBlockingQueue<Message> InBuf;
	volatile boolean en_ejecucion = true;

	public void close() throws IOException{
		en_ejecucion = false;
		this.interrupt();
	}
	
	NetInput(Socket n, ArrayBlockingQueue<Message> abq){
		this.socket = n;
		this.InBuf = abq;
	}
	
	public void run(){
		
		while (en_ejecucion) {

				try {
					stringNet = "";
					int cont = 0;
					BufferedReader in =
					        new BufferedReader(
					            new InputStreamReader(socket.getInputStream()));
					Character c = (char) in.read();
					if (c=='{'){
						stringNet = stringNet+c;
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
						stringNet = stringNet+c;
					} while (cont>0);
					}
		
					JSONObject json = (JSONObject) JSONValue.parse(stringNet);
					Message msg = Message.createFromJSON(json);
					try {
						InBuf.put(msg);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		
					} catch (IOException e1) {
						e1.printStackTrace();
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
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}
}

