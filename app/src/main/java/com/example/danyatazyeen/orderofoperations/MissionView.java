package com.example.danyatazyeen.orderofoperations;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Set;

//import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by danyatazyeen on 4/16/16.
 */

public class MissionView extends SurfaceView implements Runnable{

    private Context context;

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
    private int screenX;
    private int screenY;

    private boolean contact;
    // The players ship
    private Spaceship playerShip;

    // The player's bullet
    private ArrayList<Blaster> playerBullets;

    // The invaders bullets
    private int nextBullet;
    private int maxInvaderBullets = 50;
    private Blaster[] invadersBullets;

    // Up to 60 invaders
    private int numInvaders = 6;
    private UFO[] invaders = new UFO[numInvaders];

    // The player's shelters are built from bricks
    private Barrier[] bricks = new Barrier[400];
    private int numBricks;

    private Random rand;

    private ArrayList<Asteroid> asteroids;

    private Equation equation;

    private int level;

    private SharedPreferences sharedPref;
    private int finalScore;

    // For sound FX
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    private int currentLevel;

    // The score
    private int score = 0;

    // Lives
    private int lives;

    // How menacing should the sound be?
    private long menaceInterval = 1000;
    // Which menace sound should play next
    private boolean uhOrOh;
    // When did we last play a menacing sound
    private long lastMenaceTime = System.currentTimeMillis();

    private Bitmap background;

    // When the we initialize (call new()) on gameView
    // This special constructor method runs
    public MissionView(Context context, int x, int y) {
        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);

        // Make a globally available copy of the context so we can use it in another method
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        currentLevel = 1;
        rand = new Random();

