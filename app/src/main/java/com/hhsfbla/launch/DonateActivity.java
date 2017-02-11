package com.hhsfbla.launch;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * control logic for donating a direct contribution to a fundraiser
 * @author Heidi
 */
public class DonateActivity extends AppCompatActivity {

    final static int REQUEST_CODE = 1;

    private EditText donateAmountField;
    protected int amountRaised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.donate_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView purpose = (TextView) findViewById(R.id.donate_fname);
        purpose.setText(getIntent().getStringExtra("purpose"));
        Button proceedButton = (Button) findViewById(R.id.donate_proceed_button);

        // autofocus on amount field to expedite process
        donateAmountField = (EditText) findViewById(R.id.donate_amount_field);
        donateAmountField.requestFocus();

        // make sure keyboard doesn't cover input field
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // get client token when user is ready to pay
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBraintreeClientToken();
            }
        });
    }

    /**
     * requests client token from server in order to launch Paypal Braintree Drop-in UI for payment
     */
    public void getBraintreeClientToken() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mad2017.hhsfbla.com/braintree/generateClientToken.php", new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String clientToken) {
                DropInRequest dropInRequest = new DropInRequest().clientToken(clientToken);
                startActivityForResult(dropInRequest.getIntent(DonateActivity.this), REQUEST_CODE);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == DonateActivity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                postNonceToServer("fake-valid-nonce");
            } else if (resultCode == DonateActivity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }

    /**
     * finishes payment process
     * @param nonce - an arbitrary string for the server
     */
    void postNonceToServer(String nonce) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        // get amount
        final int amount = Integer.parseInt(donateAmountField.getText().toString());

        // send params to server
        params.put("payment_method_nonce", nonce);
        params.put("amount", amount);

        // write to firebase
        client.post("http://mad2017.hhsfbla.com/braintree/checkout.php", params,
            new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                    Log.d("Donate", responseBody);

                    // Write to Firebase
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("fundraisers/" + DonateActivity.this.getIntent().getStringExtra("fid"));
                    ref.child("amountRaised").setValue(DonateActivity.this.getIntent()
                            .getIntExtra("amountRaised", 0) + Integer.parseInt(donateAmountField
                            .getText().toString()));

                    // launch dialog on success
                    DialogFragment dialog = new DonationSuccessDialog();
                    Bundle b = new Bundle();
                    b.putString("text", "$" + amount + " donated."
                            + " A receipt has been sent to your email.");
                    dialog.setArguments(b);
                    dialog.show(getFragmentManager(), "Success");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                    Log.d("Donate", responseBody);
                    Toast.makeText(DonateActivity.this, responseBody, Toast.LENGTH_LONG).show();
                    // TODO: handle error
                }
            }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
