package com.alex.develop.fragment;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.Fragment;

/**
 * 基本Fragment
 * @author Created by alex 2014/11/13
 */
public class BaseFragment extends Fragment {
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = activity;

		try {
			PackageManager pm = act.getPackageManager();
			pkgInfo = pm.getPackageInfo(act.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected Activity act;
	protected PackageInfo pkgInfo;
}
