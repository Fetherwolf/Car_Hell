package com.arg.hmmm.carhell;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

class GameView extends View implements Runnable {
    private Player player;
    private ArrayList<Thing> things;
    //private Shield shield;
    private int screenWidth;
    private int screenHeight;
    private Bitmap background;
    private float seconds;
    private Paint paint;
    private TimerTask summonTearTask;
    private int summonTearTaskPeriod;
    private TimerTask secondsTask;
    private TimerTask summonPowerUpTask;
    private boolean needToStop;
    private boolean isdead;
    private boolean paused;
    private Bitmap ripbmp;
    private Bitmap filterbmp;
    private Paint filterpaint;
    //print 1.00
    private DecimalFormat df2;

    private Thing testPixle;

    //buttons
    private Button gaspedalS;
    private Button breakpedalS;
    private Button leftarrowS;
    private Button rightarrowS;
    private Button gaspedalL;
    private Button breakpedalL;
    private Button leftarrowL;
    private Button rightarrowL;
    private Button restart;
    private Button returnToMainButton;
    private Button resumeButton;
    private Button pauseButton;
    private Button buttonSizeChange;
    private Button volumeOnButton;
    private Button volumeOffButton;


    public GameView(Context context) {
        super(context);

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        /* this doesnt work, screen hieght and width are still 0 when out of the while.
           writing a do while makes it crash becose its infinite
           help
        while(screenWidth==0 || screenHeight==0){
            screenWidth = getResources().getDisplayMetrics().widthPixels;
            screenHeight = getResources().getDisplayMetrics().heightPixels;
        }
        */


        testPixle = new Thing(0,0,this,0,0,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.whitepixle), screenWidth/35, screenHeight/12, false));



        /*
        player = new Player(screenWidth/2,screenHeight/2,this,BitmapFactory.decodeResource(this.getResources(),R.drawable.car));
        //player.setBmp(Bitmap.createScaledBitmap(player.getBmp(), ScreenWidth/35, ScreenHeight/12, false));
        player.setBmp(Bitmap.createScaledBitmap(player.getBmp(), screenWidth/35, screenHeight/12, false));
        //to stop player from spawning at 0,0
        player.setX(screenWidth/2);
        player.setY(screenHeight/2);*/

        createPlayer();

        things = new ArrayList<Thing>();
        //summonTear();

        background = BitmapFactory.decodeResource(this.getResources(),R.drawable.background45);
        background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, false);

        filterbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.pausfilter);
        filterbmp.setHasAlpha(true);
        filterpaint = new Paint();
        filterpaint.setAlpha(150);
        filterbmp = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, false);

        ripbmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.letterf);
        ripbmp = Bitmap.createScaledBitmap(ripbmp, screenWidth/5, 2*screenHeight/5, false);

        //bigButtons = false;

        //create buttons
        createButtons();

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(screenHeight*0.05f);
        seconds = 0;

        //summonTears = true;
        startTearTimer();
        //250
        startPowerUpTimer();


        df2 = new DecimalFormat("#.##");
        startSecondsTimer();

        isdead=false;
        paused = false;

        needToStop = false;
        Thread thread = new Thread(this);
        thread.start();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(background,0,0,null);
        canvas.drawText(""+df2.format(seconds),screenWidth/50,screenHeight/12,paint);

        //testPixle.draw(canvas);
        //player.drawMatrix(canvas);
        player.draw(canvas);

        for (int i=0; i<things.size();i++) {
            things.get(i).draw(canvas);
        }

        if(Pref.getBigButtons()){
            gaspedalL.draw(canvas);
            breakpedalL.draw(canvas);
            leftarrowL.draw(canvas);
            rightarrowL.draw(canvas);
        } else{
            gaspedalS.draw(canvas);
            breakpedalS.draw(canvas);
            leftarrowS.draw(canvas);
            rightarrowS.draw(canvas);
            }

        if(isdead){
            canvas.drawBitmap(filterbmp,0,0,filterpaint);
            canvas.drawBitmap(ripbmp,screenWidth/2 - ripbmp.getWidth()/2,screenHeight/10,null);
            restart.draw(canvas);
            returnToMainButton.draw(canvas);

        } else{
            if(paused) {
                canvas.drawBitmap(filterbmp,0,0,filterpaint);
                resumeButton.draw(canvas);
                returnToMainButton.draw(canvas);
                buttonSizeChange.draw(canvas);
                if(Pref.getVolumeState())
                    volumeOnButton.draw(canvas);
                else
                    volumeOffButton.draw(canvas);
            } else{
                pauseButton.draw(canvas);
            }





        }
        invalidate();
    }

    public void removeThing(Thing thing){
        things.remove(thing);
    }

    public void addThing(Thing thing){
        things.add(thing);
    }

    public ArrayList<Thing> getThings(){
        return things;
    }

    public Thing getTestPixle(){
        return testPixle;
    }

    public Player getPlayer() {
        return player;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    private void createPlayer(){
        Bitmap  tbmp;
        switch(Pref.getPlayerImage()){
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
                tbmp= BitmapFactory.decodeResource(this.getResources(),R.drawable.whitepixle);
        }

        player = new Player(screenWidth/2,screenHeight/2,this, tbmp);

        //player.setBmp(Bitmap.createScaledBitmap(player.getBmp(), ScreenWidth/35, ScreenHeight/12, false));
        player.setBmp(Bitmap.createScaledBitmap(player.getBmp(), screenWidth/35, screenHeight/12, false));
        //to stop player from spawning at 0,0
        player.setX(screenWidth/2);
        player.setY(screenHeight/2);
    }


    //block of cheese
    private void createButtons(){
        //affects menu buttons
        final float buttonXpos = screenWidth/3f;
        final float buttonYpos = screenHeight*0.6f;
        final float distanceBetweenButtons = screenHeight*0.2f;
        final float buttonWidth = screenWidth/3f;
        final float buttonHieght = screenHeight/10f;

        gaspedalS = new Button(screenWidth-(11*screenWidth/100),screenHeight/2,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.gaspedal), screenWidth/12, screenHeight*3/10, false),true,false);
        breakpedalS = new Button(screenWidth*86/100,screenHeight*81/100,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.brakepedal), screenWidth*12/100, screenHeight*15/100, false),true,false);
        leftarrowS = new Button(screenWidth/100,20*screenHeight/24,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.leftarrow), screenWidth/7, screenHeight/7, false),true,false);
        rightarrowS = new Button(15*screenWidth/100,20*screenHeight/24,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.rightarrow), screenWidth/7, screenHeight/7, false),true,false);

        gaspedalL = new Button(screenWidth-(11*screenWidth/100),screenHeight/3,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.gaspedal), screenWidth/10, 2*screenHeight/5, false),true,false);
        breakpedalL = new Button(screenWidth-(15*screenWidth/100),18*screenHeight/24,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.brakepedal), 14*screenWidth/100, screenHeight/5, false),true,false);
        leftarrowL = new Button(screenWidth/100,18*screenHeight/24,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.leftarrow), screenWidth/5, screenHeight/5, false),true,false);
        rightarrowL = new Button(22*screenWidth/100,18*screenHeight/24,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.rightarrow), screenWidth/5, screenHeight/5, false),true,false);

        pauseButton = new Button(screenWidth- screenWidth/50f - screenHeight/11f,screenHeight/50,Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.pausewhite), screenHeight/10, screenHeight/10, false),true,true);
        restart = new Button(buttonXpos, buttonYpos,buttonWidth, buttonHieght, "ReStArT",true,true);
        returnToMainButton = new Button(buttonXpos, buttonYpos+distanceBetweenButtons,buttonWidth, buttonHieght, "Main",true,true);
        resumeButton = new Button(buttonXpos, buttonYpos,buttonWidth, buttonHieght, "rEsuMe",true,true);

        volumeOnButton = new Button(screenWidth- screenWidth/50f - screenHeight/11f,screenHeight/50, Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.volumeon), screenHeight/10, screenHeight/10, false),true,true);
        volumeOffButton = new Button(screenWidth- screenWidth/50f - screenHeight/11f,screenHeight/50, Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.volumeoff), screenHeight/10, screenHeight/10, false),true,true);

        String tText = "Button Size: S";
        if(Pref.getBigButtons())
            tText = "Button Size: L";
        buttonSizeChange = new Button(buttonXpos, buttonYpos-distanceBetweenButtons,buttonWidth, buttonHieght, tText,true,true);


    }

    private void startTearTimer(){
        summonTearTaskPeriod = 400;
        Timer summonTearTimer = new Timer();
        summonTearTask = new TimerTask() {
            @Override
            public void run() {
                //add here something to stop, if()
                if(seconds > 1){
                    summonTear();
                    if(400-seconds/20>=50)
                        summonTearTaskPeriod = 400-(int)(seconds/20);
                }
            }
        };
        summonTearTimer.scheduleAtFixedRate(summonTearTask,0,summonTearTaskPeriod);
    }

    private void startPowerUpTimer(){
        final Random rnd = new Random();
        final GameView tview = this;
        Timer summonPowerUpTimer = new Timer();
        summonPowerUpTask = new TimerTask() {
            @Override
            public void run() {
                //add here something to stop, if()
                float tx = 0, ty = 0;
                tx = rnd.nextInt(screenWidth);
                ty = rnd.nextInt(screenHeight);

                things.add(new Shield(tx,ty,tview));
            }
        };
        summonPowerUpTimer.scheduleAtFixedRate(summonPowerUpTask,/*12*1000*/0,1000*15);
    }


    private void startSecondsTimer(){
        Timer secondsTimer = new Timer();
        secondsTask = new TimerTask() {
            @Override
            public void run() {
                //add here something to stop, if()
                seconds=seconds+0.01f;
            }
        };
        secondsTimer.scheduleAtFixedRate(secondsTask,0,10);
    }

    public void summonTear(){
        float tx=0, ty=0 , tvx=0, tvy=0, speed = 0;
        Bitmap tbmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.whitepixle), screenHeight/12, screenHeight/12, false);
        Random rnd = new Random();

        //tr -> tear difficulty
        int tr = 1;
        if(seconds>=25)
            tr=3;
        else if(seconds >=10)
            tr=2;

        switch (rnd.nextInt(tr)){//chooses the type of projectile
            case 0: //random diraction random spawn
                tbmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.tear), screenHeight/12, screenHeight/12, false);
                float angle = (rnd.nextInt((135-45)+1)+45);    // rand.nextInt((max - min) + 1) + min;
                //0- left, 1- up, 2- right, 3- down
                switch(rnd.nextInt(4)) {
                    case 0:
                        tx = -tbmp.getWidth();
                        ty = rnd.nextInt(screenHeight-tbmp.getHeight());
                        angle -= 90;
                        break;
                    case 1:
                        ty = -tbmp.getHeight();
                        tx = rnd.nextInt(screenWidth-tbmp.getWidth());
                        angle*= -1f;
                        break;
                    case 2:
                        tx = screenWidth;
                        ty = rnd.nextInt(screenHeight-tbmp.getHeight());
                        angle += 90;
                        break;
                    case 3:
                        ty = screenHeight;
                        tx = rnd.nextInt(screenWidth-tbmp.getWidth());
                        //angle stays the same
                        break;
                }

                speed = screenWidth/200f;
                angle = (float)Math.toRadians(angle);
                tvx = (float)(speed*Math.cos(angle));
                tvy = (float)(speed*Math.sin(angle));
                break;
            case 1: //flys to the direction of the player
                tbmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.pinktear), screenHeight/12, screenHeight/12, false);
                //0- left, 1- up, 2- right, 3- down
                switch(rnd.nextInt(4)) {
                    case 0:
                        tx = -tbmp.getWidth();
                        ty = rnd.nextInt(screenHeight - tbmp.getHeight());
                        break;
                    case 1:
                        ty = -tbmp.getHeight();
                        tx = rnd.nextInt(screenWidth - tbmp.getWidth());
                        break;
                    case 2:
                        tx = screenWidth;
                        ty = rnd.nextInt(screenHeight - tbmp.getHeight());
                        break;
                    case 3:
                        ty = screenHeight;
                        tx = rnd.nextInt(screenWidth-tbmp.getWidth());
                        break;
                }
                float dis = (float)Math.sqrt(Math.pow(player.getX() - tx, 2) + Math.pow(player.getY() - ty, 2));
                speed = screenWidth/300f;
                float poo = speed / dis;
                tvx = (player.getX() - tx) * poo;
                tvy = (player.getY() - ty) * poo;
                break;
            case 2: //same x/y moves in strait line(up/down or left/right
                tbmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.greentear), screenHeight/12, screenHeight/12, false);
                speed = screenWidth/ 150f;
                //0- left, 1- up, 2- right, 3- down
                switch(rnd.nextInt(4)) {
                    case 0:
                        tx = -tbmp.getWidth();
                        ty = rnd.nextInt(screenHeight-tbmp.getHeight());
                        tvx = speed;
                        break;
                    case 1:
                        ty = -tbmp.getHeight();
                        tx = rnd.nextInt(screenWidth-tbmp.getWidth());
                        tvy = speed;
                        break;
                    case 2:
                        tx = screenWidth;
                        ty = rnd.nextInt(screenHeight-tbmp.getHeight());
                        tvx = -speed;
                        break;
                    case 3:
                        ty = screenHeight;
                        tx = rnd.nextInt(screenWidth-tbmp.getWidth());
                        tvy = -speed;
                        break;
                }
                break;
        }



        things.add(new Thing(tx,ty,this,tvx,tvy, tbmp));

    }

    public void looseScrean(){
        summonTearTask.cancel();
        summonPowerUpTask.cancel();
        secondsTask.cancel();
        player.stop();
        isdead = true;
    }

    private void pauseScrean(){
        summonTearTask.cancel();
        summonPowerUpTask.cancel();
        secondsTask.cancel();
        player.stop();
        for (Thing thing :
                things) {
            thing.stop();
        }
        paused = true;
    }

    private void stopPause(){
        startSecondsTimer();
        startTearTimer();
        player.start();
        for (Thing thing :
                things) {
            thing.start();
        }
        paused = false;
    }


    @Override
    public void run() {
        while(!needToStop){
            //this is a bad idea
            //summonTear();

            if(!isdead) {
                if(seconds > Pref.getHighScore()){
                    Pref.setHighScore(seconds);
                }
                if(paused){
                    resumeButton.update();
                    if(resumeButton.shouldActivate()){
                        stopPause();
                    }
                    returnToMainButton.update();
                    if(returnToMainButton.shouldActivate()){
                        returnToLogin();
                    }
                    buttonSizeChange.update();
                    if(buttonSizeChange.shouldActivate()){
                        if(Pref.getBigButtons()) {
                            buttonSizeChange.setText("Button Size: S");
                            Pref.setBigButtons(false);
                        } else {
                            buttonSizeChange.setText("Button Size: L");
                            Pref.setBigButtons(true);
                        }
                    }

                    if(Pref.getVolumeState()){
                        volumeOnButton.update();
                        if(volumeOnButton.shouldActivate()){
                            Pref.SetVolumeState(false);
                        }
                    }
                    else{
                        volumeOffButton.update();
                        if(volumeOffButton.shouldActivate()){
                            Pref.SetVolumeState(true);
                        }
                    }
                } else{
                    if(Pref.getBigButtons()) {
                        gaspedalL.update();
                        if (gaspedalL.shouldActivate()) {
                            player.setIsAccelerating(true);
                        } else
                            player.setIsAccelerating(false);

                        rightarrowL.update();
                        if (rightarrowL.shouldActivate())
                            player.turnRight();

                        leftarrowL.update();
                        if (leftarrowL.shouldActivate())
                            player.turnLeft();

                        breakpedalL.update();
                        if (breakpedalL.shouldActivate())
                            player.brake();
                    } else{
                        gaspedalS.update();
                        if (gaspedalS.shouldActivate()) {
                            player.setIsAccelerating(true);
                        } else
                            player.setIsAccelerating(false);

                        rightarrowS.update();
                        if (rightarrowS.shouldActivate())
                            player.turnRight();

                        leftarrowS.update();
                        if (leftarrowS.shouldActivate())
                            player.turnLeft();

                        breakpedalS.update();
                        if (breakpedalS.shouldActivate())
                            player.brake();
                    }

                pauseButton.update();
                if(pauseButton.shouldActivate())
                    pauseScrean();
            }} else{
                restart.update();
                if(restart.shouldActivate()){
                    System.out.println("restart");
                    needToStop = true;

                    /*
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(intent);*/

                    restartView();

                }

                returnToMainButton.update();
                if(returnToMainButton.shouldActivate()){
                    returnToLogin();
                }

            }



            try {
                Thread.sleep(15);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private  void returnToLogin(){
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
    private void restartView(){
        seconds = 0;
        player.setSpeed(0);
        player.setY(screenHeight/2);
        player.setX(screenWidth/2);

        seconds = 0;
        isdead = false;
        needToStop = false;
        player.start();
        things.clear();
        startSecondsTimer();
        startTearTimer();
        startPowerUpTimer();
    }
}
