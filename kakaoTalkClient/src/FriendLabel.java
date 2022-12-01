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
	
//	private ImageIcon status_red = new ImageIcon("./img/status_red.png");
//	private ImageIcon status_green = new ImageIcon("./img/status_green.png");
	public JCheckBox checkbox;
	private String userName; 
	private ImageIcon profile; 
	private String status;
	private MainView mainView;
	private JLabel imgLabel;
	private JLabel nameLabel;
	private JLabel statusLabel;
	
	public FriendLabel(MainView mainView, ImageIcon img, String bigText, String smallText) {
		this.profile = img;
		this.userName = bigText;
		this.status = smallText;
		this.mainView = mainView;
		setOpaque(true);
		setBackground(Color.WHITE);
		setBounds(0, 0, 280, 80);
		setPreferredSize(new Dimension(280, 70));
		setMaximumSize(new Dimension(280, 70));
		setMinimumSize(new Dimension(280, 70));
		
		imgLabel = new JLabel(profile);
		this.add(imgLabel);
		imgLabel.setBounds(5, 5, 61, 61);
		
		nameLabel = new JLabel(userName);
		this.add(nameLabel);
		nameLabel.setFont(new Font("���� ���", Font.BOLD, 16));
		nameLabel.setBounds(80, 10, 180, 25);
		
		statusLabel = new JLabel(status);
		this.add(statusLabel);
		statusLabel.setFont(new Font("���� ���", Font.PLAIN, 14));
		statusLabel.setBounds(80, 35, 180, 20);
		
		// �¶��� -> �ʷϺ�, �������� -> �Ķ���
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
				JFrame frame = new ProfileFrame();
				Point location = getLocationOnScreen();
				frame.setLocation(location.x + getWidth() + 20, location.y);
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
	
	public void updateProfile(ImageIcon profile, String name, String status) { // ������ ����
		this.profile = profile;
		this.userName = name;
		this.status = status;
		
		imgLabel.setIcon(this.profile);
		nameLabel.setText(this.userName);
		statusLabel.setText(this.status);
	}

	// ������ ��� ������
	class ProfileFrame extends JFrame {
		private static final long serialVersionUID = 1L;
		private ImageIcon imgicon;

		public ProfileFrame() {
			setTitle("������");
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
			
			JLabel nameLabel = new JLabel(userName);
			panel.add(nameLabel);
			nameLabel.setFont(new Font("���� ���", Font.BOLD, 16));
			nameLabel.setBounds(100, 110, 90, 34);
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//nameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

			JLabel statusLabel = new JLabel(status);
			panel.add(statusLabel);
			statusLabel.setBounds(58, 150, 172, 29);
			statusLabel.setFont(new Font("���� ���", Font.PLAIN, 14));
			statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//statusLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			if (userName.equals(mainView.getUserName())) {
				JButton updateprofileBtn=new JButton("������ ����");
				panel.add(updateprofileBtn);
				updateprofileBtn.setBounds(58, 200, 172, 29);
				updateprofileBtn.setFont(new Font("���� ���", Font.PLAIN, 14));
				updateprofileBtn.setHorizontalAlignment(SwingConstants.CENTER);
				updateprofileBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton btn=(JButton)e.getSource();
						if(btn.equals(updateprofileBtn))
						{
							Frame frame = new Frame("�̹���÷��");
							FileDialog fileDialog = new FileDialog(frame, "�̹��� ����", FileDialog.LOAD);
							fileDialog.setVisible(true);
							imgicon = new ImageIcon(fileDialog.getDirectory() + fileDialog.getFile());
							
							
							int width = imgicon.getIconWidth();
							int height = imgicon.getIconHeight();
							double ratio;
							Image img = imgicon.getImage();
							// Image�� �ʹ� ũ�� �ִ� ���� �Ǵ� ���� 200 �������� ��ҽ�Ų��.
							if (width > 61 || height > 61) {
								if (width > height) { // ���� ����
									ratio = (double) height / width;
									width = 61;
									height = (int) (width * ratio);
								} else { // ���� ����
									ratio = (double) width / height;
									height = 61;
									width = (int) (height * ratio);
								}
								Image new_img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
								imgicon = new ImageIcon(new_img);
							}
							
							imgLabel.setIcon(imgicon);
							ChatMsg msg=new ChatMsg(userName, "30", status);
							msg.img = imgicon;
							mainView.sendObject(msg);
						}
						
					}
				});
			}
			
			imgLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent  e) {
					Point location = getLocationOnScreen();
					new ProfileZoom().setLocation(location.x + getWidth() + 20, location.y);
					//frame.setVisible(false);
				}
			});
		}
			
		public class ProfileZoom extends JFrame {
			public ProfileZoom() {
				setTitle("������");
				setSize(300, 300);
				setResizable(false);
				setVisible(true);
				
				//TODO ���� Ȯ���� ���� ���� �����ϵ��� �����ؾ� ��
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