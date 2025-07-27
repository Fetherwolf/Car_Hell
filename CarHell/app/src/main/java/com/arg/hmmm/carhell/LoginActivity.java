package com.arg.hmmm.carhell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

public class LoginActivity extends AppCompatActivity {
    private LoginView loginView;
    private Intent musicServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginView = new LoginView(this);
        loginView.setOnTouchListener(new Input());
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Pref.createPref(this);

        /** this is a test noob*/
        //music
        MusicService.createmusics(this);

        musicServiceIntent = new Intent(getApplicationContext(),
                com.arg.hmmm.carhell.MusicService.class);
        startService(musicServiceIntent);

        MusicService.changeMusic("menu");


        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(loginView);
    }


    protected void onStop(){
        super.onStop();
        MusicService.pauseAll();
    }

    protected void onResume(){
        super.onResume();
        MusicService.changeMusic("menu");
        loginView.resume();
    }
}
