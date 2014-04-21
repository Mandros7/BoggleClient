package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class LEAVECommandMessage extends CommandMessage{
	
	public static final String SUBTYPE = "leave";

	public LEAVECommandMessage() {
		super(SUBTYPE);
	}

	
	@Override
	protected JSONObject toJSONParams() {
		return null;
	}

}
