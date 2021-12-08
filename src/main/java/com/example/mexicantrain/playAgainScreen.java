package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * ask the user if they want to play again. called after a round is finished
 */
public class playAgainScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_again_screen);

        //get the yes/no buttons for the play again question. yes will start a new round, no will output winner
        Button startNewRound = (Button) findViewById(R.id.playAnotherRoundBTN);
        Button outputWinnerBTN = (Button) findViewById(R.id.outputWinnerBTN);

        //get text views that stores the computer/human score
        // and populate them with the respectful data from the intent that called this view.
        TextView currentComputerScore = (TextView) findViewById(R.id.currentComputerScore);
        TextView currentHumanScore = (TextView) findViewById(R.id.currentHumanScore);
        currentComputerScore.setText("Computer Score: " + getIntent().getIntExtra("gameComputerScore", -1));
        currentHumanScore.setText("Human Score: " + getIntent().getIntExtra("gameHumanScore", -1));

        //yes, start a new round button (play again)
        startNewRound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //grab the intent with all the data we need, but
                // change the location so it points to MainActivity which will start the game
                Intent thisIntent = getIntent();
                thisIntent.setClass(getBaseContext(), MainActivity.class);
                startActivity(thisIntent);
            }
        });

        //no, do not start a new round, output the winner of the game
        outputWinnerBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //grab the intent with all the data we need, but
                // change the location so it points to OutputWinner
                // which will output the winner of this game.
                Intent thisIntent = getIntent();
                thisIntent.setClass(getBaseContext(), OutputWinner.class);
                startActivity(thisIntent);
            }
        });
    }
}
