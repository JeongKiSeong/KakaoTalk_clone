package utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ChatImgPanel extends JPanel {
	private ImageIcon one = null;
	
	public ChatImgPanel(ImageIcon one) {
		this.one = makeRoundedCorner(one, 30);
		
		this.setBorder(new RoundedBorder(Color.black, 1, 20));
		this.setBounds(5, 5, 61, 61);
		this.setBackground(Color.white);
	}
	

	@Override
    protected void paintComponent(Graphics g) {
       // TODO Auto-generated method stub
       super.paintComponent(g);
       // 61, 61
       
       g.drawImage(one.getImage(),0,0,61,61,this);
    }
	
	public ImageIcon makeRoundedCorner(ImageIcon img, int cornerRadius) {
		Image image = img.getImage();
		BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(image, 0, 0, null);
		bg.dispose();
		
		int w = bi.getWidth();
	    int h = bi.getHeight();
	    BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2 = output.createGraphics();

	    g2.setComposite(AlphaComposite.Src);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.WHITE);
	    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

	    g2.setComposite(AlphaComposite.SrcAtop);
	    g2.drawImage(bi, 0, 0, null);

	    g2.dispose();

	    
	    ImageIcon new_profile=new ImageIcon(output);
	    return new_profile;
	}
}
