import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


// ģ�� ���, ä�� ��� ������ ȭ��
public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Socket socket; // �������
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	public String userName = "";
	
	
	public MainView(String username, String ip_addr, String port_no) {
		userName = username;
		setBounds(100, 100, 390, 630);
		getContentPane().setLayout(null);
		setTitle("īī����");
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
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
					} else
						continue;
					
					switch (cm.getCode()) {
					// 31 : ������ ��û ���
					// 
					case "31":
						ImageIcon myProfileImg = cm.img;
						// appendProfile �Լ��� ������ �ϳ�?
						// �ϴ� append�� ���̶�� �����ϱ�. Ʋ�� ������ ���ݾ�.
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
	
	// ģ�� ��� ������ �г�
	class FriendPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private ImageIcon main_search = new ImageIcon("./img/main_search.png");
		private ImageIcon main_addFriend = new ImageIcon("./img/main_addFriend.png");
		private ImageIcon profile_default = new ImageIcon("./img/profile_default.png");
		private JTextField textField;
		private JTextPane textPane;
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
			
			JButton searchBtn = new JButton(main_search);
			topPanel.add(searchBtn);
			searchBtn.setBounds(202, 10, 41, 41);
			searchBtn.setBorderPainted(false);
			
			JButton addFriendBtn = new JButton(main_addFriend);
			topPanel.add(addFriendBtn);
			addFriendBtn.setBounds(251, 10, 41, 41);
			addFriendBtn.setBorderPainted(false);
			
			
			scrollPane = new JScrollPane();
			add(scrollPane);
			scrollPane.setBounds(0, 70, 304, 520);
			scrollPane.setBorder(null);
			
			
			textPane = new JTextPane();
			scrollPane.add(textPane);
			textPane.setBackground(Color.WHITE);
			textPane.setEditable(false);
			textPane.setFont(new Font("���� ���", Font.PLAIN, 14));
			scrollPane.setViewportView(textPane);
			addComponent(makeProfile(profile_default, "JKS", "���¸޽���"));
			for (int i=0; i<20; i++)
				addComponent(makeProfile(profile_default, "NAME", "STATUS"));
			
		}
		
		// ������ ����� �Լ�
		public Component makeProfile(ImageIcon profile, String name, String status) {
			JLabel label = new JLabel();
			label.setIcon(profile);
			label.setText("<html>" + name + "<br/>" + status + "</html>");
			
			label.setIconTextGap(20); // ������ �ؽ�Ʈ �Ÿ�
			label.setOpaque(true);
			label.setBackground(Color.WHITE);
			label.setMaximumSize(new Dimension(scrollPane.getWidth() - 25, 80));
			label.setMinimumSize(new Dimension(scrollPane.getWidth() - 25, 80));
			label.setBorder(BorderFactory.createLineBorder(Color.black));
			
			// ������ Ŭ������ �� ���� ������ ���� ������
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e)  
			    {  
					JFrame frame = new ProfileFrame();
					Point location = label.getLocationOnScreen();
					frame.setLocation(location.x + label.getWidth(), location.y);
					
					// TODO ������ ���� �г�?
					// TODO userName.equals(name)�� �� ������ ���� ����. 
			    }  
			});
			
			return label;
		}

		// ģ���� �����ʿ� ������Ʈ �߰��ϴ� �Լ�
		public void addComponent(Component component) {
			StyledDocument doc = (StyledDocument) textPane.getDocument();
		    Style style = doc.addStyle("StyleName", null);
		    StyleConstants.setComponent(style, component);
		    try {
				doc.insertString(doc.getLength(), "ignored text\n", style);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		// ������ �����ϴ� ������
		class ProfileFrame extends JFrame {
			public ProfileFrame() {
				setTitle("������ ����");
				setSize(300, 300);
				setVisible(true);
				
				JPanel panel = new JPanel();
				add(panel);
				panel.setBackground(Color.WHITE);
				panel.setLayout(null);
				

				JLabel imgLabel = new JLabel(profile_default);
				panel.add(imgLabel);
				imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
				imgLabel.setBounds(114, 36, 61, 56);
				
				JLabel nameLabel = new JLabel("nameLabel");
				panel.add(nameLabel);
				nameLabel.setFont(new Font("���� ���", Font.PLAIN, 16));
				nameLabel.setBounds(104, 143, 90, 34);

				JLabel statusLabel = new JLabel("statusLabel");
				panel.add(statusLabel);
				statusLabel.setLocation(58, 187);
				statusLabel.setSize(172, 29);
				statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
				
				// TODO ���̺� ���콺 ������ �ޱ�
			}
		}
	}
	
	
	
	// ä�� ��� ������ �г�
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
			
			textField = new JTextField("ä��");
			topPanel.add(textField);
			textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			textField.setEditable(false);
			textField.setFont(new Font("���� ���", Font.BOLD, 16));
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
			textPane.setText("ä�ù� ��ġ");
			textPane.setEditable(false);
			textPane.setBackground(new Color(255, 255, 128));
			textPane.setBounds(0, 70, 374, 520);
		}
	}
}