package com.asbozh.kidshub;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.asbozh.kidshub.database.SQLHandler;
import com.asbozh.kidshub.utilities.NetworkUtils;

public class SplashActivity extends AppCompatActivity {

    SQLHandler sqlHandler = new SQLHandler(this);
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (NetworkUtils.isOnline(this)) {
            new RefreshTask().execute("");
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    private class RefreshTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl();
                sqlHandler.open();
                sqlHandler.deleteAllCacheData();
                sqlHandler.saveCacheData(jsonResponse);
                sqlHandler.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Log.d(TAG, "DB IS UPDATED");
            } else {
                Log.d(TAG, "DB UPDATE FAILED");
            }
        }
    }

    @Override
    public void onBackPressed() {
        //nothing
    }
}
