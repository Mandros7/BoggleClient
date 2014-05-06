package es.uniovi.computadores.mensajes;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AENDNotificationMessage extends NotificationMessage {
	
	public static final String SUBTYPE = "end";
	private static final String PLAYERS_TAG = "players";	
	
	private ArrayList<PlayerStats> mPlayers;
	
	public AENDNotificationMessage(ArrayList<PlayerStats> players) {
		super(SUBTYPE);
		if ((players == null) || (players.size() == 0)) {
			throw new IllegalArgumentException("Invalid player stats");
		}
		mPlayers = players;
	}
	
	AENDNotificationMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	public ArrayList<PlayerStats> getPlayers() {
		return mPlayers;
	}
			
	@Override
	@SuppressWarnings("unchecked")
	protected JSONObject toJSONParams() {
		JSONObject params = new JSONObject();
		JSONArray playersArray = new JSONArray();
		for (int i = 0; i < mPlayers.size(); i++) {
			playersArray.add(mPlayers.get(i).toJSON());
		}
		params.put(PLAYERS_TAG, playersArray);
		return params;
	}
	
	private void parseParams(JSONObject params) {
		JSONArray array = (JSONArray) params.get(PLAYERS_TAG);
		if (array == null) {
			throw new IllegalArgumentException("The player list is missing");
		}
		mPlayers = new ArrayList<PlayerStats>(array.size());
		for (int i = 0; i < array.size(); i++) {
			JSONObject playerObject = (JSONObject) array.get(i);
			PlayerStats player = new PlayerStats(playerObject);
			mPlayers.add(player);
		}
	}
}
