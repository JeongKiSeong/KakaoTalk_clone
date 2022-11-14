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
	
	// TODO 내 채팅 색 (255, 235, 51)
	
	public ChatView(MainView mainView) {
		// mainView 오른쪽에 나오도록
		Point p = mainView.getLocation();
		setBounds(p.x + 380, p.y, 390, 630);
		getContentPane().setLayout(null);
		setTitle("채팅방 이름");
		setResizable(false);
		setVisible(true);
		
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);
		scrollPane.setBounds(0, 43, 374, 463);
		scrollPane.setBorder(null);
		scrollPane.setLayout(null);
		scrollPane.setBackground(new Color(186, 206, 224));

		// 채팅 내용이 보여질 공간
		JTextPane textArea = new JTextPane();
		textArea.setEditable(false);
		textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		scrollPane.setColumnHeaderView(textArea);
		
		// 사용자가 입력할 공간
		textField = new JTextField();
		getContentPane().add(textField);
		textField.setBounds(0, 506, 374, 43);
		textField.setBorder(null);
		textField.setColumns(10);
		textField.setBorder(BorderFactory.createEmptyBorder(10 , 10 , 10 , 10));
		
		// 사진, 파일 전송 버튼이 있는 패널
		JPanel chatOptionPanel = new JPanel();
		getContentPane().add(chatOptionPanel);
		chatOptionPanel.setBounds(0, 548, 374, 43);
		chatOptionPanel.setLayout(null);
		chatOptionPanel.setBackground(Color.WHITE);
		
		// 이모티콘 버튼
		JButton emoteBtn = new JButton(chat_emote);
		chatOptionPanel.add(emoteBtn);
		emoteBtn.setRolloverIcon(chat_emote_clicked);
		emoteBtn.setBounds(8, 3, 37, 37);
		emoteBtn.setBorderPainted(false);
		
		// 파일 버튼
		JButton fileBtn = new JButton(chat_file);
		chatOptionPanel.add(fileBtn);
		fileBtn.setRolloverIcon(chat_file_clicked);
		fileBtn.setBorderPainted(false);
		fileBtn.setBounds(52, 3, 37, 37);
		
		// 전송 버튼
		JButton sendBtn = new JButton(chat_send);
		chatOptionPanel.add(sendBtn);
		sendBtn.setBounds(300, 3, 62, 36);
		sendBtn.setBorderPainted(false);
		
		// 채팅창 이름, 채팅창 사진이 있는 패널
		JPanel chatInfoPanel = new JPanel();
		getContentPane().add(chatInfoPanel);
		chatInfoPanel.setBackground(Color.WHITE);
		chatInfoPanel.setBounds(0, 0, 374, 43);
	}
}
