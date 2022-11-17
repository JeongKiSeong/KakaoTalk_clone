import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FriendLabel extends JLabel {
	public FriendLabel() {
	}
	private static final long serialVersionUID = 1L;
	
	private ImageIcon status_red = new ImageIcon("./img/status_red.png");
	private ImageIcon status_green = new ImageIcon("./img/status_green.png");
	
	public FriendLabel(ImageIcon img, String bigText, String smallText) {
		setOpaque(true);
		setBackground(Color.WHITE);
		//setBorder(BorderFactory.createLineBorder(Color.black));
		setBounds(0, 0, 280, 80);
		setPreferredSize(new Dimension(280, 70));
		setMaximumSize(new Dimension(280, 70));
		setMinimumSize(new Dimension(280, 70));
		
		JLabel imgLabel = new JLabel(img);
		this.add(imgLabel);
		imgLabel.setBounds(5, 5, 61, 61);
		
		JLabel bigTextLabel = new JLabel(bigText);
		this.add(bigTextLabel);
		bigTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		bigTextLabel.setBounds(80, 10, 180, 25);
		
		JLabel smallTextLabel = new JLabel(smallText);
		this.add(smallTextLabel);
		smallTextLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		smallTextLabel.setBounds(80, 35, 180, 20);
		
		// TODO 온라인 -> 초록불, 오프라인 -> 파란불
		JLabel StatusLabel = new JLabel(status_green);
		this.add(StatusLabel);
		StatusLabel.setBounds(260, 30, 10, 10);
		
		// 클릭 시 프로필 프레임 생성
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)  
		    {  
				JFrame frame = new ProfileFrame(img, bigText, smallText);
				Point location = getLocationOnScreen();
				frame.setLocation(location.x + getWidth() + 20, location.y);
				
				// TODO 프로필 변경 패널?
				// TODO userName.equals(name)일 때 프로필 변경 가능. 
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

	// 프로필 띄울 프레임
	class ProfileFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		public ProfileFrame(ImageIcon profile, String name, String status) {
			setTitle("프로필");
			setSize(300, 300);
			setVisible(true);
			
			JPanel panel = new JPanel();
			add(panel);
			panel.setBackground(Color.WHITE);
			panel.setLayout(null);
			

			JLabel imgLabel = new JLabel(profile);
			panel.add(imgLabel);
			imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imgLabel.setBounds(114, 36, 61, 56);
			//imgLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			JLabel nameLabel = new JLabel(name);
			panel.add(nameLabel);
			nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			nameLabel.setBounds(100, 143, 90, 34);
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//nameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

			JLabel statusLabel = new JLabel(status);
			panel.add(statusLabel);
			statusLabel.setLocation(58, 187);
			statusLabel.setSize(172, 29);
			statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
			statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//statusLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			// TODO 레이블에 마우스 리스너 달기 -> 클릭하면 프사 확대
			// TODO 내 프로필 변경 -> userName == name이면 변경버튼 추가 or 레이블 클릭시 변경창
		}
	}
}