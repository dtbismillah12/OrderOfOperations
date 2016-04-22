package com.example.danyatazyeen.orderofoperations;

/**
 * Created by danyatazyeen on 4/6/16.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import java.util.Random;

public class Asteroid {
    private final float[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.MAGENTA};

    RectF rect;

    // The asteroids will be represented by a Bitmap
    private Bitmap bitmap;
    private String operator;
    private OperatorGenerator generator;


    // How long and high the paddle for each asteroid will be
    private float length;
    private float height;

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speedthat the paddle will move
    private float asteroidSpeed;

    // Which ways can the paddle move
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Is the ship moving and in which direction
    private int shipMoving = STOPPED;

    /**
     * Constructor of the Asteroid class
     * @param operation is the operation value from the expression assigned to the asteroid
     * @param screenX is the width of the screen
     * @param screenY is the height of the screen
     */
    public Asteroid(Context context, String operation, int screenX, int screenY){

        //Initialize Operator Generator
        generator = new OperatorGenerator();

        //Get operator
        operator = generator.getOperator();

        // Initialize a blank RectF
        rect = new RectF();

        length = screenX / 20;
        height = screenY / 20;

        int padding = screenX / 25;

        x = 0;
        y = 0;

        // Initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid);
        Canvas canvas = new Canvas();
        canvas.drawBitmap(bitmap, );

    }
}

