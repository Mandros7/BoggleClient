package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class WORDCommandMessage extends CommandMessage {

	public static final String SUBTYPE = "word";	
	private static final String WORD_TAG = "word";
	
	private String mWord;
	
	public WORDCommandMessage(WordStats word) {
		super(SUBTYPE);
		if (word == null) {
			throw new IllegalArgumentException("The word cannot be null");
		}
		setWord(word.getWord());
	}
	
	WORDCommandMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	public String getWord() {
		return mWord;
	}
	
	private void setWord(String word) {
		if (word == null) {
			throw new IllegalArgumentException("The word cannot be null");
		}
		mWord = word;
	}
			
	@Override
	@SuppressWarnings("unchecked")
	protected JSONObject toJSONParams() {
		JSONObject params = new JSONObject();
		params.put(WORD_TAG, mWord);
		return params;
	}
	
	private void parseParams(JSONObject params) {
		setWord((String) params.get(WORD_TAG));
	}
}
