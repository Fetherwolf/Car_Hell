package com.arg.hmmm.carhell;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class LoginView extends View implements Runnable {
    private Button playButton, optionsButton;
    private boolean needToStop2=true;
    private Bitmap title, background;
    int screenWidth,screenHeight;


    public LoginView(Context context) {
        super(context);


        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        playButton = new Button(screenWidth/3, screenHeight*0.4f,screenWidth/3, screenHeight/10, "PlaY",true,true);
        optionsButton = new Button(screenWidth/3, screenHeight*0.6f,screenWidth/3, screenHeight/10, "OpTiOnS",true,true);


        title = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.titleone), screenWidth/2, screenHeight/5, false);

        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.background45), screenWidth, screenHeight, false);

        resume();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(title,screenWidth/4,screenHeight*0.1f,null);

        playButton.draw(canvas);
        optionsButton.draw(canvas);

        invalidate();
    }

    @Override
    public void run() {
        while(!needToStop()){

            playButton.update();
            if(playButton.shouldActivate()){
                openGameActivity();
            }

            optionsButton.update();
            if(optionsButton.shouldActivate()){
                openOptionsActivity();
            }
        }
    }

    private void openGameActivity(){
        needToStop2 = true;
        Intent intent = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }

    private void openOptionsActivity(){
        needToStop2 = true;
        Intent intent = new Intent(getContext(), OptionsActivity.class);
        getContext().startActivity(intent);
    }

    private boolean needToStop(){
        return needToStop2;
    }

    public void resume(){
        if (needToStop2) {
            needToStop2 = false;
            Thread thread = new Thread(this);
            thread.start();
        }
    }
}
