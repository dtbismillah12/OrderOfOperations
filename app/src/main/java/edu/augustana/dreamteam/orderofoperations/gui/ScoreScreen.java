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
import android.util.TypedValue;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.augustana.dreamteam.orderofoperations.R;

import java.util.Set;
import java.util.StringTokenizer;


public class ScoreScreen extends Activity{
    private SharedPreferences sharedPref;
    private Set<String> topFiveScores;
    private String[] topFive;
    private ImageView background;
    private ImageButton restartBTN;
    private ImageButton endBTN;
    private Canvas canvas;
    private Paint paint;
    FrameLayout endScreen;
    private SurfaceHolder holder;

    private TextView highScore1;
    private TextView highScore2;
    private TextView highScore3;

    int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

//        SurfaceView surfView = (SurfaceView) findViewById(R.id.surfView);
//        holder = surfView.getHolder();
//        holder.addCallback(this);

        endScreen = (FrameLayout) findViewById(R.id.scoreView);
        background = (ImageView) findViewById(R.id.endView);
        restartBTN = (ImageButton) findViewById(R.id.restartButton);

        highScore1 = (TextView) findViewById(R.id.topScore1);
        highScore2 = (TextView) findViewById(R.id.topScore2);
        highScore3 = (TextView) findViewById(R.id.topScore3);

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


    private void draw(){
        // Make sure our drawing surface is valid or we crash
//        if (holder.getSurface().isValid()) {
//            // Lock the canvas ready to draw
//            canvas = holder.lockCanvas();

            Intent scoreIntent = getIntent();
            int finScore = scoreIntent.getIntExtra("score", 0);
            int screenHeight = scoreIntent.getIntExtra("screenHeight", 0);

            String strTopScores = sharedPref.getString("topThree", "");
            StringTokenizer st = new StringTokenizer(strTopScores, ",");
            int[] highScores = new int[3];
            for (int i = 0; i < highScores.length; i++) {
                highScores[i] = Integer.parseInt(st.nextToken());
            }

            //Draw high scores in middle of screen




//            paint.setTextSize(40);
//            paint.setColor(Color.argb(255, 249, 129, 0));
//            canvas.drawText("Top Score 1", 30, screenHeight - 150, paint);
//            canvas.drawText("Top Score 2", 30, screenHeight - 200, paint);
//            canvas.drawText("Top Score 3", 30, screenHeight - 250, paint);

//            canvas.drawText("Score: " + finScore, 5, screenHeight - 40, paint);
//            // Draw everything to the screen
//            holder.unlockCanvasAndPost(canvas);
//            endScreen.draw(canvas);

       // }
    }
    /*
    Draw High Scores to screen.
     */
    public void displayHighScores(){
        highScore1.setText("Top Score 1");
        highScore1.setText("Top Score 2");
        highScore1.setText("Top Score 3");
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