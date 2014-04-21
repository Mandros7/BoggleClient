package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class SJOINResponseMessage extends ResponseMessage{
	
	public static final String SUBTYPE = "join";
	private static final String TABLE_TAG = "table";
	
	private String mTable;

	SJOINResponseMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	SJOINResponseMessage(JSONObject params, String errorDescription) {
		super(SUBTYPE, errorDescription);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	public String getTable() {
		return mTable;
	}
	
	private void parseParams(JSONObject params) {
		mTable = (String) params.get(TABLE_TAG);
		
	}

	@Override
	protected JSONObject toJSONParams() {
		return null;
	}

}
