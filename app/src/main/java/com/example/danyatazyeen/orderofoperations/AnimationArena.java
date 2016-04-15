package com.example.danyatazyeen.orderofoperations;

import android.graphics.Canvas;

/**
 * Created by danyatazyeen on 4/11/16.
 */

import android.graphics.Canvas;

public class AnimationArena {

        private static Spaceship ship;
        private static int nextOpenSlot;


        public AnimationArena () {
            //INSTANTIATE THE SHIP
           // ship = new Spaceship();
        }

        public void update (int width, int height) {
            ship.move(0, 0, width, height);
        }

        public static void updateVelo(int accelX, int accelY){
                ship.updateVelo(-1 * accelX, accelY);
        }

        public void draw (Canvas canvas) {
            //WIPE THE CANVAS CLEAN
            canvas.drawRGB(75, 0, 130);

        }

}
