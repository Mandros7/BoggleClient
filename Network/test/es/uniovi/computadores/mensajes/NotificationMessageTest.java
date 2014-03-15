package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

import junit.framework.TestCase;

public class NotificationMessageTest extends TestCase {

	private static final String NICK = "usuario";
	private static final String WORD = "peral";
	private static final boolean ALREADY_DISCOVERED = false;
	private static final Character[][] MATRIX =
		{ 
			{ 'A', 'B', 'C', 'D' },
			{ 'E', 'F', 'G', 'H' },
			{ 'I', 'J', 'K', 'L' },
			{ 'M', 'N', 'Ñ', 'O' }
		};
	
	private static final String APLAYED_FILE = "json/aplayed.json";
	private static final String ASTART_FILE = "json/astart.json";
	private static final String AEND_FILE = "json/aend.json";

	
	public void testAPLAYEDToJSON() {
		JSONObject expectedObject = Util.loadJSONFromFile(APLAYED_FILE);
		APLAYEDNotificationMessage notification = 
				new APLAYEDNotificationMessage(NICK, new WordStats(WORD, ALREADY_DISCOVERED));		
		assertEquals(expectedObject, notification.toJSON());
	}
	
	public void testAPLAYEDParsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(APLAYED_FILE);
		APLAYEDNotificationMessage notification = (APLAYEDNotificationMessage)
				Message.createFromJSON((JSONObject) expectedObject);
		assertEquals(expectedObject, notification.toJSON());
	}
	
	public void testASTARToJSON() {
		JSONObject expectedObject = Util.loadJSONFromFile(ASTART_FILE);
		ASTARTNotificationMessage notification = 
				new ASTARTNotificationMessage(MATRIX);
		assertEquals(expectedObject, notification.toJSON());		
	}
	
	public void testASTARTParsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(ASTART_FILE);
		ASTARTNotificationMessage notification = (ASTARTNotificationMessage)
				Message.createFromJSON((JSONObject) expectedObject);
		assertEquals(expectedObject, notification.toJSON());
	}

	public void testAENDParsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(AEND_FILE);
		AENDNotificationMessage notification = (AENDNotificationMessage)
				Message.createFromJSON((JSONObject) expectedObject);
		assertEquals(expectedObject, notification.toJSON());
	}
}
