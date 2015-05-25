package com.alex.develop.stockanalyzer;

import java.io.File;
import java.util.Calendar;

import net.youmi.android.AdManager;

import com.alex.develop.stockanalyzer.R;
import com.alex.develop.settings.Remote;
import com.alex.develop.uihelper.ConfirmDialog;
import com.alex.develop.uihelper.ConfirmDialog.OnConfirmListener;
import com.alex.develop.util.ApplicationHelper;
import com.alex.develop.util.FileHelper;
import com.alex.develop.util.NetworkHelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 基本Activity
 * 
 * @author Created by alex 2014/10/23
 */
public class BaseActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 屏蔽Back键
		if(blockBack) {
			return blockBack;
		}

		// Back两次退出App
		if (backTwice2Exit && keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - exitTime > getResources().getInteger(R.integer.back_twice_duration)) {

				// 呈现于屏幕中央的Toast
				backToast = Toast.makeText(this, getString(R.string.back_twice_to_exit), Toast.LENGTH_SHORT);
				backToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				backToast.show();

				exitTime = System.currentTimeMillis();
			} else {
				ApplicationHelper.exitApplication();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// Activity不可见的时候，亦取消Back Toast的提示信息
		if(backToast != null) {
			backToast.cancel();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ApplicationHelper.remove(this);
	}
	
	@SuppressLint("CommitTransaction")
	protected FragmentTransaction getTransaction() {
		return getSupportFragmentManager().beginTransaction();
	}

	/**
	 * 是否屏蔽Back键
	 * @param backable true，屏蔽；false，不屏蔽；默认为false
	 */
	protected void blockBack(boolean blockBack) {
		this.blockBack = blockBack;
	}

	/**
	 * 设置用户点击Back键两次，是否退出App
	 * 
	 * @param backTwice2Exit
	 *            true，退出；false，退出；默认为true
	 */
	protected void setBackTwice2Exit(boolean backTwice2Exit) {
		this.backTwice2Exit = backTwice2Exit;
	}
	
	/**
	 * 检查远程服务器该App是否有更新版本
	 */
	protected void checkForUpdate() {
		Log.d("Debug-VersionCode", pkgInfo.versionCode + ", " + Remote.versionCode);
		if(true) {
			
			//弹出更新对话框
			ConfirmDialog updateDialog = new ConfirmDialog(this);
			updateDialog.setTitle(getString(R.string.software_update));
			updateDialog.setContent(getString(R.string.update_features));
			updateDialog.setCancelable(false);
			updateDialog.setOnConfirmListener(new OnConfirmListener() {

				@Override
				public void positive(DialogInterface dialog, int which) {
					// TODO 取消时后的操作
				}

				@Override
				public void negative(DialogInterface dialog, int which) {
					// TODO 确认后联网下载App
				}

			});
			updateDialog.show();
		}
	}
	
	/**
	 * 根据时间切换Activity的主题，必须在setContentView()之前调用<br>
	 * 默认白天主题R.style.AppThemeLight，夜间主题R.style.AppTheme
	 */
	protected void changeThemeByTime() {
		changeThemeByTime(R.style.AppThemeLight, R.style.AppTheme);
	}
	
	/**
	 * 根据时间切换Activity的主题，必须在setContentView()之前调用
	 * @param dayRes 白天主题
	 * @param nightRes 夜间主题
	 */
	protected void changeThemeByTime(int dayRes, int nightRes) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		
		if(getResources().getInteger(R.integer.light_theme_start) <= hour && getResources().getInteger(R.integer.dark_theme_start) > hour) {// 亮色主题
			setTheme(dayRes);
		} else {// 暗色主题
			setTheme(nightRes);
		}
	}
	
	/**
	 * Google admob
	protected void initGoogleAdmob() {
		AdView adBanner = (AdView) findViewById(R.id.adBanner);
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice(getString(R.string.admob_test_device))
			.build();
		adBanner.loadAd(adRequest);
	}
	*/
	
	/**
	 * Youmi ads
	 */
	protected void initYoumiAd() {
		AdManager.getInstance(this)
				.init(getString(R.string.youmi_pub_id), getString(R.string.youmi_ads_key), true);
	}

	/**
	 * 数据初始化
	 */
	private void initialize() {
		
		ApplicationHelper.add(this);
		
		backTwice2Exit = false;
		blockBack = false;
		
		try {
			PackageManager pm = getPackageManager();
			pkgInfo = pm.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		// 创建LoadingDialog
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setCancelable(false);

		FileHelper.init(this);
		NetworkHelper.init(this);
	}

	protected ProgressDialog loadingDialog;// 加载数据Dialog，不可取消，加载完成后dismiss即可
	protected PackageInfo pkgInfo;// App的Package信息
	private boolean backTwice2Exit;// 是否Back2次退出App
	private boolean blockBack;// 是否屏蔽Back
	private Toast backToast;// 点击Back键时，显示的Toast
	private long exitTime;// 记录第一次点击Back键的时间
}