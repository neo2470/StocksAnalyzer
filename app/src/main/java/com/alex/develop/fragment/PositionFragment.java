package com.alex.develop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.develop.stockanalyzer.R;

/**
 * Created by alex on 15-8-20.
 * 持仓明细
 */
public class PositionFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.position_fragment, container, false);
        return view;
    }
}
