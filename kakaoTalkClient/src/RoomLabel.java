import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class RoomLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private ImageIcon roomImg;
	private String room_id;
	private String roomName;
	private String lastMsg;
	private ChatView chatView;

	public RoomLabel(MainView mainView, ChatView cv, ImageIcon img, String roomName, String lastMsg, String id) {
		this.roomImg = img;
		this.room_id = id;
		this.roomName = roomName;
		this.lastMsg = lastMsg;
		this.chatView = cv;
		
		setOpaque(true);
		setBackground(Color.WHITE);
		//setBorder(BorderFactory.createLineBorder(Color.black));
		setBounds(0, 0, 280, 80);
		setPreferredSize(new Dimension(280, 70));
		setMaximumSize(new Dimension(280, 70));
		setMinimumSize(new Dimension(280, 70));
		
		JLabel imgLabel = new JLabel(roomImg);
		this.add(imgLabel);
		imgLabel.setBounds(5, 5, 61, 61);
		
		JLabel bigTextLabel = new JLabel(roomName);
		this.add(bigTextLabel);
		bigTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		bigTextLabel.setBounds(80, 10, 180, 25);
		
		JLabel smallTextLabel = new JLabel(lastMsg);
		this.add(smallTextLabel);
		smallTextLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		smallTextLabel.setBounds(80, 35, 180, 20);
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)  
		    {  
				// Double Clicked
				 if(e.getClickCount()==2) {
					cv.setVisible(true);
					Point p = mainView.getLocationOnScreen();
					cv.setLocation(p.x + getWidth() + 100, p.y);
					
//					// 서버에 입장 신호를 줘서 데이터 로딩
//					String msg = String.format("[%s] %s", mainView.getUserName(), room_id + "번 방에 입장");
//					ChatMsg obcm = new ChatMsg(mainView.getUserName(), "10", msg);
//					obcm.room_id = room_id;
//					mainView.sendObject(obcm);
				 }
		    }
		});
		
		// 마우스 over할 때 색 교체
		this.addMouseListener(new MouseAdapter(){
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        setBackground(new Color(248, 248, 248));
		    }
		    @Override
		    public void mouseExited(MouseEvent e) {
		        setBackground(Color.WHITE);
		    }
		});
	}
	
	public String getRoomId() {
		return room_id;
	}
	
	public ChatView getChatView() {
		return chatView;
	}
}
