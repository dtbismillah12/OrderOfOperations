package com.example.danyatazyeen.orderofoperations;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by danyatazyeen on 4/11/16.
 */

public class AnimationArena {

        private static Spaceship playerShip;
        private static int nextOpenSlot;
        Context context;

        // The size of the screen in pixels
        private int screenWidth;
        private int screenLength;


        public AnimationArena () {
            //INSTANTIATE THE SHIP
                playerShip = new Spaceship(context, 150, 107);
        }

        public void update (int width, int height) {
            playerShip.move(0, 0, width, height);
        }

        public static void updateVelo(int accelX, int accelY){
                playerShip.updateVelo(-1 * accelX, accelY);
        }

        public void draw (Canvas canvas) {
            //WIPE THE CANVAS CLEAN
            playerShip.draw(canvas, screenWidth, screenLength);

        }

}
