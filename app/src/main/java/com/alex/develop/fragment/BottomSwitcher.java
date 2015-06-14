package com.alex.develop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alex.develop.stockanalyzer.R;

/**
 * Created by alex on 15-6-12.
 */
public class BottomSwitcher extends BaseFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_switcher, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button stockViewBtn = (Button) act.findViewById(R.id.stockView);
        stockViewBtn.setOnClickListener(this);
        Button collectViewBtn = (Button) act.findViewById(R.id.collectView);
        collectViewBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stockView :
                act.go2StockView(false);
                break;
            case R.id.collectView :
                act.go2StockView(true);
                break;
        }
    }
}
