import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChatView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private ImageIcon chat_emote = new ImageIcon("./img/chat_emote.png");
	private ImageIcon chat_file = new ImageIcon("./img/chat_file.png");
	private ImageIcon chat_emote_clicked = new ImageIcon("./img/chat_emote_clicked.png");
	private ImageIcon chat_file_clicked = new ImageIcon("./img/chat_file_clicked.png");
	private ImageIcon chat_send = new ImageIcon("./img/chat_send.png");
	
	private JButton sendBtn;
	
	// TODO �� ä�� �� (255, 235, 51)
	
	public ChatView(Point p, String room_id, String room_name) {
		// mainView �����ʿ� ��������
		setBounds(p.x + 380, p.y, 390, 630);
		getContentPane().setLayout(null);
		setTitle(room_name);
		setResizable(false);
		setVisible(true);
		
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);
		scrollPane.setBounds(0, 43, 374, 463);
		scrollPane.setBorder(null);
		scrollPane.setLayout(null);
		scrollPane.setBackground(new Color(186, 206, 224));

		// ä�� ������ ������ ����
		JTextPane textArea = new JTextPane();
		textArea.setEditable(false);
		textArea.setFont(new Font("���� ���", Font.PLAIN, 14));
		scrollPane.setColumnHeaderView(textArea);
	
		// textSendAction
		TextSendAction textSendAction = new TextSendAction();
		
		// ����ڰ� �Է��� ����
		textField = new JTextField();
		getContentPane().add(textField);
		textField.setBounds(0, 506, 374, 43);
		textField.setBorder(null);
		textField.setColumns(10);
		textField.setBorder(BorderFactory.createEmptyBorder(10 , 10 , 10 , 10));
		textField.setCursor(new Cursor(Cursor.HAND_CURSOR));
		textField.addActionListener(textSendAction);
		
		// ����, ���� ���� ��ư�� �ִ� �г�
		JPanel chatOptionPanel = new JPanel();
		getContentPane().add(chatOptionPanel);
		chatOptionPanel.setBounds(0, 548, 374, 43);
		chatOptionPanel.setLayout(null);
		chatOptionPanel.setBackground(Color.WHITE);
		
		// ���� ��ư
		sendBtn = new JButton(chat_send);
		chatOptionPanel.add(sendBtn);
		sendBtn.setBounds(300, 3, 62, 36);
		sendBtn.setBorderPainted(false);
		sendBtn.addActionListener(textSendAction);
		sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// �̸�Ƽ�� ��ư
		JButton emoteBtn = new JButton(chat_emote);
		chatOptionPanel.add(emoteBtn);
		emoteBtn.setRolloverIcon(chat_emote_clicked);
		emoteBtn.setBounds(8, 3, 37, 37);
		emoteBtn.setBorderPainted(false);
		emoteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// ���� ��ư
		JButton fileBtn = new JButton(chat_file);
		chatOptionPanel.add(fileBtn);
		fileBtn.setRolloverIcon(chat_file_clicked);
		fileBtn.setBorderPainted(false);
		fileBtn.setBounds(52, 3, 37, 37);
		fileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
		// ä��â �̸�, ä��â ������ �ִ� �г�
		JPanel chatInfoPanel = new JPanel();
		getContentPane().add(chatInfoPanel);
		chatInfoPanel.setBackground(Color.WHITE);
		chatInfoPanel.setBounds(0, 0, 374, 43);
	}
	
	// keyboard enter key ġ�� ������ ����
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == sendBtn || e.getSource() == textField) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = textField.getText();
				//SendMessage(msg);
				System.out.println(msg);
				textField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				textField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				if (msg.contains("/exit")) // ���� ó��
					System.exit(0);
			}
		}
	}
}
