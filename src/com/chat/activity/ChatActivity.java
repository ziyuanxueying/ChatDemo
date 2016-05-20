package com.chat.activity;

import android.content.Intent;

import com.chat.R;
import com.chat.fragment.ChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;

/**
 * 聊天页面，需要fragment的使用{@link #EaseChatFragment}
 * 
 * @author fj
 * 
 */
public class ChatActivity extends BaseActivity {
	public static ChatActivity activityInstance;
	private EaseChatFragment chatFragment;
	String toChatUsername;

	@Override
	protected void setView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.em_activity_chat);
		activityInstance = this;
	}

	@Override
	protected void setDate() {
		// TODO Auto-generated method stub
		// 聊天人或群id
		toChatUsername = getIntent().getExtras().getString("userId");
		// 可以直接new EaseChatFratFragment使用
		chatFragment = new ChatFragment();
		// 传入参数
		chatFragment.setArguments(getIntent().getExtras());
		//聊天页面的显示
		getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setOperation() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
	}

	@Override
	protected void onNewIntent(Intent intent) { 
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	@Override
	public void onBackPressed() {
		chatFragment.onBackPressed();
		if (EasyUtils.isSingleActivity(this)) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

	public String getToChatUsername() {
		return toChatUsername;
	}
	
	@Override
	public void exit() {
		// TODO Auto-generated method stub
		finish();
	}
}
