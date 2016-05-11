package edu.augustana.dreamteam.orderofoperations.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import edu.augustana.dreamteam.orderofoperations.math.Equation;
import edu.augustana.dreamteam.orderofoperations.gameobjects.*;
import edu.augustana.dreamteam.orderofoperations.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

//import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by danyatazyeen on 4/16/16.
 */

public class MissionView extends SurfaceView implements Runnable{
    private static final int[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.MAGENTA, Color.BLACK};

    private Context context;

    private long startTime = System.currentTimeMillis();

    // Our game thread
    private Thread gameThread = null;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // Game is paused at the start
    private boolean paused = true;

    // A Canvas object to draw our bitmaps on and a Paint object to draw
    // bullets and barriers.
    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate
    private long fps;

    // This is used to help calculate the frames per second
    private long timeThisFrame;

    // The size of the screen in pixels
    private int screenWidth;
    private int screenHeight;

    // The player's ship
    private Spaceship playerShip;

    // The player's pistol to fire bullets
    private ArrayList<Blaster> playerBullets;

    // The invader's bullets
    private int nextBullet;
    private int maxInvaderBullets = 50;
    private Blaster[] invadersBullets;

    // Setting number of invaders
    private int numInvaders = 6;

    // Making array of UFOs the size of how many invaders there are
    private UFO[] invaders;

    // The player's shelters are built from bricks
    private BarrierBrick[] bricks = new BarrierBrick[400];
    private int numBricks;

    // ArrayList to store asteroids with operands in them
    private ArrayList<Asteroid> asteroids;

    private Equation equation;

    // A SharedPreferences variable to help store high scores
    private SharedPreferences sharedPref;

    // Keeps track of the player's final score for the current game
    private int finalScore;

    // For sound effects
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    private int currentLevel;

    // The score as the player accumulates points
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
    private Bitmap feedbackBox;
    private Bitmap noFeedbackBox;
    private Bitmap wrongFeedbackBox;
    private Bitmap correctFeedbackBox;

    private int numOperators;

    // When the we initialize (call new()) on gameView
    // This special constructor method runs
    public MissionView(Context context, int x, int y) {
        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);

        currentLevel = 1;

        // Make a globally available copy of the context so we can use it in another method
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        screenWidth = x;
        screenHeight = y;

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

        //Set background of our view to the space graphic
        background = BitmapFactory.decodeResource(getResources(), R.drawable.space_bg);

        noFeedbackBox = BitmapFactory.decodeResource(getResources(), R.drawable.gray);
        wrongFeedbackBox = BitmapFactory.decodeResource(getResources(), R.drawable.wrong);
        correctFeedbackBox = BitmapFactory.decodeResource(getResources(), R.drawable.right);

