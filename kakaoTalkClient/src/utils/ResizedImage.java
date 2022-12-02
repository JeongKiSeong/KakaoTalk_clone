package utils;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ResizedImage {
	private ImageIcon image;
	private int size;
	
	public ResizedImage(ImageIcon image, int size) {
		this.image = image;
		this.size = size;
	}
	
	public ImageIcon run() {
		int width = image.getIconWidth();
		int height = image.getIconHeight();
		double ratio;
		Image img = image.getImage();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > size || height > size) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = size;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = size;
				width = (int) (height * ratio);
			}
			Image new_img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			image = new ImageIcon(new_img);
		}
		
		return image;
	}
}
