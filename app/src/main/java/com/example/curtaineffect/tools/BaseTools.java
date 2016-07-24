package com.example.curtaineffect.tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BaseTools {
	
	public static int getWindowWidth(Context context){
		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;
		return mScreenWidth;
	}
	
	public static int getWindowHeigh(Context context){
		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		return mScreenHeigh;
	}
}
