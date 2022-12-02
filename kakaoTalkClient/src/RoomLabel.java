import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RoomLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private ImageIcon roomImg;
	private String room_id;
	private String roomName;
	private ChatView chatView;
	private JLabel smallTextLabel;

	public RoomLabel(MainView mainView, ChatView cv, ImageIcon img, String roomName, String id) {
		this.roomImg = img;
		this.room_id = id;
		this.roomName = roomName;
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
		bigTextLabel.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		bigTextLabel.setBounds(80, 10, 180, 25);
		
		smallTextLabel = new JLabel("");
		this.add(smallTextLabel);
		smallTextLabel.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 14));
		smallTextLabel.setBounds(80, 35, 180, 20);
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)  
		    {  
				// Double Clicked
				 if(e.getClickCount()==2) {
					cv.setVisible(true);
					Point p = mainView.getLocationOnScreen();
					cv.setLocation(p.x + getWidth() + 100, p.y);
				 }
		    }
		});
		
		// ¸¶¿ì½º overÇÒ ¶§ »ö ±³Ã¼
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
	
	public void setLastMsg(String lastMsg) {
		smallTextLabel.setText(lastMsg);
	}
}
