package es.uniovi.computadores.mensajes;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class Util {

	public static JSONObject loadJSONFromFile(String filename) {
		
		JSONParser parser = new JSONParser();
		try {
			return (JSONObject) parser.parse(new FileReader(filename));
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}
