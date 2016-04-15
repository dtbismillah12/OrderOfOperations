package com.example.danyatazyeen.orderofoperations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Created by danyatazyeen on 4/6/16.
 * referencing http://gamecodeschool.com/android/coding-a-space-invaders-game/
 */
public class Spaceship {
    RectF rectangle;
    private Bitmap bitmap;
    private float lengthShip;
    private float xShipLeft;
    private float heightShip;
    private float yShipTop;

    public Spaceship(Context context, int screenX, int screenY){
        rectangle = new RectF();

        lengthShip = screenX/10;
        heightShip = screenY/10;

        xShipLeft = screenX/2;
        yShipTop = screenY-20;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) lengthShip, (int) heightShip, false);
    }

}
