package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class ANICKNotificationMessage extends NotificationMessage{

	public static final String SUBTYPE = "nick";
	private static final String OLD_NICK_TAG = "old_nick";
	private static final String NEW_NICK_TAG = "new_nick";
	
	private String mOldNick;
	private String mNewNick;
	
	ANICKNotificationMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}

	private void parseParams(JSONObject params) {
		mOldNick = (String) params.get(OLD_NICK_TAG);
		mNewNick = (String) params.get(NEW_NICK_TAG);
		
	}
	
	public String getOldNick(){
		return mOldNick;
	}
	
	public String getNewNick(){
		return mNewNick;
	}


	@Override
	protected JSONObject toJSONParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
