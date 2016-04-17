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
    RectF rectangle;
    private Bitmap bitmap;
    private int x;
    private int y;
    private static int velX;
    private static int velY;
    private float widthShip;
    private float xShipLeft;
    private float heightShip;
    private float yShipTop;
    public static float frameTime = 0.666f;
    private Paint paint;

    public Spaceship(Context context, int screenX, int screenY){
        rectangle = new RectF();
        x = 100;
        y = 100;
        velX = 0;
        velY = 0;
        widthShip = screenX/10;
        heightShip = screenY/10;
        xShipLeft = screenX/2;
        yShipTop = screenY-20;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) widthShip, (int) heightShip, false);
    }

    public void move(int leftWall, int topWall,
                     int rightWall, int bottomWall) {
        //MOVE SHIP
        x += velX;
        y += velY;

        //CHECK FOR COLLISIONS ALONG WALLS
        if (y > bottomWall - (int) heightShip /2) {
            y = bottomWall - (int)heightShip/2;
            velY = -1*(velY/2);
        } else if (y < topWall + (int) heightShip /2) {
            y = topWall + (int)heightShip/2;
            velY = -1*(velY/2);
        }

        if (x > rightWall - (int) widthShip /2) {
            x = rightWall - (int) widthShip /2;
            velX = -1*(velX/2);
        } else if (x < leftWall + (int) widthShip /2) {
            x = leftWall + (int) widthShip /2;
            velX = -1*(velX/2);
        }
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public static void updateVelo(int accelX, int accelY){
        // updating velocity of ball based on v = a * t
        // according to acceleration measurements from accelerometer
        velX += (accelX*frameTime);
        velY += (accelY*frameTime);
    }

    public void draw(Canvas canvas,int screenWidth, int screenLength) {
        canvas.drawBitmap(bitmap, x, screenLength - 50, paint);
    }

}
