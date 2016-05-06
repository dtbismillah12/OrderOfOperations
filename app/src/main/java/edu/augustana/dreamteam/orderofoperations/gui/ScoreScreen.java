package edu.augustana.dreamteam.orderofoperations.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
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
        endBTN = (ImageButton) findViewById(R.id.endButton);


        restartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {

            }

        });

        endBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                System.exit(0);
            }

        });
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




}
