package edu.augustana.dreamteam.orderofoperations.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.app.AlertDialog;

import edu.augustana.dreamteam.orderofoperations.R;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageButton startBTN = (ImageButton) findViewById(R.id.startButton);
        ImageButton infoBTN = (ImageButton) findViewById(R.id.infoButton);

        startBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent startGame = new Intent(getApplicationContext(), MainActivity.class);
                startGame.setAction(startGame.ACTION_SEND);
                startActivity(startGame);
            }
        });


        infoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                AlertDialog alert = new AlertDialog.Builder(StartScreen.this).create();
                alert.setTitle("About Mission OoO: Order of Operations");
                alert.setMessage("This is a game intended for use in learning and " +
                        "practicing solving operations in the correct order.");
                alert.show();
            }
        });
    }

}
