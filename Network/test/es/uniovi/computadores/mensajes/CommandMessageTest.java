package es.uniovi.computadores.mensajes;

import junit.framework.TestCase;

import org.json.simple.JSONObject;

public class CommandMessageTest extends TestCase {

	private static final String WORD = "prueba";
	
	private static final String WORD_FILE = "json/word.json";
	private static final String START_FILE = "json/start.json";

	
	public void testWORDToJSON() {
		JSONObject expectedObject = Util.loadJSONFromFile(WORD_FILE);
		WORDCommandMessage command = 
				new WORDCommandMessage(new WordStats(WORD));		
		assertEquals(expectedObject, command.toJSON());
	}
	
	public void testWORDParsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(WORD_FILE);
		WORDCommandMessage command = (WORDCommandMessage)
				Message.createFromJSON((JSONObject) expectedObject);
		assertEquals(expectedObject, command.toJSON());
	}
	
	public void testSTARTToJSON() {
		JSONObject expectedObject = Util.loadJSONFromFile(START_FILE);
		STARTCommandMessage command = 
				new STARTCommandMessage();		
		assertEquals(expectedObject, command.toJSON());
	}
	
	public void testSTARTParsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(START_FILE);
		STARTCommandMessage command = (STARTCommandMessage)
				Message.createFromJSON((JSONObject) expectedObject);
		assertEquals(expectedObject, command.toJSON());
	}
}
