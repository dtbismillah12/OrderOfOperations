package edu.augustana.dreamteam.orderofoperations.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import edu.augustana.dreamteam.orderofoperations.R;

import java.util.Set;


public class ScoreScreen extends Activity {
    private SharedPreferences sharedPref;
    private Set<String> topFiveScores;
    private String[] topFive;
    private ImageView background;
    private ImageButton restartBTN;
    private ImageButton endBTN;
    private Canvas canvas;
    private Paint paint;
    private SurfaceHolder holder;

    int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();


        background = (ImageView) findViewById(R.id.endView);
        restartBTN = (ImageButton) findViewById(R.id.restartButton);
        //endBTN = (ImageButton) findViewById(R.id.endButton);


        restartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

        });
//
//        endBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View btn) {
//                System.exit(0);
//            }
//
//        });
    }




    //check if player's score is higher than the current highscore
    //if so, pop up window that notifies player
    public void checkHighScore(){
        sharedPref = getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        topFiveScores = sharedPref.getStringSet("topFive", null);
        topFive = topFiveScores.toArray(new String[topFiveScores.size()]);

        for(int i = 0; i < topFiveScores.size(); i++){
            if(finalScore> Integer.parseInt(topFive[i])){

            }
        }

    }

    private void draw(){
        // Make sure our drawing surface is valid or we crash
        if (holder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = holder.lockCanvas();
            paint.setTextSize(40);
            paint.setColor(Color.argb(255, 255, 255, 255));

            Intent scoreIntent = getIntent();
            int finScore = scoreIntent.getIntExtra("score", 0);
            int screenHeight = scoreIntent.getIntExtra("screenHeight", 0);

            //canvas.drawText("Score: " + finScore + 5, screenHeight - 40, paint);
            // Draw everything to the screen
            holder.unlockCanvasAndPost(canvas);
        }
    }




}