package com.arg.hmmm.carhell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Thing implements Runnable{
    protected GameView view;
    protected Bitmap bmp;
    protected Matrix matrix;
    protected Paint paint;

    protected float x;
    protected float y;
    protected float vx;
    protected float vy;

    protected Thread thread;
    protected boolean needToStop;

    public Thing(float x, float y, GameView view){
        this.x=x;
        this.y=y;
        this.view=view;
        needToStop = false;

    }

    public Thing(float x, float y, GameView view, float vx, float vy, Bitmap bmp){
        this(x,y,view);
        this.vx=vx;
        this.vy=vy;
        this.bmp=bmp;

        thread =new Thread(this,"ThreadNum1");
        thread.start();
    }


    //getters
    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public float getVx(){
        return this.vx;
    }
    public float getVy(){
        return this.vy;
    }
    public float getBmpWidth() {
        return bmp.getWidth();
    }
    public float getBmpHeight() {
        return bmp.getHeight();
    }
    public Bitmap getBmp() {
        return bmp;
    }

    //setters
    public void setX(float x){
        this.x=x;
    }
    public void setY(float y){
        this.y=y;
    }
    public void setVx(float vx){
        this.vx=vx;
    }
    public void setVy(float vy){
        this.vy=vy;
    }
    public void setBmp(Bitmap bmp){
        this.bmp=bmp;
    }
    public void stop(){
        needToStop=true;
    }
    public void start(){
        needToStop=false;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void addVx(float x){
        vx+=x;
    }
    public void addVy(float x){
        vy+=x;
    }


    public void draw(Canvas canvas){
        //canvas.drawBitmap(bmp,x,y,paint);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bmp, x, y, paint);
        canvas.setMatrix(null);
    }
    /*public void drawMatrix(Canvas canvas){
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bmp, x, y, paint);
        canvas.setMatrix(null);
    }*/

    private void checkRemove(){
        if(x<-bmp.getWidth()*2 || y< -bmp.getHeight()*2 || x>view.getScreenWidth()+bmp.getWidth() || y> view.getScreenHeight()+bmp.getWidth()){
            remove();
        }
    }

    private void remove(){
        needToStop = true;
        view.removeThing(this);
    }



    @Override
    public void run() {
        while(!no()){
            checkRemove();

            x+=vx;
            y+=vy;

            try {
                Thread.sleep(15);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean no(){
        return needToStop;
    }
}
