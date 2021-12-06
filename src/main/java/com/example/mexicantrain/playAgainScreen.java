package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class playAgainScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_again_screen);

        Button startNewRound = (Button) findViewById(R.id.playAnotherRoundBTN);
        Button outputWinnerBTN = (Button) findViewById(R.id.outputWinnerBTN);

        TextView currentComputerScore = (TextView) findViewById(R.id.currentComputerScore);
        TextView currentHumanScore = (TextView) findViewById(R.id.currentHumanScore);
        currentComputerScore.setText("Computer Score: " + getIntent().getIntExtra("gameComputerScore", -1));
        currentHumanScore.setText("Human Score: " + getIntent().getIntExtra("gameHumanScore", -1));
        //Intent restartRound = new Intent(this, MainActivity.class);

        startNewRound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("mytag", "Starting new Round.");
                //grab the intent with all the data we need, but
                // change the location so it points to main MainActivity
                Intent thisIntent = getIntent();
                thisIntent.setClass(getBaseContext(), MainActivity.class);
                startActivity(thisIntent);
            }
        });

        outputWinnerBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent thisIntent = getIntent();
                thisIntent.setClass(getBaseContext(), OutputWinner.class);
                startActivity(thisIntent);
            }
        });
    }
}
