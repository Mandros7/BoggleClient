package es.uniovi.alumno;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class FrameHelp extends JFrame {

	/*
	 * Ventana de ayuda que aparecera al accionar el boton de "Ayuda" del Frame Principal
	 * Permite manejar ambas ventanas a la vez
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public FrameHelp() {
		
		setDefaultCloseOperation(FrameNick.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextArea txtrAyudaLaBarra = new JTextArea();
		txtrAyudaLaBarra.setEditable(false);
		txtrAyudaLaBarra.setText("AYUDA\n\nLa barra inferior se utiliza para enviar palabras (pulsando enter o Enviar). \nTambien pueden formarse accionando los botones de la matriz de letras.\n\nEsta aplicacion dispone de los siguiente COMANDOS:\n\n/NICK <NICK>: Utilizado para cambiar el nick del usuario.\n Tambien puede cambiarse mediante el dialogo \ndesplegado al accionar el boton con el nick actual\n\n/JOIN <TABLE>: Unirse a una mesa\n\n/WHO: Para ver los jugadores que estan en la mesa\n\n/LEAVE: Salir de una mesa\n\n/START: Iniciar juego. \nEsta operacion puede realizarse accionando el boton INICIAR \n\n/LIST: Obtener una lista de las mesas existentes\n\n/QUIT: Cerrar la aplicacion\n\n");
		scrollPane.setViewportView(txtrAyudaLaBarra);
	}

}
