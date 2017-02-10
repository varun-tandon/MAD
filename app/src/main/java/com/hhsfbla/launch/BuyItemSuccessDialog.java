package com.hhsfbla.launch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BuyItemSuccessDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.fragment_buy_item_success_dialog, null);
        String text = getArguments().getString("text");
        builder.setView(v)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), NavDrawerActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
        ((TextView) v.findViewById(R.id.buy_success_text)).setText(text);

//        TODO: learn how to animate
//        ImageView successImage = (ImageView) getActivity().findViewById(R.id.donate_success_icon);
//        Animation expandIn = AnimationUtils.loadAnimation(getActivity(), R.anim.expand_in);
//        successImage.startAnimation(expandIn);
        return builder.create();
    }
}
