package edu.augustana.dreamteam.orderofoperations.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.danyatazyeen.orderofoperations.R;

/**
 * Created by danyatazyeen on 4/6/16.
 * referencing http://gamecodeschool.com/android/coding-a-space-invaders-game/
 */
public class Spaceship {
    public static float frameTime = 0.666f;

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

    private Context context;
    private int screenWidth;

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
        x = screenX / 2 - (length/2);
        y = screenY - (2*height);
        this.context = context;
        // Initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        // How fast is the spaceship in pixels per second
        shipSpeed = 350;

        screenWidth = screenX;
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

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
    }

    public float getHeight(){
        return height;
    }

    // This method will be used to change/set if the paddle is going left, right or nowhere
    public void setMovementState(int state){
        shipMoving = state;
    }

    // This update method will be called from update in MissionView
    // It determines if the player ship needs to move and changes the coordinates
    // contained in x if necessary
    public void update(){
        x += shipSpeed;

        if(x<=0){
            x = 0;
        } else if(x>=screenWidth-length){
            x=screenWidth-length;
        }

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

    public void updateShipSpeed(float accelX){
        shipSpeed = (accelX*frameTime);
    }

    public void destroyShip(int lives){
//        if(lives == 2) {
//            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.two_life_ship);
//        } else if (lives == 1) {
//            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.one_life_ship);
//        }
        // stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
    }

}
