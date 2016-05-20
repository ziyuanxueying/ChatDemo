package com.chat.db;

import com.hyphenate.easeui.domain.EaseUser;

public class RobotUser extends EaseUser{
	public RobotUser(String username) {
        super(username.toLowerCase());
    }
}
