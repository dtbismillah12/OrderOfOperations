package edu.augustana.dreamteam.orderofoperations.gameobjects;

import android.graphics.RectF;

/**
 * Created by danyatazyeen on 4/18/16.
 */
public class BarrierBrick {
    private RectF rect;

    private boolean isVisible;

    public BarrierBrick(int row, int column, int shelterNumber, int screenX, int screenY){

        int width = screenX / 90;
        int height = screenY / 40;

        isVisible = true;

        int brickPadding = 0;
        // The number of shelters
        int shelterPadding = screenX / 9;
        int startHeight = screenY - (screenY /8 * 2);

        rect = new RectF(column * width + brickPadding + (shelterPadding * shelterNumber) + shelterPadding + shelterPadding * shelterNumber,
                row * height + brickPadding + startHeight,
                column * width + width - brickPadding + (shelterPadding * shelterNumber) + shelterPadding + shelterPadding * shelterNumber,
                row * height + height - brickPadding + startHeight);
    }

    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean isAlive(){
        return isVisible;
    }

}
