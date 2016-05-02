package edu.augustana.dreamteam.orderofoperations.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.danyatazyeen.orderofoperations.R;

import java.util.Set;


public class ScoreScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);
    }

    SharedPreferences sharedPref;
    Set<String> topFiveScores;
    String[] topFive;

    ImageView background;
    ImageButton restartBTN;
    ImageButton endBTN;

    int finalScore;


    public ScoreScreen(){
        background = (ImageView) findViewById(R.id.endView);
        restartBTN = (ImageButton) findViewById(R.id.restartButton);
        endBTN = (ImageButton) findViewById(R.id.endButton);
    }

    //check if player's score is higher than the current highscore
    //if so, pop up window that notifies player
    public void checkHighSchore(){
        sharedPref = getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        topFiveScores = sharedPref.getStringSet("topFive", null);
        topFive = topFiveScores.toArray(new String[topFiveScores.size()]);

        for(int i = 0; i < topFiveScores.size(); i++){
            if(finalScore> Integer.parseInt(topFive[i])){

            }
        }

    }

//    restartBTN.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View btn) {
//        }
//
//    });


}
