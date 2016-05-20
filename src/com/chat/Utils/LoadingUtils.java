package com.chat.Utils;

import android.app.Activity;
import android.app.ProgressDialog;

public class LoadingUtils {
	protected static  ProgressDialog pd = new ProgressDialog(null);

	public static void Loading(Activity activity,String string) {
		pd = new ProgressDialog(activity);
//		String st = getResources().getString(R.string.Are_logged_out);
		pd.setMessage(string);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
	}
}
