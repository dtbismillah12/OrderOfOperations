package edu.augustana.dreamteam.orderofoperations.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import edu.augustana.dreamteam.orderofoperations.math.*;
import edu.augustana.dreamteam.orderofoperations.gameobjects.*;
import edu.augustana.dreamteam.orderofoperations.R;

import java.util.ArrayList;

/**
 * Created by danyatazyeen on 4/16/16.
 */

public class MissionView extends SurfaceView implements Runnable{
    // Visual objects
    private static final int[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.MAGENTA, Color.BLACK};
    private SurfaceHolder ourHolder; // SurfaceHolder to lock the surface before we draw our graphics
    private int screenWidth; // The size of the screen in pixels
    private int screenHeight;
    private Canvas canvas; // Canvas and paint objects for drawing
    private Paint paint;
    private Bitmap background;
    private Bitmap feedbackBox;
    private Bitmap noFeedbackBox;
    private Bitmap wrongFeedbackBox;
    private Bitmap correctFeedbackBox;
    private Context context;

    // Control objects
    private long startTime = System.currentTimeMillis();
    private Thread gameThread = null; // Our game thread
    private volatile boolean playing; // A boolean which we will set when the game is running or not
    private boolean paused = true; // Game is paused at the start
    private long fps; // This variable tracks the game frame rate
    private long timeThisFrame; // used to calculate the frames per second
    private SharedPreferences sharedPref; // SharedPreferences variable to help store high scores
    private SharedPreferences.Editor editor;
    private int score;
    private int finalScore; // Final score after death
    private int currentLevel;
    private int lives;
    private int touches;

    // Game model objects
    private Spaceship playerShip;
    private ArrayList<Blaster> playerBullets;
    private int nextBullet;
    private int maxInvaderBullets = 50;
    private Blaster[] invadersBullets;
    private int numInvaders;
    private UFO[] invaders; // array of UFOs the size of how many invaders there are
    private BarrierBrick[] bricks = new BarrierBrick[400]; // player's shelters are built from bricks
    private int numBricks;
    private ArrayList<Asteroid> asteroids; // asteroids with operators in them
    private int numOperators; // Number of operators to be in equation
    private Equation equation;

    /*
    Constructor for MissionView
    */
    public MissionView(Context context, int x, int y) {
        super(context);

        currentLevel = 1;
        score = 0;

        this.context = context; // Make a globally available copy of the context so we can use it in another method

        ourHolder = getHolder(); // Initialize ourHolder and paint objects
        paint = new Paint();
        screenWidth = x;
        screenHeight = y;

        //Prepare bitmap graphics
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
        touches = 0;
        numInvaders = 6;
        lives = 3;

        // initialize all the game objects
        playerShip = new Spaceship(context, screenWidth, screenHeight -(screenHeight /10)); // Make a new player space ship

        if(levelsPassed>=10){
            numOperators = 4;
        } else if (levelsPassed>=5){
            numOperators = 3;
        } else {
            numOperators = 2;
        }
        equation = new Equation(numOperators);
        playerBullets = new ArrayList<Blaster>();
        invadersBullets = new Blaster[maxInvaderBullets];
        asteroids = new ArrayList<Asteroid>();
        invaders = new UFO[numInvaders];

        addInvaderBullets();
        addInvaders();
        buildBarriers();

        feedbackBox = noFeedbackBox; //Start with grey box for no feedback
    }

    @Override
    /*
      Runs the game, calculates the frames per second,
      plays the sounds for the approaching aliens
     */
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis(); // Capture the current time in milliseconds in startFrameTime

            if(!paused){
                update();
            }
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame; // Calculate the fps this frame for later update use
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
        playerShip.update();
        updateInvaderBullets();
        updateInvaders();
        updateAsteroids();
        updatePlayerBullets();
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
            canvas = ourHolder.lockCanvas(); // Lock the canvas ready to draw

            canvas.drawBitmap(background, 0, 0, paint);
            canvas.drawBitmap(playerShip.getBitmap(lives), playerShip.getX(), playerShip.getY(), paint);
            canvas.drawBitmap(feedbackBox, screenWidth - 70, screenHeight - 87, paint);

            drawAsteroids();
            drawInvaders();

            paint.setColor(Color.argb(255, 255, 255, 255)); // Setting brush color to white

            drawBricks();
            drawPlayBullets();
            drawInvadBullets();
            drawEquationText();
            drawGameInfoText();

