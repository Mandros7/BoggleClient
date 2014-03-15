package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class SSTARTResponseMessage extends ResponseMessage {

	public static final String SUBTYPE = "start";
		
	public SSTARTResponseMessage() {
		super(SUBTYPE);
	}
	
	public SSTARTResponseMessage(String errorDescription) {
		super(SUBTYPE, errorDescription);
	}
	
	SSTARTResponseMessage(JSONObject params) {
		super(SUBTYPE);
		if (params != null) {
			throw new IllegalArgumentException("The response does not accept parameters");
		}
	}
	
	SSTARTResponseMessage(JSONObject params, String errorDescription) {
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
