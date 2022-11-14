import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.*;

public class ChatView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private ImageIcon chat_emote = new ImageIcon("./img/chat_emote.png");
	private ImageIcon chat_file = new ImageIcon("./img/chat_file.png");
	private ImageIcon chat_emote_clicked = new ImageIcon("./img/chat_emote_clicked.png");
	private ImageIcon chat_file_clicked = new ImageIcon("./img/chat_file_clicked.png");
	private ImageIcon chat_send = new ImageIcon("./img/chat_send.png");
	
	// TODO �� ä�� �� (255, 235, 51)
	
	public ChatView(MainView mainView) {
		// mainView �����ʿ� ��������
		Point p = mainView.getLocation();
		setBounds(p.x + 380, p.y, 390, 630);
		getContentPane().setLayout(null);
		setTitle("ä�ù� �̸�");
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
		
		// ����ڰ� �Է��� ����
		textField = new JTextField();
		getContentPane().add(textField);
		textField.setBounds(0, 506, 374, 43);
		textField.setBorder(null);
		textField.setColumns(10);
		textField.setBorder(BorderFactory.createEmptyBorder(10 , 10 , 10 , 10));
		
		// ����, ���� ���� ��ư�� �ִ� �г�
		JPanel chatOptionPanel = new JPanel();
		getContentPane().add(chatOptionPanel);
		chatOptionPanel.setBounds(0, 548, 374, 43);
		chatOptionPanel.setLayout(null);
		chatOptionPanel.setBackground(Color.WHITE);
		
		// �̸�Ƽ�� ��ư
		JButton emoteBtn = new JButton(chat_emote);
		chatOptionPanel.add(emoteBtn);
		emoteBtn.setRolloverIcon(chat_emote_clicked);
		emoteBtn.setBounds(8, 3, 37, 37);
		emoteBtn.setBorderPainted(false);
		
		// ���� ��ư
		JButton fileBtn = new JButton(chat_file);
		chatOptionPanel.add(fileBtn);
		fileBtn.setRolloverIcon(chat_file_clicked);
		fileBtn.setBorderPainted(false);
		fileBtn.setBounds(52, 3, 37, 37);
		
		// ���� ��ư
		JButton sendBtn = new JButton(chat_send);
		chatOptionPanel.add(sendBtn);
		sendBtn.setBounds(300, 3, 62, 36);
		sendBtn.setBorderPainted(false);
		
		// ä��â �̸�, ä��â ������ �ִ� �г�
		JPanel chatInfoPanel = new JPanel();
		getContentPane().add(chatInfoPanel);
		chatInfoPanel.setBackground(Color.WHITE);
		chatInfoPanel.setBounds(0, 0, 374, 43);
	}
}
