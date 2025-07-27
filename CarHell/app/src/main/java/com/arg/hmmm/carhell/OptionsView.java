package com.arg.hmmm.carhell;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class OptionsView  extends View implements Runnable  {
    private boolean needToStop=true;
    private Button resumeButton, carScrollLeft, carScrollRight;
    private Bitmap background, title;
    private int pid; //Player Image pointer
    private int pidp, pidm;//pid plus, pid minus
    private String[] playerImages;
    private Bitmap[] playerBmp;
    private boolean[] unlocked;
    private float xPosLeft, xPosRight, xPosMiddle;
    private String[] unlocktext;
    private Paint unlockPaint;

    int screenWidth,screenHeight;

    public OptionsView(Context context) {
        super(context);

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        resumeButton = new Button(screenWidth/3f, screenHeight*0.7f,screenWidth/3f, screenHeight/10f, "ReSUmE",true,true);

        title = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.optionstitle), screenWidth/2, screenHeight/5, false);
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.background45), screenWidth, screenHeight, false);

        carScrollLeft = new Button(screenHeight*0.3f,screenHeight*0.4f,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.bluearrowleft), screenWidth/8, screenHeight/7, false),true,true);
        carScrollRight = new Button(screenWidth*0.7f,screenHeight*0.4f,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.bluearrowright), screenWidth/8, screenHeight/7, false),true,true);

        playerImages = new String[8];
        playerImages[0] = "yellow";
        playerImages[1] = "green";
        playerImages[2] = "gray";

        playerImages[3] = "blue";
        playerImages[4] = "pink";
        playerImages[5] = "orange";
        playerImages[6] = "white";
        playerImages[7] = "secret";

        unlocktext = new String[playerImages.length-3];
        unlocktext[0] = "Unlocked by playing a game";
        unlocktext[1] = "Unlocked by surviving 10 seconds";
        unlocktext[2] = "Unlocked by surviving 17.5 seconds";
        unlocktext[3] = "Unlocked by surviving 25 seconds";
        unlocktext[4] = "Unlocked by surviving 420 seconds";


        unlocked = new boolean[playerImages.length];
        setUnlocked();
        /*
        for(int i = 0; i <3; i++)
            unlocked[i] = true;
        for(int i=3; i< 3+Pref.getUnlockLevel(); i++)
            if(i<unlocked.length)
                unlocked[i] = true;*/


        setPid();

        playerBmp = new Bitmap[playerImages.length];

        setBmps();

        xPosLeft = carScrollLeft.getX() + carScrollLeft.getImage().getWidth() + playerBmp[0].getWidth()*2;
        xPosMiddle = (carScrollLeft.getX() + carScrollLeft.getImage().getWidth() + carScrollRight.getX())/2;
        xPosRight = carScrollRight.getX() - playerBmp[0].getWidth()*3;

        unlockPaint = new Paint();
        unlockPaint.setColor(Color.WHITE);
        unlockPaint.setTextSize(screenHeight*0.05f);

        resume();
    }

    private void setUnlocked(){
        for(int i = 0; i <3; i++)
            unlocked[i] = true;
        unlocked[3] = (Pref.getHighScore()>=1);
        unlocked[4] = (Pref.getHighScore()>=10);
        unlocked[5] = (Pref.getHighScore()>=17.5);
        unlocked[6] = (Pref.getHighScore()>=25);
        unlocked[7] = (Pref.getHighScore()>=420);
    }

    private void setPid(){
        switch(Pref.getPlayerImage()){
            case "green":
                pid = 1;
                break;
            case "gray":
                pid = 2;
                break;
            case "blue":
                pid = 3;
                break;
            case "pink":
                pid = 4;
                break;
            case "orange":
                pid = 5;
                break;
            case "white":
                pid = 6;
                break;
            case "secret":
                pid = 7;
                break;
            default:
                pid = 0;
        }
        pidm=pid-1;
        pidp=pid+1;
        if(pidm < 0)
            pidm = playerImages.length-1;
        if(pidp == playerImages.length)
            pidp = 0;
    }

    private void setBmps(){
        for(int i=0; i<playerImages.length; i++)
            playerBmp[i] = setBmps2(i);
    }

    private Bitmap setBmps2(int x){

        Bitmap tbmp;

        if(!unlocked[x])
            tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.lock);
        else{
        switch(playerImages[x]){
            case "yellow":
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.car);
                break;
            case "green":
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.cargreen);
                break;
            case "gray":
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.cargray);
                break;
            case "blue":
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.carinverted);
                break;
            case "pink":
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.carpink);
                break;
            case "orange":
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.carorange);
                break;
            case "white":
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.carwhite);
                break;
            case "secret":
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.secretskin);
                break;
            default:
                tbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.whitepixle);

        }}

        tbmp = Bitmap.createScaledBitmap(tbmp, screenWidth/35, screenHeight/12, false);

        return tbmp;
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(title,screenWidth/4,screenHeight*0.1f,null);


        resumeButton.draw(canvas);
        carScrollLeft.draw(canvas);
        carScrollRight.draw(canvas);

        canvas.drawBitmap(playerBmp[pidm], xPosLeft,carScrollLeft.getY(),null);
        canvas.drawBitmap(playerBmp[pid], xPosMiddle,carScrollLeft.getY(),null);
        canvas.drawBitmap(playerBmp[pidp], xPosRight, carScrollRight.getY(),null);

        if(pid>=3)
            canvas.drawText(unlocktext[pid-3],xPosMiddle-unlockPaint.measureText(unlocktext[pid-3])/2,carScrollLeft.getY()-unlockPaint.getTextSize(),unlockPaint);

        invalidate();
    }
 
    @Override
    public void run() {
        while(!needToStop){
            resumeButton.update();
            if(resumeButton.shouldActivate()){
                if(unlocked[pid])
                    Pref.setPlayerImage(playerImages[pid]);
                openLoginActivity();
            }

            carScrollLeft.update();
            if(carScrollLeft.shouldActivate()){
                pid--;
                if(pid<0)
                    pid = playerImages.length-1;
                pidp= pid+1;
                pidm=pid-1;
                if(pidp >= playerImages.length)
                    pidp = 0;
                if(pidm < 0)
                    pidm = playerImages.length-1;
            }
            carScrollRight.update();
            if(carScrollRight.shouldActivate()){
                pid++;
                if(pid == playerImages.length)
                    pid = 0;
                pidm=pid-1;
                pidp=pid+1;
                if(pidm < 0)
                    pidm = playerImages.length-1;
                if(pidp >= playerImages.length)
                    pidp = 0;
            }

            try {
                Thread.sleep(15);
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void openLoginActivity(){
        needToStop = true;
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(intent);
    }

    public void resume(){
        if (needToStop) {
            needToStop = false;
            Thread thread = new Thread(this);
            thread.start();
        }
    }
}
