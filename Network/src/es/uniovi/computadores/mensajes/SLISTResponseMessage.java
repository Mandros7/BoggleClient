package es.uniovi.computadores.mensajes;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SLISTResponseMessage extends ResponseMessage{

	public static final String SUBTYPE = "list";
	private static final String TABLES_TAG = "tables";
	
	private ArrayList<TableStats> mTables;

	SLISTResponseMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	SLISTResponseMessage(JSONObject params, String errorDescription) {
		super(SUBTYPE, errorDescription);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}

	private void parseParams(JSONObject params) {
		JSONArray array = (JSONArray) params.get(TABLES_TAG);
		mTables = new ArrayList<TableStats>(array.size());
		for (int i = 0; i < array.size(); i++) {
			JSONObject tableObject = (JSONObject) array.get(i);
			TableStats table = new TableStats(tableObject);
			mTables.add(table);
		}
	}

	public ArrayList<TableStats> getTables() {
		return mTables;
	}
	
	@Override
	protected JSONObject toJSONParams() {
		return null;
	}
}
