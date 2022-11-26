import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class FriendDialog extends JDialog {
	private JDialog dialog;
	private JTextPane friendTextPane;
	
	public FriendDialog(MainView mainView, ChatView chatView) {
		dialog = this;
		Point p = chatView.getLocationOnScreen();
		this.setBounds(p.x + 390, p.y, 304, 520);
		this.setTitle("친구 초대");
		this.setVisible(true);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setBackground(Color.white);
		panel.setBounds(0, 0, 304, 520);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		scrollPane.setBounds(0, 0, 288, 401);
		scrollPane.setBorder(null);
		
		
		friendTextPane = new JTextPane();
		scrollPane.add(friendTextPane);
		friendTextPane.setBackground(Color.WHITE);
		friendTextPane.setEditable(false);
		friendTextPane.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		scrollPane.setViewportView(friendTextPane);
		
		JButton btnNewButton = new JButton("초대하기");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<FriendLabel> FriendLabelList = mainView.getFriendLabelList();
				int count = 0;
				String userList = "";
				for (FriendLabel label : FriendLabelList) {
					if (label.isSelected()) {
						count++;
						userList += label.getUserName() + " ";
					}
				}
				if (count != 0) { // 선택한 사람이 있을 때만 채팅방 생성
					ChatMsg obcm = new ChatMsg(mainView.getUserName(), "90", chatView.getRoomName());
					obcm.userlist = userList;
					obcm.room_id = chatView.getRoomId();
					mainView.sendObject(obcm);
					
					dialog.dispose();
				}
			}
		});
		btnNewButton.setBounds(100, 415, 97, 46);
		panel.add(btnNewButton);
	}
	// JTextPane에 컴포넌트 추가하는 함수
		public void addComponent(Component component) {
		    try {
				StyledDocument doc = (StyledDocument) friendTextPane.getDocument();
			    Style style = doc.addStyle("StyleName", null);
				if (component == null) {
					doc.insertString(doc.getLength(), "\n", style);
					return;
				}
					
			    StyleConstants.setComponent(style, component);
				doc.insertString(doc.getLength(), "ignored text\n", style);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
}
