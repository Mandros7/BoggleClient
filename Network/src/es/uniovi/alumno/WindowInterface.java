package es.uniovi.alumno;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import es.uniovi.computadores.mensajes.*;
import es.uniovi.alumno.Client;

//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;
//import com.jgoodies.forms.factories.FormFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class WindowInterface extends JFrame implements Client.OutputInterface {
	
	
	/*
	 * Esta interfaz tiene botones en una matriz sobre los que se puede actuar al 
	 * iniciar la partida. Al hacer clic sobre ellos se a������aden a la linea de comandos 
	 * para enviar una palabra. 
	 * El contenido de los botones se actualiza al recibir la confirmacion de inicio de juego
	 * 
	 * Tambien dispone de un boton de START que solo se puede pulsar cuando se esta dentro de 
	 * una mesa y no se ha iniciado el juego
	 * 
	 * Hay dos etiquetas a la izquierda de la linea de comandos que indican
	 * <MESA ACTUAL>  y <NICK ACTUAL>
	 * 
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private JTextField commandInputField;
	JTextArea textMessages;
	private JPanel panel;
	private JLabel TableLabel;
	private JLabel NickLabel;
	private JPanel NickPanel;
	private JPanel matrixPanel;
	private JButton oneOne;
	private JButton oneTwo;
	private JButton oneThree;
	private JButton oneFour;
	private JButton twoOne;
	private JButton twoTwo;
	private JButton twoThree;
	private JButton twoFour;
	private JButton threeOne;
	private JButton threeTwo;
	private JButton threeThree;
	private JButton threeFour;
	private JButton fourOne;
	private JButton fourTwo;
	private JButton fourThree;
	private JButton fourFour;
	private JButton[][] butonlar;
	
	public static Client bc;
	private JButton btnStart;
	
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final WindowInterface window = new WindowInterface();
					window.frame.setVisible(true);
					
					Thread UserOutput = new Thread () {

						public void run() {
							
							while (Client.en_ejecucion){
								
									try {
										Message msg = bc.InBuf.take();
										window.CheckIfASTART(msg);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
							}
						}
					};
					
				
					
					UserOutput.start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WindowInterface(Client boggle){
		WindowInterface.bc = boggle;
		main(null);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public WindowInterface() {
		initialize(bc);
		this.frame.setVisible(true);
	}

	
	
	/**
	 * Initialize the contents of the frame.
	 */
		private void initialize(final Client bc) {
			
			frame = new JFrame();
			BorderLayout borderLayout = (BorderLayout) frame.getContentPane().getLayout();
			borderLayout.setVgap(5);
			frame.setBounds(100, 100, 648, 300);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
			
			textMessages = new JTextArea();
			scrollPane.setViewportView(textMessages);
			textMessages.setEditable(false);
			 ((DefaultCaret) textMessages.getCaret()).setUpdatePolicy(
			         DefaultCaret.ALWAYS_UPDATE);
			
			
			JPanel southPanel = new JPanel();
			frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
			southPanel.setLayout(new BorderLayout(5, 0));
			
			final JButton sendButton = new JButton("Enviar");
			sendButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String txt = commandInputField.getText();
					if(txt.length()>0){
						commandInputField.setText("");
						String txt2 = bc.eraseBlanks(txt);
						String[] datos = txt2.split(" ");
						sendCommand(datos);
					}
				}
			});
			southPanel.add(sendButton, BorderLayout.EAST);
			
			commandInputField = new JTextField();
			commandInputField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						sendButton.doClick();
					}
				}
			});
			southPanel.add(commandInputField, BorderLayout.CENTER);
			commandInputField.setColumns(10);
			
			panel = new JPanel();
			southPanel.add(panel, BorderLayout.WEST);
			
			TableLabel = new JLabel();
			TableLabel.setText("Ninguna");
			panel.add(TableLabel);
			
			NickPanel = new JPanel();
			panel.add(NickPanel);
			
			NickLabel = new JLabel();
			NickPanel.add(NickLabel);
			NickLabel.setText("Ninguno");
			
			matrixPanel = new JPanel();
			frame.getContentPane().add(matrixPanel, BorderLayout.EAST);
			
			butonlar= new JButton[4][4];
			GridBagLayout gbl_matrixPanel = new GridBagLayout();
			gbl_matrixPanel.columnWidths = new int[]{38, 75, 75, 75, 75, 0};
			gbl_matrixPanel.rowHeights = new int[]{29, 29, 29, 29, 0, 0, 0};
			gbl_matrixPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_matrixPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			matrixPanel.setLayout(gbl_matrixPanel);
			
			
			oneOne = new JButton(" ");
			oneOne.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+oneOne.getText());
				}
			});
			oneOne.setEnabled(false);
			GridBagConstraints gbc_oneOne = new GridBagConstraints();
			gbc_oneOne.anchor = GridBagConstraints.NORTHWEST;
			gbc_oneOne.insets = new Insets(0, 0, 5, 5);
			gbc_oneOne.gridx = 1;
			gbc_oneOne.gridy = 0;
			matrixPanel.add(oneOne, gbc_oneOne);
			butonlar[0][0]=oneOne;
			
			oneTwo = new JButton(" ");
			oneTwo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+oneTwo.getText());
				}
			});
			oneTwo.setEnabled(false);
			GridBagConstraints gbc_oneTwo = new GridBagConstraints();
			gbc_oneTwo.anchor = GridBagConstraints.NORTHWEST;
			gbc_oneTwo.insets = new Insets(0, 0, 5, 5);
			gbc_oneTwo.gridx = 2;
			gbc_oneTwo.gridy = 0;
			matrixPanel.add(oneTwo, gbc_oneTwo);
			butonlar[0][1]=oneTwo;
			
			oneThree = new JButton(" ");
			oneThree.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+oneThree.getText());
				}
			});
			oneThree.setEnabled(false);
			GridBagConstraints gbc_oneThree = new GridBagConstraints();
			gbc_oneThree.anchor = GridBagConstraints.NORTHWEST;
			gbc_oneThree.insets = new Insets(0, 0, 5, 5);
			gbc_oneThree.gridx = 3;
			gbc_oneThree.gridy = 0;
			matrixPanel.add(oneThree, gbc_oneThree);
			butonlar[0][2]=oneThree;
			
			oneFour = new JButton(" ");
			oneFour.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+oneFour.getText());
				}
			});
			oneFour.setEnabled(false);
			GridBagConstraints gbc_oneFour = new GridBagConstraints();
			gbc_oneFour.anchor = GridBagConstraints.NORTHWEST;
			gbc_oneFour.insets = new Insets(0, 0, 5, 0);
			gbc_oneFour.gridx = 4;
			gbc_oneFour.gridy = 0;
			matrixPanel.add(oneFour, gbc_oneFour);
			butonlar[0][3]=oneFour;
			
			twoOne = new JButton(" ");
			twoOne.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+twoOne.getText());
				}
			});
			twoOne.setEnabled(false);
			GridBagConstraints gbc_twoOne = new GridBagConstraints();
			gbc_twoOne.anchor = GridBagConstraints.NORTHWEST;
			gbc_twoOne.insets = new Insets(0, 0, 5, 5);
			gbc_twoOne.gridx = 1;
			gbc_twoOne.gridy = 1;
			matrixPanel.add(twoOne, gbc_twoOne);
			butonlar[1][0]=twoOne;
			
			twoTwo = new JButton(" ");
			twoTwo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+twoTwo.getText());
				}
			});
			twoTwo.setEnabled(false);
			GridBagConstraints gbc_twoTwo = new GridBagConstraints();
			gbc_twoTwo.anchor = GridBagConstraints.NORTHWEST;
			gbc_twoTwo.insets = new Insets(0, 0, 5, 5);
			gbc_twoTwo.gridx = 2;
			gbc_twoTwo.gridy = 1;
			matrixPanel.add(twoTwo, gbc_twoTwo);
			butonlar[1][1]=twoTwo;
			
			twoThree = new JButton(" ");
			twoThree.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+twoThree.getText());
				}
			});
			twoThree.setEnabled(false);
			GridBagConstraints gbc_twoThree = new GridBagConstraints();
			gbc_twoThree.anchor = GridBagConstraints.NORTHWEST;
			gbc_twoThree.insets = new Insets(0, 0, 5, 5);
			gbc_twoThree.gridx = 3;
			gbc_twoThree.gridy = 1;
			matrixPanel.add(twoThree, gbc_twoThree);
			butonlar[1][2]=twoThree;
						
						twoFour = new JButton(" ");
						twoFour.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								commandInputField.setText(commandInputField.getText()+twoFour.getText());
							}
						});
						twoFour.setEnabled(false);
						GridBagConstraints gbc_twoFour = new GridBagConstraints();
						gbc_twoFour.anchor = GridBagConstraints.NORTHWEST;
						gbc_twoFour.insets = new Insets(0, 0, 5, 0);
						gbc_twoFour.gridx = 4;
						gbc_twoFour.gridy = 1;
						matrixPanel.add(twoFour, gbc_twoFour);
						butonlar[1][3]=twoFour;
			
						threeOne = new JButton(" ");
						threeOne.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								commandInputField.setText(commandInputField.getText()+threeOne.getText());
							}
						});
						threeOne.setEnabled(false);
						GridBagConstraints gbc_threeOne = new GridBagConstraints();
						gbc_threeOne.anchor = GridBagConstraints.NORTHWEST;
						gbc_threeOne.insets = new Insets(0, 0, 5, 5);
						gbc_threeOne.gridx = 1;
						gbc_threeOne.gridy = 2;
						matrixPanel.add(threeOne, gbc_threeOne);
						butonlar[2][0]=threeOne;
			
			threeTwo = new JButton(" ");
			threeTwo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+threeTwo.getText());

				}
			});
			threeTwo.setEnabled(false);
			GridBagConstraints gbc_threeTwo = new GridBagConstraints();
			gbc_threeTwo.anchor = GridBagConstraints.NORTHWEST;
			gbc_threeTwo.insets = new Insets(0, 0, 5, 5);
			gbc_threeTwo.gridx = 2;
			gbc_threeTwo.gridy = 2;
			matrixPanel.add(threeTwo, gbc_threeTwo);
			butonlar[2][1]=threeTwo;
			
			threeThree = new JButton(" ");
			threeThree.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commandInputField.setText(commandInputField.getText()+threeThree.getText());

				}
			});
			threeThree.setEnabled(false);
			GridBagConstraints gbc_threeThree = new GridBagConstraints();
			gbc_threeThree.anchor = GridBagConstraints.NORTHWEST;
			gbc_threeThree.insets = new Insets(0, 0, 5, 5);
			gbc_threeThree.gridx = 3;
			gbc_threeThree.gridy = 2;
			matrixPanel.add(threeThree, gbc_threeThree);
			butonlar[2][2]=threeThree;
						
						threeFour = new JButton(" ");
						threeFour.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								commandInputField.setText(commandInputField.getText()+threeFour.getText());

							}
						});
						threeFour.setEnabled(false);
						GridBagConstraints gbc_threeFour = new GridBagConstraints();
						gbc_threeFour.anchor = GridBagConstraints.NORTHWEST;
						gbc_threeFour.insets = new Insets(0, 0, 5, 0);
						gbc_threeFour.gridx = 4;
						gbc_threeFour.gridy = 2;
						matrixPanel.add(threeFour, gbc_threeFour);
						butonlar[2][3]=threeFour;
						
						fourOne = new JButton(" ");
						fourOne.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								commandInputField.setText(commandInputField.getText()+fourOne.getText());

							}
						});
						fourOne.setEnabled(false);
						GridBagConstraints gbc_fourOne = new GridBagConstraints();
						gbc_fourOne.anchor = GridBagConstraints.NORTHWEST;
						gbc_fourOne.insets = new Insets(0, 0, 5, 5);
						gbc_fourOne.gridx = 1;
						gbc_fourOne.gridy = 3;
						matrixPanel.add(fourOne, gbc_fourOne);
						butonlar[3][0]=fourOne;
						
						fourTwo = new JButton(" ");
						fourTwo.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								commandInputField.setText(commandInputField.getText()+fourTwo.getText());

							}
						});
						fourTwo.setEnabled(false);
						GridBagConstraints gbc_fourTwo = new GridBagConstraints();
						gbc_fourTwo.anchor = GridBagConstraints.NORTHWEST;
						gbc_fourTwo.insets = new Insets(0, 0, 5, 5);
						gbc_fourTwo.gridx = 2;
						gbc_fourTwo.gridy = 3;
						matrixPanel.add(fourTwo, gbc_fourTwo);
						butonlar[3][1]=fourTwo;
						
									
									fourThree = new JButton(" ");
									fourThree.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											commandInputField.setText(commandInputField.getText()+fourThree.getText());

										}
									});
									fourThree.setEnabled(false);
									GridBagConstraints gbc_fourThree = new GridBagConstraints();
									gbc_fourThree.anchor = GridBagConstraints.NORTHWEST;
									gbc_fourThree.insets = new Insets(0, 0, 5, 5);
									gbc_fourThree.gridx = 3;
									gbc_fourThree.gridy = 3;
									matrixPanel.add(fourThree, gbc_fourThree);
									butonlar[3][2]=fourThree;
			
						
						fourFour = new JButton(" ");
						fourFour.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								commandInputField.setText(commandInputField.getText()+fourFour.getText());

							}
						});
						fourFour.setEnabled(false);
						GridBagConstraints gbc_fourFour = new GridBagConstraints();
						gbc_fourFour.insets = new Insets(0, 0, 5, 0);
						gbc_fourFour.anchor = GridBagConstraints.NORTHWEST;
						gbc_fourFour.gridx = 4;
						gbc_fourFour.gridy = 3;
						matrixPanel.add(fourFour, gbc_fourFour);
						butonlar[3][3]=fourFour;
						
						btnStart = new JButton("INICIAR PARTIDA");
						btnStart.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								bc.start();
							}
						});
						btnStart.setEnabled(false);
						GridBagConstraints gbc_btnStart = new GridBagConstraints();
						gbc_btnStart.gridwidth = 2;
						gbc_btnStart.insets = new Insets(0, 0, 0, 5);
						gbc_btnStart.gridx = 2;
						gbc_btnStart.gridy = 5;
						matrixPanel.add(btnStart, gbc_btnStart);
						
			
		
	}
		
	public String printASTART(Character[][] matriz) {
		
		String txtASTART = "Se ha iniciado la partida" + "\n" + "------------" + "\n";
		Client.setMATRIX(matriz);
		for (int i=0; i<matriz.length;i++){
			String line = "";
			for (int j = 0; j<matriz.length;j++){
				line = line + (matriz[i][j]);
				if (j!=matriz.length-1){
					line = line + ", ";
				}
			}
			txtASTART = txtASTART + line + "\n";
		}
		txtASTART = txtASTART + "------------";
		Client.astart_recibido = true;
		refreshMatrix();
		return txtASTART;
	}
	
	private void refreshMatrix() {
		Character[][] matrix = Client.getMATRIX();
		for (int i = 0; i < butonlar.length; i++) {
			for (int j = 0; j < butonlar[i].length; j++) {
				butonlar[i][j].setText(Character.toString(matrix[i][j]));
				butonlar[i][j].setEnabled(true);
			}
		}
	}
	
	private void CheckIfASTART(Message msg) {
		if (msg instanceof ASTARTNotificationMessage) {
			Client.setMATRIX(((ASTARTNotificationMessage)msg).getMatrix());
			refreshMatrix();
		}
		else {
			textMessages.append(checkResponse(msg)+"\n");
		}
	}
	
	private void cleanMatrix() {
		for (int i = 0; i < butonlar.length; i++) {
			for (int j = 0; j < butonlar[i].length; j++) {
				butonlar[i][j].setText(" ");
				butonlar[i][j].setEnabled(false);
			}
			
		}
		
	}

	public String printAEND(ArrayList<PlayerStats> players) {
		int i = 0;
		String txtAEND = "La partida ha terminado.\n";
		cleanMatrix();
		btnStart.setEnabled(true);
		while (i<players.size()) {
			txtAEND = txtAEND + "El jugador " +
					players.get(i).getNick() +
						" ha obtenido una puntuacion de " +
						players.get(i).getScore() + " punto(s)\n";
			i++;
		}
		return txtAEND;
	}
	
	public String printAJOIN(String nick_usuario) {
		String txtAJOIN = "El usuario " +
				nick_usuario +
					" se ha unido a la mesa";
		return txtAJOIN;
	}
	
	public String printANICK(String new_nick, String old_nick) {
		String txtANICK = "El usuario " +
				old_nick +
					" ha cambiado su nick por " +
						new_nick;
		return txtANICK;
	}
	
	public String printALEAVE(String nick_leave) {
		String txtALEAVE = "El usuario " +
				nick_leave +
					" ha abandonado la mesa.";
		return txtALEAVE;
	}
	
	public String printSWORD(WordStats StatsPalabra) {
		String txtSWORD;
		String word = StatsPalabra.getWord();
		boolean discoveredResponse = StatsPalabra.isAlreadyDiscovered();
		if (!discoveredResponse){
			txtSWORD = "Se ha registrado la siguiente palabra nueva: " + word;
		}
		else {
			txtSWORD = "La palabra " + word + " ya existia.";
		}
		Client.astart_recibido = false;
		return txtSWORD;
	}
	
	public String printSJOIN(String table) {
		btnStart.setEnabled(true);
		String txtSJOIN = "Te has unido a la mesa " + table;
		TableLabel.setText(table);
		
		return txtSJOIN;
	}
	
	public String printSLEAVE() {
		String txtSLEAVE = "Has abadonado la mesa.";
		btnStart.setEnabled(false);
		Client.setTABLE("Ninguna");
		return txtSLEAVE;
	}
	
	public String printSNICK(String nick) {
		String txtSNICK = "Tu nuevo nick es: " + nick;
		NickLabel.setText(nick);
		return txtSNICK;
	}
	
	public String printSLIST(ArrayList<TableStats> tables) {
		String txtSLIST = "";
		String anadido;
		int tam = tables.size();
		
		if (tam==0) {
			txtSLIST = "No hay ningun jugador en ninguna mesa";
		}
		else {
			for (int i=0; i<tam; i++) {
				String info_tables = "En la mesa " +
						tables.get(i).getName() +
							" hay " +
							tables.get(i).getPlayerCount() +
								" jugador(es)";
				if (tables.get(i).getAlreadyPlaying()) {
					anadido = " y la partida ya se ha iniciado.";
				}
				else {
					anadido = " y la partida aun no se ha iniciado";
				}
				txtSLIST = info_tables + anadido;
			}
		}
		return txtSLIST;
	}
	
	public String printSSTART() {
		String txtSSTART = "Esperando al resto de jugadores...";
		btnStart.setEnabled(false);
		return txtSSTART;
	}
	
	public String printSWHO(String[] array_jugadores) {
		String txtSWHO = "";
		int tam = array_jugadores.length;
		if (tam==1) {
			txtSWHO = "El jugador de esta mesa es: " + array_jugadores[0];
		}
		else {
			String jugadores = "Los jugadores de esta mesa son: ";
			for (int i=0; i<tam; i++) {
				if (i==0) {
					jugadores = jugadores + array_jugadores[i];
				}
				else if (i==tam-1) {
					jugadores = jugadores + " y " + array_jugadores[i];
				}
				else {
					jugadores = jugadores + ", " + array_jugadores[i];
				}
			}
			txtSWHO = jugadores;
		}
		return txtSWHO;
	}

	@Override
	public String printAPLAYED(String nick, int length, boolean discoveredPlayed) {
		String txtAPLAYED;
		if (!discoveredPlayed){
			txtAPLAYED = "El jugador "+nick+" ha encontrado una nueva palabra de "+length+" letras.";
		}
		else{
			txtAPLAYED = "El jugador "+nick+" ha encontrado una palabra de "+length+" letras que ya habia sido descubierta.";
		}
		return txtAPLAYED;
	}

	public void sendCommand(String[] datos) {
		String[] comandos = {"start","join","nick","leave","word","list","who","quit"};
		boolean com_incom = false;
		if (datos!=null){
			if (datos[0].charAt(0)=='/') {
	        	datos[0] = datos[0].toUpperCase();
	        	
	        	switch (datos[0]) {
	        	case ("/START"):
					bc.start();
					break;
	        	case ("/NICK"):
	         		if (datos.length!=2) {
	         			textMessages.append("Numero de arumentos incorrecto.\n" +
	         					"Formato: /nick <nick>"+"\n");
	         		}
	         		else {
	            		bc.changeNick(datos[1]);
	         		}
	          		break;
	         	case ("/JOIN"):
	         		if (datos.length!=2) {
	         			textMessages.append("Numero de arumentos incorrecto.\n" +
	         					"Formato: /join <mesa>"+"\n");
	         		}
	         		else {
	            		bc.joinTable(datos[1]);
	         		}
	          		break;
	         	case ("/LEAVE"):
	 				bc.leaveTable();
	          		break;
	         	case ("/LIST"):
	      			bc.listTables();
	      			break;
	         	case ("/WHO"):
	          		bc.listTablePlayers();
	              	break;
	         	case ("/QUIT"):
	         		Client.en_ejecucion = false;
	         	
	          		break;
	         	case ("/WORD"):
	         		if (datos.length>1){
	         			if (datos.length>2){
	         				textMessages.append("Solo se reconoce "+datos[1]+" como palabra"+"\n");
	         			}
	         			if (datos[1].length()<3) {
	         				textMessages.append("Longitud minima: 3 caracteres."+"\n");
	         			}
	         			else {
	         				bc.wordFound(new WordStats(datos[1]));
	         			}
	         		}
	         		else {
	         			textMessages.append("Debes escribir una palabra.\n" +
	         					"Formato: /word <palabra>"+"\n");
	         		}
	          		break;
	         	default:
	         		textMessages.append("Has introducido un comando incorrecto."+"\n");
	         		break;
	        	}
	        }
			 //El usuario escribe una palabra
	        else {
	        	//Para comprobar si el usuario escribio el comando sin /
	        	for (int i=0; i<comandos.length; i++) {
					if (datos[0].equals(comandos[i])) {
						com_incom = true;
					}
				}
				if (com_incom) {
					textMessages.append("Has escrito el comando sin \"/\"."+"\n");
					com_incom = false;
				}
				else {
					if (datos.length>1){
						textMessages.append("Solo se reconoce \""+datos[0]+"\" como palabra"+"\n");
	     			}
	     			if (datos[0].length()<3) {
	     				textMessages.append("Longitud minima: 3 caracteres."+"\n");
	     			}
	     			else {
	     				bc.wordFound(new WordStats(datos[0]));
	     			}
				}
	        }
		}
	}
	
	@Override
	public String checkResponse(Message msg) {
		String mesa = Client.getTABLE();
		
		if (msg instanceof NotificationMessage) {
			if (msg instanceof ASTARTNotificationMessage){
				return printASTART(((ASTARTNotificationMessage)msg).getMatrix());
			}
			if (msg instanceof APLAYEDNotificationMessage){
				String nick = ((APLAYEDNotificationMessage) msg).getNick();
				int length = ((APLAYEDNotificationMessage) msg).getWordLength();
				boolean discoveredPlayed = ((APLAYEDNotificationMessage) msg).isAlreadyDiscovered();
				
				return printAPLAYED(nick,length,discoveredPlayed);
			}
			if (msg instanceof AENDNotificationMessage) {
				return printAEND(((AENDNotificationMessage)msg).getPlayers());
			}
			if (msg instanceof AJOINNotificationMessage) {
				String nick_usuario = ((AJOINNotificationMessage)msg).getNick();

				return printAJOIN(nick_usuario);
			}
			if (msg instanceof ANICKNotificationMessage) {
				String new_nick = ((ANICKNotificationMessage)msg).getNewNick();
				String old_nick = ((ANICKNotificationMessage)msg).getOldNick();
				
				return printANICK(new_nick,old_nick);
			}
			if (msg instanceof ALEAVENotificationMessage) {
				String nick_leave = ((ALEAVENotificationMessage)msg).getNick();

				return printALEAVE(nick_leave);
			}
			
		}
		if (msg instanceof ResponseMessage){
			if (((ResponseMessage) msg).isError()){
				String error = "ERROR:" + ((ResponseMessage) msg).getErrorDescription();
				return error;
			}
			else{
				if (msg instanceof SWORDResponseMessage){
					WordStats StatsPalabra = ((SWORDResponseMessage) msg).getWord();
					
					return printSWORD(StatsPalabra);
				}
				if (msg instanceof SJOINResponseMessage) {
					mesa = ((SJOINResponseMessage)msg).getTable();

					return printSJOIN(mesa);
				}
				if (msg instanceof SLEAVEResponseMessage) {
					return printSLEAVE();
				}
				if (msg instanceof SNICKResponseMessage) {
					String nick = ((SNICKResponseMessage)msg).getNick();

					return printSNICK(nick);
				}
				if (msg instanceof SLISTResponseMessage) {
					
					return printSLIST(((SLISTResponseMessage)msg).getTables());
					
				}
				if (msg instanceof SSTARTResponseMessage){

					return printSSTART();
				}
				if (msg instanceof SWHOResponseMessage) {
					
					return printSWHO(((SWHOResponseMessage)msg).getNicks());
				}
			}
		}
		return "Error";
	}

}




