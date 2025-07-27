package com.arg.hmmm.carhell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends Thing{
    private boolean isAccelerating;
    private float speed;
    private float angle;
    private float movementFixer;
    private final float speedFactor = view.getScreenHeight()/200f;
    private boolean isSheildActive;
    private TimerTask powerUpDuration;
    private Bitmap shieldBmp;


    public Player(int x, int y, GameView gameView, Bitmap bmp){
        super(x,y,gameView);
        vy=0;
        vx=0;
        this.bmp = bmp;
        matrix = new Matrix();

        shieldBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(),R.drawable.shield), view.getScreenWidth()/12, view.getScreenWidth()/12, false);

        isAccelerating=false;
        //-90 to point upwords
        angle = 0;
        speed = 0;

        isSheildActive = false;

        movementFixer = (float)Math.toRadians(-90);


        thread =new Thread(this,"ThreadNum1");
        thread.start();

    }

    public void setIsAccelerating(boolean x){
        isAccelerating=x;
    }

    public void setSpeed(float x){
        this.speed=x;
    }

    public void turnRight(){
        //speed !=0
        if(Math.abs(speed) >= 1){
            angle += (float)Math.toRadians(5);
            rotateImage();
        }

    }

    public void turnLeft(){
        //speed !=0
        if(Math.abs(speed) >= 1){
            angle -= (float)Math.toRadians(5);
            rotateImage();
        }

    }

    public void brake(){
        if(speed-speedFactor*1.5 >= 0)
            speed -=2; // speedFactor*1.5
        else if(speed <= 0)
            speed -= 1.5; //speedFactor
    }

    public void rotateImage(){
        /*
        matrix.reset();
        matrix.setTranslate(x, y);
        matrix.postRotate(angle, x+getBmpWidth()/2,y+getBmpHeight()/2);*/
        /*
        matrix.reset();
        matrix.setTranslate( x, y );
        matrix.postRotate(angle , x,y);
        matrix.postTranslate(x, y);
        bmp = Bitmap.createBitmap(bmp, 0, 0, (int)getBmpWidth(), (int)getBmpHeight(), matrix, true);*/

            matrix.reset();
            matrix.setRotate((float) Math.toDegrees(angle), x + bmp.getWidth() / 2f, y + bmp.getHeight() / 2f);

            /*
            -(float)Math.toRadians(-90)
            in order of the car to pint up the starting ange needs to be -90(rad)
             */
    }

    /*     //this function has been moved to Thing
    public void drawMatrix(Canvas canvas){
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bmp, x, y, null);
        canvas.setMatrix(null);
    }*/


    private void checkBoundaries(){
        if(y+getBmpHeight()/2<0) {
            y = view.getScreenHeight()-getBmpHeight()/2;
        }else if(y+getBmpHeight()/2>view.getScreenHeight()){
            y=0;}

        if(x+getBmpWidth()/2<0) {
            x = view.getScreenWidth()-getBmpWidth()/2;
        }else if(x+getBmpWidth()/2>view.getScreenWidth()){
            x=vx;}
    }

    private void checkImpact(){
        ArrayList<Thing> things = view.getThings();
        if (things != null) {
            Thing tThing;
            float ty = y+(getBmpHeight()-getBmpWidth())/2;

            for(int i=0; i<things.size();i++){
                tThing = things.get(i);
                if(x+bmp.getWidth()>=tThing.getX()&& tThing.getX()+tThing.getBmpWidth()>=x &&
                        ty+bmp.getHeight()>=tThing.getY()&& tThing.getY()+tThing.getBmpHeight()>=ty){
                    if(tThing instanceof Shield){
                        activateShild();
                        view.removeThing(tThing);
                    }
                    else
                        view.looseScrean();
                }

            }
        }
    }

    private void activateShild(){
        isSheildActive = true;
        Timer powerUpDorationTimer = new Timer();
        powerUpDuration = new TimerTask() {
            @Override
            public void run() {
                //add here something to stop, if()
                isSheildActive = false;
            }
        };
        powerUpDorationTimer.schedule(powerUpDuration,1000*5);
    }

    @Override
    public void draw(Canvas canvas){
        //canvas.drawBitmap(bmp,x,y,paint);
        super.draw(canvas);
        if(isSheildActive){
            canvas.setMatrix(matrix);
            canvas.drawBitmap(shieldBmp, x+shieldBmp.getWidth()/2, y+shieldBmp.getHeight()/2, paint);
            canvas.setMatrix(null);
        }
    }


    @Override
    public void run() {
        while(yes()) {
            x += vx;
            y += vy;

            view.getTestPixle().setX(x);
            view.getTestPixle().setY(y+(getBmpHeight()-getBmpWidth())/2);

            rotateImage();

            if (!isAccelerating) {
                if(speed-1>=0) //speed-speedFactor/2
                    speed -= 1;// speedFactor/2
                else if(speed+1<=0)//speed+speedFactor/2
                    speed+=1;// speedFactor/2
                else
                    speed = 0;
            } else {
                if(speed<speedFactor*4)//speedFactor*4
                    speed +=2;//speedFactor
            }
            checkBoundaries();

            if(!isSheildActive)
                checkImpact();

            //movementFixer is recuiered else when the car points upwords it will move to the right
            vx = (float)(speed*Math.cos(angle+movementFixer));
            vy = (float)(speed*Math.sin(angle+movementFixer));

            try {
                Thread.sleep(15);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean yes(){
        return !needToStop;
    }
}