package com.hhsfbla.launch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * dialog to launch when donation is successful
 * @author Heidi
 */
public class DonationSuccessDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.fragment_donation_success_dialog, null);
        String text = getArguments().getString("text");
        builder.setView(v)
            // Add action buttons
            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    getActivity().finish();
                }
            });

        // Set to correct text
        ((TextView) v.findViewById(R.id.donation_success_text)).setText(text);

//        TODO: learn how to animate
//        ImageView successImage = (ImageView) getActivity().findViewById(R.id.donate_success_icon);
//        Animation expandIn = AnimationUtils.loadAnimation(getActivity(), R.anim.expand_in);
//        successImage.startAnimation(expandIn);
        return builder.create();
    }
}