        prepareLevel(currentLevel-1);
    }

    /*
    This method restarts the the round with a new expression
    @param levelsPassed: the level the player is on
    */
    private void prepareLevel(int levelsPassed){
        currentLevel = levelsPassed + 1;
        // Here we will initialize all the game objects

        //TODO: the rest of this basically looks model-related, goes into GameArena
        // Make a new player space ship
        playerShip = new Spaceship(context, screenWidth, screenHeight -(screenHeight /10));

        if(levelsPassed>=10){
            numOperators = 4;
        } else if (levelsPassed>=5){
            numOperators = 3;
        } else {
            numOperators = 2;
        }

        equation = new Equation(numOperators);

        lives = 3;

        // Prepare the players bullet
        playerBullets = new ArrayList<Blaster>();

        invadersBullets = new Blaster[maxInvaderBullets];
        for(int i = 0; i<invadersBullets.length; i++){
            invadersBullets[i] = new Blaster(screenHeight);
        }

        // Adding asteroids to the view
        asteroids = new ArrayList<Asteroid>();

        invaders = new UFO[numInvaders];

        // Build an army of invaders
        for(int i = 0; i < invaders.length; i++){
            invaders[i] = new UFO(context, i, screenWidth, screenHeight -(screenHeight /10));
        }

        // Build the shelters
        numBricks = 0;
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new BarrierBrick(row, column, shelterNumber, screenWidth, screenHeight -(screenHeight /10));
                    numBricks++;
                }
            }
        }

        // Reset the menace level
        menaceInterval = 1000;

        feedbackBox = noFeedbackBox;
    }

    @Override
    /*
      Runs the game, calculates the frames per second,
      plays the sounds for the approaching aliens
     */
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if(!paused){
                update();
            }

            draw();

            if(!paused){
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

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }



    }

    /*
    Checks if the aliens have bumped the side of the screen to move
    them down if needed. Also checks if player has lost, save score
    into finalScore; then retrieves high scores and checks if finalScore
    is greater than one of them.
     */
    private void update(){
        addAsteroids();

        boolean lost = false;  // Has the player lost

        playerShip.update();
        updateInvaderBullets();
        updateInvaders();
        updateAsteroids();
        updatePlayerBullets();

        if(lost){
            finalScore = score;

            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            int[] topScores = new int[3];
            StringBuilder strbuilder = new StringBuilder();
            for (int i = 0; i < topScores.length; i++) {
                strbuilder.append(topScores[i]).append(",");
            }
            SharedPreferences.Editor editor = sharedPref.edit();
            sharedPref.edit().putString("topThree", strbuilder.toString());

            prepareLevel(currentLevel-1);
            score = 0;
        }

        if(equation.correctEquation()){
            playerWon();
        }
    }

    /*
    Draws elements of the game view
     */
    private void draw(){
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Setting brush color to white
            paint.setColor(Color.argb(255, 255, 255, 255));

            canvas.drawBitmap(background, 0, 0, paint);
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), playerShip.getY(), paint);
            canvas.drawBitmap(feedbackBox, screenWidth - 70, screenHeight - 87, paint);

            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);

            drawAsteroids();
            drawInvaders();

            paint.setColor(Color.argb(255, 255, 255, 255));

            drawBricks();
            drawPlayBullets();
            drawInvadBullets();
            drawEquationText();

            paint.setColor(Color.argb(255, 68, 6, 117));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives + "  Level: " + currentLevel, 5, screenHeight - 40, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // If SpaceInvadersActivity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
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
                Blaster bullet = new Blaster(screenHeight);
                playerBullets.add(bullet);
                bullet.shoot(playerShip.getX() + playerShip.getLength() / 2, playerShip.getY(), bullet.UP);
                soundPool.play(shootID, 1, 1, 0, 0, 1);
                break;
        }
        return true;
    }

    public void updateShipMovement(float xAccel){
        playerShip.updateShipSpeed(-1 * xAccel);
    }

    public void updateInvaderBullets(){
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getImpactPointY() > screenHeight){
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
            if(bricks[j].isAlive()){
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
                playerLost();
            }
        }
    }

    public void updateInvaders(){
        for(int i = 0; i < invaders.length; i++){
            if(invaders[i].isAlive()) {
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
                if (invaders[i].getX() > screenWidth - invaders[i].getLength()
                        || invaders[i].getX() < 0){
                    invadersClosing();
                }
            }
            if (invaders[i].getY() > screenHeight - screenHeight / 10) {
                playerLost();
            }
        }
    }

    public void updatePlayerBullets(){
        for(int j = 0; j < playerBullets.size(); j++) {
            if (playerBullets.get(j).getStatus()) {
                playerBullets.get(j).update(fps);
                checkPlayerHitBricks(j);
                checkPlayerHitAsteroids(j);
                if (numInvaders > 0) {
                    checkPlayerHitInvader(j);
                }
                if(playerBullets.get(j).getImpactPointY() < 0){
                    playerBullets.remove(j);
                }
            }
        }
    }

    public void checkPlayerHitInvader(int j){
        for (int i = 0; i < invaders.length; i++) {
            if (invaders[i].isAlive()) {
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
            if (bricks[i].isAlive()) {
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
            if (asteroids.get(i).isVisible()) {
                Asteroid ast = asteroids.get(i);
                if (RectF.intersects(playerBullets.get(j).getRect(), ast.getRect())) {
                    if(equation.isCorrectOperator(ast.getAsteroidOperator())){
                        asteroids.remove(i);
                        soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        playerBullets.get(j).setInactive();
                        score = score + 50;
                        feedbackBox = correctFeedbackBox;
                    } else {
                        asteroids.remove(i);
                        soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        playerBullets.get(j).setInactive();
                        score = score - 50;
                        feedbackBox = wrongFeedbackBox;
                    }
                }
            }
        }
    }

    public void checkAsteroidHitPlayer(int i){
        if (asteroids.get(i).isVisible()) {
            Asteroid ast = asteroids.get(i);
            if (RectF.intersects(playerShip.getRect(), ast.getRect())) {
                ast.setInvisible();
                asteroids.remove(i);
                soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                lives--;
                playerShip.destroyShip(lives);

                if(lives==0){
                    playerLost();
                }
            }
        }
    }

    public void drawInvaders(){
        for(int i = 0; i < invaders.length; i++) {
            if (invaders[i].isAlive()) {
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
            if(bricks[i].isAlive()) {
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

    public void addAsteroids(){
        long timeDiff = System.currentTimeMillis() - startTime;
        if(timeDiff > 2500){
            Asteroid ast = new Asteroid(context, screenWidth, screenHeight, numOperators);
            asteroids.add(ast);
            startTime = System.currentTimeMillis();
        }
    }

    public void updateAsteroids() {
        for(int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).update(fps);
            if(asteroids.get(i).getY()>screenHeight){
                asteroids.remove(i);
            }
            checkAsteroidHitPlayer(i);
        }
    }

    public void drawAsteroids() {
        for(int i = 0; i < asteroids.size(); i++) {
            Asteroid ast = asteroids.get(i);
            if(ast.isVisible()){
                canvas.drawBitmap(ast.getBitmap(), ast.getX(), ast.getY(), paint);
                paint.setColor(colors[ast.getAsteroidOperator().getIndex()]);
                canvas.drawText(ast.getAsteroidOperator().getOperatorChar(), ast.getX() + (ast.getWidth() / 4), ast.getY() + (ast.getHeight() / 2) + 10, paint);
            }
        }
    }

    public void playerLost(){
        paused = true;
        //score = 0;

        //launches score screen and sends over player's score
        Intent endGame = new Intent(context.getApplicationContext(), ScoreScreen.class);
        endGame.setAction(endGame.ACTION_SEND);
        endGame.putExtra("score", score);
        endGame.putExtra("screenHeight", screenHeight);
        context.startActivity(endGame);
    }

    public void playerWon(){
        paused = true;
        prepareLevel(currentLevel);
    }

    public void invadersClosing(){
        for(int i = 0; i<invaders.length; i++){
            invaders[i].dropDownAndReverse();
        }
    }

    private void drawEquationText(){
        StringBuilder eq = equation.getEquation();
        int location = screenWidth/(numOperators+1);
        int operatorIndex = 0;
        for(int i = 0; i<eq.length(); i++){
            if(equation.isOperator(i)){
                paint.setColor(colors[equation.getOperators().get(operatorIndex).getIndex()]);
                operatorIndex++;
            } else {
                paint.setColor(Color.argb(255, 68, 6, 117));
            }
            canvas.drawText(eq.substring(i, i+1), location, 55, paint);
            location += 40;
        }
    }
}
