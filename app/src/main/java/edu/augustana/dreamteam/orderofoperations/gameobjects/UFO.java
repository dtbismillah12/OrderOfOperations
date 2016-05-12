package edu.augustana.dreamteam.orderofoperations.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import edu.augustana.dreamteam.orderofoperations.R;

import java.util.Random;

/**
 * Created by danyatazyeen on 4/6/16.
 */
public class UFO {
    RectF rect;

    Random generator = new Random();

    // The player ship will be represented by a Bitmap
    private Bitmap bitmap1;
    private Bitmap bitmap2;

    // How long and high our paddle will be
    private float length;
    private float height;

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speedthat the paddle will move
    private float shipSpeed;

    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Is the ship moving and in which direction
    private int shipMoving = RIGHT;

    private boolean isVisible;

    private int chanceOfFire;

    public UFO (Context context, int column, int screenX, int screenY) {

        // Initialize a blank RectF
        rect = new RectF();

        length = screenX / 20;
        height = screenY / 20;

        isVisible = true;

        chanceOfFire = 30;

        int padding = screenX / 23;

        x = column * (length + padding);
        y = screenY-(9*screenY/10);

        // Initialize the bitmap
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien1);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien2);

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                (int) (length),
                (int) (height),
                false);

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                (int) (length),
                (int) (height),
                false);

        // How fast is the invader in pixels per second
        shipSpeed = 40;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean isAlive(){
        return isVisible;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return bitmap1;
    }

    public Bitmap getBitmap2(){
        return bitmap2;
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

    public void dropDownAndReverse(){
        if(shipMoving == LEFT){
            shipMoving = RIGHT;
        }else if(shipMoving == RIGHT){
            shipMoving = LEFT;
        }
        y = y + height;
    }

    public boolean takeAim(float playerShipX, float playerShipLength){
        // If near the player
        if((playerShipX + playerShipLength > x && playerShipX + playerShipLength < x + length) ||
                (playerShipX > x && playerShipX < x + length)) {
            if(generator.nextInt(chanceOfFire) == 0) {
                return true;
            }
        } else {
            if(generator.nextInt(1000) == 0){
                return true;
            }
        }
        return false;
    }

    public void increaseChanceOfFire(int factor){
        chanceOfFire = chanceOfFire / factor;
    }
}
