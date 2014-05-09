package es.uniovi.computadores.mensajes;

import org.json.simple.JSONObject;

import junit.framework.TestCase;

public class NotificationMessageTest extends TestCase {

	private static final String NICK = "usuario";
	private static final String WORD = "peral";
	private static final boolean ALREADY_DISCOVERED = false;
	private static final Character[][] MATRIX4 =
		{ 
			{ 'A', 'B', 'C', 'D' },
			{ 'E', 'F', 'G', 'H' },
			{ 'I', 'J', 'K', 'L' },
			{ 'M', 'N', 'Ñ', 'O' }
		};
	private static final Character[][] MATRIX5 =
		{ 
			{ 'A', 'B', 'C', 'D', 'P' },
			{ 'E', 'F', 'G', 'H', 'Q' },
			{ 'I', 'J', 'K', 'L', 'R' },
			{ 'M', 'N', 'Ñ', 'O', 'S' },
			{ 'T', 'U', 'V', 'W', 'X' }
		};
	
	private static final String APLAYED_FILE = "json/aplayed.json";
	private static final String ASTART4_FILE = "json/astart-matrix4.json";
	private static final String ASTART5_FILE = "json/astart-matrix5.json";
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
	
	public void testASTAR4ToJSON() {
		JSONObject expectedObject = Util.loadJSONFromFile(ASTART4_FILE);
		ASTARTNotificationMessage notification = 
				new ASTARTNotificationMessage(MATRIX4);
		assertEquals(expectedObject, notification.toJSON());		
	}
	
	public void testASTART4Parsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(ASTART4_FILE);
		ASTARTNotificationMessage notification = (ASTARTNotificationMessage)
				Message.createFromJSON((JSONObject) expectedObject);
		assertEquals(expectedObject, notification.toJSON());
	}
	
	public void testASTAR5ToJSON() {
		JSONObject expectedObject = Util.loadJSONFromFile(ASTART5_FILE);
		ASTARTNotificationMessage notification = 
				new ASTARTNotificationMessage(MATRIX5);
		assertEquals(expectedObject, notification.toJSON());		
	}
	
	public void testASTART5Parsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(ASTART5_FILE);
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
