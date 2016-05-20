package com.chat.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	/**
	 * 储存name
	 * 
	 * @param context
	 * @param id
	 */
	public static void setName(Context context, String name) {
		SharedPreferences spf = context.getSharedPreferences("login", Activity.MODE_PRIVATE);
		Editor editor = spf.edit();
		editor.putString("name", name);
		editor.commit();

	}

	/**
	 * 获取name
	 * 
	 * @param context
	 */
	public static String getName(Context context) {
		SharedPreferences spf = context.getSharedPreferences("login", Activity.MODE_PRIVATE);

		return spf.getString("name", "");

	}

	/**
	 * 存储密码
	 * 
	 * @param context
	 * @param id
	 */
	public static void setPass(Context context, String pass) {
		SharedPreferences spf = context.getSharedPreferences("login", Activity.MODE_PRIVATE);
		Editor editor = spf.edit();
		editor.putString("pass", pass);
		editor.commit();

	}

	/**
	 * 获取密码
	 * 
	 * @param context
	 */
	public static String getPass(Context context) {
		SharedPreferences spf = context.getSharedPreferences("login", Activity.MODE_PRIVATE);

		return spf.getString("pass", "");

	}
}
