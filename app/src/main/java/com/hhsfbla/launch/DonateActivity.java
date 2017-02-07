package com.hhsfbla.launch;

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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class DonateActivity extends AppCompatActivity {

    final static int REQUEST_CODE = 1;

    private EditText donateAmountField;

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
        donateAmountField = (EditText) findViewById(R.id.donate_amount_field);
        donateAmountField.requestFocus();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBraintreeClientToken();
            }
        });
    }

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

    void postNonceToServer(String nonce) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        float amount = Float.parseFloat(donateAmountField.getText().toString());
        params.put("payment_method_nonce", nonce);
        params.put("amount", amount);
        client.post("http://mad2017.hhsfbla.com/braintree/checkout.php", params,
            new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                    Log.d("Donate", responseBody);
                    Toast.makeText(DonateActivity.this, responseBody, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                    Log.d("Donate", responseBody);
                    Toast.makeText(DonateActivity.this, responseBody, Toast.LENGTH_LONG).show();
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
