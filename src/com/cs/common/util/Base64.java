package com.cs.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Base64 {
	//加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = java.util.Base64.getEncoder().encodeToString(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			try {
				java.util.Base64.getDecoder().decode(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	public static String putImage(Image image)
		throws IOException {
		// Image->bufferreImage
		BufferedImage bimg = new BufferedImage(image.getWidth(null),
			image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = bimg.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		// bufferImage->base64
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bimg, "jpg", outputStream);
		String base64Img = java.util.Base64.getEncoder().encodeToString(outputStream.toByteArray());

		return "data:image/png;base64," + base64Img;
	}
}
