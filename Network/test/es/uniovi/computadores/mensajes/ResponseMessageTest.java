package es.uniovi.computadores.mensajes;

import junit.framework.TestCase;

import org.json.simple.JSONObject;

public class ResponseMessageTest extends TestCase {

	private static final String WORD = "pinti";
	private static boolean ALREADY_DISCOVERED = false;
	private static final String ERROR_DESCRIPTION = "La palabra no está en el diccionario";
	
	private static final String SWORD_FILE = "json/sword.json";
	private static final String SSTART_FILE = "json/sstart.json";

	
	public void testSWORDToJSON() {
		JSONObject expectedObject = Util.loadJSONFromFile(SWORD_FILE);
		SWORDResponseMessage response = 
				new SWORDResponseMessage(new WordStats(WORD, ALREADY_DISCOVERED), ERROR_DESCRIPTION);		
		assertEquals(expectedObject, response.toJSON());
	}
	
	public void testSWORDParsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(SWORD_FILE);
		SWORDResponseMessage response = (SWORDResponseMessage)
				Message.createFromJSON((JSONObject) expectedObject);
		assertEquals(expectedObject, response.toJSON());
	}
	
	public void testSSTARTToJSON() {
		JSONObject expectedObject = Util.loadJSONFromFile(SSTART_FILE);
		SSTARTResponseMessage response = 
				new SSTARTResponseMessage();		
		assertEquals(expectedObject, response.toJSON());
	}
	
	public void testSSTARTParsing() {
		JSONObject expectedObject = Util.loadJSONFromFile(SSTART_FILE);
		SSTARTResponseMessage response = (SSTARTResponseMessage)
				Message.createFromJSON((JSONObject) expectedObject);
		assertEquals(expectedObject, response.toJSON());
	}
}
