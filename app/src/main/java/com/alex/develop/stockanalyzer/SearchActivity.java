package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.os.Bundle;

/**
 * Created by alex on 15-6-15.
 */
public class SearchActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        ActionBar actionBar = getActionBar();
        if(null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.search_action_bar_titile);
        }
    }


}
