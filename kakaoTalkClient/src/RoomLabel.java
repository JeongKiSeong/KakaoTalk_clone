import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		
		JPanel imgPanel=new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				g.drawImage(roomImg.getImage(),0,0,getWidth()/2-2,getHeight()/2-2,this);
				g.drawImage(roomImg.getImage(),getWidth()/2+2,0,getWidth()/2-2,getHeight()/2-2,this);
				g.drawImage(roomImg.getImage(),0,getHeight()/2+2,getWidth()/2-2,getHeight()/2-2,this);
				g.drawImage(roomImg.getImage(),getWidth()/2+2,getHeight()/2+2,getWidth()/2-2,getHeight()/2-2,this);
			}
		};
		
		//JLabel imgLabel = new JLabel(roomImg);
		this.add(imgPanel);
		imgPanel.setBounds(5, 5, 61, 61);
		
		JLabel bigTextLabel = new JLabel(roomName);
		this.add(bigTextLabel);
		bigTextLabel.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		bigTextLabel.setBounds(80, 10, 180, 25);
		
		JLabel smallTextLabel = new JLabel(lastMsg);
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
}
