package com.example.danyatazyeen.orderofoperations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Paint;

/**
 * Created by danyatazyeen on 4/6/16.
 * referencing http://gamecodeschool.com/android/coding-a-space-invaders-game/
 */
public class Spaceship {
    RectF rect;

    // The player ship will be represented by a Bitmap
    private Bitmap bitmap;

    // How long and high our paddle will be
    private float length;
    private float height;

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speedthat the paddle will move
    private float shipSpeed;

    // Which ways can the paddle move
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Is the ship moving and in which direction
    private int shipMoving = STOPPED;

    public Spaceship(Context context, int screenX, int screenY){

        // Initialize a blank RectF
        rect = new RectF();

        length = screenX/10;
        height = screenY/25;

        // Start ship in roughly the screen center
        x = screenX / 2;
        y = screenY - 20;

        // Initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        // How fast is the spaceship in pixels per second
        shipSpeed = 350;
    }

    public RectF getRect(){
        return rect;
    }

    // This is a getter method to make the rectangle that
    // defines our paddle available in BreakoutView class
    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getX(){
        return x;
    }

    public float getLength(){
        return length;
    }

    // This method will be used to change/set if the paddle is going left, right or nowhere
    public void setMovementState(int state){
        shipMoving = state;
    }

    // This update method will be called from update in SpaceInvadersView
    // It determines if the player ship needs to move and changes the coordinates
    // contained in x if necessary
    public void update(long fps){
        if(shipMoving == LEFT){
            x = x - shipSpeed / fps;
        }

        if(shipMoving == RIGHT){
            x = x + shipSpeed / fps;
        }

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

//    RectF rectangle;
//    private Bitmap bitmap;
//    private int x;
//    private int y;
//    private static int velX;
//    private static int velY;
//    private float widthShip;
//    private float xShipLeft;
//    private float heightShip;
//    private float yShipTop;
//    public static float frameTime = 0.666f;
//    private Paint paint;
//
//    public Spaceship(Context context, int screenX, int screenY){
//        rectangle = new RectF();
//        x = 100;
//        y = 100;
//        velX = 0;
//        velY = 0;
//        widthShip = screenX/10;
//        heightShip = screenY/10;
//        xShipLeft = screenX/2;
//        yShipTop = screenY-20;
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
//        bitmap = Bitmap.createScaledBitmap(bitmap, (int) widthShip, (int) heightShip, false);
//    }
//
//    public void move(int leftWall, int topWall,
//                     int rightWall, int bottomWall) {
//        //MOVE SHIP
//        x += velX;
//        y += velY;
//
//        //CHECK FOR COLLISIONS ALONG WALLS
//        if (y > bottomWall - (int) heightShip /2) {
//            y = bottomWall - (int)heightShip/2;
//            velY = -1*(velY/2);
//        } else if (y < topWall + (int) heightShip /2) {
//            y = topWall + (int)heightShip/2;
//            velY = -1*(velY/2);
//        }
//
//        if (x > rightWall - (int) widthShip /2) {
//            x = rightWall - (int) widthShip /2;
//            velX = -1*(velX/2);
//        } else if (x < leftWall + (int) widthShip /2) {
//            x = leftWall + (int) widthShip /2;
//            velX = -1*(velX/2);
//        }
//    }
//
//    public Bitmap getBitmap(){
//        return bitmap;
//    }
//
//    public int getX(){
//        return x;
//    }
//
//    public int getY(){
//        return y;
//    }
//
//    public static void updateVelo(int accelX, int accelY){
//        // updating velocity of ball based on v = a * t
//        // according to acceleration measurements from accelerometer
//        velX += (accelX*frameTime);
//        velY += (accelY*frameTime);
//    }
//
//    public void draw(Canvas canvas,int screenWidth, int screenLength) {
//        canvas.drawBitmap(bitmap, x, screenLength - 50, paint);
//    }

}
