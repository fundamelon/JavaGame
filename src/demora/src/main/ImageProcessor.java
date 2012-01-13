package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.*;

public class ImageProcessor {
	public static Image colorToAlpha(BufferedImage in, final Color color) {
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;
			public final int filterRGB(int x, int y, int rgb) {
				if((rgb | 0xFF000000) == markerRGB) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		
		ImageProducer ip = new FilteredImageSource(in.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
		
	}
	
	public static Image colorToAlpha(BufferedImage in, int color) {
		return colorToAlpha(in, new Color(color, true));
	}
	
	public static BufferedImage imageToBufferedImage(Image in) {
		BufferedImage bufferedImage = new BufferedImage(in.getWidth(null), in.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(in, 0, 0, null);
		g2.dispose();
		return bufferedImage;
	}
}
