package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;


/**
 * Representa un mensaje de notificación.
 *
 */
public abstract class NotificationMessage extends Message {
	
	static final String TYPE = "notification";
	
	protected NotificationMessage(String subtype) {
		super(TYPE, subtype);
	}
	
	static NotificationMessage createFromJSON(String subtype, JSONObject params) {
		switch (subtype) {
			case APLAYEDNotificationMessage.SUBTYPE:
				return new APLAYEDNotificationMessage(params);
				
			case ASTARTNotificationMessage.SUBTYPE:
				return new ASTARTNotificationMessage(params);

			case AENDNotificationMessage.SUBTYPE:
				return new AENDNotificationMessage(params);
				
			default:
				throw new IllegalArgumentException("Unknown notification message");			
		}		
	}
}
