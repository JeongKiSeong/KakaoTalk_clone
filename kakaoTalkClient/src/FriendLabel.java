import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FriendLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private ImageIcon status_red = new ImageIcon("./img/status_red.png");
	private ImageIcon status_green = new ImageIcon("./img/status_green.png");
	private JCheckBox checkbox;
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
		
		JLabel imgLabel = new JLabel(img);
		this.add(imgLabel);
		imgLabel.setBounds(5, 5, 61, 61);
		
		JLabel bigTextLabel = new JLabel(bigText);
		this.add(bigTextLabel);
		bigTextLabel.setFont(new Font("���� ���", Font.BOLD, 16));
		bigTextLabel.setBounds(80, 10, 180, 25);
		
		JLabel smallTextLabel = new JLabel(smallText);
		this.add(smallTextLabel);
		smallTextLabel.setFont(new Font("���� ���", Font.PLAIN, 14));
		smallTextLabel.setBounds(80, 35, 180, 20);
		
		// TODO �¶��� -> �ʷϺ�, �������� -> �Ķ���
//		JLabel StatusLabel = new JLabel(status_green);
//		this.add(StatusLabel);
//		StatusLabel.setBounds(260, 30, 10, 10);
		
		checkbox = new JCheckBox();
		this.add(checkbox);
		checkbox.setBounds(260, 25, 20, 20);
		checkbox.setBackground(Color.WHITE);
		
		// Ŭ�� �� ������ ������ ����
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)  
		    {  
				JFrame frame = new ProfileFrame(img, bigText, smallText);
				Point location = getLocationOnScreen();
				frame.setLocation(location.x + getWidth() + 20, location.y);
				
				// TODO ������ ���� �г�?
				// TODO userName.equals(name)�� �� ������ ���� ����. 
		    }  
		});
		
		// ���콺 over�� �� �� ��ü
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

	// ������ ��� ������
	class ProfileFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		public ProfileFrame(ImageIcon profile, String name, String status) {
			setTitle("������");
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
			nameLabel.setFont(new Font("���� ���", Font.BOLD, 16));
			nameLabel.setBounds(100, 133, 90, 34);
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//nameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

			JLabel statusLabel = new JLabel(status);
			panel.add(statusLabel);
			statusLabel.setBounds(58, 177, 172, 29);
			statusLabel.setFont(new Font("���� ���", Font.PLAIN, 14));
			statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//statusLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			// TODO ���̺� ���콺 ������ �ޱ� -> Ŭ���ϸ� ���� Ȯ��
			// TODO �� ������ ���� -> userName == name�̸� �����ư �߰� or ���̺� Ŭ���� ����â
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
				setTitle("������");
				setSize(300, 300);
				setVisible(true);
				
				Image img = profile.getImage();
				// â�� �������� 300, 300�� ���缭 �̹����� ����
				Image changeImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
				ImageIcon changeIcon = new ImageIcon(changeImg);
				
				JLabel jprofile=new JLabel(changeIcon);
				add(jprofile);
			}
		}
	}
	
	// üũ�ڽ� Ȯ�� �޼ҵ�
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