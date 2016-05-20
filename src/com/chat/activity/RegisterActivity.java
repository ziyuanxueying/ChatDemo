package com.chat.activity;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.DemoHelper;
import com.chat.R;
import com.chat.Utils.ToastUtil;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class RegisterActivity extends BaseActivity{

	private EditText ed_userName;
	private EditText ed_password;
	private EditText ed_confirmPwd;
	private Button bt_reg;
	
	@Override
	protected void setView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_register);
	}

	@Override
	protected void setDate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		ed_userName = (EditText) findViewById(R.id.username);
		ed_password = (EditText) findViewById(R.id.password);
		ed_confirmPwd = (EditText) findViewById(R.id.confirm_password);
		bt_reg = (Button) findViewById(R.id.reg_bt);
	}

	@Override
	protected void setOperation() {
		// TODO Auto-generated method stub
		bt_reg.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reg_bt:
			final String username = ed_userName.getText().toString().trim();
			final String pwd = ed_password.getText().toString().trim();
			String confirm_pwd = ed_confirmPwd.getText().toString().trim();
			if (TextUtils.isEmpty(username)) {
				ToastUtil.showToast(this, getResources().getString(R.string.User_name_cannot_be_empty));
				ed_userName.requestFocus();
				return;
			} else if (TextUtils.isEmpty(pwd)) {
				ToastUtil.showToast(this, getResources().getString(R.string.Password_cannot_be_empty));
				ed_password.requestFocus();
				return;
			} else if (TextUtils.isEmpty(confirm_pwd)) {
				ToastUtil.showToast(this, getResources().getString(R.string.Confirm_password_cannot_be_empty));
				ed_confirmPwd.requestFocus();
				return;
			} else if (!pwd.equals(confirm_pwd)) {
				ToastUtil.showToast(this, getResources().getString(R.string.Two_input_password));
				return;
			}
			
			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
				final ProgressDialog pd = new ProgressDialog(this);
				pd.setMessage(getResources().getString(R.string.Is_the_registered));
				pd.show();

				new Thread(new Runnable() {
				public void run() {
					try {
						// 调用sdk注册方法
					EMClient.getInstance().createAccount(username, pwd);
					runOnUiThread(new Runnable() {
						public void run() {
							if (!RegisterActivity.this.isFinishing())
								pd.dismiss();
								// 保存用户名
							DemoHelper.getInstance().setCurrentUserName(username);
							ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.Registered_successfully));
							}
						});
						} catch (final HyphenateException e) {
							runOnUiThread(new Runnable() {
								public void run() {
									if (!RegisterActivity.this.isFinishing())
										pd.dismiss();
									int errorCode=e.getErrorCode();
									if(errorCode==EMError.NETWORK_ERROR){
										Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
									}else if(errorCode == EMError.USER_ALREADY_EXIST){
										Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
									}else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
										Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
									}else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
									    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
									}else{
										Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
									}
								}
							});
						}
					}
				}).start();

			}
			break;

		default:
			break;
		}
	}
	
	public void back(View view) {
		finish();
	}
}
