import javax.swing.JFrame;
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

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;


// 친구 목록, 채팅 목록 나오는 화면
public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Socket socket; // 연결소켓
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	
	
	public MainView(String username, String ip_addr, String port_no) {
		setBounds(100, 100, 390, 630);
		getContentPane().setLayout(null);
		setTitle("카카오톡");
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		FriendPanel friendPanel = new FriendPanel(this);
		ChatroomPanel chatroomPanel = new ChatroomPanel(this);
		new MenuPanel(this, friendPanel, chatroomPanel);
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			
			ListenNetwork net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	// Server Message를 수신해서 화면에 표시
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
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
					} else
						continue;
					
					switch (cm.getCode()) {
					case "200":
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
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}
	}
		

	class MenuPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private ImageIcon main_friend = new ImageIcon("./img/main_friend.png");
		private ImageIcon main_friend_clicked = new ImageIcon("./img/main_friend_clicked.png");
		private ImageIcon main_chatroom = new ImageIcon("./img/main_chatroom.png");
		private ImageIcon main_chatroom_clicked = new ImageIcon("./img/main_chatroom_clicked.png");
		private ButtonGroup btnGroup = new ButtonGroup();
		
		public MenuPanel(JFrame frame, FriendPanel friendPanel, ChatroomPanel chatroomPanel) {
			frame.add(this);
			setBackground(new Color(236, 236, 237));
			setBounds(0, 0, 70, 591);
			setLayout(null);

						
			JToggleButton friendBtn = new JToggleButton(main_friend);
			add(friendBtn);
			friendBtn.setSelected(true);
			btnGroup.add(friendBtn);
			friendBtn.setSelectedIcon(main_friend_clicked);
			friendBtn.setRolloverIcon(main_friend_clicked);
			friendBtn.setBounds(0, 35, 70, 70);
			friendBtn.setBorderPainted(false);
			
			JToggleButton chatroomBtn = new JToggleButton(main_chatroom);
			add(chatroomBtn);
			btnGroup.add(chatroomBtn);
			chatroomBtn.setSelectedIcon(main_chatroom_clicked);
			chatroomBtn.setRolloverIcon(main_chatroom_clicked);
			chatroomBtn.setBounds(0, 103, 70, 70);
			chatroomBtn.setBorderPainted(false);
			
			// 친구, 채팅 버튼 토글 이벤트
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
	
	// 친구 목록 나오는 패널
	class FriendPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private ImageIcon main_search = new ImageIcon("./img/main_search.png");
		private ImageIcon main_addFriend = new ImageIcon("./img/main_addFriend.png");
		private JTextField textField;
		
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
			
			textField = new JTextField("친구");
			topPanel.add(textField);
			textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			textField.setEditable(false);
			textField.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			textField.setBackground(Color.WHITE);
			textField.setBounds(12, 22, 87, 29);
			textField.setColumns(10);
			
			JButton searchBtn = new JButton(main_search);
			topPanel.add(searchBtn);
			searchBtn.setBounds(202, 10, 41, 41);
			searchBtn.setBorderPainted(false);
			
			JButton addFriendBtn = new JButton(main_addFriend);
			topPanel.add(addFriendBtn);
			addFriendBtn.setBounds(251, 10, 41, 41);
			addFriendBtn.setBorderPainted(false);
			
			
			JScrollPane scrollPane = new JScrollPane();
			add(scrollPane);
			scrollPane.setBounds(0, 141, 304, 450);
			
			
			JTextPane textPane = new JTextPane();
			scrollPane.add(textPane);
			textPane.setText("친구들 프로필");
			textPane.setBackground(Color.LIGHT_GRAY);
			textPane.setEditable(false);
			textPane.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
			scrollPane.setViewportView(textPane);
			
			JButton btnNewButton = new JButton("내 프로필");;
			add(btnNewButton);
			btnNewButton.setBounds(0, 70, 304, 61);
		}
	}
	
	
	// 채팅 목록 나오는 패널
	class ChatroomPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		
		private ImageIcon main_search = new ImageIcon("./img/main_search.png");
		private ImageIcon main_addChat = new ImageIcon("./img/main_addChat.png");
		private JTextField textField;

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
			
			textField = new JTextField("채팅");
			topPanel.add(textField);
			textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			textField.setEditable(false);
			textField.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			textField.setBackground(Color.WHITE);
			textField.setBounds(12, 22, 87, 29);
			textField.setColumns(10);
			
			JButton searchBtn = new JButton(main_search);
			topPanel.add(searchBtn);
			searchBtn.setBounds(202, 10, 41, 41);
			searchBtn.setBorderPainted(false);
			
			JButton addChatBtn = new JButton(main_addChat);
			topPanel.add(addChatBtn);
			addChatBtn.setBounds(251, 10, 41, 41);
			addChatBtn.setBorderPainted(false);
			
			JTextPane textPane = new JTextPane();
			add(textPane);
			textPane.setText("채팅방 위치");
			textPane.setEditable(false);
			textPane.setBackground(new Color(255, 255, 128));
			textPane.setBounds(0, 70, 374, 520);
		}
	}
}