//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;

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

		private Socket client_socket;
		public String UserName = "";
		public String UserStatus;
		public ImageIcon ProfileImg;

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
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
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			ReloadProfile();
		}

		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < UserVec.size(); i++) {
				UserService user = UserVec.elementAt(i);
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
			for (int i = 0; i < UserVec.size(); i++) {
				UserService user = UserVec.elementAt(i);
				AppendText("프로필 전송 중 : " + user.UserName);
				String message = user.UserName + '|' + user.UserStatus;
				ChatMsg send = new ChatMsg("SERVER", "0", message);
				send.img = user.ProfileImg;
				WriteAllObject(send);
			}
			WriteAllObject(new ChatMsg("SERVER", "0", "### 끝 ###"));
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
								this.UserName = user.UserName;
								this.UserStatus = user.UserStatus;
								this.ProfileImg = user.ProfileImg;
								found = true;
								LogoutVec.remove(this); // 로그아웃 벡터에서 제거
								
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
						// RoomVec 돌면서 userlist에 this.Uesrname있으면
						// 60으로 방 정보 전송하고
						// 방마다 입장 시간 불러와서 -> for문을 여러 개 돌아야 하나?
						// ChatVec 돌면서 입장 시간 이후 Msg를 유저에게 전송
						
						if (found) {
							// 방 정보 전송
							for (int i = 0; i < RoomVec.size(); i++) {
								RoomData room = RoomVec.elementAt(i);
								if (room.getUserlist().contains(this.UserName)) { // 방에 유저가 있으면
									ChatMsg ob = new ChatMsg("SERVER","60", room.room_name);
									ob.userlist = String.join(" ", room.getUserlist());
									ob.room_id = room.getRoomId();
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
						
						// 방마다 입장 시간을 가져와야하는데.. 
						
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
						for (String userName : userList) {
							// 채팅방 참여자들에게 방 번호 전송
							for (int i = 0; i < UserVec.size(); i++) {
								UserService user = UserVec.elementAt(i);
								if (user.UserName.equals(userName)) { 
									ChatMsg ob = new ChatMsg("SERVER","60", roomName);
									ob.userlist = cm.userlist;
									ob.room_id = Integer.toString(roomNum);
									user.WriteOneObject(ob);
								}
							}
						}
						RoomVec.add(new RoomData(Integer.toString(roomNum), roomName, userList, cm.time));
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
						
						// uservc 돌면서 초대된 사람들한테 "60" . userlist랑 room_id 담아서
						for (int i=0; i<data.length; i++) {
							for (int j = 0; j < UserVec.size(); j++) {
								UserService user = UserVec.elementAt(j);
								if (user.UserName.equals(data[i])) { 
									ChatMsg cm90 = new ChatMsg("SERVER", "60", cm.getData());
									cm90.userlist = String.join(" ", r.getUserlist());
									cm90.room_id = r.getRoomId();
									user.WriteOneObject(cm90);
								}
							} 
						}
						break;
						
						
					case "200": // 일반 메시지
						ChatVec.add(cm);
						sendToRoomUser(cm);
						break;
						
						
					case "210": // 사진
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
						for (int j = 0; j < UserVec.size(); j++) {
							UserService user = UserVec.elementAt(j);
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
