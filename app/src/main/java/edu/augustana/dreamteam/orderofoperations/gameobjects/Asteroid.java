package edu.augustana.dreamteam.orderofoperations.gameobjects;

/**
 * Created by danyatazyeen on 4/6/16.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import edu.augustana.dreamteam.orderofoperations.R;
import edu.augustana.dreamteam.orderofoperations.math.Operator;

import java.util.Random;

public class Asteroid {

    RectF rect;

    // The asteroids will be represented by a Bitmap
    private Bitmap bitmap;
    private Operator myOperator;


    // How long and high the paddle for each asteroid will be
    private float length;
    private float height;

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speedthat the paddle will move
    private float asteroidSpeed;

    private int screenWidth;

    private Random rand;

    private boolean isVisible;

    /**
     * Constructor of the Asteroid class
     * @param screenX is the width of the screen
     * @param screenY is the height of the screen
     */
    public Asteroid(Context context, int screenX, int screenY, int numOperators){
        rand = new Random();

        isVisible = true;

        //Initialize Operator Generator
        myOperator = new Operator();

        // Initialize a blank RectF
        rect = new RectF();

        length = screenX / 10;
        height = screenY / 20;

        int padding = screenX / 25;

        x = rand.nextInt(screenX-(int)length);
        y = screenY-(9*screenY/10);

        // Initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        screenWidth = screenX;

        asteroidSpeed = 60;

    }

    public Operator getAsteroidOperator(){
        return myOperator;
    }

    public void setInvisible(){
        isVisible = false;
    }
    public boolean isVisible(){
        return isVisible;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getWidth(){
        return length;
    }

    public float getHeight(){
        return height;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void update(float fps){
        y = y + asteroidSpeed/fps;

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

}

