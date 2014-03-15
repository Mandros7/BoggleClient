package es.uniovi.computadores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import junit.framework.TestResult;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import es.uniovi.computadores.mensajes.*;

public class Network {

	private static final int USER_COUNT = 4;
	private static final int MAX_MESSAGES_ENQUEUED = 20;
	private final static Logger LOGGER = Logger.getLogger(Network.class
			.getName());

	private ArrayBlockingQueue<byte[]> mMsgInQueue;
	private ArrayBlockingQueue<byte[]> mMsgOutQueue;
	private boolean mClosed;
	private UserSimulator[] mUserSimulators;
	private ServerSimulator mServerSimulator;
	private Object mLock = new Object();

	/**
	 * Crea un objeto de tipo Network. Una vez creado ya se pueden enviar y
	 * recibir mensajes. No olvides llamar a close() para cerrar la red y
	 * detener todos los hilos.
	 */
	public Network() {
		initializeQueues();
		initializeServerSimulator();
		mClosed = false;
		startServer();
	}

	/**
	 * Cierra la red. Los hilos que estén bloquedos enviando o recibiendo
	 * mensajes de la red recibiran una excepción de tipo InterruptedException.
	 * 
	 * @throws IllegalStateException
	 *             si la red ya está cerrada.
	 */
	public void close() {
		testClosed();
		mClosed = true;
		closeUserSimulators();
		closeServer();
		LOGGER.info("Network stopped");
	}

	/**
	 * Recibe el siguiente mensaje de la red bloqueando el hilo si no hay
	 * mensajes pendientes.
	 * 
	 * @return el mensaje recibido.
	 * @throws IllegalStateException
	 *             si la red está cerrada.
	 * @throws InterruptedException
	 *             si el hilo es interrumpido mientras espera por un mensaje.
	 */
	public byte[] recv() throws InterruptedException {
		testClosed();
		return mMsgOutQueue.take();
	}

	/**
	 * Envía un mensaje a la red bloqueando el hilo si no hay espacio para
	 * enviar mensajes.
	 * 
	 * @param msg
	 *            el mensaje a enviar.
	 * @throws IllegalStateException
	 *             si la red está cerrada.
	 * @throws InterruptedException
	 *             si el hilo es interrumpido mientras envía un mensaje.
	 */
	public void send(byte[] msg) throws InterruptedException {
		testClosed();
		mMsgInQueue.put(msg);
	}

	/**
	 * Dispara una excepción si la red está cerrada.
	 * 
	 * @throws IllegalStateException
	 *             si la red está cerrada.
	 */
	private void testClosed() {
		if (mClosed)
			throw new IllegalStateException("The network is already closed");
	}

	/**
	 * Inicializa las colas de entrada y salida de mensajes a la red.
	 */
	private void initializeQueues() {
		mMsgInQueue = new ArrayBlockingQueue<byte[]>(MAX_MESSAGES_ENQUEUED);
		mMsgOutQueue = new ArrayBlockingQueue<byte[]>(MAX_MESSAGES_ENQUEUED);
	}

	/**
	 * Inicializa el simulador del servidor que responde a los comandos del
	 * cliente.
	 */
	private void initializeServerSimulator() {
		mServerSimulator = new ServerSimulator();
	}

	/**
	 * Inicializa los simuladores de usuarios que inyectan notificaciones de
	 * palabras en la red.
	 */
	private void initializeUserSimulators() {
		synchronized (mLock) {
			mUserSimulators = new UserSimulator[USER_COUNT];
			for (int i = 0; i < USER_COUNT; i++) {
				mUserSimulators[i] = new UserSimulator(i);
			}
		}
	}

	/**
	 * Arranca el servidor.
	 */
	private void startServer() {
		mServerSimulator.start();
	}

	/**
	 * Detiene el servidor.
	 */
	private void closeServer() {
		mServerSimulator.interrupt();
		mServerSimulator = null;
	}

	/**
	 * Arranca los simuladores de usuarios.
	 */
	private void startUserSimulators() {
		synchronized (mLock) {
			for (int i = 0; i < USER_COUNT; i++) {
				mUserSimulators[i].start();
			}
		}
	}

	/**
	 * Detiene los simuladores de usuarios.
	 */
	private void closeUserSimulators() {
		synchronized (mLock) {
			if (mUserSimulators != null) {
				for (int i = 0; i < USER_COUNT; i++) {
					if (mUserSimulators[i] != null) {
						mUserSimulators[i].interrupt();
					}
				}
				mUserSimulators = null;
			}
		}
	}

