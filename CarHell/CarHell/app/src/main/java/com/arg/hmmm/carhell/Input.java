package com.arg.hmmm.carhell;

import android.content.Context;
import android.graphics.PointF;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Input implements View.OnTouchListener {
    public static SparseArray<PointF> touchPoints;
    /*public static float x = 0;
    public static float y = 0;*/
    public static PointF upPos;
    public static boolean down = false;
    public static int downId = -1;

    public Input() {
        touchPoints = new SparseArray<>();
    }

    public static SparseArray<PointF> getTouchPoints() {
        if (touchPoints == null)
            return new SparseArray<>();
        return touchPoints.clone();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /*x = event.getX();
        y = event.getY();*/

        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        upPos = null;
        downId = -1;
        down = false;
        switch(maskedAction){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:   //ACTION_POINTER_DOWN is used for multiple touch inputs
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                touchPoints.put(pointerId, f);
                down = true;
                downId = pointerId;
                break;
            case MotionEvent.ACTION_MOVE:/*
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = touchPoints.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }*/
                break;
            case MotionEvent.ACTION_UP:
                upPos = new PointF(event.getX(), event.getY());
                touchPoints.remove(pointerId);
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touchPoints.remove(pointerId);
                break;
        }
        return true;
    }
}
