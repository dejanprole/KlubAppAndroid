package com.vozoljubitelji.klubapp;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private CheckBox notificationCheckbox;
    private MySharedPreference mySharedPreference;
    private boolean hasUserSubscribed;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        checkPlayServices();
        mySharedPreference = new MySharedPreference(this);
        notificationCheckbox = (CheckBox)findViewById(R.id.subscribe);
        final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
        boolean sentToken = mySharedPreference.hasUserSubscribeToNotification();
        if (sentToken) {
            notificationCheckbox.setChecked(true);
            notificationCheckbox.setEnabled(false);
            notificationCheckbox.setText(getString(R.string.registered));
            Snackbar.make(coordinatorLayoutView, getString(R.string.registered), Snackbar.LENGTH_LONG).show();
        } else {
            notificationCheckbox.setChecked(false);
            notificationCheckbox.setEnabled(false);
            notificationCheckbox.setText(getString(R.string.registered));
            Snackbar.make(coordinatorLayoutView, getString(R.string.un_register), Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}