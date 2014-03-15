package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class WordStats {
	
	public static final int MIN_WORD_LENGTH = 3;
	private static final String WORD_TAG = "word";	
	private static final String ALREADY_DISCOVERED_TAG = "already_discovered";
	
	private String mWord;
	private boolean mAlreadyDiscovered;
	
	public WordStats(String word, boolean alreadyDiscovered) {			
		if ((word == null) || (word.length() < MIN_WORD_LENGTH)) {
			throw new IllegalArgumentException("The length of the word should be at least " + MIN_WORD_LENGTH);
		}
		mWord = word;
		mAlreadyDiscovered = alreadyDiscovered;
	}
	
	public WordStats(String word) {
		this(word, false);
	}
	
	WordStats(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException("The JSON object cannot be null");
		}
		mWord = (String) json.get(WORD_TAG);
		mAlreadyDiscovered = (Boolean) json.get(ALREADY_DISCOVERED_TAG);		
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

	public boolean isAlreadyDiscovered() {
		return mAlreadyDiscovered;
	}	
}
