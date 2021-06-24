package com.example.noteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_activity);

        //Delay 2 second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StartMainActivity();
            }
        },2000);


    }

    //Relate Delays 2 Seconds
    public void onGetStartedClick(){
        StartMainActivity();
    }

    //Relate Delays 2 seconds
    private void StartMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
