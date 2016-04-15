package com.example.danyatazyeen.orderofoperations;

import android.graphics.RectF;

/**
 * Created by danyatazyeen on 4/15/16.
 */
public class Blaster {
    private int x;
    private int y;
    private RectF rectangle;
    public final int UP = 0;
    public final int DOWN = 1;
    float speed = 350;
    private int width = 1;
    private int height;

    private boolean shooting;

    public Blaster (int screenY) {
        height = screenY/20;
        shooting = false;
        rectangle = new RectF();
    }

//    public void update(long fps){
//        if(heading == UP){
//            //y = y - speed / fps;
//        }else{
//            //y = y + speed / fps;
//        }
//        rectangle.left = x;
//        rectangle.right = x + width;
//        rectangle.top = y;
//        rectangle.bottom = y + height;
//
//    }

    public RectF getRect(){
        return  rectangle;
    }

    public boolean getStatus(){
        return shooting;
    }

    public void setInactive(){
        shooting = false;
    }

//    public float getImpactPointY(){
//        if (heading == DOWN){
//            return y + height;
//        }else{
//            return  y;
//        }
//
//    }
}
