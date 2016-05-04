package edu.augustana.dreamteam.orderofoperations.gameobjects;

import android.content.Context;

/**
 * Created by danyatazyeen on 4/11/16.
 */

public class GameArena {

        private static Spaceship playerShip;
        private static int nextOpenSlot;
        Context context;

        // The size of the screen in pixels
        private int screenWidth;
        private int screenLength;


        public GameArena() {
            //INSTANTIATE THE SHIP
                playerShip = new Spaceship(context, 150, 107);
        }

//        public void update (int width, int height) {
//            playerShip.move(0, 0, width, height);
//        }
//
//        public static void updateVelo(int accelX, int accelY){
//                playerShip.updateVelo(-1 * accelX, accelY);
//        }
//
//        public void draw (Canvas canvas) {
//            //WIPE THE CANVAS CLEAN
//            playerShip.draw(canvas, screenWidth, screenLength);
//
//        }

}
