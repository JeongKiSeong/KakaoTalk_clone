import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import utils.MakeRoundedCorner;


// ģ�� ���, ä�� ��� ������ ȭ��
public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;

	private ImageIcon profile_default = new ImageIcon("./img/profile_default.png");
	private Socket socket; // �������
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private String userName = "�⺻ �̸�";
	private String userStatus = "�⺻ ���¸޽���";
	private ImageIcon profileImg = profile_default;
	
	private List<FriendLabel> FriendLabelList = new ArrayList<FriendLabel>();
	private List<RoomLabel> RoomLabelList = new ArrayList<RoomLabel>();
	private List<FriendLabel> DialogFriendLabelList = new ArrayList<FriendLabel>();
	
	private JTextPane friendTextPane;
	private JTextPane roomTextPane;
	private FriendPanel friendPanel;
	private ChatroomPanel chatroomPanel;
	private JToggleButton friendBtn;
	private JToggleButton chatroomBtn;
	
	private MainView mainView;
	

	public String getUserName() {
		return userName;
	}
	public ImageIcon getProfile() {
		return profileImg;
	}
	public String getStatus() {
		return userStatus;
	}
	
	public MainView(String username, String ip_addr, String port_no) {
		mainView = this;
		userName = username;
		setBounds(100, 100, 390, 630);
		getContentPane().setLayout(null);
		setTitle("īī����");
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		friendPanel = new FriendPanel(this);
		chatroomPanel = new ChatroomPanel(this);
		new MenuPanel();
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			
			// ���� �� 0������ �� ���� ����
			ChatMsg obcm = new ChatMsg(userName, "0", userStatus);
			obcm.img = profileImg;
			sendObject(obcm);
			
			ListenNetwork net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	// Server Message�� �����ؼ� ȭ�鿡 ǥ��
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
 						msg = String.format("[%s] [%s] %s", cm.getCode(), cm.getId(), cm.getData());
						//System.out.println(msg);
					} else
						continue;
					
					switch (cm.getCode()) {
				    // ������ ��ε�
					case "0":
						// "���̸�|���¸޽���"
						String data = cm.getData();
						if (data.equals("### ���� ###")) { // ���� ��ȣ�� ���� List ����
							FriendLabelList.removeAll(FriendLabelList);
							// textpane ����
							friendTextPane.setText("");
						}
						else if (data.equals("### �� ###")) {
							//�� ��ȣ ������ ��ü �׸���
							for (FriendLabel label : FriendLabelList)
								// �� �������� ��ܿ�
								if (label.getUserName().equals(userName)) {
									addComponent(friendTextPane, label);
									label.checkbox.setSelected(true);
									label.checkbox.setEnabled(false);
									
									profileImg = label.getProfile();
									userStatus = label.getStatus();
								}
							
							for (FriendLabel label : FriendLabelList)
								if (!label.getUserName().equals(userName))
									addComponent(friendTextPane, label);
							
						}
						else {  // �� ��ȣ�� ���� ������ ��� add
							String profile[] = data.split("\\|");
							// TODO ������ �ձ۰�
							//ImageIcon round_img = new MakeRoundedCorner(cm.img, 30).run();
							FriendLabelList.add(new FriendLabel(mainView, cm.img, profile[0], profile[1]));
						}
						break;

						
					
					case "30": // ������, �̸�, ���� ����
						for (FriendLabel friendlabel : FriendLabelList) {
							// TODO FrinedLabel�� �ִ� ImgLable ����
							if (friendlabel.getUserName().equals(cm.getId())) {
								// TODO
								friendlabel.updateProfile(cm.img, cm.getId(), cm.getData());
							}
						}
						if (cm.getId().equals(userName)) {
							profileImg = cm.img;
							userStatus = cm.getData();
						}
						
						// ä�ù� ������ ����
						for (RoomLabel roomLabel : RoomLabelList) {
							List<MsgStatusPanel> msgStatusPanelList = roomLabel.getChatView().getMsgStatusPanel();
							for (MsgStatusPanel msgStatusPanel : msgStatusPanelList) {
								msgStatusPanel.replaceImage(cm.getId(), cm.img);
							}
						}
						break;
						
						
					case "40": // �� ���� ����
						for (RoomLabel roomLabel : RoomLabelList) {
							if (roomLabel.getRoomId().equals(cm.room_id)) {
								roomLabel.setRoomName(cm.getData());
							}
						}
						
						break;
						
						
					case "50":
						List<String> ul =Arrays.asList(cm.userlist.split(" "));
						DialogFriendLabelList.removeAll(DialogFriendLabelList);
						ChatView chatView = null;
						FriendDialog fd = null;
						for (RoomLabel room : RoomLabelList) {
							if (room.getRoomId().equals(cm.room_id)) {
								chatView = room.getChatView();
							}
						}
						fd = chatView.getDialog();
						
						for (FriendLabel label : FriendLabelList) {
							// FriendLabel ����
							FriendLabel drl = new FriendLabel(mainView, label.getProfile(), label.getUserName(), label.getStatus());
							if (ul.contains(label.getUserName())) {
								drl.checkbox.setEnabled(false);
							}
							DialogFriendLabelList.add(drl);
							fd.addComponent(drl);
						
						}
						
						break;

						
					case "60": // ä�ù� ��ȣ�� ä�ù� ���̺� ����
						// TODO ������ �����ʵ� ���ļ� �� �������� �ؾ���
						String roomName = cm.getData();
						ChatView cv= new ChatView(mainView, cm.room_id, roomName);
						RoomLabel rl = new RoomLabel(mainView, cv, profile_default, roomName, cm.room_id);
						RoomLabelList.add(rl);
						addComponent(roomTextPane, rl);
						break;
						
						
					// �Ϲ� �޽���
					case "200":
						for (RoomLabel roomLabel : RoomLabelList) {
							if (cm.room_id.equals(roomLabel.getRoomId())) {
								String format = "aa hh:mm";
								SimpleDateFormat type = new SimpleDateFormat(format);
								String time = type.format(cm.time.getTime());
								// ���� ���� �޽���
								if (cm.getId().equals(userName)) {
									roomLabel.getChatView().AppendTextRight(cm.getData());
								}
								else {
									roomLabel.getChatView().AppendTextLeft(cm.profile, cm.getId(), cm.getData(), time);
								}
								roomLabel.setLastMsg(cm.getData());
							}
						}
						break;
						
					// ����
					case "210":
						for (RoomLabel roomLabel : RoomLabelList) {
							if (cm.room_id.equals(roomLabel.getRoomId())) {
								String format = "aa hh:mm";
								SimpleDateFormat type = new SimpleDateFormat(format);
								String time = type.format(cm.time.getTime());
								// ���� ���� �޽���
								if (cm.getId().equals(userName)) {
									roomLabel.getChatView().AppendImageRight(cm.img, time);
								}
								else {
									roomLabel.getChatView().AppendImageLeft(cm.profile, cm.getId(), cm.img, time);
								}
								roomLabel.setLastMsg(cm.getData());
							}
						}
						break;			
						
					case "220": //����
						System.out.println("���ϸ� : " + cm.file.getName());
						//cm.file.createNewFile();
							
						break;
					}
				} catch (IOException e) {
					try {
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����

			}
		}
	}


	public void sendObject(Object ob) { // ������ �޼����� ������ �޼ҵ�
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("�޼��� �۽� ����!!");
		}
	}

	class MenuPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private ImageIcon main_friend = new ImageIcon("./img/main_friend.png");
		private ImageIcon main_friend_clicked = new ImageIcon("./img/main_friend_clicked.png");
		private ImageIcon main_chatroom = new ImageIcon("./img/main_chatroom.png");
		private ImageIcon main_chatroom_clicked = new ImageIcon("./img/main_chatroom_clicked.png");
		private ButtonGroup btnGroup = new ButtonGroup();
		
		public MenuPanel() {
			mainView.add(this);
			setBackground(new Color(236, 236, 237));
			setBounds(0, 0, 70, 591);
			setLayout(null);

						
			friendBtn = new JToggleButton(main_friend);
			add(friendBtn);
			friendBtn.setSelected(true);
			btnGroup.add(friendBtn);
			friendBtn.setSelectedIcon(main_friend_clicked);
			friendBtn.setRolloverIcon(main_friend_clicked);
			friendBtn.setBounds(0, 35, 70, 70);
			friendBtn.setBorderPainted(false);
			friendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			chatroomBtn = new JToggleButton(main_chatroom);
			add(chatroomBtn);
			btnGroup.add(chatroomBtn);
			chatroomBtn.setSelectedIcon(main_chatroom_clicked);
			chatroomBtn.setRolloverIcon(main_chatroom_clicked);
			chatroomBtn.setBounds(0, 103, 70, 70);
			chatroomBtn.setBorderPainted(false);
			chatroomBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			// ģ��, ä�� ��ư ��� �̺�Ʈ
			friendBtn.addItemListener(new ItemListener() {
				   public void itemStateChanged(ItemEvent ev) {
				      if(ev.getStateChange()==ItemEvent.SELECTED){
			            	friendPanel.setVisible(true);
			            	chatroomPanel.setVisible(false);
				      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			            	friendPanel.setVisible(false);
			            	chatroomPanel.setVisible(true);
				      }
				   }
				});
			
		}
	}
	
	// JTextPane�� ������Ʈ �߰��ϴ� �Լ�
	public void addComponent(JTextPane textPane, Component component) {
		StyledDocument doc = (StyledDocument) textPane.getDocument();
	    Style style = doc.addStyle("StyleName", null);
	    StyleConstants.setComponent(style, component);
	    try {
			doc.insertString(doc.getLength(), "ignored text\n", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	// ģ�� ��� ������ �г�
	class FriendPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private ImageIcon main_search = new ImageIcon("./img/main_search.png");
		private ImageIcon main_addFriend = new ImageIcon("./img/main_addFriend.png");
		private ImageIcon profile_default = new ImageIcon("./img/profile_default.png");
		private ImageIcon main_addChat = new ImageIcon("./img/main_addChat.png");
		private JTextField textField;
		private JScrollPane scrollPane;
		
		public FriendPanel(JFrame frame) {
			frame.add(this);
			setBounds(70, 0, 304, 591);
			setBackground(Color.WHITE);
			setLayout(null);
			
			JPanel topPanel = new JPanel();
			topPanel.setBounds(0, 0, 374, 61);
			add(topPanel);
			topPanel.setBackground(Color.WHITE);
			topPanel.setLayout(null);
			
			textField = new JTextField("ģ��");
			topPanel.add(textField);
			textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			textField.setEditable(false);
			textField.setFont(new Font("���� ���", Font.BOLD, 16));
			textField.setBackground(Color.WHITE);
			textField.setBounds(12, 22, 87, 29);
			textField.setColumns(10);
			
//			JButton searchBtn = new JButton(main_search);
//			topPanel.add(searchBtn);
//			searchBtn.setBounds(202, 10, 41, 41);
//			searchBtn.setBorderPainted(false);
//			searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			
//			JButton addFriendBtn = new JButton(main_addFriend);
//			topPanel.add(addFriendBtn);
//			addFriendBtn.setBounds(251, 10, 41, 41);
//			addFriendBtn.setBorderPainted(false);
//			addFriendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			JButton addRoomBtn = new JButton(main_addChat);
			topPanel.add(addRoomBtn);
			addRoomBtn.setBounds(251, 10, 41, 41);
			addRoomBtn.setBorderPainted(false);
			addRoomBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			// ä�� �߰� ��ư Ŭ�� �̺�Ʈ
			addRoomBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int count = 0;
					String userList = "";
					for (FriendLabel label : FriendLabelList) {
						if (label.isSelected()) {
							count++;
							userList += label.getUserName() + " ";
						}
					}
					if (count != 0) { // ������ ����� ���� ���� ä�ù� ����
						String roomName = String.join(", ", userList.split(" "));
						ChatMsg obcm = new ChatMsg(userName, "60", roomName);
						obcm.userlist = userList;
						sendObject(obcm);
						
						chatroomBtn.doClick(); // ä�ø������ ����
					}
				}
			});
			
			
			scrollPane = new JScrollPane();
			add(scrollPane);
			scrollPane.setBounds(0, 70, 304, 520);
			scrollPane.setBorder(null);
			
			
			friendTextPane = new JTextPane();
			scrollPane.add(friendTextPane);
			friendTextPane.setBackground(Color.WHITE);
			friendTextPane.setEditable(false);
			friendTextPane.setFont(new Font("���� ���", Font.PLAIN, 14));
			scrollPane.setViewportView(friendTextPane);
			
			
			// ��ũ�� �� ���� �ø���
			friendTextPane.setSelectionStart(0);
			friendTextPane.setSelectionEnd(0);
			
		}		
	}
	
	
	
	// ä�� ��� ������ �г�
	class ChatroomPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		
		private ImageIcon main_search = new ImageIcon("./img/main_search.png");
		private ImageIcon main_addChat = new ImageIcon("./img/main_addChat.png");
		private ImageIcon profile_default = new ImageIcon("./img/profile_default.png");
		private JTextField textField;
		private JScrollPane scrollPane;

		public ChatroomPanel(JFrame frame) {
			frame.add(this);
			setBounds(70, 0, 304, 591);
			setBackground(Color.WHITE);
			setLayout(null);
			setVisible(false);
			
			JPanel topPanel = new JPanel();
			topPanel.setBounds(0, 0, 374, 61);
			add(topPanel);
			topPanel.setBackground(Color.WHITE);
			topPanel.setLayout(null);
			
			textField = new JTextField("ä��");
			topPanel.add(textField);
			textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			textField.setEditable(false);
			textField.setFont(new Font("���� ���", Font.BOLD, 16));
			textField.setBackground(Color.WHITE);
			textField.setBounds(12, 22, 87, 29);
			textField.setColumns(10);
			
//			JButton searchBtn = new JButton(main_search);
//			topPanel.add(searchBtn);
//			searchBtn.setBounds(202, 10, 41, 41);
//			searchBtn.setBorderPainted(false);
//			searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
//			JButton addChatBtn = new JButton(main_addChat);
//			topPanel.add(addChatBtn);
//			addChatBtn.setBounds(251, 10, 41, 41);
//			addChatBtn.setBorderPainted(false);
//			addChatBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			scrollPane = new JScrollPane();
			add(scrollPane);
			scrollPane.setBounds(0, 70, 304, 520);
			scrollPane.setBorder(null);
			
			roomTextPane = new JTextPane();
			scrollPane.add(roomTextPane);
			roomTextPane.setBackground(Color.WHITE);
			roomTextPane.setEditable(false);
			roomTextPane.setFont(new Font("���� ���", Font.PLAIN, 14));
			scrollPane.setViewportView(roomTextPane);
			
			// ��ũ�� �� ���� �ø���
			roomTextPane.setSelectionStart(0);
			roomTextPane.setSelectionEnd(0);
		}

	}
	
	public List<FriendLabel> getFriendLabelList() {
		return DialogFriendLabelList;
	}
}