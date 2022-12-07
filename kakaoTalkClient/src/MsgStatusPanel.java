import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import utils.ResizedImage;
import utils.RoundedBorder;

// 해쉬태그에 친구 목록을 넣어놓고, 친구 이름으로 서치
// 벡터로 for 돌면서 해도 됨. 꼭 해쉬태그로 할 필요 없음.
//친구목록 다 for돌면서 바꾸고, 채팅방도 다 바꾸고, 채팅방도 다 바꾸고
//-> 서버가 모든 데이터를 가지고 있어야 함

// 이미지 크기 : (40, 40)
public class MsgStatusPanel extends JPanel {
	private JLabel profileLabel;
	private JLabel nameLabel;
	private MainView mainView;
	
	public MsgStatusPanel(ChatView chatView, ImageIcon profile, String name, String time) {
		this.mainView = chatView.getMainVeiw();
		setLayout(null);
		setBackground(new Color(186, 206, 224));
		setBounds(0, 0, 170, 60);
		this.setPreferredSize(new Dimension(170, 60));
		
		profileLabel = new JLabel(profile);
		add(profileLabel);
		profileLabel.setBounds(0, 10, 40, 40);
		profileLabel.setBackground(Color.WHITE);
		profileLabel.setBorder(new RoundedBorder(Color.black, 1, 20));
		profileLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		nameLabel = new JLabel(name);
		add(nameLabel);
		nameLabel.setBounds(48, 5, 93, 22);
		nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		
		JLabel timeLabel = new JLabel();
		add(timeLabel);
		timeLabel.setBounds(48, 28, 77, 25);
		
		timeLabel.setText(time);
		timeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		
		
		// 프로필 클릭 시 뜨는 프로필 화면
//		profileLabel.addMouseListener(new MouseAdapter() { 
//		    public void mouseClicked(MouseEvent e) {
//		    	
//		    }
//		});
	}
	
	
	public void replaceImage(String name, ImageIcon img) {
		if (nameLabel.getText().equals(name))
			profileLabel.setIcon(new ResizedImage(img, 40).run());
		
	}
}