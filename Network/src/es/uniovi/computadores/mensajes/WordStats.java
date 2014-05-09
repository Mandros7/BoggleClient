package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class WordStats {
	
	private static final String WORD_TAG = "word";	
	private static final String ALREADY_DISCOVERED_TAG = "already_discovered";
	
	private String mWord;
	private boolean mAlreadyDiscovered;
	
	public WordStats(String word, boolean alreadyDiscovered) {			
		setWord(word);
		mAlreadyDiscovered = alreadyDiscovered;
	}
	
	public WordStats(String word) {
		this(word, false);
	}
	
	WordStats(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException("The JSON object cannot be null");
		}
		setWord((String) json.get(WORD_TAG));
		Boolean bool = (Boolean) json.get(ALREADY_DISCOVERED_TAG);
		if (bool == null) {
			throw new IllegalArgumentException("Invalid word stats");
		}
		mAlreadyDiscovered = bool;
	}
		
	@SuppressWarnings("unchecked")
	JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(WORD_TAG, mWord);
		json.put(ALREADY_DISCOVERED_TAG, mAlreadyDiscovered);
		return json;
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

	public boolean isAlreadyDiscovered() {
		return mAlreadyDiscovered;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(mWord);
		if (mAlreadyDiscovered) {
			builder.append(" (repetida)");
		}
		return builder.toString();
	}
}
