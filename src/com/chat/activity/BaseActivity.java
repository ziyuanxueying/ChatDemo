/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chat.activity;

import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.chat.Utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	protected NotificationManager notificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setOnCreate();
	}

	private void setOnCreate() {
		// TODO Auto-generated method stub
		setView();
		setDate();
		init();
		setOperation();
	}

	/** 设置布局 第一步 */
	protected abstract void setView();

	/** 接收上级，填充数据 第二步 */
	protected abstract void setDate();

	/** 实例化控件 第三步 */
	protected abstract void init();

	/** 其他操作 第四步 */
	protected abstract void setOperation();

	@Override
	protected void onResume() {
		super.onResume();
		// umeng
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// umeng
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 当按下BACK键时，会被onKeyDown捕获，判断是BACK键，则执行exit方法。
	 * 在exit方法中，会首先判断isExit的值，如果为false的话，则置为true，同时会弹出提示，并在2000毫秒（2秒）后发出一个消息，
	 * 在Handler中将此值还原成false。
	 * 如果在发送消息间隔的2秒内，再次按了BACK键，则再次执行exit方法，此时isExit的值已为true，则会执行退出的方法。
	 */
	public void exit() {
		if (!isExit) {
			isExit = true;
			ToastUtil.showToast(getApplicationContext(), "再按一次退出程序");
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}
}
