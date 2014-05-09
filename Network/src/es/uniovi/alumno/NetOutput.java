package es.uniovi.alumno;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import es.uniovi.computadores.mensajes.Message;

class NetOutput extends Thread {
	// Hilo de salida de datos hacia la red
	
	Socket socket;
	ArrayBlockingQueue<Message> OutBuf;
	Message msg;
	volatile boolean en_ejecucion = true;
	
	public void close() throws IOException{
		this.interrupt();
	}
	
	NetOutput(Socket n, ArrayBlockingQueue<Message> abq){
		
		this.socket = n;
		this.OutBuf = abq;
	}
	
	public void run(){
		
		while (Client.en_ejecucion) {
			
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
		try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}