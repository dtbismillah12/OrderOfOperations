package edu.augustana.dreamteam.orderofoperations.gui;

import android.content.Intent;
import android.text.Html;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
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

        //button to start the game
        startBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent startGame = new Intent(getApplicationContext(), MainActivity.class);
                startGame.setAction(startGame.ACTION_SEND);
                startActivity(startGame);
            }
        });


        //show information about app and give credits
        infoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Spanned span = Html.fromHtml("<a href=\"http://gamecodeschool.com/android/coding-a-space-invaders-game/\"> Space Invaders Tutorial</a>");

                AlertDialog alert = new AlertDialog.Builder(StartScreen.this).create();
                alert.setTitle("About Mission OoO: Order of Operations");
                alert.setMessage("This is a game intended for use in learning and " +
                        "practicing solving operations in the correct order." +
                        "\n" + "\n" +
                        "To play, just shoot the asteroid containing the next correct operator" +
                        " in the expression. But be sure to watch out for the oncoming alien threat!" +
                        "\n" + "\n" +
                        "Thanks to John Horton and GameCodeSchool.com for their great " +
                        span.toString() +
                        " which served as a useful springboard in the beginning phases of " +
                        "Mission OoO's development." +
                        "\n" + "\n" +
                        "Thanks and happy playing!" +
                                "\n" + "\n" +
                                "Authors: Jeff Prior, Danya Tazyeen, Kelsey Self, Scott Davis, Nick Caputo." +
                                "\n" + "\n" +
                                "Graphics by Danya Tazyeen and Jeff Prior."

                );
                alert.show();
            }
        });
    }

}
