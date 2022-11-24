import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class RoomLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	public RoomLabel(MainView mainView, ImageIcon img, String bigText, String smallText, String room_id) {
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
		bigTextLabel.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 16));
		bigTextLabel.setBounds(80, 10, 180, 25);
		
		JLabel smallTextLabel = new JLabel(smallText);
		this.add(smallTextLabel);
		smallTextLabel.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 14));
		smallTextLabel.setBounds(80, 35, 180, 20);
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)  
		    {  
				// Double Clicked
				 if(e.getClickCount()==2) {
					JFrame frame = new ChatView(mainView, room_id, bigText);
					Point p = mainView.getLocationOnScreen();
					frame.setLocation(p.x + getWidth() + 100, p.y);
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
}
