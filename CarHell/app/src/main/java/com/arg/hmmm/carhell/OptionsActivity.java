package com.arg.hmmm.carhell;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {
    private OptionsView optionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optionsView = new OptionsView(this);
        optionsView.setOnTouchListener(new Input());
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        MusicService.changeMusic("menu");

        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(optionsView);

    }

    protected void onStop(){
        super.onStop();
        MusicService.pauseAll();
    }

    protected void onResume(){
        super.onResume();
        MusicService.changeMusic("menu");
        optionsView.resume();
    }
}