        // This SoundPool is deprecated but don't worry
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        try{
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("shoot.ogg");
            shootID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);

        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        prepareLevel(currentLevel-1);
    }

    private void prepareLevel(int levelsPassed){
        numInvaders = 6;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.space_bg);
        // Here we will initialize all the game objects

        // Make a new player space ship
        playerShip = new Spaceship(context, screenX, screenY-(screenY/10));

        equation = new Equation(levelsPassed);

        lives = 3;

        // Prepare the players bullet
        playerBullets = new ArrayList<Blaster>();
        invadersBullets = new Blaster[maxInvaderBullets];
        for(int i = 0; i<invadersBullets.length; i++){
            invadersBullets[i] = new Blaster(screenY);
        }
        asteroids = new ArrayList<Asteroid>();
        Asteroid ast = new Asteroid(context, screenX, screenY, currentLevel-1);
        asteroids.add(ast);
        ast = new Asteroid(context, screenX, screenY, currentLevel-1);
        asteroids.add(ast);
        ast = new Asteroid(context, screenX, screenY, currentLevel-1);
        asteroids.add(ast);
        ast = new Asteroid(context, screenX, screenY, currentLevel-1);
        asteroids.add(ast);

        // Build an army of invaders
        for(int i = 0; i < invaders.length; i ++ ){
            invaders[i] = new UFO(context, i, screenX, screenY-(screenY/10));
        }

        // Build the shelters
        numBricks = 0;
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new Barrier(row, column, shelterNumber, screenX, screenY-(screenY/10));
                    numBricks++;
                }
            }
        }


        // Reset the menace level
        menaceInterval = 1000;

    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if(!paused){
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // We will do something new here towards the end of the project
            // Play a sound based on the menace level
            if(!paused) {
                if ((startFrameTime - lastMenaceTime)> menaceInterval) {
                    if (uhOrOh) {
                        // Play Uh
                        soundPool.play(uhID, 1, 1, 0, 0, 1);

                    } else {
                        // Play Oh
                        soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }

                    // Reset the last menace time
                    lastMenaceTime = System.currentTimeMillis();
                    // Alter value of uhOrOh
                    uhOrOh = !uhOrOh;
                }
            }

        }



    }

    private void update(){

        // Did an invader bump into the side of the screen
        boolean bumped = false;

        // Has the player lost
        boolean lost = false;

        // Move the player's ship
        playerShip.update();

        // Update all the invaders bullets
        updateInvaderBullets();

        // Update all the invaders if visible
        bumped = updateInvaders(bumped);

        updateAsteroids();

        updatePlayerBullets();

        // Did an invader bump into the edge of the screen
        if(bumped){
            // Move all the invaders down and change direction
            for(int i = 0; i < numInvaders; i++){
                invaders[i].dropDownAndReverse();

                // Have the invaders landed
                if(invaders[i].getY() > screenY - screenY / 10){
                    lost = true;
                }



            }

            // Increase the menace level
            // By making the sounds more frequent
            menaceInterval = menaceInterval - 80;


        }


        if(lost){
            finalScore = score;
            //Set<String> highScores = new String[] {0,0,0};

//            sharedPref = getSharedPreferences("HighScores", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putStringSet("topFive", highScores);

            //prepareLevel(currentLevel-1);
            score = 0;



        }
    }


    private void draw(){
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            //canvas.drawColor(Color.argb(255, 221, 160, 221));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 255, 255, 255));


            canvas.drawBitmap(background, 0, 0, paint);
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), playerShip.getY(), paint);

            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);

            drawAsteroids();
            drawInvaders();

            paint.setColor(Color.argb(255, 255, 255, 255));

            drawBricks();
            drawPlayBullets();
            drawInvadBullets();

            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives + "  Level: " + currentLevel, 50, screenY - 40, paint);

            // Drawing text for equation in different colors
            /*StringBuilder eq = equation.getEquation();
            int prevLocation = screenX/3;
            int operatorIndex = 0;
            for(int i = 0; i<eq.length(); i++){
                if(equation.isOperator(i)){
                    paint.setColor(operatorIndex);
                    operatorIndex++;
                }
            }
            */

            canvas.drawText(equation.toString(), screenX/3, 55, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // If SpaceInvadersActivity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
//            Intent startGame = new Intent(getContext(), ScoreScreen.class);
//            startGame.setAction(startGame.ACTION_SEND);
//            context.startActivity(startGame);
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If SpaceInvadersActivity is started then
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        paused = false;

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                Blaster bullet = new Blaster(screenY);
                playerBullets.add(bullet);
                bullet.shoot(playerShip.getX() + playerShip.getLength() / 2, playerShip.getY(), bullet.UP);
                soundPool.play(shootID, 1, 1, 0, 0, 1);
                break;
        }
        return true;
    }

    public void updateShipMovement(float xAccel){
        playerShip.updateShipSpeed(-1*xAccel);
    }

    public void updateInvaderBullets(){
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getImpactPointY() > screenY){
                invadersBullets[i].setInactive();
            }

            if(invadersBullets[i].getStatus()){
                invadersBullets[i].update(fps);

                checkInvaderHitBricks(i);
                checkInvaderHitPlayer(i);
            }
        }
    }

    public void checkInvaderHitBricks(int i){
        for(int j = 0; j < numBricks; j++){
            if(bricks[j].getVisibility()){
                if (RectF.intersects(invadersBullets[i].getRect(), bricks[j].getRect())){
                    // A collision has occurred
                    invadersBullets[i].setInactive();
                    bricks[j].setInvisible();
                    soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                }
            }
        }
    }

    public void checkInvaderHitPlayer(int i){
        if (RectF.intersects(playerShip.getRect(), invadersBullets[i].getRect())){
            invadersBullets[i].setInactive();
            lives --;
            soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);
            playerShip.destroyShip(lives);

            // Is it game over?
            if(lives == 0){
                paused = true;
                lives = 3;
                score = 0;
                prepareLevel(currentLevel-1);

            }
        }
    }

    public boolean updateInvaders(boolean bumped){
        for(int i = 0; i < numInvaders; i++){
            if(invaders[i].getVisibility()) {

                // Move the next invader
                invaders[i].update(fps);

                // Does he want to take a shot?
                if(invaders[i].takeAim(playerShip.getX(), playerShip.getLength())){

                    // If so try and spawn a bullet

                    if(invadersBullets[i].shoot(invaders[i].getX() + invaders[i].getLength() / 2, invaders[i].getY(), 1)) {

                        // Shot fired
                        // Prepare for the next shot
                        nextBullet = (nextBullet + 1) % invadersBullets.length;
                    }

                }

                // If that move caused them to bump the screen change bumped to true
                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0){

                    bumped = true;

                }
            }
        }
        return bumped;
    }

    public void updatePlayerBullets(){
        for(int j = 0; j < playerBullets.size(); j++) {
            if (playerBullets.get(j).getStatus()) {
                playerBullets.get(j).update(fps);
                checkPlayerHitBricks(j);
                checkPlayerHitAsteroids(j);
                if (numInvaders > 0) {
                    checkPlayerHitInvader(j);
                } else {
                    paused = true;
                    score = 0;
                    lives = 3;
                    currentLevel++;
                    prepareLevel(currentLevel - 1);
                }
            }
        }
    }

    public void checkPlayerHitInvader(int j){
        for (int i = 0; i < invaders.length; i++) {
            if (invaders[i].getVisibility()) {
                if (RectF.intersects(playerBullets.get(j).getRect(), invaders[i].getRect())) {
                    invaders[i].setInvisible();
                    soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                    playerBullets.get(j).setInactive();
                    score = score + 10;
                    numInvaders--;
                }
            }
        }
    }

    public void checkPlayerHitBricks(int j){
        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].getVisibility()) {
                if (RectF.intersects(playerBullets.get(j).getRect(), bricks[i].getRect())) {
                    // A collision has occurred
                    playerBullets.get(j).setInactive();
                    bricks[i].setInvisible();
                    soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                }
            }
        }
    }

    public void checkPlayerHitAsteroids(int j){
        for(int i = 0; i < asteroids.size(); i++){
            if (asteroids.get(i).getVisibility()) {
                Asteroid ast = asteroids.get(i);
                if (RectF.intersects(playerBullets.get(j).getRect(), ast.getRect())) {
                    ast.setInvisible();
                    soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                    playerBullets.get(j).setInactive();
                    score = score + 50;
                }
            }
        }
    }

    public void drawInvaders(){
        for(int i = 0; i < numInvaders; i++) {
            if (invaders[i].getVisibility()) {
                if (uhOrOh) {
                    canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                } else {
                    canvas.drawBitmap(invaders[i].getBitmap2(), invaders[i].getX(), invaders[i].getY(), paint);
                }
            }
        }
    }

    public void drawBricks(){
        for(int i = 0; i < numBricks; i++){
            if(bricks[i].getVisibility()) {
                canvas.drawRect(bricks[i].getRect(), paint);
            }
        }
    }

    public void drawPlayBullets() {
        for(int i = 0; i<playerBullets.size(); i++){
            if (playerBullets.get(i).getStatus()) {
                canvas.drawRect(playerBullets.get(i).getRect(), paint);
            }
        }
    }

    public void drawInvadBullets(){
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()) {
                canvas.drawRect(invadersBullets[i].getRect(), paint);
            }
        }
    }

    public void updateAsteroids() {
        for(int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).update(fps);
        }
    }

    public void drawAsteroids() {
        for(int i = 0; i < asteroids.size(); i++) {
            Asteroid ast = asteroids.get(i);
            if(ast.isVisible){
                canvas.drawBitmap(ast.getBitmap(), ast.getX(), ast.getY(), paint);
                paint.setColor(ast.getAsteroidOperator().getColor());
                canvas.drawText(ast.getAsteroidOperator().getOperator(), ast.getX() + (ast.getWidth() / 4), ast.getY() + (ast.getHeight() / 2) + 10, paint);
            }
        }
    }
}
