package com.cs.common.util;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class codeUtile {
	private static int width = 110;// 定义图片的width
	private static int height = 35;// 定义图片的height
	private static int codeCount = 4;// 定义图片上显示验证码的个数
	private static int xx = 20;
	private static int fontHeight = 20;
	private static int codeY = 21;
	private static char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	/**
	 * 生成一个map集合
	 * code为生成的验证码
	 * codePic为生成的验证码BufferedImage对象
	 */
	public static Map<String, Object> generateCodeAndPic() {
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// Graphics2D gd = buffImg.createGraphics();
		// Graphics2D gd = (Graphics2D) buffImg.getGraphics();
		Graphics gd = buffImg.getGraphics();
		// 创建一个随机数生成器类
		Random random = new Random();
		// 将图像填充为白色
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		// 设置字体。
		gd.setFont(font);

		// 画边框。
		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);

		// 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
		gd.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red, green, blue;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String code = String.valueOf(codeSequence[random.nextInt(33)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);

			// 用随机产生的颜色将验证码绘制到图像中。
			gd.setColor(new Color(red, green, blue));
			gd.drawString(code, (i + 1) * xx, codeY);

			// 将产生的四个随机数组合在一起。
			randomCode.append(code);
		}
		Map<String, Object> map = new HashMap<>();
		//存放验证码
		map.put("code", randomCode);
		//存放生成的验证码BufferedImage对象
		map.put("codePic", buffImg);
		return map;
	}

/*
	public static void main(String[] args) throws Exception {

		System.out.println("验证码的值为：" + getStringRandom(7));
	}
*/


	//生成随机数字和字母,
	public static String getStringRandom(int length) {

		StringBuilder val = new StringBuilder();
		Random random = new Random();

		//参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			//输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				//输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val.append((char) (random.nextInt(26) + temp));
			} else {
				val.append(String.valueOf(random.nextInt(10)));
			}
		}
		return val.toString();
	}
}