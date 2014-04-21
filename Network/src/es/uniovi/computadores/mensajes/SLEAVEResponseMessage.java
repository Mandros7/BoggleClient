package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class SLEAVEResponseMessage extends ResponseMessage {

	public static final String SUBTYPE = "leave";
		
	SLEAVEResponseMessage(JSONObject params) {
		super(SUBTYPE);
		if (params != null) {
			throw new IllegalArgumentException("The response does not accept parameters");
		}
	}
	
	SLEAVEResponseMessage(JSONObject params, String errorDescription) {
		super(SUBTYPE, errorDescription);
		if (params != null) {
			throw new IllegalArgumentException("The response does not accept parameters");
		}
	}
		
	@Override
	protected JSONObject toJSONParams() {		
		return null;
	}
}
