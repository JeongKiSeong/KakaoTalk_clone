import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private ImageIcon chat_emote = new ImageIcon("./img/chat_emote.png");
	private ImageIcon chat_file = new ImageIcon("./img/chat_file.png");
	private ImageIcon chat_emote_clicked = new ImageIcon("./img/chat_emote_clicked.png");
	private ImageIcon chat_file_clicked = new ImageIcon("./img/chat_file_clicked.png");
	private ImageIcon chat_send = new ImageIcon("./img/chat_send.png");
	
	private JButton sendBtn;
	private JTextPane textPane;
	private MainView mainView;
	private String room_id;
	
	public ChatView(MainView mainView, String room_id, String room_name) {
		this.mainView = mainView;
		this.room_id = room_id;
		// mainView 오른쪽에 나오도록
		Point p = mainView.getLocationOnScreen();
		setBounds(p.x + 380, p.y, 390, 630);
		getContentPane().setLayout(null);
		setTitle(room_name);
		setResizable(false);
		setVisible(true);
		
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		scrollPane.setBounds(0, 43, 374, 463);
		scrollPane.setBorder(null);

		// 채팅 내용이 보여질 공간
		textPane = new JTextPane();
		scrollPane.add(textPane);
		textPane.setEditable(false);
		textPane.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		textPane.setBackground(new Color(186, 206, 224));
		scrollPane.setViewportView(textPane);
	
		// textSendAction
		TextSendAction textSendAction = new TextSendAction();
		
		// 사용자가 입력할 공간
		textField = new JTextField();
		getContentPane().add(textField);
		textField.setBounds(0, 506, 374, 43);
		textField.setBorder(null);
		textField.setColumns(10);
		textField.setBorder(BorderFactory.createEmptyBorder(10 , 10 , 10 , 10));
		textField.setCursor(new Cursor(Cursor.HAND_CURSOR));
		textField.addActionListener(textSendAction);
		
		// 사진, 파일 전송 버튼이 있는 패널
		JPanel chatOptionPanel = new JPanel();
		getContentPane().add(chatOptionPanel);
		chatOptionPanel.setBounds(0, 548, 374, 43);
		chatOptionPanel.setLayout(null);
		chatOptionPanel.setBackground(Color.WHITE);
		
		// 전송 버튼
		sendBtn = new JButton(chat_send);
		chatOptionPanel.add(sendBtn);
		sendBtn.setBounds(300, 3, 62, 36);
		sendBtn.setBorderPainted(false);
		sendBtn.addActionListener(textSendAction);
		sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// 이모티콘 버튼
		JButton emoteBtn = new JButton(chat_emote);
		chatOptionPanel.add(emoteBtn);
		emoteBtn.setRolloverIcon(chat_emote_clicked);
		emoteBtn.setBounds(8, 3, 37, 37);
		emoteBtn.setBorderPainted(false);
		emoteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// 파일 버튼
		JButton fileBtn = new JButton(chat_file);
		chatOptionPanel.add(fileBtn);
		fileBtn.setRolloverIcon(chat_file_clicked);
		fileBtn.setBorderPainted(false);
		fileBtn.setBounds(52, 3, 37, 37);
		fileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
		// 채팅창 이름, 채팅창 사진이 있는 패널
		JPanel chatInfoPanel = new JPanel();
		getContentPane().add(chatInfoPanel);
		chatInfoPanel.setBackground(Color.WHITE);
		chatInfoPanel.setBounds(0, 0, 374, 43);
		
	}
	
	public void AppendTextLeft(ImageIcon profile, String name, String text) {
		SetLeftAlign();
		textPane.insertComponent(new MsgStatusPanel(profile, name));
		addComponent(textPane, null);
		addComponent(textPane, new MsgLabel(text, "L"));
		textPane.setCaretPosition(textPane.getDocument().getLength());
	}
	
	public void AppendTextRight(String text) {
		SetRightAlign();
		addComponent(textPane, new MsgLabel(text, "R"));
		textPane.setCaretPosition(textPane.getDocument().getLength());
	}
	
	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == sendBtn || e.getSource() == textField) {
				String msg = null;
				msg = textField.getText();
				if (msg.equals(""))
					return;
				ChatMsg cm = new ChatMsg(mainView.getUserName(), "200", msg);
				cm.room_id = room_id;
				cm.img = mainView.getProfile();
				mainView.sendObject(cm);
				
				textField.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				textField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
			}
		}
	}
	
	// JTextPane에 컴포넌트 추가하는 함수
	public void addComponent(JTextPane textPane, Component component) {
	    try {
			StyledDocument doc = (StyledDocument) textPane.getDocument();
		    Style style = doc.addStyle("StyleName", null);
			if (component == null) {
				doc.insertString(doc.getLength(), "\n", style);
				return;
			}
				
		    StyleConstants.setComponent(style, component);
			doc.insertString(doc.getLength(), "ignored text\n", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void SetRightAlign() {
		StyledDocument doc = textPane.getStyledDocument();
		
		SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        
        doc.setParagraphAttributes(doc.getLength(), 1, right, false);
	}
	
	public void SetLeftAlign() {
		StyledDocument doc = textPane.getStyledDocument();
		
		SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        
        doc.setParagraphAttributes(doc.getLength(), 1, left, false);
	}
}
