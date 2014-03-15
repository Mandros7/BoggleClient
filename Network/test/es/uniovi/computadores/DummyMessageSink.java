package es.uniovi.computadores;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import es.uniovi.computadores.mensajes.Message;

/**
 * Representa un sumidero de mensajes para leer los mensajes que
 * vienen por la red.
 */
class DummyMessageSink extends Thread {

	private Network mNetwork;
	
	public DummyMessageSink(Network network) {
		mNetwork = network;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				// Esperamos un mensaje de la red
				byte[] bytes = mNetwork.recv();

				// Lo parseamos
				String textMsg = new String(bytes);
				JSONObject json = (JSONObject) JSONValue.parse(textMsg);
				Message msg = Message.createFromJSON(json);

				// Simplemente lo descartamos
			}
		} catch (InterruptedException ex) {
			// No hacemos nada
		} catch (IllegalStateException ex) {
			// Se ha cerrado la red
		}
	}
}
