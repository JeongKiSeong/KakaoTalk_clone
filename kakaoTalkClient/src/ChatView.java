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
		// mainView �����ʿ� ��������
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

		// ä�� ������ ������ ����
		textPane = new JTextPane();
		scrollPane.add(textPane);
		textPane.setEditable(false);
		textPane.setFont(new Font("���� ���", Font.PLAIN, 14));
		textPane.setBackground(new Color(186, 206, 224));
		scrollPane.setViewportView(textPane);
	
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
	
	// keyboard enter key ġ�� ������ ����
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == sendBtn || e.getSource() == textField) {
				String msg = null;
				msg = textField.getText();
				if (msg.equals(""))
					return;
				ChatMsg cm = new ChatMsg(mainView.getUserName(), "200", msg);
				cm.room_id = room_id;
				cm.img = mainView.getProfile();
				mainView.sendObject(cm);
				
				textField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				textField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
			}
		}
	}
	
	// JTextPane�� ������Ʈ �߰��ϴ� �Լ�
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
