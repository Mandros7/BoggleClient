package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class SWORDResponseMessage extends ResponseMessage {

	public static final String SUBTYPE = "word";
		
	private WordStats mWord;
	
	public SWORDResponseMessage(WordStats word) {
		super(SUBTYPE);
		if (word == null) {
			throw new IllegalArgumentException("The word cannot be null");
		}
		mWord = word;
	}
	
	public SWORDResponseMessage(WordStats word, String errorDescription) {
		super(SUBTYPE, errorDescription);
		if (word == null) {
			throw new IllegalArgumentException("The word cannot be null");
		}
		mWord = word;
	}
	
	SWORDResponseMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	SWORDResponseMessage(JSONObject params, String errorDescription) {
		super(SUBTYPE, errorDescription);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	
	public WordStats getWord() {
		return mWord;
	}

	@Override
	protected JSONObject toJSONParams() {
		JSONObject params = mWord.toJSON();
		return params;
	}
	
	private void parseParams(JSONObject params) {
		mWord = new WordStats(params);
	}
}
