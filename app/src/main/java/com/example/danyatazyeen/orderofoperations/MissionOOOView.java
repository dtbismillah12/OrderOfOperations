package com.example.danyatazyeen.orderofoperations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

/**
 * Created by danyatazyeen on 4/16/16.
 */
public class MissionOOOView extends SurfaceView implements Runnable{
    Context context;
    RelativeLayout background;

    // This is our thread
    private Thread gameThread = null;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // Game is paused at the start
    private boolean paused = true;

    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate
    private long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // The size of the screen in pixels
    private int screenWidth;
    private int screenLength;

    // The players ship
    private Spaceship playerShip;

    // The player's bullet
    private Blaster bullet;

    // The invaders bullets
    private Blaster[] invadersBullets = new Blaster[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;

    // Up to 60 invaders
    Obstacle[] invaders = new Obstacle[60];
    int numInvaders = 0;

    // The score
    int score = 0;

    // Lives
    private int lives = 3;


//    // For sound FX
//    private SoundPool soundPool;
//    private int playerExplodeID = -1;
//    private int invaderExplodeID = -1;
//    private int shootID = -1;
//    private int damageShelterID = -1;
//    private int uhID = -1;
//    private int ohID = -1;
//
//    // How menacing should the sound be?
//    private long menaceInterval = 1000;
//    // Which menace sound should play next
//    private boolean uhOrOh;
//    // When did we last play a menacing sound
//    private long lastMenaceTime = System.currentTimeMillis();

    public MissionOOOView(Context context, int x, int y) {
        super(context);
        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();
        screenWidth = x;
        screenLength = y;
    }

    private void prepareLevel() {

        // Here we will initialize all the game objects

        // Make a new player space ship

        // Prepare the players bullet

        // Initialize the invadersBullets array

        // Build an army of invaders

        // Build the shelters

    }

    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We may use this if we want to add animation
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // We will do something new here towards the end of the project

        }
    }
    private void update() {

        // If the ship hits an obstacle, this is true. Otherwise, false.
        boolean bumped = false;

        // True if player has lost
        boolean lost = false;

        if(lost){
            prepareLevel();
        }

        // Update the players bullet

        // Has the player's bullet hit the top of the screen

        // Has an alien bullet hit the bottom of the screen

        // Has the player's bullet hit an invader

        // Has an alien bullet hit the player ship
    }

    private void draw(){
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the player spaceship

            // Draw the invaders

            // Draw the players bullet if active

            // Draw the alien's bullets if active

            // Draw the score and remaining lives
            // Change the brush color
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    //Stops thread if MainActivity is stopped
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    //Starts thread when MainActivity started
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Overriding SurfaceView class's onTouchListener
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}

