package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;


/**
 * Representa un comando.
 *
 */
public abstract class CommandMessage extends Message {
	
	static final String TYPE = "command";
	
	protected CommandMessage(String subtype) {
		super(TYPE, subtype);
	}
	
	static CommandMessage createFromJSON(String subtype, JSONObject params) {
		switch (subtype) {
			case WORDCommandMessage.SUBTYPE:
				return new WORDCommandMessage(params);
				
			case STARTCommandMessage.SUBTYPE:
				return new STARTCommandMessage(params);
				
			default:
				throw new IllegalArgumentException("Unknown command message");			
		}		
	}
}
