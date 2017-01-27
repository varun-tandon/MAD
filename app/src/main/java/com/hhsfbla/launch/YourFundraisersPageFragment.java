package com.hhsfbla.launch;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhenfangchen on 1/26/17.
 */

public class YourFundraisersPageFragment extends Fragment {
    View accountView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountView = inflater.inflate(R.layout.activity_your_fundraisers, container, false);
        return accountView;
    }
}
