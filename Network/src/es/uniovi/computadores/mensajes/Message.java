package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;


/**
 * Clase base de la que derivan todos los mensajes.
 *
 */
public abstract class Message {
	
	private static final String TYPE_TAG = "type";
	private static final String SUBTYPE_TAG = "subtype";
	private static final String PARAMS_TAG = "params";
	
	private String mType;
	private String mSubtype;
	
	
	protected Message(String type, String subtype) {
		mType = type;
		mSubtype = subtype;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(TYPE_TAG, mType);
		json.put(SUBTYPE_TAG, mSubtype);
		JSONObject params = toJSONParams();
		if (params != null) {
			json.put(PARAMS_TAG, params);
		}
		return json;
	}
	
	protected abstract JSONObject toJSONParams();
	
	
	public static Message createFromJSON(JSONObject json) {
		String type = (String) json.get(TYPE_TAG);
		String subtype = (String) json.get(SUBTYPE_TAG);
		JSONObject params = (JSONObject) json.get(PARAMS_TAG);
		switch (type) {
			case ResponseMessage.TYPE:
				return ResponseMessage.createFromJSON(subtype, params, json);
				
			case NotificationMessage.TYPE:
				return NotificationMessage.createFromJSON(subtype, params);
				
			case CommandMessage.TYPE:
				return CommandMessage.createFromJSON(subtype, params);
				
			default:
				throw new IllegalArgumentException("Unknown message type");			
		}		
	}
}