	/**
	 * Saca un mensaje de la cola de mensajes entrantes bloqueando el hilo si no
	 * hay mensajes pendientes.
	 * 
	 * @return el mensaje.
	 * @throws IllegalStateException
	 *             si la red está cerrada.
	 * @throws InterruptedException
	 *             si el hilo es interrumpido mientras desencola un mensaje.
	 */
	private byte[] dequeueIncomingMessage() throws InterruptedException {
		testClosed();
		return mMsgInQueue.take();
	}

	/**
	 * Mete un mensaje en la cola de mensajes salientes bloqueando el hilo si no
	 * hay hueco en la cola.
	 * 
	 * @param msg
	 *            el mensaje.
	 * @throws IllegalStateException
	 *             si la red está cerrada.
	 * @throws InterruptedException
	 *             si el hilo es interrumpido mientras encola un mensaje.
	 */
	private void enqueueIncomingMessage(byte[] msg) throws InterruptedException {
		testClosed();
		mMsgOutQueue.put(msg);
	}

	/**
	 * Se llama desde el servidor para notificar que una partida ha empezado.
	 */
	private void onPlayStarted() {
		testClosed();
		LOGGER.info("Play started");
		// Arrancamos los simuladores de los usuarios
		initializeUserSimulators();
		startUserSimulators();
	}

	/**
	 * Se llama desde el servidor para notificar que una partida ha terminado.
	 */
	private void onPlayFinished() {
		testClosed();
		// Paramos los simuladores de los usuarios
		closeUserSimulators();
		LOGGER.info("Play stopped");
	}

	/**
	 * Clase para simular los mensajes generadas por el servidor. Inyecta
	 * tráfico en la cola de mensajes salientes de la red. La duración de un
	 * juego está limitada a un númmero comandos de palabra descubierta enviados
	 * por el cliente aunque no se refieran a palabras válidas, sin tener en
	 * cuenta ningún tipo de temporización.
	 * 
	 */
	private class ServerSimulator extends Thread {

		private static final String SERVER_NAME = "servidor";
		private static final String WORD_ERROR_MESSAGE = "La palabra no se acepta";
		private static final String START_ERROR_MESSAGE = "Juego ya iniciado";
		private static final int MAX_WORDS_COMMANDS = 5;

		private int mWordCommands;
		private boolean mPlaying;

		public ServerSimulator() {
			setName(SERVER_NAME);
		}

		/**
		 * Simula el comportamiento del servidor.
		 */
		@Override
		public void run() {
			try {
				LOGGER.info("Server started");
				mWordCommands = 0;
				mPlaying = false;
				while (true) {
					// Esperamos un mensaje en la cola de entrada de la red
					byte[] bytes = Network.this.dequeueIncomingMessage();

					// Lo parseamos
					String textMsg = "";
					try {
						textMsg = new String(bytes);
						JSONObject json = (JSONObject) JSONValue.parse(textMsg);
						Message msg = Message.createFromJSON(json);
						if (!(msg instanceof CommandMessage)) {
							throw new IllegalStateException(
									"The message is not a valid command");
						}
						LOGGER.info("Server processing: "
								+ msg.toJSON().toString());
						processCommand((CommandMessage) msg);
						testEndOfGame();
					} catch (Exception ex) {
						LOGGER.info("Server: error while processing message '"
								+ textMsg + "': " + ex.getMessage());
					}
				}
			} catch (InterruptedException ex) {
				// No hacemos nada
			} catch (IllegalStateException ex) {
				// Se ha cerrado la red
			}
			LOGGER.info("Server stopped");
		}

		/**
		 * Procesa un comando entrante.
		 * 
		 * @param cmd
		 *            comando recibido desde el cliente.
		 */
		private void processCommand(CommandMessage cmd)
				throws InterruptedException {
			ResponseMessage response;
			boolean start = false;

			// Depende del tipo de comando
			if (cmd instanceof WORDCommandMessage) {
				response = processWORDCommand((WORDCommandMessage) cmd);
				mWordCommands++;
			} else if (cmd instanceof STARTCommandMessage) {
				response = processSTARTCommand((STARTCommandMessage) cmd);
				if (!response.isError()) {
					start = true;
				}
			} else {
				throw new IllegalStateException("Unsupported command message");
			}

			// Encolamos la respuesta
			sendMessage(response);

			// Enviar mensaje ASTART si es el comienzo del juego
			if (start) {
				ASTARTNotificationMessage notification = createASTARTNotification();
				sendMessage(notification);
				mPlaying = true;
				Network.this.onPlayStarted();
			}
		}