            ourHolder.unlockCanvasAndPost(canvas); // Draw everything to the screen
        }
    }

    /*
    Builds white barriers near bottom of screen for player to hide behind
     */
    private void buildBarriers(){
        numBricks = 0;
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new BarrierBrick(row, column, shelterNumber, screenWidth, screenHeight -(screenHeight /10));
                    numBricks++;
                }
            }
        }
    }

    /*
    Adds UFOs to the screen
     */
    private void addInvaders(){
        for(int i = 0; i < invaders.length; i++){
            invaders[i] = new UFO(context, i, screenWidth, screenHeight -(screenHeight /10));
        }
    }

    /*
    Creates the bullets the UFOs are firing
     */
    private void addInvaderBullets(){
        for(int i = 0; i<invadersBullets.length; i++){
            invadersBullets[i] = new Blaster(screenHeight);
        }
    }

    /*
    Creates Asteroids which contain the operators in the expression
    and drop down at a slow rate.
     */
    private void addAsteroids(){
        long timeDiff = System.currentTimeMillis() - startTime;
        if(timeDiff > 3000 && asteroids.size() <= 3){
            Asteroid ast = new Asteroid(context, screenWidth, screenHeight, numOperators, equation.getAnOperator());
            asteroids.add(ast);
            startTime = System.currentTimeMillis();
        }
    }

    /*
    Allows the player to move by tilting the screen
    @param xAccel: the rate of horizontal acceleration the player can move with
     */
    public void updateShipMovement(float xAccel){
        playerShip.updateShipSpeed(-1 * xAccel);
    }

    /*
    Checks if individual bullets have hit anything or gone off the screen.
    If so, they are set inactive.
     */
    private void updateInvaderBullets(){
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

    /*
    Makes sure to keep the UFOs going across the screen, turns them around if they bump the edge,
    and prepares the invaders to shoot.
     */
    private void updateInvaders(){
        for(int i = 0; i < invaders.length; i++){
            if(invaders[i].isAlive()) {
                invaders[i].update(fps);
                if(invaders[i].takeAim(playerShip.getX(), playerShip.getLength())){ // Does invader want to take a shot?
                    if(invadersBullets[i].shoot(invaders[i].getX() + invaders[i].getLength() / 2, invaders[i].getY(), 1)) { // try and spawn bullet
                        nextBullet = (nextBullet + 1) % invadersBullets.length; // Shot fired and prepare for the next shot
                    }
                }
                if (invaders[i].getX() > screenWidth - invaders[i].getLength() // Check if invaders bump edge of screen
                        || invaders[i].getX() < 0){
                    invadersClosing();
                }
            }
            if (invaders[i].getY() > screenHeight - screenHeight / 10) {
                playerLost();
            }
        }
    }

    /*
    Turns the UFOs around and moves them down a row if they have
    bumped the edge of the screen
     */
    private void invadersClosing(){
        for(int i = 0; i<invaders.length; i++){
            invaders[i].dropDownAndReverse();
        }
    }

    /*
    Checks if the player's bullets have hit anything
     */
    private void updatePlayerBullets(){
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

    /*
    Removes asteroids if they have gone off the screen and whether they
    have hit the player.
     */
    private void updateAsteroids() {
        for(int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).update(fps);
            if(asteroids.get(i).getY()>screenHeight){
                asteroids.remove(i);
            }
            checkAsteroidHitPlayer(i);
        }
    }

    /*
    Checks if the UFO bullets have hit the bottom barriers and subtracts
    bricks where the barrier has been shot.
     */
    private void checkInvaderHitBricks(int i){
        for(int j = 0; j < numBricks; j++){
            if(bricks[j].isAlive()){
                if (RectF.intersects(invadersBullets[i].getRect(), bricks[j].getRect())){ // A collision has occurred
                    invadersBullets[i].setInactive();
                    bricks[j].setInvisible();
                }
            }
        }
    }

    /*
    Checks if invader has shot player.
    If so, decreases the player's lives by 1.
     */
    private void checkInvaderHitPlayer(int i){
        if (RectF.intersects(playerShip.getRect(), invadersBullets[i].getRect())){
            invadersBullets[i].setInactive();
            lives --;
            if(lives == 0){ // game over?
                playerLost();
            }
        }
    }

    /*
    Checks if the player has shot an invader.
    If so, it removes the invader and adds 10 to the player's score.
     */
    private void checkPlayerHitInvader(int j){
        for (int i = 0; i < invaders.length; i++) {
            if (invaders[i].isAlive()) {
                if (RectF.intersects(playerBullets.get(j).getRect(), invaders[i].getRect())) {
                    invaders[i].setInvisible();
                    playerBullets.get(j).setInactive();
                    score = score + 10;
                    numInvaders--;
                }
            }
        }
    }

    /*
    Checks if the player's bullets have hit the barrier and sets the shot
    bricks invisible.
     */
    private void checkPlayerHitBricks(int j){
        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].isAlive()) {
                if (RectF.intersects(playerBullets.get(j).getRect(), bricks[i].getRect())) {
                    // A collision has occurred
                    playerBullets.get(j).setInactive();
                    bricks[i].setInvisible();
                }
            }
        }
    }

    /*
    Checks if the Player's bullet has hit an asteroid and removes it.
    It then increases the player's score by 50 if correct.
    It subtracts 50 if incorrect.
    Immediate feedback about correct/incorrect is given by setting the
    feedback button to red(wrong) or green(right).
     */
    private void checkPlayerHitAsteroids(int j){
        for(int i = 0; i < asteroids.size(); i++){
            if (asteroids.get(i).isVisible()) {
                Asteroid ast = asteroids.get(i);
                if (RectF.intersects(playerBullets.get(j).getRect(), ast.getRect())) {
                    if(equation.isCorrectOperator(ast.getAsteroidOperator())){
                        asteroids.remove(i);
                        playerBullets.get(j).setInactive();
                        score = score + 50;
                        feedbackBox = correctFeedbackBox;
                    } else {
                        asteroids.remove(i);
                        playerBullets.get(j).setInactive();
                        score = score - 50;
                        feedbackBox = wrongFeedbackBox;
                    }
                }
            }
        }
    }

    /*
    Checks if the player has been hit by an oncoming asteroid
    and reduces lives remaining if so.
    */
    private void checkAsteroidHitPlayer(int i){
        if (asteroids.get(i).isVisible()) {
            Asteroid ast = asteroids.get(i);
            if (RectF.intersects(playerShip.getRect(), ast.getRect())) {
                ast.setInvisible();
                asteroids.remove(i);
                lives--;
                if(lives==0){
                    playerLost();
                }
            }
        }
    }

    /*
    Draws the UFOs to the screen.
    */
    private void drawInvaders(){
        for(int i = 0; i < invaders.length; i++) {
            if (invaders[i].isAlive()) {
                canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
            }
        }
    }

    /*
    Draws the blocks of barriers on the screen.
    */
    private void drawBricks(){
        for(int i = 0; i < numBricks; i++){
            if(bricks[i].isAlive()) {
                canvas.drawRect(bricks[i].getRect(), paint);
            }
        }
    }

    /*
    Draws the player's bullets on the screen.
    */
    private void drawPlayBullets() {
        for(int i = 0; i<playerBullets.size(); i++){
            if (playerBullets.get(i).getStatus()) {
                canvas.drawRect(playerBullets.get(i).getRect(), paint);
            }
        }
    }

    /*
    Draws the UFO bullets on the screen.
     */
    private void drawInvadBullets(){
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()) {
                canvas.drawRect(invadersBullets[i].getRect(), paint);
            }
        }
    }

    /*
    Draws the Asteroids to the screen
     */
    private void drawAsteroids() {
        for(int i = 0; i < asteroids.size(); i++) {
            Asteroid ast = asteroids.get(i);
            if (ast.isVisible()) {
                canvas.drawBitmap(ast.getBitmap(), ast.getX(), ast.getY(), paint);
                paint.setColor(colors[ast.getAsteroidOperator().getIndex()]);
                canvas.drawText(ast.getAsteroidOperator().getOperatorChar(), ast.getX() + (ast.getWidth() / 4), ast.getY() + (ast.getHeight() / 2) + 10, paint);
            }
        }
    }

    /*
    Draws the expression to the top of the screen
     */
    private void drawEquationText(){
        paint.setTextSize(40);
        ArrayList<EquationTerm> equationTerms = equation.getEquation();
        int location = screenWidth/(numOperators+1);
        for(int i = 0; i<equationTerms.size(); i++){
            if(equationTerms.get(i).isOperator()){
                paint.setColor(colors[equationTerms.get(i).getOperator().getIndex()]);
            } else {
                paint.setColor(Color.argb(255, 68, 6, 117));
            }
            canvas.drawText(equationTerms.get(i).getTerm(), location, 55, paint);
            location += 40;
        }
    }

    /*
    Draws the Score, number of lives remaining, and
    the current level to the top of the screen.
     */
    private void drawGameInfoText() {
        paint.setColor(Color.argb(255, 68, 6, 117)); // Setting brush color to purple
        paint.setTextSize(35);
        canvas.drawText("Score: " + score + "   Lives: " + lives + "  Level: " + currentLevel, 20, screenHeight - 45, paint);
    }

    /*
    If the player wins, prepares the next level.
     */
    private void playerWon() {
        paused = true;
        prepareLevel(currentLevel);
    }

    /*
    Launches the Score screen and initializes high scores to zero if the game has never been
    played before on the specific device.
    */
    private void playerLost(){
        paused = true;

        //launches score screen and sends over player's score
        Intent endGame = new Intent(context.getApplicationContext(), ScoreScreen.class);
        endGame.setAction(endGame.ACTION_SEND);
        endGame.putExtra("score", score);
        endGame.putExtra("screenHeight", screenHeight);
        context.startActivity(endGame);


        System.out.println("Absolute Path: " + Environment.getExternalStorageDirectory().getAbsolutePath());

        finalScore = score;
        sharedPref = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if (sharedPref.getString("first", "").equals("")){
            editor.putString("first", "30");

        }
        if (sharedPref.getString("second", "").equals("")){
            editor.putString("second", "20");

        }
        if (sharedPref.getString("second", "").equals("")) {
            editor.putString("third", "3");
        }

        editor.apply();
    }

    /*
    If SpaceInvadersActivity is paused/stopped shutdown our thread.
     */
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    /*
     If SpaceInvadersActivity is started then start our thread.
     */
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /*
    Allows player to shoot multiple bullets at once.
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        paused = false;
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                touches++;
                if(touches>1){
                    Blaster bullet = new Blaster(screenHeight);
                    playerBullets.add(bullet);
                    bullet.shoot(playerShip.getX() + playerShip.getLength() / 2, playerShip.getY(), bullet.UP);
                    break;
                }
        }
        return true;
    }
}
