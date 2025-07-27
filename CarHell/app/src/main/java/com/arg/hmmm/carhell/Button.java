package com.arg.hmmm.carhell;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.SparseArray;

public class Button {
    protected float x, y, width, height;

    public Bitmap getImage() {
        return image;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setText(String text) {
        this.text = text;
    }

    protected String text;
    protected boolean activate = false, clicked = false, pressed = false, shouldBePressed, activateOnActionUp;
    protected Bitmap image;

    public Button(float posX, float posY, float width, float height, String text, boolean shouldBePressed, boolean activateOnActionUp) {
        this.x = posX;
        this.y = posY;
        this.width = width;
        this.height = height;
        this.text = text;
        this.image = null;
        this.shouldBePressed = shouldBePressed;
        this.activateOnActionUp = activateOnActionUp;
    }


    public Button(float posX, float posY, Bitmap image, boolean shouldBePressed, boolean activateOnActionUp) {
        this.x = posX;
        this.y = posY;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.image = image;
        this.shouldBePressed = shouldBePressed;
        this.activateOnActionUp = activateOnActionUp;
    }

    public boolean shouldActivate(){
        return activate;
    }

    public void draw(Canvas canvas){
        if (image == null) {
            Paint paint = new Paint();
                paint.setARGB(255, 120, 120, 120);
                if (clicked)
                    paint.setARGB(255, 100, 100, 100);
                canvas.drawRect(x, y, x + width, y + height, paint);
                paint.setARGB(255, 10, 10, 10);
                paint.setTextAlign(Paint.Align.CENTER);
                float textSize = getTextSizeForHeight(paint, height * 0.75f, "Testing");
                paint.setTextSize(textSize);
                int xPos = (int) (x + width / 2);
                int yPos = (int) (y + height / 2 - (paint.descent() + paint.ascent()) / 2);
                canvas.drawText(text, xPos, yPos, paint);
        }
        else {
            Paint tpaint =new Paint();    //original idea™
            if (clicked){
                tpaint.setAlpha(150);}
            canvas.drawBitmap(image, x, y, tpaint);
        }
    }

    public void update(){
        activate = false;
        if (activateOnActionUp) {
            if (clicked && Input.upPos != null) {
                float x = Input.upPos.x;
                float y = Input.upPos.y;
                if (x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height) {
                    activate = true;
                }
            }
        }

        boolean newClicked = false;
        SparseArray<PointF> touchPoints = Input.getTouchPoints();
        if(touchPoints.size() == 0)
            pressed = false;
        for (int i = 0; i < touchPoints.size(); i++) {
            int key = touchPoints.keyAt(i);
            if(shouldBePressed && !Input.down && !pressed)
                break;
            if(shouldBePressed && Input.downId != key && !pressed) {
                continue;
            }
            if(touchPoints.get(key) == null) {
                continue;
            }
            float x = touchPoints.get(key).x;
            float y = touchPoints.get(key).y;
            if (x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height) {
                newClicked = true;
                pressed = true;
            }
        }
        if(!newClicked && pressed)
            pressed = false;
        clicked = newClicked;
        if (!activateOnActionUp)

            activate = clicked;
    }


    //util
    private float getTextSizeForHeight(Paint paint, float desiredHeight, String text) {
        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredHeight / bounds.height();

        // Set the paint for that size.
        return desiredTextSize;
    }
}
