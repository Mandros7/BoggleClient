package es.uniovi.alumno;

import java.util.concurrent.ArrayBlockingQueue;

import es.uniovi.computadores.mensajes.*;

public class MessageBuilder {
	
	ArrayBlockingQueue<Message> OutBuf;
	
	public MessageBuilder(ArrayBlockingQueue<Message> abq) {
		this.OutBuf = abq;
	}
	
	void startGame(){
		STARTCommandMessage start = new STARTCommandMessage();
		try {
			OutBuf.put(start);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void changeNick(String newNick){
		NICKCommandMessage nick = new NICKCommandMessage(newNick);
		try {
    		OutBuf.put(nick);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
	}
	
	void joinTable(String table){
		JOINCommandMessage join = new JOINCommandMessage(table);
		try {
    		OutBuf.put(join);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
	}
	
	void leaveTable(){
		LEAVECommandMessage leave = new LEAVECommandMessage();
  		try {
        		OutBuf.put(leave);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
	}
	
	void listTables(){
		LISTCommandMessage list = new LISTCommandMessage();
  		try {
      		OutBuf.put(list);
      	} catch (InterruptedException e) {
      		e.printStackTrace();
      	}
	}
	
	void whoTable(){
		WHOCommandMessage who = new WHOCommandMessage();
  		try {
      		OutBuf.put(who);
      	} catch (InterruptedException e) {
      		e.printStackTrace();
      	}
	}
	
	void sendWord(String w){
		WORDCommandMessage word = new WORDCommandMessage(new WordStats(w));
        try {
			OutBuf.put(word);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

}
