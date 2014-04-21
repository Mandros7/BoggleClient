package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class TableStats {

	private static final String NAME_TAG = "name";	
	private static final String PLAYER_COUNT_TAG = "player_count";
	private static final String ALREADY_PLAYING_TAG = "already_playing";
	
	private String mName;
	private boolean mAlreadyPlaying;
	private int mPlayerCount;
	
	TableStats(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException("The JSON object cannot be null");
		}
		mName = (String) json.get(NAME_TAG);
		mAlreadyPlaying = (Boolean) json.get(ALREADY_PLAYING_TAG);	
		mPlayerCount = (int) json.get(PLAYER_COUNT_TAG);
	}
	
	public String getName(){
		return mName;
	}
	
	public int getPlayerCount(){
		return mPlayerCount;
	}
	
	public boolean getAlreadyPlaying(){
		return mAlreadyPlaying;
	}
}
