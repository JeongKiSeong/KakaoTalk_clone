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

// �ؽ��±׿� ģ�� ����� �־����, ģ�� �̸����� ��ġ
// ���ͷ� for ���鼭 �ص� ��. �� �ؽ��±׷� �� �ʿ� ����.
//ģ����� �� for���鼭 �ٲٰ�, ä�ù浵 �� �ٲٰ�, ä�ù浵 �� �ٲٰ�
//-> ������ ��� �����͸� ������ �־�� ��

// �̹��� ũ�� : (40, 40)
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
		nameLabel.setFont(new Font("���� ���", Font.BOLD, 12));
		
		JLabel timeLabel = new JLabel();
		add(timeLabel);
		timeLabel.setBounds(48, 28, 77, 25);
		
		String format = "aa hh:mm";
		Calendar today = Calendar.getInstance();

		SimpleDateFormat type = new SimpleDateFormat(format);
		timeLabel.setText(type.format(today.getTime()));
		timeLabel.setFont(new Font("���� ���", Font.PLAIN, 12));
		
	}
}