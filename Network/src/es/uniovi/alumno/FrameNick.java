package es.uniovi.alumno;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FrameNick extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnOk;
	private JTextField textField;
	private JFrame mainJFrame;
	
	private int close(){
		mainJFrame.setEnabled(true);
		return FrameNick.DISPOSE_ON_CLOSE;
	}
	
	
	/**
	 * Create the frame.
	 */
	public FrameNick(final Client bc, JFrame frame) {
		this.mainJFrame = frame;
		setDefaultCloseOperation(close());
		setBounds(100, 100, 444, 65);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel newNickLabel = new JLabel("Introduce tu nuevo nick: ");
		contentPane.add(newNickLabel, BorderLayout.WEST);
		
		textField = new JTextField();
		contentPane.add(textField, BorderLayout.CENTER);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String nick = textField.getText();
				if (nick!=""){
					bc.changeNick(nick);
					close();
				}
			}
		});
		contentPane.add(btnOk, BorderLayout.EAST);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					btnOk.doClick();
				}
			}
		});
	}

}
