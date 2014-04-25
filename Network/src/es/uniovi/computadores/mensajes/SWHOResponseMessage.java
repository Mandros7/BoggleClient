package es.uniovi.computadores.mensajes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SWHOResponseMessage extends ResponseMessage{

	public static final String SUBTYPE = "who";
	private static final String NICKS_TAG = "nicks";
	
	private String [] mNicks;
	
	SWHOResponseMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	SWHOResponseMessage(JSONObject params, String errorDescription) {
		super(SUBTYPE, errorDescription);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}

	private void parseParams(JSONObject params) {
		
		JSONArray row = (JSONArray) params.get(NICKS_TAG);
		String [] Nicks = new String[row.size()];
		for (int i=0;i<row.size();i++){
			Nicks[i]=(String) row.get(i);
		}
		mNicks = Nicks;
	}

	public String [] getNicks(){
		return mNicks;
	}
	@Override
	protected JSONObject toJSONParams() {
		return null;
	}



}
