package com.alex.develop.stockanalyzer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.alex.develop.fragment.AddStockFragment;
import com.alex.develop.fragment.CandleFragment;
import com.alex.develop.fragment.StockFragment;

/**
 * App入口
 * @author Created by alex 2014/10/23
 */
public class MainActivity extends BaseActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBackTwice2Exit(true);
		changeThemeByTime();
		setContentView(R.layout.main_activity);

		if(null == savedInstanceState) {
			go2StockView(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.actions_search :
				go2AddStockView();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void go2StockView(boolean isCollectView) {
		FragmentTransaction transaction = getTransaction();

		StockFragment stockFragment = new StockFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean(StockFragment.ARG_IS_COLLECT_VIEW, isCollectView);
		stockFragment.setArguments(bundle);

		transaction.replace(LAYOUT_CONTENT_ID, stockFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void go2CandleView(int stockIndex) {
		FragmentTransaction transaction = getTransaction();

		CandleFragment candleFragment = new CandleFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(CandleFragment.ARG_STOCK_INDEX, stockIndex);
		candleFragment.setArguments(bundle);

		transaction.replace(LAYOUT_CONTENT_ID, candleFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void go2AddStockView() {
		FragmentTransaction transaction = getTransaction();
		transaction.replace(LAYOUT_CONTENT_ID, new AddStockFragment());
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void showBottomSwitcher(boolean show) {
		Fragment bottomSwitcher = getSupportFragmentManager().findFragmentById(R.id.bottomSwithcer);

		FragmentTransaction transaction = getTransaction();
		if(show) {
			if(bottomSwitcher.isHidden()) {
				transaction.show(bottomSwitcher);
			}
		} else {
			if(!bottomSwitcher.isHidden()) {
				transaction.hide(bottomSwitcher);
			}
		}
		transaction.commit();
	}

	private final int LAYOUT_CONTENT_ID = R.id.content;// 用于放置各种不同的Fragment
}