package com.chat.activity;

import java.io.ByteArrayOutputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chat.DemoHelper;
import com.chat.R;
import com.chat.Utils.ToastUtil;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
/**
 * 个人信息
 * @author fj
 *
 */
public class PersionInfoActivity extends BaseActivity {
	private static final int REQUESTCODE_PICK = 1;
	private static final int REQUESTCODE_CUTTING = 2;
	
	private String username;
	private ImageView headAvatar;
	private ImageView headPhotoUpdate;
	private ImageView iconRightArrow;
	private TextView tvNickName;
	private TextView tvUsername;
	private ProgressDialog dialog;
	private RelativeLayout rlNickName;
	@Override
	protected void setView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_perinfo);
	}
	@Override
	protected void setDate() {
		// TODO Auto-generated method stub
		username = getIntent().getStringExtra("username");
	}
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		headAvatar = (ImageView) findViewById(R.id.user_head_avatar);
		headPhotoUpdate = (ImageView) findViewById(R.id.user_head_headphoto_update);
		tvUsername = (TextView) findViewById(R.id.user_username);
		tvNickName = (TextView) findViewById(R.id.user_nickname);
		rlNickName = (RelativeLayout) findViewById(R.id.rl_nickname);
		iconRightArrow = (ImageView) findViewById(R.id.ic_right_arrow);
	}
	@Override
	protected void setOperation() {
		// TODO Auto-generated method stub
		rlNickName.setOnClickListener(this);
		headAvatar.setOnClickListener(this);
//		if (username.equals(EMClient.getInstance().getCurrentUser())) {
//		}
		tvUsername.setText(EMClient.getInstance().getCurrentUser());
		EaseUserUtils.setUserNick(username, tvNickName);
		EaseUserUtils.setUserAvatar(this, username, headAvatar);
//		asyncFetchUserInfo(username);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_nickname:
			final EditText editText = new EditText(this);
			new AlertDialog.Builder(this).setTitle(R.string.setting_nickname).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
					.setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String nickString = editText.getText().toString();
							if (TextUtils.isEmpty(nickString)) {
								Toast.makeText(PersionInfoActivity.this, getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
								return;
							}
							updateRemoteNick(nickString);
						}
					}).setNegativeButton(R.string.dl_cancel, null).show();
			break;
		case R.id.user_head_avatar:
			uploadHeadPhoto();
			break;
		default:
			break;
		}
	}
	
	public void asyncFetchUserInfo(String username){
		DemoHelper.getInstance().getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {
			
			@Override
			public void onSuccess(EaseUser user) {
				if (user != null) {
				    DemoHelper.getInstance().saveContact(user);
				    if(isFinishing()){
				        return;
				    }
					tvNickName.setText(user.getNick());
					if(!TextUtils.isEmpty(user.getAvatar())){
						 Glide.with(PersionInfoActivity.this).load(user.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(headAvatar);
					}else{
					    Glide.with(PersionInfoActivity.this).load(R.drawable.ease_default_avatar).into(headAvatar);
					}
				}
			}
			
			@Override
			public void onError(int error, String errorMsg) {
			}
		});
	}
	
	/**
	 * 换昵称
	 * @param nickName
	 */
	private void updateRemoteNick(final String nickName) {
		dialog = ProgressDialog.show(this, getString(R.string.dl_update_nick), getString(R.string.dl_waiting));
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean updatenick = DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(nickName);
				if (PersionInfoActivity.this.isFinishing()) {
					return;
				}
				if (!updatenick) {
					runOnUiThread(new Runnable() {
					public void run() {
						//更新昵称失败
						ToastUtil.showToast(getApplicationContext(), "更新昵称失败!");
						dialog.dismiss();
						}
					});
				} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						//更新昵称成功
						ToastUtil.showToast(getApplicationContext(), "更新昵称成功!");
						tvNickName.setText(nickName);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * 换头像
	 */
	private void uploadHeadPhoto() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.dl_title_upload_photo);
		builder.setItems(new String[] { getString(R.string.dl_msg_take_photo),getString(R.string.dl_msg_local_upload) },
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					switch (which) {
					case 0:
						Toast.makeText(PersionInfoActivity.this,getString(R.string.toast_no_support),
									Toast.LENGTH_SHORT).show();
						break;
					case 1:
						Intent pickIntent = new Intent(Intent.ACTION_PICK,null);
						pickIntent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
							startActivityForResult(pickIntent, REQUESTCODE_PICK);
						break;
					default:
						break;
					}
				}
			});
		builder.create().show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE_PICK:
			if (data == null || data.getData() == null) {
				return;
			}
			startPhotoZoom(data.getData());
			break;
		case REQUESTCODE_CUTTING:
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}
	
	/**
	 * save the picture data
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(getResources(), photo);
			headAvatar.setImageDrawable(drawable);
			uploadUserAvatar(Bitmap2Bytes(photo));
		}

	}
	
	private void uploadUserAvatar(final byte[] data) {
		dialog = ProgressDialog.show(this, getString(R.string.dl_update_photo), getString(R.string.dl_waiting));
		new Thread(new Runnable() {

			@Override
			public void run() {
				final String avatarUrl = DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(data);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						if (avatarUrl != null) {
							Toast.makeText(PersionInfoActivity.this, getString(R.string.toast_updatephoto_success),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(PersionInfoActivity.this, getString(R.string.toast_updatephoto_fail),
									Toast.LENGTH_SHORT).show();
						}

					}
				});

			}
		}).start();

		dialog.show();
	}
	
	public byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	/**
	 * 返回
	 */
	public void back(View v){
		finish();
	}
}
