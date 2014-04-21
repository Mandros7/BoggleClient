package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class LISTCommandMessage extends CommandMessage{
	
	public static final String SUBTYPE = "list";

		protected LISTCommandMessage() {
		super(SUBTYPE);
	}

	
	@Override
	protected JSONObject toJSONParams() {
		return null;
	}

}


