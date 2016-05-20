package com.chat;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;

import com.hyphenate.easeui.controller.EaseUI;

public class ChatApplication extends Application {
	
	public static Context applicationContext;
	private static ChatApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
    @Override
    public void onCreate() {
        super.onCreate();
        EaseUI.getInstance().init(this, null);
        applicationContext = this;
        instance = this;
        
        //init demo helper
        DemoHelper.getInstance().init(applicationContext);
    }
    
	public static ChatApplication getInstance() {
		return instance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	
    /** 如何获取processAppName */
    private String getAppName(int pID) {
    	String processName = null;
    	ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
    	List l = am.getRunningAppProcesses();
    	Iterator i = l.iterator();
    	PackageManager pm = this.getPackageManager();
    	while (i.hasNext()) {
    		ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
    		try {
    			if (info.pid == pID) {
    				CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
    				// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
    				// info.processName +"  Label: "+c.toString());
    				// processName = c.toString();
    				processName = info.processName;
    				return processName;
    			}
    		} catch (Exception e) {
    			// Log.d("Process", "Error>> :"+ e.toString());
    		}
    	}
    	return processName;
    }
    
}
