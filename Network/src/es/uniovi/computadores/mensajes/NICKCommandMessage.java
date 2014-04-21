package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class NICKCommandMessage extends CommandMessage{
	
	public static final String SUBTYPE = "nick";
	private static final String NICK_TAG = "nick";
	
	private String mNick;

	public NICKCommandMessage(String nick) {
		super(SUBTYPE);
		// TODO Auto-generated constructor stub
		if (nick == null) {
			throw new IllegalArgumentException("The nick cannot be null");
		}
		mNick = nick;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected JSONObject toJSONParams() {
		JSONObject params = new JSONObject();
		params.put(NICK_TAG, mNick);
		return params;
	}

}
