package com.alex.develop.stockanalyzer;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
		changeThemeByTime();
		setContentView(R.layout.main_activity);

		if(null == savedInstanceState) {
			go2StockView();
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

	public void go2AddStockView() {
		FragmentTransaction transaction = getTransaction();
		transaction.replace(LAYOUT_CONTENT_ID, new AddStockFragment());
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void go2StockView() {
		FragmentTransaction transaction = getTransaction();
		transaction.replace(LAYOUT_CONTENT_ID, new StockFragment());
		transaction.commit();
	}

	public void go2CandleView(int stockIndex) {
		FragmentTransaction transaction = getTransaction();
		CandleFragment candleFragment = new CandleFragment();
		candleFragment.setStockIndex(stockIndex);
		transaction.replace(LAYOUT_CONTENT_ID, candleFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private final int LAYOUT_CONTENT_ID = R.id.content;// 用于放置各种不同的Fragment
}