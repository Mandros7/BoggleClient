package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;


/**
 * Representa un mensaje de notificaci�n.
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
				
			case ALEAVENotificationMessage.SUBTYPE:
				return new ALEAVENotificationMessage(params);
				
			case ANICKNotificationMessage.SUBTYPE:
				return new ANICKNotificationMessage(params);
				
			case AJOINNotificationMessage.SUBTYPE:
				return new AJOINNotificationMessage(params);
				
			default:
				throw new IllegalArgumentException("Unknown notification message");			
		}		
	}
}
