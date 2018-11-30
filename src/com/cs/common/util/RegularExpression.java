package com.cs.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {

	/**
	 * 身份证验证
	 *
	 * @return 验证通过返回true
	 */
	public static boolean isID_card(final String str) {
		//身份证验证规则
		String regEx = "(^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)?$)";
//		String regEx = "(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		// 字符串是否与正则表达式相匹配
		return matcher.matches();
	}


	public static Date isDate(String str) {
		String birthday = null;
		Date birthdate = null;
		try {
			int num = str.length();
			if (num == 15) {
				birthday = str.substring(7, 12);
			}

			if (num == 18) {
				birthday = str.substring(7, 14);
			}
			birthdate = new SimpleDateFormat("yyyyMMdd")
							.parse(birthday);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return birthdate;
	}


	/**
	 * 手机号验证
	 *
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(final String str) {
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 *
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(final String str) {
		boolean b = false;
		Pattern p2 = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$");         // 验证没有区号的
		Matcher m = p2.matcher(str);
		b = m.matches();
		return b;
	}

}