		/**
		 * Procesa un comando de tipo WORD entrante.
		 * 
		 * @param command
		 *            el comando.
		 * @return la respuesta generada.
		 */
		private ResponseMessage processWORDCommand(WORDCommandMessage command) {
			// Simplemente generamos una respuesta SWORD
			SWORDResponseMessage response;
			Random random = new Random();
			if (random.nextBoolean()) {
				// No se admite la palabra
				WordStats word = new WordStats(command.getWord(), false);
				response = new SWORDResponseMessage(word, WORD_ERROR_MESSAGE);
			} else {
				// Se admite la palabra
				WordStats word = new WordStats(command.getWord(),
						random.nextBoolean());
				response = new SWORDResponseMessage(word);
			}
			return response;
		}

		/**
		 * Procesa un comando de tipo START entrante.
		 * 
		 * @param command
		 *            el comando.
		 * @return la respuesta generada.
		 */
		private ResponseMessage processSTARTCommand(STARTCommandMessage command) {
			// Respondemos
			SSTARTResponseMessage response;
			if (mPlaying) {
				// No se admite el comando
				response = new SSTARTResponseMessage(START_ERROR_MESSAGE);
			} else {
				response = new SSTARTResponseMessage();
				mPlaying = true;
			}
			return response;
		}

		private void testEndOfGame() throws InterruptedException {
			if ((mWordCommands >= MAX_WORDS_COMMANDS) && (mPlaying)) {
				// Generamos la notificación de fin
				mWordCommands = 0;
				AENDNotificationMessage notification = createAENDNotification();
				sendMessage(notification);
				mPlaying = false;
				Network.this.onPlayFinished();
			}
		}

		private ASTARTNotificationMessage createASTARTNotification() {
			Character[][] MATRIX = { { 'A', 'B', 'C', 'D' },
					{ 'E', 'F', 'G', 'H' }, { 'I', 'J', 'K', 'L' },
					{ 'M', 'N', 'Ñ', 'O' } };
			return new ASTARTNotificationMessage(MATRIX);
		}

		private AENDNotificationMessage createAENDNotification() {
			ArrayList<PlayerStats> players = new ArrayList<PlayerStats>();
			for (int i = 0; i < Network.USER_COUNT; i++) {
				players.add(createPlayerStats(i));
			}
			return new AENDNotificationMessage(players);
		}

		private PlayerStats createPlayerStats(int index) {
			ArrayList<WordStats> words = new ArrayList<WordStats>();
			words.add(new WordStats("palabra1" + index, false));
			words.add(new WordStats("palabra2" + index, true));
			return new PlayerStats("Usuario" + index, 10, words);
		}

		private void sendMessage(Message msg) throws InterruptedException {
			LOGGER.info("Server sending: " + msg.toJSON().toString());
			Network.this.enqueueIncomingMessage(msg.toJSON().toString()
					.getBytes());
		}
	}

	/**
	 * Clase para simular las notificaciones generadas por la actividad de un
	 * usuario. Inyecta tráfico en la cola de mensajes salientes de la red.
	 * 
	 */
	private class UserSimulator extends Thread {

		private static final String NICK_TEMPLATE = "simulador";
		private static final int MAX_WAITING_TIME_SECS = 10;
		private static final int MAX_WORD_LENGTH = 7;
		private static final int MIN_WORD_LENGTH = 3;
		private static final char DUMMY_CHAR = 'x';

		private String mNick;

		public UserSimulator(int index) {
			mNick = NICK_TEMPLATE + index;
			setName(mNick);
		}

		/**
		 * Simula las notificaciones debidas a un usuario.
		 */
		@Override
		public void run() {
			Random random = new Random();

			try {
				LOGGER.info("User '" + mNick + "' started");
				while (true) {
					// Dormimos un tiempo aleatorio
					sleep(random.nextInt(MAX_WAITING_TIME_SECS) * 1000);

					// Generamos la notificación de palabra descubierta
					int wordLength = random.nextInt(MAX_WORD_LENGTH
							- MIN_WORD_LENGTH + 1)
							+ MIN_WORD_LENGTH;
					final char[] array = new char[wordLength];
					Arrays.fill(array, DUMMY_CHAR);
					String word = new String(array);
					boolean alreadyDiscovered = random.nextBoolean();

					APLAYEDNotificationMessage notification = new APLAYEDNotificationMessage(
							mNick, new WordStats(word, alreadyDiscovered));

					// Encolamos en la cola de salida a la red
					LOGGER.info("User '" + mNick + "' sending: "
							+ notification.toJSON().toString());
					Network.this.enqueueIncomingMessage(notification.toJSON()
							.toString().getBytes());
				}
			} catch (InterruptedException ex) {
				// No hacemos nada
			} catch (IllegalStateException ex) {
				// Se ha cerrado la red
			}
			LOGGER.info("User '" + mNick + "' stopped");
		}
	}
}
