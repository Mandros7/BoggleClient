package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

public class JOINCommandMessage extends CommandMessage{

	public static final String SUBTYPE = "join";
	private static final String TABLE_TAG = "table";
	
	private String mTable;
	
	public JOINCommandMessage(String table) {
		super(SUBTYPE);
		if (table == null) {
			throw new IllegalArgumentException("The table cannot be null");
		}
		mTable = table;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected JSONObject toJSONParams() {
		JSONObject params = new JSONObject();
		params.put(TABLE_TAG, mTable);
		return params;
	}

}
