package es.uniovi.alumno;

import es.uniovi.computadores.mensajes.*;

public class MessageBuilder {
	
	public STARTCommandMessage CreateStartCommand() {
		return new STARTCommandMessage();
	}
	
	public JOINCommandMessage CreateJoinCommand(String mesa) {
		return new JOINCommandMessage(mesa);
	}
	
	public NICKCommandMessage CreateNickCommand(String nick) {
		return new NICKCommandMessage(nick);
	}
	
	public LEAVECommandMessage CreateLeaveCommand() {
		return new LEAVECommandMessage();
	}
	
	public LISTCommandMessage CreateListCommand() {
		return new LISTCommandMessage();
	}
	
	public WORDCommandMessage CreateWordCommand(WordStats word) {
		return new WORDCommandMessage(word);
	}
	
	public WHOCommandMessage CreateWhoCommand() {
		return new WHOCommandMessage();
	}	
	
}