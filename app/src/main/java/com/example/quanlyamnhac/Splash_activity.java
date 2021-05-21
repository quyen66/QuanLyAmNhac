package com.example.quanlyamnhac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash_activity extends AppCompatActivity {
    static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.tv_welcome);
        tv.clearAnimation();
        tv.startAnimation(a);
        new SplashAdapter().execute();
    }
    public class SplashAdapter extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Intent i = new Intent(Splash_activity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, Splash_activity.SPLASH_TIME_OUT);
        }
    }
}
