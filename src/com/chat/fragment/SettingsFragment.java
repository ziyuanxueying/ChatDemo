package com.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.chat.R;
import com.chat.Utils.SharedPreferencesUtil;
import com.chat.activity.LoginActivity;
import com.chat.activity.PersionInfoActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class SettingsFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        Button logoutButton = (Button) getView().findViewById(R.id.btn_logout);
        Button perBtn = (Button) getView().findViewById(R.id.btn_perinfo);
        logoutButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(false, new EMCallBack() {
                    
                    @Override
                    public void onSuccess() {
                    	// 重新显示登陆页面
    					SharedPreferencesUtil.setPass(getContext(), "");
    					Intent intent = new Intent(getActivity(), LoginActivity.class);
    					startActivity(intent);
    					getActivity().finish();
                    }
                    
                    @Override
                    public void onProgress(int progress, String status) {
                        
                    }
                    
                    @Override
                    public void onError(int code, String error) {
                        
                    }
                });
            }
        });
        perBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(getActivity(),PersionInfoActivity.class));
				startActivity(new Intent(getActivity(), PersionInfoActivity.class).putExtra("setting", true)
				        .putExtra("username", EMClient.getInstance().getCurrentUser()));
			}
		});
    }
}
