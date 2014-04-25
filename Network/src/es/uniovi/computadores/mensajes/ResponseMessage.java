package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;


/**
 * Representa un mensaje de respuesta.
 *
 */
public abstract class ResponseMessage extends Message {
	
	static final String TYPE = "response";
	private static final String ERROR_TAG = "error";
	private static final String DESCRIPTION_TAG = "description";
	
	private boolean mError;
	private String mDescription;
	
	protected ResponseMessage(String subtype, String errorDescription) {
		this(subtype, true, errorDescription);
		if (errorDescription == null) {
			throw new IllegalArgumentException("The error description cannot be null");
		}
	}
	
	protected ResponseMessage(String subtype) {
		this(subtype, false, null);
	}
		
	private ResponseMessage(String subtype, boolean error, String description) {
		super(TYPE, subtype);
		mError = error;
		mDescription = description;
	}
	
	public boolean isError() {
		return mError;
	}
	
	public String getErrorDescription() {
		return mDescription;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {		
		JSONObject json = super.toJSON();
		json.put(ERROR_TAG, mError);
		if (mError) {
			json.put(DESCRIPTION_TAG, mDescription);
		}
		return json;
	}
	
	static ResponseMessage createFromJSON(String subtype, JSONObject params, JSONObject json) {
		boolean error = (Boolean) json.get(ERROR_TAG);
		String description = null;
		if (error) {
			description = (String) json.get(DESCRIPTION_TAG);
		}
				
		switch (subtype) {
			case SWORDResponseMessage.SUBTYPE:
				if (error) {
					return new SWORDResponseMessage(params, description);
				}
				return new SWORDResponseMessage(params);
				
			case SSTARTResponseMessage.SUBTYPE:
				if (error) {
					return new SSTARTResponseMessage(params, description);
				}
				return new SSTARTResponseMessage(params);
			
				
			case SJOINResponseMessage.SUBTYPE:
				if (error) {
					return new SJOINResponseMessage(params, description);
				}
				return new SJOINResponseMessage(params);
				
			case SLEAVEResponseMessage.SUBTYPE:
				if (error) {
					return new SLEAVEResponseMessage(params, description);
				}
				return new SLEAVEResponseMessage(params);
				
			case SWHOResponseMessage.SUBTYPE:
				if (error) {
					return new SWHOResponseMessage(params, description);
				}
				return new SWHOResponseMessage(params);
				
			case SLISTResponseMessage.SUBTYPE:
				if (error) {
					return new SLISTResponseMessage(params, description);
				}
				return new SLISTResponseMessage(params);
				
			case SNICKResponseMessage.SUBTYPE:
				if (error) {
					return new SNICKResponseMessage(params, description);
				}
				return new SNICKResponseMessage(params);

			default:
				throw new IllegalArgumentException("Unknown response message");			
		}		
	}
}