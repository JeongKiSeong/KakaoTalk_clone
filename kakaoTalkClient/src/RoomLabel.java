import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.ChatImgPanel;
import utils.RoundedBorder;

public class RoomLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private ImageIcon roomImg;
	private String room_id;
	private String roomName;
	private ChatView chatView;
	private JLabel smallTextLabel;
	private JLabel bigTextLabel;
	private JPanel imgPanel;

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
		
		imgPanel = new ChatImgPanel(img);
	    this.add(imgPanel);
		
		bigTextLabel = new JLabel(roomName);
		this.add(bigTextLabel);
		bigTextLabel.setFont(new Font("���� ���", Font.BOLD, 16));
		bigTextLabel.setBounds(80, 10, 180, 25);
		
		smallTextLabel = new JLabel("");
		this.add(smallTextLabel);
		smallTextLabel.setFont(new Font("���� ���", Font.PLAIN, 14));
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
		
		// ���콺 over�� �� �� ��ü
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
	
	public void setRoomName(String roomName) {
		bigTextLabel.setText(roomName);
	}
	public void setRoomImg(ImageIcon img) {
		this.remove(imgPanel);
		imgPanel = new ChatImgPanel(img);
	    this.add(imgPanel);
	    imgPanel.revalidate();
	    imgPanel.repaint();
	}
}
