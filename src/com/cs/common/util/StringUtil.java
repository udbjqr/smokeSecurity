package com.cs.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.jdbc.PgArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Formatter;


/**
 * String utility class.
 */
public final class StringUtil {
	public static final Logger logger = LogManager.getLogger(StringUtil.class.getName());
	public static final String NEWLINE;
	private static final char PACKAGE_SEPARATOR_CHAR = '.';


	static {
		// Determine the newline character of the current platform.
		String newLine;

		Formatter formatter = new Formatter();
		try {
			newLine = formatter.format("%n").toString();
		} catch (Exception e) {
			// Should not reach here, but just in case.
			newLine = "\n";
		} finally {
			formatter.close();
		}

		NEWLINE = newLine;
	}

	/**
	 * The shortcut to {@link #simpleClassName(Class) simpleClassName(o.getClass())}.
	 */
	public static String simpleClassName(Object o) {
		if (o == null) {
			return "null_object";
		} else {
			return simpleClassName(o.getClass());
		}
	}

	/**
	 * Generates a simplified name from a {@link Class}.  Similar to {@link Class#getSimpleName()}, but it works fine
	 * with anonymous classes.
	 */
	public static String simpleClassName(Class<?> clazz) {
		String className = ObjectUtil.checkNotNull(clazz, "clazz").getName();
		final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		if (lastDotIdx > -1) {
			return className.substring(lastDotIdx + 1);
		}
		return className;
	}

	private StringUtil() {
		// Unused.
	}

	/**
	 * 判断明文加密之后与密码是否匹配,
	 *
	 * @param plaintext 明文
	 * @param passwd    密码
	 * @return 匹配返回true, 不匹配返回false
	 */
	public static boolean matchPasswd(String plaintext, String passwd) {
		return encrypt(plaintext).equals(passwd);
	}

	/**
	 * 加密码一个明文,此过程返回对应的密码,如出现错误,返回NULL
	 *
	 * @param plaintext 需要加密的文本
	 * @return
	 */
	public static String encrypt(String plaintext) {
		MessageDigest en;
		try {
			en = MessageDigest.getInstance("SHA");
			en.update(plaintext.getBytes());
			String pass = Base64.getEncoder().encodeToString(en.digest());
			return pass.trim();
		} catch (NoSuchAlgorithmException e) {
			logger.error("加密出错.", e);
		}
		return null;
	}

	public static String MD5(String data) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 转换一个对象至JSON的值显示.
	 *
	 * @param object 要转换的对象
	 * @return 对象为空，返回""
	 */
	public static String converToJSONString(Object object) {
		if (object != null) {
//			logger.debug(object.getClass().toString() + " \t " + object.toString());
			if (object instanceof String) {
				return "\"" + ((String) object).replace("\"", "\\\"") + "\"";
			} else if (object instanceof Integer || object instanceof Long) {
				return object.toString();
			} else if (object instanceof Date) {
				SimpleDateFormat format = new SimpleDateFormat(InternalConstant.DATE_FORMAT);

				return "\"" + format.format(object) + "\"";
			} else if (object instanceof String[]) {
				StringBuilder builder = new StringBuilder();
				for (String s : (String[]) object) {
					builder.append("\"").append(s).append("\"").append(",");
				}
				if (builder.length() > 0) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("]");
				builder.insert(0, "[");

				return builder.toString();
			} else if (object instanceof Integer[]) {
				StringBuilder builder = new StringBuilder();
				for (Integer s : (Integer[]) object) {
					builder.append(s).append(",");
				}
				if (builder.length() > 0) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("]");
				builder.insert(0, "[");

				return builder.toString();
			} else if (object instanceof PgArray) {
				StringBuilder builder = new StringBuilder();
				try {
					Object array = ((PgArray) object).getArray();
					if (array instanceof Integer[]) {
						for (Integer s : (Integer[]) array) {
							builder.append(s).append(",");
						}
					}
					if (array instanceof String[]) {
						for (String s : (String[]) array) {
							builder.append("\"").append(s).append("\"").append(",");
						}
					}
				} catch (SQLException e) {
					logger.error("出现异常", e);
				}
				if (builder.length() > 0) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("]");
				builder.insert(0, "[");

				return builder.toString();
			} else {
				return object.toString();
			}
		}

		return "\"\"";
	}

	public static boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	public static boolean isEmptyAndUndefined(String value) {
		return value == null || value.isEmpty() || "undefined".equals(value);
	}


	public static String trim(String original, String removed) {
		if (isEmpty(original)) {
			return original;
		}

		int begin = 0, end = original.length();
		if (original.startsWith(removed)) {
			begin = removed.length();
		}

		if (original.endsWith(removed)) {
			end = original.length() - removed.length();
		}

		return original.substring(begin, end);
	}


}
