package com.chat.Utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * toast工具 ToastUtil.java
 * 
 * @author GPC
 * 
 *         创建于：2015-2-27
 */
public class ToastUtil {

	private static Toast mToast;

	/**
	 * 将toast封装起来，连续点击时可以覆盖上一个
	 */
	public static void showToast(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			mToast.show();
		} else {
			mToast.setText(text);
			mToast.show();
		}
	}

	public static void backgrundreAlpha(float bgAlpha, Context context) {
		// 设置全屏
		WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
		lp.alpha = bgAlpha;
		((Activity) context).getWindow().setAttributes(lp);
	}

	/**
	 * 实现粘贴功能
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	public static String paste(Context context) {
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

	/**
	 * 实现文本复制功能
	 * 
	 * @param text
	 * @param context
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	public static void copy(String text, Context context) {
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
		cmb.setText(text.trim());
	}
}