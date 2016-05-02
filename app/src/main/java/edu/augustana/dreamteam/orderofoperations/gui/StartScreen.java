package edu.augustana.dreamteam.orderofoperations.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.danyatazyeen.orderofoperations.R;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageView background = (ImageView) findViewById(R.id.startView);

        ImageButton startBTN = (ImageButton) findViewById(R.id.startButton);

        startBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent startGame = new Intent(getApplicationContext(), MainActivity.class);
                startGame.setAction(startGame.ACTION_SEND);
                startActivity(startGame);
            }
        });

    }

}
