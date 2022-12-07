import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.border.AbstractBorder;

import utils.RoundedBorderChat;


public class MsgLabel  extends JLabel {
	private static final long serialVersionUID = 1L;


	public MsgLabel(String text, String align) {
		setOpaque(true);
		AbstractBorder brdr;
		this.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		
		
		// TODO 글자가 길 때 줄바꿈 처리 방법 생각하기
		this.setText(text);
		

        if (align.equals("L")) {
        	brdr = new RoundedBorderChat(Color.BLACK,1,6,8);
    		this.setBackground(Color.WHITE);
        }
        else {
        	brdr = new RoundedBorderChat(Color.BLACK,1,6,8,false);
    		this.setBackground(new Color(255, 235, 51));
        }
        setBorder(brdr);
	}
	
	
}
