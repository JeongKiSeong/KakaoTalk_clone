import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import utils.MakeRoundedCorner;

public class FriendLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private ImageIcon status_red = new ImageIcon("./img/status_red.png");
	private ImageIcon status_green = new ImageIcon("./img/status_green.png");
	public JCheckBox checkbox;
	private String userName; 
	private ImageIcon profile; 
	private String status;
	private MainView mainView;
	
	public FriendLabel(MainView mainView, ImageIcon img, String bigText, String smallText) {
		this.profile = img;
		this.userName = bigText;
		this.status = smallText;
		this.mainView=mainView;
		setOpaque(true);
		setBackground(Color.WHITE);
		//setBorder(BorderFactory.createLineBorder(Color.black));
		setBounds(0, 0, 280, 80);
		setPreferredSize(new Dimension(280, 70));
		setMaximumSize(new Dimension(280, 70));
		setMinimumSize(new Dimension(280, 70));
		
		// 프로필 둥글게
		JLabel imgLabel = new JLabel(new MakeRoundedCorner(img, 30).run());
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
//		JLabel StatusLabel = new JLabel(status_green);
//		this.add(StatusLabel);
//		StatusLabel.setBounds(260, 30, 10, 10);
		
		checkbox = new JCheckBox();
		this.add(checkbox);
		checkbox.setBounds(260, 25, 20, 20);
		checkbox.setBackground(Color.WHITE);
		
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
		        checkbox.setBackground(new Color(248, 248, 248));
		    }
		    @Override
		    public void mouseExited(MouseEvent e) {
		        setBackground(Color.WHITE);
		        checkbox.setBackground(Color.WHITE);
		    }
		});
	}

	// 프로필 띄울 프레임
	class ProfileFrame extends JFrame {
		private static final long serialVersionUID = 1L;
		private ImageIcon imgicon;

		public ProfileFrame(ImageIcon profile, String name, String status) {
			setTitle("프로필");
			setSize(300, 300);
			setResizable(false);
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
			nameLabel.setBounds(100, 133, 90, 34);
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//nameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

			JLabel statusLabel = new JLabel(status);
			panel.add(statusLabel);
			statusLabel.setBounds(58, 177, 172, 29);
			statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
			statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//statusLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			if (name.equals(mainView.getUserName())) {
				JButton updateprofileBtn=new JButton("프로필 변경");
				panel.add(updateprofileBtn);
				updateprofileBtn.setBounds(58, 177, 172, 29);
				updateprofileBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
				updateprofileBtn.setHorizontalAlignment(SwingConstants.CENTER);
				updateprofileBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton btn=(JButton)e.getSource();
						if(btn.equals(updateprofileBtn))
						{
							Frame frame = new Frame("이미지첨부");
							FileDialog fileDialog = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
							fileDialog.setVisible(true);
							imgicon = new ImageIcon(fileDialog.getDirectory() + fileDialog.getFile());
							imgLabel.setIcon(imgicon);
							ChatMsg msg=new ChatMsg(userName, "30", "프로필 변경됨");
							msg.profile=imgicon;
							mainView.sendObject(msg);
						}
						
					}
				});
			}
			
			// TODO 레이블에 마우스 리스너 달기 -> 클릭하면 프사 확대
			// TODO 내 프로필 변경 -> userName == name이면 변경버튼 추가 or 레이블 클릭시 변경창
			imgLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent  e) {
					Point location = getLocationOnScreen();
					new ProfileZoom(profile).setLocation(location.x + getWidth() + 20, location.y);
					//frame.setVisible(false);
				}
			});
		}
			
		public class ProfileZoom extends JFrame {
			public ProfileZoom(ImageIcon profile) {
				setTitle("프로필");
				setSize(300, 300);
				setResizable(false);
				setVisible(true);
				
				Image img = profile.getImage();
				// 창의 사이즈인 300, 300에 맞춰서 이미지를 변경
				Image changeImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
				ImageIcon changeIcon = new ImageIcon(changeImg);
				
				JLabel jprofile=new JLabel(changeIcon);
				add(jprofile);
			}
		}
	}
	
	// 체크박스 확인 메소드
	public boolean isSelected() {
		boolean isSel = checkbox.isSelected();
		if (isSel)
			checkbox.doClick();
		return isSel;
	}
	
	// get getUserName
	public String getUserName() {
		return userName;
	}
	public ImageIcon getProfile() {
		return profile;
	}
	public String getStatus() {
		return status;
	}
}