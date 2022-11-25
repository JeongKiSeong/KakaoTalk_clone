import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

// 해쉬태그에 친구 목록을 넣어놓고, 친구 이름으로 서치
// 벡터로 for 돌면서 해도 됨. 꼭 해쉬태그로 할 필요 없음.
//친구목록 다 for돌면서 바꾸고, 채팅방도 다 바꾸고, 채팅방도 다 바꾸고
//-> 서버가 모든 데이터를 가지고 있어야 함

// 이미지 크기 : (40, 40)
public class MsgStatusPanel extends JPanel {
	private ImageIcon profile_default = new ImageIcon("./img/profile_default.png");

	public MsgStatusPanel(ImageIcon profile, String name) {
		setLayout(null);
		setBackground(new Color(186, 206, 224));
		setBounds(0, 0, 170, 60);
		this.setPreferredSize(new Dimension(170, 60));
		
		JButton profileButton = new JButton(profile);
		add(profileButton);
		profileButton.setBounds(0, 10, 40, 40);
		profileButton.setBorderPainted(false);
		profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JLabel nameLabel = new JLabel(name);
		add(nameLabel);
		nameLabel.setBounds(48, 5, 93, 22);
		nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		
		JLabel timeLabel = new JLabel();
		add(timeLabel);
		timeLabel.setBounds(48, 28, 77, 25);
		
		String format = "aa hh:mm";
		Calendar today = Calendar.getInstance();

		SimpleDateFormat type = new SimpleDateFormat(format);
		timeLabel.setText(type.format(today.getTime()));
		timeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		
	}
}