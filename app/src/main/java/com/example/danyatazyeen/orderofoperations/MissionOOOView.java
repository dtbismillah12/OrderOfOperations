package com.example.danyatazyeen.orderofoperations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by danyatazyeen on 4/16/16.
 */
public class MissionOOOView {
    Context context;

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

    public SpaceInvadersView(Context context, int x, int y){
        super(context);
        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();
        screenWidth = x;
        screenLength = y;
    }

    private void prepareLevel(){

        // Here we will initialize all the game objects

        // Make a new player space ship

        // Prepare the players bullet

        // Initialize the invadersBullets array

        // Build an army of invaders

        // Build the shelters

    }
}

