package com.example.danyatazyeen.orderofoperations;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    //view of game
    MissionOOOView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        gameView = new MissionOOOView(this, size.x, size.y);
        setContentView(gameView);
    }

    // Executed when player starts the game
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    // Executed when player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }
}

}
