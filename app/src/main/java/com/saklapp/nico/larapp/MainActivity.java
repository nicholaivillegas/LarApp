package com.saklapp.nico.larapp;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.saklapp.nico.larapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding activityMainBinding;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.editMessage.setOnClickListener(this);
        activityMainBinding.buttonNumber.setOnClickListener(this);
        activityMainBinding.buttonSend.setOnClickListener(this);

        if (!isNumberSet()) {
            hideFields();
        } else {
            Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_message:
                activityMainBinding.editMessage.setText("");
                break;

            case R.id.button_send:
                GPSTracker gps = new GPSTracker(this);
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                String message = activityMainBinding.editMessage.getText().toString() + "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
                sendSMS(number, message);
                break;

            case R.id.button_number:
                saveNumber(activityMainBinding.editNumber.getText().toString());
                showFields();
                Toast.makeText(this, "NUMBER SAVED", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveNumber(String number) {
        SharedPreferences.Editor editor = getSharedPreferences("Number", MODE_PRIVATE).edit();
        editor.putString("number", number);
        editor.apply();
    }

    private boolean isNumberSet() {
        SharedPreferences prefs = getSharedPreferences("Number", MODE_PRIVATE);
        number = prefs.getString("number", "");
        if (String.valueOf(number).isEmpty()) {
            return false;
        } else return true;
    }

    private void hideFields() {
        activityMainBinding.buttonNumber.setVisibility(View.VISIBLE);
        activityMainBinding.editNumber.setVisibility(View.VISIBLE);
        activityMainBinding.tilNumber.setVisibility(View.VISIBLE);
        activityMainBinding.tilMessage.setVisibility(View.GONE);
        activityMainBinding.editMessage.setVisibility(View.GONE);
        activityMainBinding.buttonSend.setVisibility(View.GONE);
    }

    private void showFields() {
        activityMainBinding.buttonNumber.setVisibility(View.GONE);
        activityMainBinding.editNumber.setVisibility(View.GONE);
        activityMainBinding.tilNumber.setVisibility(View.GONE);
        activityMainBinding.tilMessage.setVisibility(View.VISIBLE);
        activityMainBinding.editMessage.setVisibility(View.VISIBLE);
        activityMainBinding.buttonSend.setVisibility(View.VISIBLE);
    }

    private void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
