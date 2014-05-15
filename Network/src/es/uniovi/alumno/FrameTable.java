package es.uniovi.alumno;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;


public class FrameTable extends JFrame {

	
	/*
	 * En su creacion, esta ventana manda un comando LIST al servidor e imprime el resultado.
	 * Permite unirse a una mesa introudciendo el nombre, y tambien permite actualizar las mesas con un boton
	 */
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JFrame mainJFrame;
	public JTextArea txtTables;
	
	/*
	 * Esta funcion devuelve el control a la ventana principal y cierra la ventana de unirse a una mesa
	 */
	private int close(){
		mainJFrame.setEnabled(true);
		this.dispose();
		return FrameTable.DISPOSE_ON_CLOSE;
	}

	/**
	 * Create the frame.
	 */
	public FrameTable(final Client bc, JFrame frame) {
		this.mainJFrame = frame;
		setDefaultCloseOperation(close());
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
		
		txtTables = new JTextArea();
		txtTables.setEditable(false);
		scrollPane.setViewportView(txtTables);
		
		JPanel panelNewTable = new JPanel();
		contentPane.add(panelNewTable, BorderLayout.SOUTH);
		
		JLabel lblMesa = new JLabel("Mesa:  ");
		panelNewTable.add(lblMesa);
		
		textField = new JTextField();
		panelNewTable.add(textField);
		textField.setColumns(10);
		
		final JButton btnUnirse = new JButton("Unirse");
		btnUnirse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().length()>0){
					bc.joinTable(textField.getText());
					close();
				}
			}
		});
		panelNewTable.add(btnUnirse);
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					btnUnirse.doClick();
				}
			}
		});
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bc.listTables();
			}
		});
		panelNewTable.add(btnActualizar);
		
		bc.listTables();
	}

}
