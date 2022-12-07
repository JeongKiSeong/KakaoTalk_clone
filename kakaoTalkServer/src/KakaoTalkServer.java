//JavaObjServer.java ObjectStream 기반 채팅 Server

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

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector<UserService> UserVec = new Vector<UserService>(); // 로그인한 사람 저장할 벡터
	private Vector<UserService> LogoutVec = new Vector<UserService>(); // 로그아웃한 사람 저장 벡터
	private Vector<RoomData> RoomVec = new Vector<RoomData>();
	private Vector<ChatMsg> ChatVec = new Vector<ChatMsg>();
	private int roomNum = 0; // 방 번호 배정용

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
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
					
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getId() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
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
			// 매개변수로 넘어온 자료 저장
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
			LogoutVec.add(this); // 로그아웃 벡터에 저장
			user_vc.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			ReloadProfile();
		}

		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
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
			WriteAllObject(new ChatMsg("SERVER", "0", "### 시작 ###"));
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				AppendText("프로필 전송 중 : " + user.UserName);
				String message = user.UserName + '|' + user.UserStatus;
				ChatMsg send = new ChatMsg("SERVER", "0", message);
				send.img = user.ProfileImg;
				WriteAllObject(send);
			}
			WriteAllObject(new ChatMsg("SERVER", "0", "### 끝 ###"));
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
			
			if (imgList.size() == 1) { // 1인
				g.drawImage(imgList.get(0).getImage(),0,0,61,61,null);
			} else if (imgList.size() == 2) { // 2인
				g.drawImage(imgList.get(0).getImage(),0,0,35,35,null);
		        g.drawImage(imgList.get(1).getImage(),26,26,35,35,null);
			} else if (imgList.size() == 3) { // 3인
				g.drawImage(imgList.get(0).getImage(),16,1,29,29,null);
	            g.drawImage(imgList.get(1).getImage(),1,31,29,29,null);
	            g.drawImage(imgList.get(2).getImage(),31,31,29,29,null);
			} else { // 4인 이상
	            g.drawImage(imgList.get(0).getImage(),0,0,61/2-1,61/2-1,null);
	            g.drawImage(imgList.get(1).getImage(),61/2+1,0,61/2-1,61/2-1,null);
	            g.drawImage(imgList.get(2).getImage(),0,61/2+1,61/2-1,61/2-1,null);
	            g.drawImage(imgList.get(3).getImage(),61/2+1,61/2+1,61/2-1,61/2-1,null);
			}

			
			return new ImageIcon(off_Image);
		}
		
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
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
					case "0": // 접속 알림
						boolean found = false;
						// uservec을 돌면서 접속한 기록이 있는지 확인
						for (int i = 0; i < LogoutVec.size(); i++) {
							UserService user = LogoutVec.elementAt(i);
							if (user.UserName.equals(cm.getId())) { 
								// 발견하면 그 사용자를 로그아웃 벡터에서 제거하고 
								// 유저벡터에 추가한 this를 유저벡터에서 제거
								this.UserName = user.UserName;
								this.UserStatus = user.UserStatus;
								this.ProfileImg = user.ProfileImg;
								found = true;
								LogoutVec.remove(user); // 로그아웃 벡터에서 제거
								
								//System.out.println("이미 존재하던 사용자");
								break;
							}
						}
						
						if (!found) { // 새로 접속한 유저
							// 접속한 유저 정보를 저장
							this.UserName = cm.getId();
							this.UserStatus = cm.getData();
							this.ProfileImg = cm.img;
							//System.out.println("새로운 사용자");
						}
						
						
						ReloadProfile();
						
						
						// 기존 유저라면 채팅방 목록, 채팅 메시지 로딩						
						if (found) {
							// 방 정보 전송
							for (int i = 0; i < RoomVec.size(); i++) {
								RoomData room = RoomVec.elementAt(i);
								if (room.getUserlist().contains(this.UserName)) { // 방에 유저가 있으면
									ChatMsg ob = new ChatMsg("SERVER","60", room.room_name);
									ob.userlist = String.join(" ", room.getUserlist());
									ob.room_id = room.getRoomId();
									ob.img = room.room_img;
									this.WriteOneObject(ob);
									
									// 채팅 전송
									Calendar enterTime = room.getEnterTime(this.UserName);
									for (int j = 0; j < ChatVec.size(); j++) {
										ChatMsg chatMsg = ChatVec.elementAt(j);
										if (room.getRoomId().equals(chatMsg.room_id))
											// 입장 시간 이후 채팅만 전송
											if (enterTime.before(cm.time)) {
												this.WriteOneObject(chatMsg);
											}
									}
								}
							}
						}
						
						break;

						
					case "10": // 프로필 로딩
						ReloadProfile();
						break;
				
						
					case "30": // 프로필 변경
						this.ProfileImg = cm.img;
						this.UserStatus = cm.getData();
						WriteAllObject(cm); 
						
						// ChatVec 순환하면서 cm.profile 변경하기
						String name = cm.getId();
						for (int j = 0; j < ChatVec.size(); j++) {
							ChatMsg chatMsg = ChatVec.elementAt(j);
							if (name.equals(chatMsg.getId()))
								chatMsg.profile = new ResizedImage(cm.img, 40).run();
						}
						
						
						for (int i=0; i<RoomVec.size(); i++) {
							RoomData room = RoomVec.elementAt(i);
							if (room.getUserlist().contains(name)) {
								// 방에 프사 변경한 사람이 있으면 방 이미지 변경
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
								

								ChatMsg cm41 = new ChatMsg("SERVER", "41", "방 사진 변경");
								cm41.img = room.room_img;
								cm41.room_id = room.getRoomId();
								WriteAllObject(cm41);
								
							}
						}
						
						
						break;
						
					
					case "40": // 방 정보 변경
						for (int i = 0; i < RoomVec.size(); i++) {
							RoomData room = RoomVec.elementAt(i);
							if (cm.room_id.equals(room.getRoomId())) { 
								room.room_name = cm.getData();
								break;
							}
						}
						WriteAllObject(cm); 
						break;
						
						
					case "50": // 방 참여자 목록 요청
						for (int i = 0; i < RoomVec.size(); i++) {
							RoomData room = RoomVec.elementAt(i);
							if (cm.room_id.equals(room.getRoomId())) { 
								cm.userlist = String.join(" ", room.getUserlist());
								break;
							}
						}
						this.WriteOneObject(cm);
						break;
						
						
					case "60": // 채팅방 생성 요청
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
							// 채팅방 참여자들에게 방 번호 전송
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
						
						
					case "90": // 채팅방에 친구 초대
						RoomData r = null;
						String[] data = cm.userlist.split(" ");
						for (int i=0; i<RoomVec.size(); i++) {
							RoomData room = RoomVec.elementAt(i);
							if (cm.room_id.equals(room.getRoomId())) {
								ArrayList<String> newUserlist = new ArrayList<>();
								newUserlist.addAll(room.getUserlist());
								newUserlist.addAll(Arrays.asList(data));
								room.setUserlist(newUserlist);
								
								// 입장 시간 저장
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

						ChatMsg cm41 = new ChatMsg("SERVER", "41", "방 사진 변경");
						cm41.img = r.room_img;
						cm41.room_id = r.getRoomId();
						WriteAllObject(cm41);
						
						// uservc 돌면서 초대된 사람들한테 "60" . userlist랑 room_id 담아서
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
						
						
					case "200": // 일반 메시지
						cm.profile = new ResizedImage(cm.profile, 40).run();
						ChatVec.add(cm);
						sendToRoomUser(cm);
						break;
						
						
					case "210": // 사진
						cm.profile = new ResizedImage(cm.profile, 40).run();
						ChatVec.add(cm);
						sendToRoomUser(cm);
						break;
						
					case "220": // 파일
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
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
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
