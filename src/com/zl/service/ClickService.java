package com.zl.service;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class ClickService extends AccessibilityService{

	public static final String TAG = "ClickService";
	
	private boolean flag1 = false;
	private boolean flag2 = false;
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		int eventType = event.getEventType();
		switch (eventType) {
		case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
			String className = event.getClassName().toString();
			Log.d(TAG, className);
			if ("com.android.settings.applications.InstalledAppDetailsTop".equals(className)) {
				performClick();
			}else if ("android.app.AlertDialog".equals(className)) {
				performClick2();
			}
			break;
		}
		
	}

	private void performClick2() {
		if (flag2) {
			return;
		}
		flag2 = true;
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//		recycle(nodeInfo);
		if (nodeInfo != null) {
			List<AccessibilityNodeInfo> list = nodeInfo
					.findAccessibilityNodeInfosByText("确定");
			for (AccessibilityNodeInfo n : list) {
				n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
		}
	}

	@SuppressLint("NewApi")
	private void performClick() {
		if (flag1) {
			return;
		}
		flag1 = true;
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo != null) {
			List<AccessibilityNodeInfo> list = nodeInfo
					.findAccessibilityNodeInfosByText("强行停止");
			for (AccessibilityNodeInfo n : list) {
				n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
		}
	}
	
	
	/**
	 * 打印一个节点的结构
	 * @param info
	 */
	@SuppressLint("NewApi")
	public void recycle(AccessibilityNodeInfo info) {  
        if (info.getChildCount() == 0) { 
        	if(info.getText() != null){
        		if("确定".equals(info.getText().toString())){
        			//这里有一个问题需要注意，就是需要找到一个可以点击的View
                	info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                	AccessibilityNodeInfo parent = info.getParent();
                	while(parent != null){
                		if(parent.isClickable()){
                			parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                			break;
                		}
                		parent = parent.getParent();
                	}
                	
            	}
        	}
        	
        } else {  
            for (int i = 0; i < info.getChildCount(); i++) {  
                if(info.getChild(i)!=null){  
                    recycle(info.getChild(i));  
                }  
            }  
        }  
    }  
	@Override
	public void onInterrupt() {
		
	}

}
