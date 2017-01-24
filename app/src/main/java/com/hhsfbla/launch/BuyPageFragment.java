package com.hhsfbla.launch;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Varun on 1/22/2017.
 */

public class BuyPageFragment extends Fragment{
    View accountView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountView = inflater.inflate(R.layout.activity_buy_page, container, false);
        return accountView;
    }
}
