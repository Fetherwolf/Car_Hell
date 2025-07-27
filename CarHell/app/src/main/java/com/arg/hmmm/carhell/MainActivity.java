package com.arg.hmmm.carhell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        gameView.setOnTouchListener(new Input());
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //music
        MusicService.changeMusic("level");

        /*
        musicServiceIntent = new Intent(getApplicationContext(),
                MusicService.class);
        startService(musicServiceIntent);*/

        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(gameView);
    }

    protected void onStop(){
        super.onStop();
        MusicService.pauseAll();
    }

    protected void onResume(){
        super.onResume();
        MusicService.changeMusic("level");
        gameView.resume();
    }

}
