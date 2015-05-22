package com.alex.develop.settings;

/**
 * 服务器上待更新App的信息，在检测App升级的时候使用
 */
public class Remote {
	
	/**
	 * 解析服待更新App的信息
	 * @param info JSON 数据
	 */
	public static void init (String info) {
		versionCode = 10;
	}
	
	/**
	 * 待更新App的版本号
	 */
	public static int versionCode;
	
	/**
	 * 待更新App的版本名称
	 */
	public static String versionName;
	
	/**
	 * 待更新App更新的新特性
	 */
	public static String newFeatures;
}
