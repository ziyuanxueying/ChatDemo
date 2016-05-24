package com.chat.fragment;

import java.util.Hashtable;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.chat.DemoHelper;
import com.chat.activity.ChatActivity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;

/**
 * 联系人列表
 * @author fj
 * 
 */
public class ContactListFragment extends EaseContactListFragment {
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		//注册上下文菜单
        registerForContextMenu(listView);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		//设置联系人数据
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((EaseUser)listView.getItemAtPosition(position)).getUsername();
                // demo中直接进入聊天页面，实际一般是进入用户详情页
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
            }
        });

		super.setUpView();
	}
}
