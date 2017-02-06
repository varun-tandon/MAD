package com.hhsfbla.launch;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Varun on 1/25/2017.
 */

public class FundraiserFragment extends Fragment {

    private View fundraiserView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fundraiserView = inflater.inflate(R.layout.activity_launch_home, container, false);

        Bundle data = getArguments();
        ImageView fundraiserImg = (ImageView) fundraiserView.findViewById(R.id.fundraiser_image);
        fundraiserImg.setImageBitmap((Bitmap) data.getParcelable("bitmap"));
        setTextForTextView(R.id.campaign_title_textview, data.getString("purpose"));
        setTextForTextView(R.id.nonprofit_org_name_textview, data.getString("orgname"));
        setTextForTextView(R.id.homepage_campaign_description, data.getString("description"));
        setTextForTextView(R.id.progress_text, data.getString("progressText"));

        return fundraiserView;
    }

    public void setTextForTextView(int id, String text) {
        View v = fundraiserView.findViewById(id);
        if (v instanceof TextView) {
            ((TextView) v).setText(text);
        }
    }
}
