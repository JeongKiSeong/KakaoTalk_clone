import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class EmoticonDialog extends JDialog {
	private JPanel panel;
	private MainView mainView;
	
	public EmoticonDialog(ChatView chatView) {
		mainView = chatView.getMainVeiw();
		this.setTitle("�̸�Ƽ��");
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		Point p = chatView.getLocationOnScreen();
		this.setBounds(p.x - 290, p.y + 230, 300, 400);
		
		JScrollPane scroll = new JScrollPane();
		getContentPane().add(scroll);
		scroll.setBounds(0, 0, 284, 361);
		panel = new JPanel();
		scroll.add(panel);
		scroll.setViewportView(panel);
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(0, 4));
		
		List<File> imgList= getImgFileList(new File("./img/emoticon"));
		
		for (File f : imgList) {
			ImageIcon emoticon = new ImageIcon(f.getPath());
			// ���� ũ�� ��ȯ
			Image temp = emoticon.getImage();
			temp = temp.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			ImageIcon resizedEmoteicon = new ImageIcon(temp);
			JButton Btn = new JButton(resizedEmoteicon);
			panel.add(Btn);
			Btn.setBorderPainted(false);
			Btn.setBackground(Color.WHITE);
			Btn.setPreferredSize(new Dimension(50, 50));
			
			Btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 210���� ����ó�� ������
					ChatMsg cm = new ChatMsg(mainView.getUserName(), "210", "Emoticon");
					cm.img = resizedEmoteicon;
					cm.room_id = chatView.getRoomId();
					cm.profile = mainView.getProfile();
					mainView.sendObject(cm);
				}
			});
		}
	}
	
	// JTextPane�� ������Ʈ �߰��ϴ� �Լ�
		public void addComponent(JTextPane textPane, Component component) {
		    try {
				StyledDocument doc = (StyledDocument) textPane.getDocument();
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
	
	/**
     * �ش� ����� �̹��� ���� ��� ��ȯ 
     */
    public static List<File> getImgFileList(File file){        
            
        List<File> resultList = new ArrayList<File>(); //�̹��� ������ ������ ����Ʈ ����
        
         //������ �̹��������� ���� ���� ������� �� ����Ʈ ��ȯ.
        //System.out.println("�������� ����: "+file.exists());
        if(!file.exists()) return resultList;
        
        File[] list = file.listFiles(new FileFilter() { //���ϴ� ���ϸ� �������� ���� FileFilter����
            
            String strImgExt = "jpg|jpeg|png|gif|bmp"; //����� �̹���Ÿ��
            
            @Override
            public boolean accept(File pathname) {                            
                
                //System.out.println(pathname);
                boolean chkResult = false;
                if(pathname.isFile()) {
                    String ext = pathname.getName().substring(pathname.getName().lastIndexOf(".")+1);
                    //System.out.println("Ȯ����:"+ext);
                    chkResult = strImgExt.contains(ext.toLowerCase());        
                    //System.out.println(chkResult +" "+ext.toLowerCase());
                } else {
                    chkResult = true;
                }
                return chkResult;
            }
        });
        for(File f : list) {            
            if(f.isDirectory()) {
                //�����̸� �̹�������� �������� ����޼��带 ���ȣ��
                resultList.addAll(getImgFileList(f));                 
            }else {            
                //������ �ƴϰ� �����̸� ����Ʈ(resultList)�� �߰�
                resultList.add(f);
            }
        }            
        return resultList; 
    }

}
