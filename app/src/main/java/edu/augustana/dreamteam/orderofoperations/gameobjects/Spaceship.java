package edu.augustana.dreamteam.orderofoperations.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import edu.augustana.dreamteam.orderofoperations.R;

/**
 * Created by danyatazyeen on 4/6/16.
 * referencing http://gamecodeschool.com/android/coding-a-space-invaders-game/
 */
public class Spaceship {
    public static float frameTime = 0.666f;

    private RectF rect;

    // The player ship will be represented by a Bitmap
    private Bitmap goodShipBitmap;
    private Bitmap damagedShipBitmap;
    private Bitmap damagedShipBitmap2;

    // How long and high our paddle will be
    private float length;
    private float height;

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speed that the paddle will move
    private float shipSpeed;

    private Context context;
    private int screenWidth;

    /*
    Constructor for the Spaceship (the player's ship)
    @param context: Context from the main activity
    @param screenX: the width of the screen
    @param screenY: the height of the screen
     */
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
        goodShipBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        damagedShipBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.two_life_ship);
        damagedShipBitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.one_life_ship);

        // stretch the bitmap to a size appropriate for the screen resolution
        goodShipBitmap = Bitmap.createScaledBitmap(goodShipBitmap,
                (int) (length),
                (int) (height),
                false);
        damagedShipBitmap = Bitmap.createScaledBitmap(damagedShipBitmap,
                (int) (length),
                (int) (height),
                false);
        damagedShipBitmap2 = Bitmap.createScaledBitmap(damagedShipBitmap2,
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

    /*
    This is a getter method to make the rectangle that
    defines our paddle available in BreakoutView class
     */
    public Bitmap getBitmap(int lives){
        if(lives == 3) {
            return goodShipBitmap;
        } else if (lives == 2) {
            return damagedShipBitmap;
        } else {
            return damagedShipBitmap2;
        }
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

    /*
    This update method will be called from update in MissionView
    It determines if the player ship needs to move and changes the coordinates
    contained in x if necessary
     */
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

    /*
    Adjusts the speed at which the playership is moving.
     */
    public void updateShipSpeed(float accelX){
        shipSpeed = (accelX*frameTime);
    }
}