package com.alex.develop.entity;

import android.util.Log;

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
		Log.d("Print", info);
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


	public final static String GITHUB_STORE_RUL = "https://github.com/zxfhacker/zxdstore/raw/master/StocksAnalyzer/";
	public final static String GITHUB_STOCK_LIST_URL = GITHUB_STORE_RUL + "data/sh_sz_list.csv";
	public final static String GITHUB_MANIFEST_URL = GITHUB_STORE_RUL + "manifest.xml";
	public final static String GITHUB_FILE_CHARSET = "UTF-8";
}
