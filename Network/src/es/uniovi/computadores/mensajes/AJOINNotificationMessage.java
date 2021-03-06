package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class AJOINNotificationMessage extends NotificationMessage{

	public static final String SUBTYPE = "join";
	private static final String NICK_TAG = "nick";
	
	private String mNick;
	
	AJOINNotificationMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}

	private void parseParams(JSONObject params) {
		mNick = (String) params.get(NICK_TAG);
		
	}
	
	public String getNick(){
		return mNick;
	}

	@Override
	protected JSONObject toJSONParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
