package com.cs.common.util;

import java.util.HashMap;

/**
 * 这是一个单例的全局存储数据对象.
 * <p>
 * 此对象采用键、值对方式存储。
 * <p>
 * 可以使用xxx.xxx的方式，取数据。
 * <p>
 * “.”在名称当中出现的时候将被做为分隔符。
 *
 * @author yimin
 */

public class GlobalSave {
	public static final GlobalSave GLOBAL_SAVE = new GlobalSave();
	private final HashMap<String, Object> content;

	private final String separator = ".";

	public static GlobalSave getInstance() {
		return GLOBAL_SAVE;
	}

	private GlobalSave() {
		content = new HashMap<>();
	}

	/**
	 * 将保存的对象值取出.
	 *
	 * @param key 指明要返回的对象.
	 * @param <T> 指定返回的对象的类型，如果无法强转的，将会报异常
	 * @return 保存的值，如果不存在，返回null
	 */
	public synchronized <T> T get(String key) {
		key = StringUtil.trim(key, separator);

		HashMap<String, Object> map = locate(key);
		return map == null ? null : (T) map.get(getLastName(key));
	}


	public <T> T get(String key, T defaultValue) {
		T result = get(key);
		return result == null ? defaultValue : result;
	}

	/**
	 * 保存一个数据.
	 * <p>
	 * 如果保存的数据已经存在，将覆盖原数据.
	 *
	 * @param key   指明要保存的值的名称
	 * @param value 要保存的值。
	 */
	public synchronized void set(String key, Object value) {
		key = StringUtil.trim(key, separator);

		HashMap<String, Object> temp, next = content;
		int index = 0, endIndex;

		while ((endIndex = key.indexOf(separator, index)) > 0) {
			if ((temp = getMap(key.substring(index, endIndex), next)) == null) {
				temp = new HashMap<>();
			}
			next.put(key.substring(index, endIndex), temp);
			next = temp;

			index = endIndex + 1;
		}

		next.put(getLastName(key), value);
	}

	/**
	 * 删除并返回删除的值的记录.
	 *
	 * @param key 指明的名称
	 * @param <T> 指定返回的对象的类型
	 * @return 保存的值，如果不存在，返回null
	 */
	public synchronized <T> T remove(String key) {
		key = StringUtil.trim(key, separator);

		HashMap<String, Object> map = locate(key);
		return map == null ? null : (T) map.remove(getLastName(key));
	}

	private String getLastName(String key) {
		int index;
		if ((index = key.lastIndexOf(separator)) == -1) {
			return key;
		}

		return key.substring(index + 1, key.length());
	}

	private HashMap<String, Object> locate(String key) {
		if (!key.contains(separator)) {
			//如果未有"."分隔符
			return content;
		}

		HashMap<String, Object> temp = content;
		int index = 0, endIndex;

		while ((endIndex = key.indexOf(separator, index)) > 0) {
			if ((temp = getMap(key.substring(index, endIndex), temp)) == null) {
				return null;
			}
			index = endIndex + 1;
		}

		return temp;
	}

	private HashMap<String, Object> getMap(String key, HashMap<String, Object> content) {
		Object object = content.get(key);
		if (object != null && object instanceof HashMap) {
			//noinspection unchecked
			return (HashMap<String, Object>) object;
		}

		return null;
	}
}
