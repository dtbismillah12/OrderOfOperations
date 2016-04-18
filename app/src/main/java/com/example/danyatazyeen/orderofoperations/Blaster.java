package com.example.danyatazyeen.orderofoperations;

import android.graphics.RectF;

/**
 * Created by danyatazyeen on 4/15/16.
 */
public class Blaster {
    private float x;
    private float y;

    private RectF rect;

    // Which way is it shooting
    public final int UP = 0;
    public final int DOWN = 1;

    // Going nowhere
    int heading = -1;
    float speed =  350;

    private int width = 1;
    private int height;

    private boolean isActive;

    public Blaster(int screenY) {

        height = screenY / 20;
        isActive = false;

        rect = new RectF();
    }

    public RectF getRect(){
        return  rect;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

    public float getImpactPointY(){
        if (heading == DOWN){
            return y + height;
        }else{
            return  y;
        }
    }

    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }

        // Bullet already active
        return false;
    }

    public void update(long fps){

        // Just move up or down
        if(heading == UP){
            y = y - speed / fps;
        }else{
            y = y + speed / fps;
        }

        // Update rect
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }
}


//    private int x;
//    private int y;
//    private RectF rectangle;
//    public final int UP = 0;
//    public final int DOWN = 1;
//    float speed = 350;
//    private int width = 1;
//    private int height;
//
//    private boolean shooting;
//
//    public Blaster (int screenY) {
//        height = screenY/20;
//        shooting = false;
//        rectangle = new RectF();
//    }
//
////    public void update(long fps){
////        if(heading == UP){
////            //y = y - speed / fps;
////        }else{
////            //y = y + speed / fps;
////        }
////        rectangle.left = x;
////        rectangle.right = x + width;
////        rectangle.top = y;
////        rectangle.bottom = y + height;
////
////    }
//
//    public RectF getRect(){
//        return  rectangle;
//    }
//
//    public boolean getStatus(){
//        return shooting;
//    }
//
//    public void setInactive(){
//        shooting = false;
//    }
//
////    public float getImpactPointY(){
////        if (heading == DOWN){
////            return y + height;
////        }else{
////            return  y;
////        }
////
////    }
//}
