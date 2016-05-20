package com.chat.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.DemoHelper;
import com.chat.R;
import com.chat.Utils.SharedPreferencesUtil;
import com.chat.Utils.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;

public class LoginActivity extends BaseActivity {

	private Button bt_login, bt_register;
	private EditText ed_username;
	private EditText ed_password;
	private String userName, password;
	private boolean progressShow;
	private String TAG = "FJ";
	private String name,pass;

	@Override
	protected void setView() {
		// TODO Auto-generated method stub
		// 如果登录成功过，直接进入主页面
		name = SharedPreferencesUtil.getName(this);
		pass = SharedPreferencesUtil.getPass(this);
		if (!TextUtils.isEmpty(pass)) {
			final Intent intent = new Intent(this, MainActivity.class);
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					LoginActivity.this.startActivity(intent);
					finish();
				}
			};
			timer.schedule(task, 1000 * 1);
//			startActivity(new Intent(LoginActivity.this, MainActivity.class));
//			finish();
//			return;
		}
//		else{
			setContentView(R.layout.activity_login);
//		}
	}

	@Override
	protected void setDate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		bt_register = (Button) findViewById(R.id.bt_register);
		bt_login = (Button) findViewById(R.id.bt_login);
		ed_username = (EditText) findViewById(R.id.username);
		ed_password = (EditText) findViewById(R.id.password);
	}

	@Override
	protected void setOperation() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(name)) {
			ed_username.setText(name);
		}
		if (!TextUtils.isEmpty(pass)) {
			ed_password.setText(pass);
		}
		bt_register.setOnClickListener(this);
		bt_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login:
			userName = ed_username.getText().toString();
			password = ed_password.getText().toString();
			if (!EaseCommonUtils.isNetWorkConnected(this)) {
				ToastUtil.showToast(this, getResources().getString(R.string.network_isnot_available));
				return;
			}
			if (TextUtils.isEmpty(userName)) {
				ToastUtil.showToast(this, getResources().getString(R.string.User_name_cannot_be_empty));
				return;
			}
			if (TextUtils.isEmpty(password)) {
				ToastUtil.showToast(this, getResources().getString(R.string.Password_cannot_be_empty));
				return;
			}
			
			/** 数据加载中... */
			progressShow = true;
			final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
			pd.setCanceledOnTouchOutside(false);
			pd.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					android.util.Log.i("FJ", "EMClient.getInstance().onCancel");
					// Log.i("FJ", "EMClient.getInstance().onCancel");
					progressShow = false;
				}
			});
			pd.setMessage(getString(R.string.Is_landing));
			pd.show();
			
			EMClient.getInstance().login(userName, password, new EMCallBack() {

				@Override
				public void onSuccess() {
					Log.d(TAG, "login: onSuccess");

					if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
						pd.dismiss();
					}

					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
				    EMClient.getInstance().groupManager().loadAllGroups();
				    EMClient.getInstance().chatManager().loadAllConversations();
					
					// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
//					boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
//							DemoApplication.currentUserNick.trim());
//					if (!updatenick) {
//						Log.e("LoginActivity", "update current user nick fail");
//					}
					/** 异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务) */
					DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

				    SharedPreferencesUtil.setName(getApplicationContext(), userName);
				    SharedPreferencesUtil.setPass(getApplicationContext(), password);
					// 进入主页面
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}

				@Override
				public void onProgress(int progress, String status) {
					Log.d(TAG, "login: onProgress");
				}

				@Override
				public void onError(final int code, final String message) {
					Log.d(TAG, "login: onError: " + code);
					if (!progressShow) {
						return;
					}
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			break;
		case R.id.bt_register:
			startActivity(new Intent(this,RegisterActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

}
