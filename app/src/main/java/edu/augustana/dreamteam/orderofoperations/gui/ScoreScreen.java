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
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.augustana.dreamteam.orderofoperations.R;

import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;


public class ScoreScreen extends MainActivity{
    private SharedPreferences sharedPref2;
    private ImageView background;
    private ImageButton restartBTN;
    private ImageButton endBTN;
    private Canvas canvas;
    private Paint paint;
    private Context context;
    FrameLayout endScreen;
    private SurfaceHolder holder;
    private int[] highScores;
    private SharedPreferences.Editor editor;

    private TextView highScore1;
    private TextView highScore2;
    private TextView highScore3;
    private TextView yourScore;

    int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        context = getApplicationContext();

        endScreen = (FrameLayout) findViewById(R.id.scoreView);
        background = (ImageView) findViewById(R.id.endView);
        restartBTN = (ImageButton) findViewById(R.id.restartButton);

        highScore1 = (TextView) findViewById(R.id.topScore1);
        highScore2 = (TextView) findViewById(R.id.topScore2);
        highScore3 = (TextView) findViewById(R.id.topScore3);

        yourScore = (TextView) findViewById(R.id.playerScore);

        displayHighScores();


        restartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

        });
    }

    /*
    Draw High Scores to screen.
     */
    public void displayHighScores(){
        //get player's final score from extras in intent
        Intent scoreIntent = getIntent();
        int finScore = scoreIntent.getIntExtra("score", 0);
        int screenHeight = scoreIntent.getIntExtra("screenHeight", 0);

        //retrieve top 3 scores
        sharedPref2 = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        String firstPlace = sharedPref2.getString("first", "no");
        String secondPlace = sharedPref2.getString("second", "no");
        String thirdPlace = sharedPref2.getString("third", "no");

        yourScore.setText("Your Score: "+ finScore);

        highScore1.setText("1st Place: " + firstPlace);
        highScore2.setText("2nd Place: " + secondPlace);
        highScore3.setText("3rd Place: " + thirdPlace);

        checkHighScore(finScore, firstPlace, secondPlace, thirdPlace, sharedPref2, editor);
    }


    //check if player's score is higher than the current highscore
    //if so, pop up window that notifies player
    public void checkHighScore(int finScore, String firstPlace, String secondPlace, String thirdPlace, SharedPreferences sharedPref2, SharedPreferences.Editor editor){

        //add top scores to ArrayList in order from lowest to highest
        ArrayList<Integer> intHighScores = new ArrayList<Integer>();
        intHighScores.add(Integer.parseInt(thirdPlace));
        intHighScores.add(Integer.parseInt(secondPlace));
        intHighScores.add(Integer.parseInt(firstPlace));

        editor = sharedPref2.edit();

        //is final score greater than first place score?
        if(finScore > intHighScores.get(2)) {
            Toast.makeText(getApplicationContext(), "NEW HIGH SCORE!", Toast.LENGTH_LONG).show();
            editor.putString("first", Integer.toString(finScore));
            editor.putString("second", firstPlace);
            editor.putString("third", secondPlace);
            editor.apply();

            highScore1.setText("1st Place: " + Integer.toString(finScore));
            highScore1.setTextColor(getResources().getColor(R.color.gold));
            highScore2.setText("2nd Place: " + firstPlace);
            highScore3.setText("3rd Place: " + secondPlace);
        }

        //is final score greater than second place?
        else if (finScore > intHighScores.get(1)) {
            Toast.makeText(getApplicationContext(), "NEW HIGH SCORE!", Toast.LENGTH_LONG).show();
            editor.putString("first", firstPlace);
            editor.putString("second", Integer.toString(finScore));
            editor.putString("third", secondPlace);
            editor.apply();

            highScore1.setText("1st Place: " + firstPlace);
            highScore2.setText("2nd Place: " + Integer.toString(finScore));
            highScore2.setTextColor(getResources().getColor(R.color.gold));
            highScore3.setText("3rd Place: " + secondPlace);
        }

        //is final score greater than third place?
        else if (finScore > intHighScores.get(0)) {
            Toast.makeText(getApplicationContext(), "NEW HIGH SCORE!", Toast.LENGTH_LONG).show();
            editor.putString("first", firstPlace);
            editor.putString("second", secondPlace);
            editor.putString("third", Integer.toString(finScore));
            editor.apply();

            highScore1.setText("1st Place: " + firstPlace);
            highScore2.setText("2nd Place: " + secondPlace);
            highScore3.setText("3rd Place: " + Integer.toString(finScore));
            highScore3.setTextColor(getResources().getColor(R.color.gold));


        }

    }


}