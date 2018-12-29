package com.cs.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class FileUtil {
	private static final Logger logger = LogManager.getLogger(FileUtil.class.getName());


	/**
	 * 返回指定文件名的流
	 *
	 * @param fileName 文件名
	 * @return 输入流
	 */

	public static InputStream readFile(String fileName) {
		URLConnection uConn;
		try {
			uConn = Loader.getResource(fileName, FileUtil.class.getClassLoader()).openConnection();
			uConn.setUseCaches(false);
			return uConn.getInputStream();
		} catch (Exception e) {
			logger.error("读取文件{} 发生异常。", fileName, e);
			return null;
		}
	}

	/**
	 * 根据给定的编码，从文件中读取字符串
	 *
	 * @param configURL URL地址
	 * @param code      指定文件的编码格式
	 * @return 读取到的字符串
	 */
	public static String getProperties(URL configURL, String code) {
		logger.debug("Reading configuration from URL {}", configURL);
		InputStream stream;
		URLConnection uConn;
		BufferedReader reader;
		StringBuilder builder = new StringBuilder();

		try {
			uConn = configURL.openConnection();
			uConn.setUseCaches(false);
			stream = uConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(stream, code);
			reader = new BufferedReader(inputStreamReader);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				builder.append(tempString).append(StringUtil.NEWLINE);
			}
			reader.close();

			return builder.toString();
		} catch (Exception e) {
			logger.error("{} error", configURL, e);
			return null;
		}
	}


	public static Properties getPropertiesFromString(String config) {
		Properties properties = new Properties();
		try {
			properties.load(new StringReader(config));
		} catch (Exception e) {
			logger.error("配置转换出错：", e);
		}
		return properties;
	}


	/**
	 * 获得可上传的文件类型
	 */
	public static List<String> getFileTypeList() {
		List<String> fileTypeList = new ArrayList<String>();
		fileTypeList.add("jpg");
		fileTypeList.add("jpeg");
		fileTypeList.add("bmp");
		fileTypeList.add("png");
		fileTypeList.add("gif");
		return fileTypeList;
	}

	/****
	 * 文件转Base64
	 */
	static String readBase64FromFile(String url) throws IOException {
		String format = url.substring(url.lastIndexOf(".") + 1);
		byte[] buffer = Files.readAllBytes(Paths.get(url));

		return "data:image/" + format + ";base64," + Base64.getEncoder().encodeToString(buffer);
	}


	//添加水印
	public static BufferedImage waterMark(String logoText, Font font, String text, int originalWidth) {
		BufferedImage bufferedImage = new BufferedImage(originalWidth, 160, BufferedImage.TYPE_INT_RGB);


		Graphics2D g2d = bufferedImage.createGraphics();

		bufferedImage = g2d.getDeviceConfiguration().createCompatibleImage(originalWidth, 160, Transparency.TRANSLUCENT);
		g2d.dispose();
		g2d = bufferedImage.createGraphics();

		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(1));

		AttributedString ats = new AttributedString(logoText);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ats.addAttribute(TextAttribute.FONT, font, 0, logoText.length());
		AttributedCharacterIterator iterator = ats.getIterator();

		g2d.drawString(iterator, (originalWidth / 2) - logoText.length() - 100, 60);

		if (text.length() > 0) {
			AttributedString atst = new AttributedString(text);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			atst.addAttribute(TextAttribute.FONT, font, 0, text.length());
			AttributedCharacterIterator iterators = atst.getIterator();
			g2d.drawString(iterators, (originalWidth / 4) - text.length(), 120);
		}

		return bufferedImage;
	}


	//执行命令方法
	public static String publicComands(String commands, String url) {
		logger.debug(commands);
		logger.debug(url);
		try {
			Process process = Runtime.getRuntime().exec(commands);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			BufferedReader input = new BufferedReader(ir);
			String lines;
			while ((lines = input.readLine()) != null) {
				logger.debug(lines);
			}
		} catch (Exception e) {
			logger.debug(e);
		}
		return url;
	}

}