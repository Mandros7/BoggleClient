package es.uniovi.computadores;

import junit.framework.TestCase;

import org.json.simple.JSONObject;

import es.uniovi.computadores.mensajes.STARTCommandMessage;
import es.uniovi.computadores.mensajes.WORDCommandMessage;
import es.uniovi.computadores.mensajes.WordStats;

public class NetworkTest extends TestCase {

	private static final String[] WORDS = { "prueba", "palabra",
			"computadores", "teleco", "foo" };

	private static final int START_TIME_SECS = 1;
	private static final int WAITING_TIME_SECS = 5;
	private static final int STOP_TIME_SECS = 1;
	private static final int N = 2;

	public void test1() {
		try {
			// Creo la red y el sumidero de paquetes
			Network network = new Network();
			DummyMessageSink sink = new DummyMessageSink(network);
			sink.start();

			for (int iteration = 0; iteration < N; iteration++) {
				// Espero un tiempo
				Thread.sleep(START_TIME_SECS * 1000);
				STARTCommandMessage start = new STARTCommandMessage();
				network.send(start.toJSON().toString().getBytes());

				// Palabras
				for (int i = 0; i < WORDS.length; i++) {
					// Espero
					Thread.sleep(WAITING_TIME_SECS * 1000);
					WORDCommandMessage word = new WORDCommandMessage(
							new WordStats(WORDS[i]));
					network.send(word.toJSON().toString().getBytes());
				}
				Thread.sleep(STOP_TIME_SECS * 1000);
			}
			sink.interrupt();
			network.close();
		} catch (InterruptedException ex) {
			assertTrue(false);
		}
	}
}