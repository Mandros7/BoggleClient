package es.uniovi.computadores.mensajes;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PlayerStats {

	private static final String NICK_TAG = "nick";
	private static final String WORDS_TAG = "words";
	private static final String SCORE_TAG = "score";

	private String mNick;
	private int mScore;
	private ArrayList<WordStats> mWords;

	public PlayerStats(String nick, int score, ArrayList<WordStats> words) {
		if (nick == null || nick == "") {
			throw new IllegalArgumentException("Invalid nick");
		}
		if (words == null) {
			throw new IllegalArgumentException(
					"The list of words cannot be null");
		}
		mNick = nick;
		mScore = score;
		mWords = words;
	}

	PlayerStats(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException("The JSON object cannot be null");
		}
		mNick = (String) json.get(NICK_TAG);
		mScore = ((Long) json.get(SCORE_TAG)).intValue();
		JSONArray array = (JSONArray) json.get(WORDS_TAG);
		mWords = new ArrayList<WordStats>(array.size());
		for (int i = 0; i < array.size(); i++) {
			JSONObject wordObject = (JSONObject) array.get(i);
			WordStats word = new WordStats(wordObject);
			mWords.add(word);
		}
	}

	@SuppressWarnings("unchecked")
	JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(NICK_TAG, mNick);
		json.put(SCORE_TAG, new Long(mScore));
		JSONArray wordsArray = new JSONArray();
		for (int i = 0; i < mWords.size(); i++) {
			wordsArray.add(mWords.get(i).toJSON());
		}
		json.put(WORDS_TAG, wordsArray);
		return json;
	}

	public String getNick() {
		return mNick;
	}

	public int getScore() {
		return mScore;
	}

	public ArrayList<WordStats> getWords() {
		return mWords;
	}
}
