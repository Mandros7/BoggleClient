package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class APLAYEDNotificationMessage extends NotificationMessage {

	public static final String SUBTYPE = "played";	
	private static final String NICK_TAG = "nick";
	private static final String WORD_LENGTH_TAG = "letter_count";
	private static final String ALREADY_DISCOVERED_TAG = "already_discovered";
	
	private String mNick;
	private int mWordLength;
	private boolean mAlreadyDiscovered;
	
	public APLAYEDNotificationMessage(String nick, WordStats word) {
		super(SUBTYPE);
		if (word == null) {
			throw new IllegalArgumentException("The word cannot be null");
		}
		if (nick == null || nick == "") {
			throw new IllegalArgumentException("Invalid nick");
		}
		mNick = nick;
		mWordLength = word.getWord().length();
		mAlreadyDiscovered = word.isAlreadyDiscovered();
	}
	
	APLAYEDNotificationMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	public String getNick() {
		return mNick;
	}

	public int getWordLength() {
		return mWordLength;
	}

	public boolean isAlreadyDiscovered() {
		return mAlreadyDiscovered;
	}
			
	@Override
	@SuppressWarnings("unchecked")
	protected JSONObject toJSONParams() {
		JSONObject params = new JSONObject();
		params.put(NICK_TAG, mNick);
		params.put(WORD_LENGTH_TAG, new Long(mWordLength));
		params.put(ALREADY_DISCOVERED_TAG, new Boolean(mAlreadyDiscovered));
		return params;
	}
	
	private void parseParams(JSONObject params) {
		mNick = (String) params.get(NICK_TAG);
		mWordLength = ((Long) params.get(WORD_LENGTH_TAG)).intValue();
		mAlreadyDiscovered = (Boolean) params.get(ALREADY_DISCOVERED_TAG);
	}
}
