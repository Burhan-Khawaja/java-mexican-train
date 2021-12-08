package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * output the winner of the game. this class is called when the user chooses no to playing another round
 */
public class OutputWinner extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_winner);

        //get text that will display who won, the computer score and the human score.
        TextView whoWon = (TextView) findViewById(R.id.whoWon);
        TextView outputComputerScore = (TextView) findViewById(R.id.outputComputerScore);
        TextView outputHumanScore = (TextView) findViewById(R.id.outputHumanScore);

        //get intent that started this page.
        Intent intentData = getIntent();

        //retrieve computer score and human score from the intent
        int computerScore = intentData.getIntExtra("gameComputerScore", -1);
        int humanScore = intentData.getIntExtra("gameHumanScore", -1);

        //add text to views on the page to let player know what the score was for each player
        outputComputerScore.setText("Computer Score: " + computerScore);
        outputHumanScore.setText("Human Score: " + humanScore);

        //output winner.
        if(computerScore == humanScore) {
            whoWon.setText("The game is a tie!");
        }
        else if (humanScore > computerScore) {
            whoWon.setText("You Lost!");
        }
        else {
            whoWon.setText("You Won!");
        }

    }
}
