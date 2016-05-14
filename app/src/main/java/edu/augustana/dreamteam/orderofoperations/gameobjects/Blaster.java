package edu.augustana.dreamteam.orderofoperations.gameobjects;

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

    /*
    Constructor for the Blaster class
     */
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

    /*
    Sets the gun inactive
     */
    public void setInactive(){
        isActive = false;
        x = 0;
        y = 0;
    }

    /*
    Returns the y point at which the bullet made impact with an object
     */
    public float getImpactPointY(){
        if (heading == DOWN){
            return y + height;
        }else{
            return  y;
        }
    }

    /*
    This method shoots a bullet from either the UFO or player
    @return: true if bullet is fired or false if bullet already active
     */
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

    /*
    Moves the bounding box of the bullet as it is moving.
     */
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
