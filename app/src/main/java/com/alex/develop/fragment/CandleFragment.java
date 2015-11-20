package com.alex.develop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.develop.stockanalyzer.R;

/**
 * Created by alex on 15-11-18.
 */
public class CandleFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.candle_fragment, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
