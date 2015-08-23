package com.alex.develop.stockanalyzer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.alex.develop.fragment.PositionFragment;
import com.alex.develop.fragment.SelectFragment;
import com.alex.develop.fragment.StockFragment;
import com.alex.develop.ui.NonSlidableViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * App入口
 *
 * @author Created by alex 2014/10/23
 */
public class MainActivity extends BaseActivity implements StockFragment.OnStockSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (null == fragList) {

            fragList = new ArrayList<>();

            // 行情
            fragList.add(new StockFragment());

            // 自选
            StockFragment stockFragment = new StockFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(StockFragment.ARG_IS_COLLECT_VIEW, true);
            stockFragment.setArguments(bundle);
            fragList.add(stockFragment);

            // 持仓
            fragList.add(new PositionFragment());

            // 选股
            fragList.add(new SelectFragment());
        }

        Analyzer.setLoadView(findViewById(R.id.loading));

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
            case R.id.action_search:
                Intent intent = new Intent();
                intent.setClass(this, SearchActivity.class);
                startActivityForResult(intent, StockFragment.REQUEST_SEARCH_STOCK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 自选股数据刷新
        fragList.get(1).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStockSelected(int index, int from) {
        CandleActivity.start(this, index, from);
    }

    public void onNavClicked(View view) {
        boolean isChecked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.marketStock:
                if (isChecked) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.collectStock:
                if (isChecked) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.positionStock:
                if (isChecked) {
                    viewPager.setCurrentItem(2);
                }
                break;
            case R.id.selectStock:
                if (isChecked) {
                    viewPager.setCurrentItem(3);
                }
                break;
        }
    }

    private class ViewHolder extends FragmentPagerAdapter {

        public ViewHolder(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        @Override
        public int getCount() {
            return fragList.size();
        }
    }

    private NonSlidableViewPager viewPager;
    private List<Fragment> fragList;
}