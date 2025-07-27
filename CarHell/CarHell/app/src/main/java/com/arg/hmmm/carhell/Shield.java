package com.arg.hmmm.carhell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;


import java.util.Timer;
import java.util.TimerTask;

public class Shield extends Thing {
    private TimerTask secondsTask;



    public Shield(float x, float y, GameView gameView){
        super(x,y,gameView);
        matrix = new Matrix();
        paint = new Paint();

        bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.shieldcolor), view.getScreenHeight()/12, view.getScreenHeight()/12, false);

        lifeTimer();


    }

    private  void lifeTimer(){
        Timer secondsTimer = new Timer();
        bmp.setHasAlpha(true);
        secondsTask = new TimerTask() {
            int alfa = 255;
            int sycle = 0;
            boolean b = true;
            @Override
            public void run() {
                //add here something to stop, if()
                if(b){
                    if(alfa >90)
                        alfa -= 5;
                    else
                        b = false;
                }
                else{
                    if(alfa < 255)
                        alfa += 5;
                    else
                        b = true;
                }

                paint.setAlpha(alfa);
                sycle++;
                if(sycle == 500)
                    removeShield();
            }
        };
        secondsTimer.scheduleAtFixedRate(secondsTask, 1000 * 5,10);
    }
    private void removeShield(){
        view.removeThing(this);//if executed inside timer this = timer
    }
}
