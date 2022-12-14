import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	private ImageIcon chat_img_clicked = new ImageIcon("./img/chat_img.png");
	private ImageIcon chat_img = new ImageIcon("./img/chat_img_clicked.png");
	

	private ImageIcon main_addFriend_clicked = new ImageIcon("./img/main_addFriend.png");
	private ImageIcon main_addFriend = new ImageIcon("./img/main_addFriend_clicked.png");
	private ImageIcon chat_userlist = new ImageIcon("./img/chat_userlist.png");
	private ImageIcon chat_userlist_clicked = new ImageIcon("./img/chat_userlist_clicked.png");
	
	private JButton sendBtn;
	private JButton imgBtn;
	private JButton emoteBtn;
	private JButton fileBtn;
	private JTextPane textPane;
	private MainView mainView;
	private String room_id;
	private ChatView chatView;
	private FriendDialog friendDialog;
	private String room_name;
	private JLabel roomNameLabel;
	private List<MsgStatusPanel> msgStatusPanelList = new ArrayList<MsgStatusPanel>();
	
	
	public ChatView(MainView mainView, String room_id, String room_name) {
		this.mainView = mainView;
		this.room_id = room_id;
		this.room_name = room_name;
		chatView = this;
		// mainView ???????? ????????
		Point p = mainView.getLocationOnScreen();
		setBounds(p.x + 380, p.y, 390, 630);
		getContentPane().setLayout(null);
		setTitle(room_name);
		setResizable(false);
		setVisible(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);
		scrollPane.setBounds(0, 43, 374, 463);
		scrollPane.setBorder(null);

		// ???? ?????? ?????? ????
		textPane = new JTextPane();
		scrollPane.add(textPane);
		textPane.setEditable(false);
		textPane.setFont(new Font("???? ????", Font.PLAIN, 14));
		textPane.setBackground(new Color(186, 206, 224));
		scrollPane.setViewportView(textPane);
	
		// textSendAction
		TextSendAction textSendAction = new TextSendAction();
		
		// ???????? ?????? ????
		textField = new JTextField();
		getContentPane().add(textField);
		textField.setBounds(0, 506, 374, 43);
		textField.setBorder(null);
		textField.setColumns(10);
		textField.setBorder(BorderFactory.createEmptyBorder(10 , 10 , 10 , 10));
		textField.setCursor(new Cursor(Cursor.HAND_CURSOR));
		textField.addActionListener(textSendAction);
		
		// ????, ???? ???? ?????? ???? ????
		JPanel chatOptionPanel = new JPanel();
		getContentPane().add(chatOptionPanel);
		chatOptionPanel.setBounds(0, 548, 374, 43);
		chatOptionPanel.setLayout(null);
		chatOptionPanel.setBackground(Color.WHITE);
		
		// ???? ????
		sendBtn = new JButton(chat_send);
		chatOptionPanel.add(sendBtn);
		sendBtn.setBounds(300, 3, 62, 36);
		sendBtn.setBorderPainted(false);
		sendBtn.addActionListener(textSendAction);
		sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// ???????? ????
		emoteBtn = new JButton(chat_emote);
		chatOptionPanel.add(emoteBtn);
		emoteBtn.setRolloverIcon(chat_emote_clicked);
		emoteBtn.setBounds(8, 3, 37, 37);
		emoteBtn.setBorderPainted(false);
		emoteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		emoteBtn.addActionListener(new AnotherSendAction());
		
		// ???? ????
		fileBtn = new JButton(chat_file);
		chatOptionPanel.add(fileBtn);
		fileBtn.setRolloverIcon(chat_file_clicked);
		fileBtn.setBorderPainted(false);
		fileBtn.setBounds(52, 3, 37, 37);
		fileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		fileBtn.addActionListener(new AnotherSendAction());
		
		// ???? ????
		imgBtn = new JButton(chat_img);
		chatOptionPanel.add(imgBtn);
		imgBtn.setRolloverIcon(chat_img_clicked);
		imgBtn.setBorderPainted(false);
		imgBtn.setBounds(96, 3, 37, 37);
		imgBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		imgBtn.addActionListener(new AnotherSendAction());
			
		// ?????? ????, ?????? ?????? ???? ????
		JPanel chatInfoPanel = new JPanel();
		getContentPane().add(chatInfoPanel);
		chatInfoPanel.setBackground(Color.WHITE);
		//chatInfoPanel.setBackground(new Color(186, 206, 224));
		chatInfoPanel.setBounds(0, 0, 374, 43);
		chatInfoPanel.setLayout(null);
		
		// ?? ????
		roomNameLabel = new JLabel(room_name);
		chatInfoPanel.add(roomNameLabel);
		roomNameLabel.setBounds(12, 10, 227, 23);
		roomNameLabel.setFont(new Font("???? ????", Font.BOLD, 16));
		roomNameLabel.addMouseListener(new MouseAdapter() { 
		    public void mouseClicked(MouseEvent e) {
		    	String input = JOptionPane.showInputDialog(null,
		    			"", "?? ???? ????", JOptionPane.INFORMATION_MESSAGE);
		    	
		    	if (input != null) {
					ChatMsg msg = new ChatMsg(mainView.getUserName(), "40", input);
					msg.room_id = room_id;
					mainView.sendObject(msg);
					roomNameLabel.setText(input);
		    	}
		    }
		});
		
		// ???? ????
//		JButton addFriendBtn = new JButton(main_addFriend);
//		chatInfoPanel.add(addFriendBtn);
//		addFriendBtn.setRolloverIcon(main_addFriend_clicked);
//		addFriendBtn.setBounds(280, 0, 41, 41);
//		addFriendBtn.setBorderPainted(false);
		
		// ?????? ???? ????
		JButton userlistBtn = new JButton(chat_userlist);
		chatInfoPanel.add(userlistBtn);
		userlistBtn.setRolloverIcon(chat_userlist_clicked);
		userlistBtn.setBorderPainted(false);
		userlistBtn.setBounds(333, 0, 41, 41);
		userlistBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		userlistBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				friendDialog = new FriendDialog(mainView, chatView);
				// ?????? userlist ?????????? ????
				ChatMsg cm = new ChatMsg(mainView.getUserName(), "50", "");
				cm.room_id = room_id;
				mainView.sendObject(cm);
				
			}
		});
		
	}
	
	public void AppendTextLeft(ImageIcon profile, String name, String text, String time) {
		SetLeftAlign();
		MsgStatusPanel msgStatusPanel = new MsgStatusPanel(chatView, profile, name, time);
		msgStatusPanelList.add(msgStatusPanel);
		textPane.insertComponent(msgStatusPanel);
		addComponent(textPane, null);
		addComponent(textPane, new MsgLabel(text, "L"));
		textPane.setCaretPosition(textPane.getDocument().getLength());
	}
	
	public void AppendTextRight(String text) {
		SetRightAlign();
		addComponent(textPane, new MsgLabel(text, "R"));
		textPane.setCaretPosition(textPane.getDocument().getLength());
	}
	
	public void AppendImageLeft(ImageIcon profile, String name, ImageIcon ori_icon, String time) {
		SetLeftAlign();
		addComponent(textPane, null);
		MsgStatusPanel msgStatusPanel = new MsgStatusPanel(chatView, profile, name, time);
		msgStatusPanelList.add(msgStatusPanel);
		textPane.insertComponent(msgStatusPanel);
		AppendImage(ori_icon);
		addComponent(textPane, null);
		textPane.setCaretPosition(textPane.getDocument().getLength());
	}
	
	public void AppendImageRight(ImageIcon ori_icon, String time) {
		SetRightAlign();
		AppendImage(ori_icon);
		textPane.setCaretPosition(textPane.getDocument().getLength());
	}
	
	public void AppendImage(ImageIcon ori_icon) {
		int len = textPane.getDocument().getLength();
		textPane.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		ImageIcon icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image?? ???? ???? ???? ???? ???? ???? 200 ???????? ??????????.
		if (width > 200 || height > 200) {
			if (width > height) { // ???? ????
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // ???? ????
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			icon = new ImageIcon(new_img);
		} else
			icon = ori_icon;
		
		JLabel img = new JLabel(icon);
		addComponent(textPane, img);
		
		// ???? ?? ?????? ?????? ????
		img.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)  
		    {  
				JFrame f = new JFrame(); //creates jframe f
				f.setResizable(false);
		        ImageIcon image = icon; //imports the image
		        JLabel lbl = new JLabel(image); //puts the image into a jlabel
		        f.getContentPane().add(lbl); //puts label inside the jframe
		        f.setSize(300, 300); //gets h and w of image and sets jframe to the size
		        Point p = img.getLocationOnScreen();
		        Point p2 = chatView.getLocationOnScreen();
		        f.setLocation(p2.x + 390, p.y - 65); //sets the location of the jframe
		        f.setVisible(true); //makes the jframe visible
		    }  
		});
		
	}
	
	// keyboard enter key ???? ?????? ????
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button?? ???????? ?????? ???????? Enter key ????
			if (e.getSource() == sendBtn || e.getSource() == textField) {
				String msg = null;
				msg = textField.getText();
				if (msg.equals(""))
					return;
				ChatMsg cm = new ChatMsg(mainView.getUserName(), "200", msg);
				cm.room_id = room_id;
				cm.profile = mainView.getProfile();
				
				mainView.sendObject(cm);				
				textField.setText(""); // ???????? ?????? ???? ?????? ???????? ??????.
				textField.requestFocus(); // ???????? ?????? ?????? ???? ?????? ?????? ??????????
			}
		}
	}
	
	// ????, ????, ???????? ???? ??????
	class AnotherSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// ???? ????
			if (e.getSource() == imgBtn) {
				Frame frame = new Frame("??????????");
				FileDialog fileDialog = new FileDialog(frame, "?????? ????", FileDialog.LOAD);
				fileDialog.setVisible(true);
				ChatMsg cm = new ChatMsg(mainView.getUserName(), "210", "?????? ??????????.");
				cm.room_id = room_id;
				cm.img = new ImageIcon(fileDialog.getDirectory() + fileDialog.getFile());
				cm.profile = mainView.getProfile();
				
				mainView.sendObject(cm);
			}
			
			// ???????? ????
			else if (e.getSource() == emoteBtn) {
				new EmoticonDialog(chatView);
			}
			
			else if (e.getSource() == fileBtn) {
				Frame frame = new Frame("???? ????");
				FileDialog fileDialog = new FileDialog(frame, "???? ????", FileDialog.LOAD);
				fileDialog.setVisible(true);
				
				// 220 ???? ???? ????
				ChatMsg cm = new ChatMsg(mainView.getUserName(), "220", "?????? ??????????.");
				cm.room_id = chatView.getRoomId();
				cm.profile = mainView.getProfile();
				cm.file = new File(fileDialog.getDirectory() + fileDialog.getFile());
				mainView.sendObject(cm);
				
			}
		}
	}
	
	// JTextPane?? ???????? ???????? ????
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
	
	public FriendDialog getDialog() {
		return friendDialog;
	}
	
	public String getRoomId() {
		return room_id;
	}
	
	public String getRoomName() {
		return room_name;
	}
	
	public void setRoomName(String room_name) {
		this.room_name = room_name;
		roomNameLabel.setText(room_name);
		this.setTitle(room_name);
	}
	
	public MainView getMainVeiw() {
		return mainView;
	}
	
	public List<MsgStatusPanel> getMsgStatusPanel() {
		return msgStatusPanelList;
	}
	
}
