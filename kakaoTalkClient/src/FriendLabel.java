<<<<<<< HEAD
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.GraphicAttribute;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
=======
>>>>>>> branch 'master' of https://github.com/JeongKiSeong/KakaoTalk_clone.git
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class FriendLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private ImageIcon status_red = new ImageIcon("./img/status_red.png");
	private ImageIcon status_green = new ImageIcon("./img/status_green.png");
	public JCheckBox checkbox;
	private String userName; 
	private ImageIcon profile; 
	private String status;
	
	public FriendLabel(ImageIcon img, String bigText, String smallText) {
		this.profile = img;
		this.userName = bigText;
		this.status = smallText;
		setOpaque(true);
		setBackground(Color.WHITE);
		//setBorder(BorderFactory.createLineBorder(Color.black));
		setBounds(0, 0, 280, 80);
		setPreferredSize(new Dimension(280, 70));
		setMaximumSize(new Dimension(280, 70));
		setMinimumSize(new Dimension(280, 70));

		Image image=img.getImage();
		BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(image, 0, 0, null);
		bg.dispose();
		BufferedImage bi2=makeRoundedCorner(bi,25);
		ImageIcon icon=new ImageIcon(bi2);
		
		//LineBorder border=new LineBorder(Color.WHITE,5,true);
		JLabel imgLabel = new JLabel(icon);
		//imgLabel.setBorder(border);
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
	
	public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
	    int w = image.getWidth();
	    int h = image.getHeight();
	    BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2 = output.createGraphics();
	    
	    // This is what we want, but it only does hard-clipping, i.e. aliasing
	    // g2.setClip(new RoundRectangle2D ...)

	    // so instead fake soft-clipping by first drawing the desired clip shape
	    // in fully opaque white with antialiasing enabled...
	    g2.setComposite(AlphaComposite.Src);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.WHITE);
	    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
	    
	    // ... then compositing the image on top,
	    // using the white shape from above as alpha source
	    g2.setComposite(AlphaComposite.SrcAtop);
	    g2.drawImage(image, 0, 0, null);
	    
	    g2.dispose();
	    
	    return output;
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
			nameLabel.setBounds(100, 133, 90, 34);
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//nameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

			JLabel statusLabel = new JLabel(status);
			panel.add(statusLabel);
			statusLabel.setBounds(58, 177, 172, 29);
			statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
			statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//statusLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
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