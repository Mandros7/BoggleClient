package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class WHOCommandMessage extends CommandMessage{
	
	public static final String SUBTYPE = "who";

	public WHOCommandMessage() {
		super(SUBTYPE);
	}

	@Override
	protected JSONObject toJSONParams() {
		return null;
	}

}


