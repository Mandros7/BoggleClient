package es.uniovi.alumno;

import java.util.concurrent.ArrayBlockingQueue;

import es.uniovi.computadores.mensajes.*;

public class MessageBuilder {
	
	ArrayBlockingQueue<Message> OutBuf;
	
	public MessageBuilder(ArrayBlockingQueue<Message> abq) {
		this.OutBuf = abq;
	}
	
	
	void newInput(String input){
		if (input.length()>2){
      	  String inputFixed = input.replaceAll(" +", " ").trim();
      	  //Sustuye los espacios si son 2 o mas seguidos por uno unico y elimina espacios al principio y al final.
            String [] data = inputFixed.split(" ");             
            if (inputFixed.charAt(0)=='/'){
            	data[0] = data[0].toUpperCase();
                switch (data[0]) {
                    case ("/START"):		                   
                          startGame();
							break;
                    case ("/NICK"):
                    	if (data.length!=2) {
                    		System.out.println("Numero de arumentos incorrecto.\n" +
                    				"Formato: /nick <nick>");
                    	}
                    	else {
	                    		changeNick(data[1]);
                    	}
                    	break;
                    case ("/JOIN"):
                    	if (data.length!=2) {
                    		System.out.println("Numero de arumentos incorrecto.\n" +
                    				"Formato: /join <mesa>");
                    	}
                    	else {
	                    		joinTable(data[1]);
                    	}
                    	break;
                    	
                    case ("/LEAVE"):
                    		leaveTable();
                    	break;
                    	
                    case ("/MATRIX"):
                    	if (Client.getMATRIX() != null){
                    		Character[][] MATRIX = Client.getMATRIX();
	                        	System.out.println("------------");
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
                    	else {
                    		System.out.println("No se ha recibido matriz del servidor");
                    	}
                    	break;
                    	
                    case ("/LIST"):
                		listTables();
                    	break;
                    	
                    case ("/WHO"):
                		whoTable();
                    	break;
                    	
                    case ("/QUIT"):
                  	System.out.println("Hasta luego.");
                    	break;
                    	
                    case ("/WORD"):
                    if (data.length>1){
	                        if (data.length>2){
	                        	System.out.println("Solo se reconoce "+data[1]+" como palabra");	                 
	                        }
	                        if (data[1].length()>2){
	                        	sendWord(data[1]);
	                        }
	                        else {
	                        	System.out.println("La longitud minima de la palabra es 3 caracteres");
	                        }
	                
                    	}
                    	else {
                    		System.out.println("Debes escribir una palabra.\n" +
                    				"Formato: /word <palabra>");
                    	}
                    	break;
                    default:
                    	System.out.println("Has introducido un comando incorrecto.");
                        break;
                }
            }
            else{
                    sendWord(data[0]);
            }
        }
        else {
      	  System.out.println("Longitud minima: 3 caracteres.");
        }
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
