package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class SNICKResponseMessage extends ResponseMessage{
	
	public static final String SUBTYPE = "nick";
	private static final String NICK_TAG = "nick";
	
	private String mNick;
	
	SNICKResponseMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	SNICKResponseMessage(JSONObject params, String errorDescription) {
		super(SUBTYPE, errorDescription);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	public String getTable() {
		return mNick;
	}
	
	private void parseParams(JSONObject params) {
		mNick = (String) params.get(NICK_TAG);
		
	}

	@Override
	protected JSONObject toJSONParams() {
		return null;
	}

}
