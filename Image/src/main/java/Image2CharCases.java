import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

public class Image2CharCases {

	public static void main(String[] args) {

		BufferedImage image = null;
		String chars = "@&%#M8XOHLTI!)=+;:,^.' ";
		String path = "D:\\Github\\Persistencelayer\\Image\\src\\main\\resources\\1.jpg";

		try {

			image = ImageIO.read(new File(path));

			int width = image.getWidth();
			int height = image.getHeight();
			
			for (int y = 0; y < height; y += 4) {
				for (int x = 0; x < width; x += 2) {

					int pixel = image.getRGB(x, y);
					int R = (pixel & 0xff0000) >> 16;
					int G = (pixel & 0xff00) >> 8;
					int B = pixel & 0xff;

					double gray = 0.299F * R + 0.578F * G + 0.114F * B;
					int index = (int) Math.floor(gray * chars.length() / 256);
					char symbol = chars.charAt(index);

					System.out.print(symbol);
				}
				System.out.println();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
