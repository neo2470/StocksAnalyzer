package com.alex.develop.stockanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.alex.develop.fragment.StockFragment;
import com.alex.develop.ui.NonSlidableViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * App入口
 * @author Created by alex 2014/10/23
 */
public class MainActivity extends BaseActivity implements StockFragment.OnStockSelectedListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		if(null == viewList) {
			viewList = new ArrayList<>();

			// 市场行情
			viewList.add(new StockFragment());

			// 自选股
			StockFragment stockFragment = new StockFragment();
			Bundle bundle = new Bundle();
			bundle.putBoolean(StockFragment.ARG_IS_COLLECT_VIEW, true);
			stockFragment.setArguments(bundle);

			viewList.add(new StockFragment());
		}

		ViewHolder viewHolder = new ViewHolder(getSupportFragmentManager());
		viewPager = (NonSlidableViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(viewHolder);
		viewPager.setOffscreenPageLimit(3);
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
			case R.id.action_search :
				Intent intent = new Intent();
				intent.setClass(this, SearchActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);

		}
	}

	@Override
	public void onStockSelected(int index) {
		Intent intent = new Intent();
		intent.setClass(this, CandleActivity.class);
		intent.putExtra(CandleActivity.ARG_STOCK_INDEX, index);
		startActivity(intent);
	}

	public void onNavClicked(View view) {
		boolean isChecked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
			case R.id.marketStock :
				if(isChecked) {
					viewPager.setCurrentItem(0);
				}
				break;
			case R.id.collectStock :
				if(isChecked) {
					viewPager.setCurrentItem(1);
				}
				break;
		}
	}

	private NonSlidableViewPager viewPager;
	private List<Fragment> viewList;

	private class ViewHolder extends FragmentPagerAdapter {

		public ViewHolder(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return viewList.get(position);
		}

		@Override
		public int getCount() {
			return viewList.size();
		}
	}
}