//JavaObjServer.java ObjectStream ��� ä�� Server

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class KakaoTalkServer extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector<UserService> UserVec = new Vector<UserService>(); // �α����� ��� ������ ����
	private Vector<UserService> LogoutVec = new Vector<UserService>(); // �α׾ƿ��� ��� ���� ����
	private Vector<RoomData> RoomVec = new Vector<RoomData>();
	private Vector<ChatMsg> ChatVec = new Vector<ChatMsg>();
	private int roomNum = 0; // �� ��ȣ ������

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KakaoTalkServer frame = new KakaoTalkServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public KakaoTalkServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
					new_user.start(); // ���� ��ü�� ������ ����
					AppendText("���� ������ �� " + UserVec.size());
					
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("����ڷκ��� ���� object : " + str+"\n");
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getId() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
	class UserService extends Thread {
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Vector user_vc;
		private Socket client_socket;
		public String UserName = "";
		public String UserStatus;
		public ImageIcon ProfileImg;

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// �Ű������� �Ѿ�� �ڷ� ����
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Logout() {
			LogoutVec.add(this); // �α׾ƿ� ���Ϳ� ����
			user_vc.removeElement(this); // Logout�� ���� ��ü�� ���Ϳ��� �����
			ReloadProfile();
		}

		// ��� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				user.WriteOneObject(ob);
			}
		}
		
		
		public void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error");		
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		public void ReloadProfile() {
			WriteAllObject(new ChatMsg("SERVER", "0", "### ���� ###"));
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				AppendText("������ ���� �� : " + user.UserName);
				String message = user.UserName + '|' + user.UserStatus;
				ChatMsg send = new ChatMsg("SERVER", "0", message);
				send.img = user.ProfileImg;
				WriteAllObject(send);
			}
			WriteAllObject(new ChatMsg("SERVER", "0", "### �� ###"));
		}
		
		public ImageIcon makeRoundedCorner(ImageIcon img, int cornerRadius) {
			Image image = img.getImage();
			int size = image.getHeight(null) > image.getWidth(null) ? image.getHeight(null) : image.getWidth(null);
			BufferedImage output = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			
			int w = output.getWidth();
		    int h = output.getHeight();
		    Graphics2D g2 = output.createGraphics();

		    g2.setComposite(AlphaComposite.Src);
		    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    g2.setColor(Color.WHITE);
		    g2.fill(new RoundRectangle2D.Float(0, 0, size, size, cornerRadius, cornerRadius));

		    g2.setComposite(AlphaComposite.SrcAtop);
		    g2.drawImage(image, (size-image.getWidth(null)), 0, null);

		    g2.dispose();

		    
		    ImageIcon new_profile=new ImageIcon(output);
		    return new_profile;
		}
		
		public ImageIcon createRoomImg(List<ImageIcon> imgList) {
			
			BufferedImage off_Image = new BufferedImage(61, 61, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = off_Image.createGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, 61, 61);
			
			if (imgList.size() == 1) { // 1��
				g.drawImage(imgList.get(0).getImage(),0,0,61,61,null);
			} else if (imgList.size() == 2) { // 2��
				g.drawImage(imgList.get(0).getImage(),0,0,35,35,null);
		        g.drawImage(imgList.get(1).getImage(),26,26,35,35,null);
			} else if (imgList.size() == 3) { // 3��
				g.drawImage(imgList.get(0).getImage(),16,1,29,29,null);
	            g.drawImage(imgList.get(1).getImage(),1,31,29,29,null);
	            g.drawImage(imgList.get(2).getImage(),31,31,29,29,null);
			} else { // 4�� �̻�
	            g.drawImage(imgList.get(0).getImage(),0,0,61/2-1,61/2-1,null);
	            g.drawImage(imgList.get(1).getImage(),61/2+1,0,61/2-1,61/2-1,null);
	            g.drawImage(imgList.get(2).getImage(),0,61/2+1,61/2-1,61/2-1,null);
	            g.drawImage(imgList.get(3).getImage(),61/2+1,61/2+1,61/2-1,61/2-1,null);
			}

			
			return new ImageIcon(off_Image);
		}
		
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					switch(cm.getCode()) {
					case "0": // ���� �˸�
						boolean found = false;
						// uservec�� ���鼭 ������ ����� �ִ��� Ȯ��
						for (int i = 0; i < LogoutVec.size(); i++) {
							UserService user = LogoutVec.elementAt(i);
							if (user.UserName.equals(cm.getId())) { 
								// �߰��ϸ� �� ����ڸ� �α׾ƿ� ���Ϳ��� �����ϰ� 
								// �������Ϳ� �߰��� this�� �������Ϳ��� ����
								this.UserName = user.UserName;
								this.UserStatus = user.UserStatus;
								this.ProfileImg = user.ProfileImg;
								found = true;
								LogoutVec.remove(user); // �α׾ƿ� ���Ϳ��� ����
								
								//System.out.println("�̹� �����ϴ� �����");
								break;
							}
						}
						
						if (!found) { // ���� ������ ����
							// ������ ���� ������ ����
							this.UserName = cm.getId();
							this.UserStatus = cm.getData();
							this.ProfileImg = cm.img;
							//System.out.println("���ο� �����");
						}
						
						
						ReloadProfile();
						
						
						// ���� ������� ä�ù� ���, ä�� �޽��� �ε�						
						if (found) {
							// �� ���� ����
							for (int i = 0; i < RoomVec.size(); i++) {
								RoomData room = RoomVec.elementAt(i);
								if (room.getUserlist().contains(this.UserName)) { // �濡 ������ ������
									ChatMsg ob = new ChatMsg("SERVER","60", room.room_name);
									ob.userlist = String.join(" ", room.getUserlist());
									ob.room_id = room.getRoomId();
									ob.img = room.room_img;
									this.WriteOneObject(ob);
									
									// ä�� ����
									Calendar enterTime = room.getEnterTime(this.UserName);
									for (int j = 0; j < ChatVec.size(); j++) {
										ChatMsg chatMsg = ChatVec.elementAt(j);
										if (room.getRoomId().equals(chatMsg.room_id))
											// ���� �ð� ���� ä�ø� ����
											if (enterTime.before(cm.time)) {
												this.WriteOneObject(chatMsg);
											}
									}
								}
							}
						}
						
						break;

						
					case "10": // ������ �ε�
						ReloadProfile();
						break;
				
						
					case "30": // ������ ����
						this.ProfileImg = cm.img;
						this.UserStatus = cm.getData();
						WriteAllObject(cm); 
						
						// ChatVec ��ȯ�ϸ鼭 cm.profile �����ϱ�
						String name = cm.getId();
						for (int j = 0; j < ChatVec.size(); j++) {
							ChatMsg chatMsg = ChatVec.elementAt(j);
							if (name.equals(chatMsg.getId()))
								chatMsg.profile = new ResizedImage(cm.img, 40).run();
						}
						
						
						for (int i=0; i<RoomVec.size(); i++) {
							RoomData room = RoomVec.elementAt(i);
							if (room.getUserlist().contains(name)) {
								// �濡 ���� ������ ����� ������ �� �̹��� ����
								List<ImageIcon> imgList1 = new ArrayList<ImageIcon>();
								for (String userName : room.getUserlist()) {
									for (int j = 0; j < user_vc.size(); j++) {
										UserService user = (UserService) user_vc.elementAt(j);
										if (user.UserName.equals(userName)) {
											imgList1.add(user.ProfileImg);
										}
									}
								}
								ImageIcon room_img1 = createRoomImg(imgList1);
								room.room_img = room_img1;
								

								ChatMsg cm41 = new ChatMsg("SERVER", "41", "�� ���� ����");
								cm41.img = room.room_img;
								cm41.room_id = room.getRoomId();
								WriteAllObject(cm41);
								
							}
						}
						
						
						break;
						
					
					case "40": // �� ���� ����
						for (int i = 0; i < RoomVec.size(); i++) {
							RoomData room = RoomVec.elementAt(i);
							if (cm.room_id.equals(room.getRoomId())) { 
								room.room_name = cm.getData();
								break;
							}
						}
						WriteAllObject(cm); 
						break;
						
						
					case "50": // �� ������ ��� ��û
						for (int i = 0; i < RoomVec.size(); i++) {
							RoomData room = RoomVec.elementAt(i);
							if (cm.room_id.equals(room.getRoomId())) { 
								cm.userlist = String.join(" ", room.getUserlist());
								break;
							}
						}
						this.WriteOneObject(cm);
						break;
						
						
					case "60": // ä�ù� ���� ��û
						String userList[] = cm.userlist.split(" ");
						String roomName = cm.getData();
						List<ImageIcon> imgList = new ArrayList<ImageIcon>();
						
						for (String userName : userList) {
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.equals(userName)) {
									//imgList.add(makeRoundedCorner(user.ProfileImg, 20));
									imgList.add(user.ProfileImg);
								}
							}
						}
						ImageIcon room_img = createRoomImg(imgList);
						
						
						for (String userName : userList) {
							// ä�ù� �����ڵ鿡�� �� ��ȣ ����
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.equals(userName)) { 
									ChatMsg ob = new ChatMsg("SERVER","60", roomName);
									ob.userlist = cm.userlist;
									ob.room_id = Integer.toString(roomNum);
									ob.img = room_img;
									user.WriteOneObject(ob);
								}
							}
						}
						RoomData roomdata = new RoomData(Integer.toString(roomNum), roomName, userList, cm.time);
						roomdata.room_img = room_img;
						RoomVec.add(roomdata);
						roomNum++;
						break;
						
						
					case "90": // ä�ù濡 ģ�� �ʴ�
						RoomData r = null;
						String[] data = cm.userlist.split(" ");
						for (int i=0; i<RoomVec.size(); i++) {
							RoomData room = RoomVec.elementAt(i);
							if (cm.room_id.equals(room.getRoomId())) {
								ArrayList<String> newUserlist = new ArrayList<>();
								newUserlist.addAll(room.getUserlist());
								newUserlist.addAll(Arrays.asList(data));
								room.setUserlist(newUserlist);
								
								// ���� �ð� ����
								room.setEnterTime(cm.getData(), cm.time);
								r = room;
								break;
							}
						}

						List<ImageIcon> imgList1 = new ArrayList<ImageIcon>();
						for (String userName : r.getUserlist()) {
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.equals(userName)) {
									imgList1.add(user.ProfileImg);
								}
							}
						}
						
						ImageIcon room_img1 = createRoomImg(imgList1);
						r.room_img = room_img1;

						ChatMsg cm41 = new ChatMsg("SERVER", "41", "�� ���� ����");
						cm41.img = r.room_img;
						cm41.room_id = r.getRoomId();
						WriteAllObject(cm41);
						
						// uservc ���鼭 �ʴ�� ��������� "60" . userlist�� room_id ��Ƽ�
						for (int i=0; i<data.length; i++) {
							for (int j = 0; j < user_vc.size(); j++) {
								UserService user = (UserService) user_vc.elementAt(j);
								if (user.UserName.equals(data[i])) { 
									ChatMsg cm90 = new ChatMsg("SERVER", "60", cm.getData());
									cm90.userlist = String.join(" ", r.getUserlist());
									cm90.room_id = r.getRoomId();
									cm90.img = r.room_img;
									user.WriteOneObject(cm90);
								}
							} 
						}
						break;
						
						
					case "200": // �Ϲ� �޽���
						cm.profile = new ResizedImage(cm.profile, 40).run();
						ChatVec.add(cm);
						sendToRoomUser(cm);
						break;
						
						
					case "210": // ����
						cm.profile = new ResizedImage(cm.profile, 40).run();
						ChatVec.add(cm);
						sendToRoomUser(cm);
						break;
						
					case "220": // ����
						cm.profile = new ResizedImage(cm.profile, 40).run();
						ChatVec.add(cm);
						sendToRoomUser(cm);
						break;
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			} // while
		} // run
		
		public void sendToRoomUser(ChatMsg cm) {
			for (int i = 0; i < RoomVec.size(); i++) {
				RoomData room = RoomVec.elementAt(i);
				if (cm.room_id.equals(room.getRoomId())) {
					for (String userName : room.getUserlist()) {
						for (int j = 0; j < user_vc.size(); j++) {
							UserService user = (UserService) user_vc.elementAt(j);
							if (user.UserName.equals(userName)) { 
								user.WriteOneObject(cm);
							}
						} 
					}
				}
			}
		}
		
	}

}
