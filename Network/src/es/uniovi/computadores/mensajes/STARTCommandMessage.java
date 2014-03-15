package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class STARTCommandMessage extends CommandMessage {

	public static final String SUBTYPE = "start";	
	
	public STARTCommandMessage() {
		super(SUBTYPE);
	}
	
	STARTCommandMessage(JSONObject params) {
		super(SUBTYPE);
		if (params != null) {
			throw new IllegalArgumentException("The command does not accept parameters");
		}
	}
	
	@Override
	protected JSONObject toJSONParams() {
		return null;
	}
}
