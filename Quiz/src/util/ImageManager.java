package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageManager {

	// public static void main(String[] args) throws IOException {

	public static void compressImage(String srcPath, String destPath)
			throws IOException {

		File input = new File(srcPath);
		BufferedImage image = ImageIO.read(input);
		System.out.println("Size:: " + input.length() / 1024 + "KB");
		float comQuality = //0.9f;
				calcualteCompressionQuality(input.length());
		File compressedImageFile = new File(destPath);
		OutputStream os = new FileOutputStream(compressedImageFile);

		Iterator<ImageWriter> writers = ImageIO .getImageWritersByFormatName("jpg");
		ImageWriter writer = (ImageWriter) writers.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		
		System.out.println("comQuality:: "+comQuality);
		param.setCompressionQuality(comQuality);
		writer.write(null, new IIOImage(image, null, null), param);
		os.close();
		ios.close();
		writer.dispose();
		System.out.println("Size of new file:: " + compressedImageFile.length() / 1024);
		System.out.println("Calculated size:: " + (input.length() * comQuality) / 1024);
		System.out.println("Over");

	}

	private static float calcualteCompressionQuality(long size) {
		for (int i = 9; i > 0; i--) {
			if ((size * i) / (1024*10) < 100)
				return (float)i / 10;
		}
		return 0.1f;
	}

	public static void main(String[] args) throws IOException {
		String srcPath = "D:\\Test\\IMG_20140707_105708.jpg";
		String destPath = "D:\\Test\\compress1.jpg";
		compressImage(srcPath, destPath);
	}
}
